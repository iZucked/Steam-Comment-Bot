/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintChecker;
import com.mmxlabs.scheduler.optimiser.lso.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A pairwise constraint respecting initial sequence builder. This sorts elements by their earliest arrival time and then constructs a sequence for each resource by picking the earliest element which
 * can be appended to the route without violating the consequent pairwise constraint.
 * 
 * @author hinton
 * 
 * @param <T>
 */
public class ConstrainedInitialSequenceBuilder<T> implements IInitialSequenceBuilder<T> {

	private static final Logger log = LoggerFactory.getLogger(ConstrainedInitialSequenceBuilder.class);

	/**
	 * The initial maximum acceptable lateness; every cargo in the solution should be feasible on the fastest ship in the fleet with this much slack.
	 */
	private static final int INITIAL_MAX_LATENESS = 48;

	private final List<IPairwiseConstraintChecker<T>> pairwiseCheckers;
	private TravelTimeConstraintChecker<T> travelTimeChecker;

	class ChunkChecker<T> {
		private final LegalSequencingChecker<T> checker;

		public ChunkChecker(LegalSequencingChecker<T> realChecker) {
			this.checker = realChecker;
		}

		public boolean canFollow(SequenceChunk<T> chunk1, SequenceChunk<T> chunk2, IResource resource) {
			final T tail1 = chunk1.last();
			final T head2 = chunk2.first();
			return checker.allowSequence(tail1, head2, resource);
		}

		public boolean canInsert(SequenceChunk<T> before, SequenceChunk<T> added, SequenceChunk<T> after, IResource resource) {
			return canFollow(before, added, resource) && canFollow(added, after, resource);
		}
	}

	class SequenceChunk<T> extends ArrayList<T> {
		int resourceCount;
		private boolean endElement;

		public T first() {
			return get(0);
		}

		public T last() {
			return get(size() - 1);
		}

		public int getResourceCount() {
			return resourceCount;
		}

		public void setResourceCount(int resourceCount) {
			this.resourceCount = resourceCount;
		}

		public void setEndElement(boolean b) {
			this.endElement = b;
		}

		public boolean isEndElement() {
			return this.endElement;
		}
	}

	public ConstrainedInitialSequenceBuilder(Collection<IConstraintCheckerFactory> factories) {
		this.pairwiseCheckers = new ArrayList<IPairwiseConstraintChecker<T>>();
		for (IConstraintCheckerFactory factory : factories) {
			IConstraintChecker<T> checker = factory.instantiate();
			if (checker instanceof IPairwiseConstraintChecker) {
				pairwiseCheckers.add((IPairwiseConstraintChecker<T>) checker);
			}
			if (checker instanceof TravelTimeConstraintChecker) {
				this.travelTimeChecker = (TravelTimeConstraintChecker<T>) checker;
			}
		}
	}

	public ConstrainedInitialSequenceBuilder(List<IPairwiseConstraintChecker<T>> pairwiseCheckers) {
		this.pairwiseCheckers = pairwiseCheckers;
	}

	@Override
	public ISequences<T> createInitialSequences(final IOptimisationData<T> data, final ISequences<T> suggestion, Map<T, IResource> resourceSuggestion) {

		if (resourceSuggestion == null)
			resourceSuggestion = Collections.emptyMap();

		@SuppressWarnings("unchecked")
		final IPortTypeProvider<T> portTypeProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);

		@SuppressWarnings("unchecked")
		final IPortSlotProvider<T> portSlotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);

		@SuppressWarnings("unchecked")
		final IStartEndRequirementProvider<T> startEndRequirementProvider = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);

		@SuppressWarnings("unchecked")
		final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);

		LegalSequencingChecker<T> checker = new LegalSequencingChecker<T>(data, pairwiseCheckers);

		final int initialMaxLateness = (travelTimeChecker == null) ? 0 : travelTimeChecker.getMaxLateness();

		if (travelTimeChecker != null)
			travelTimeChecker.setMaxLateness(INITIAL_MAX_LATENESS);

		// stick together elements which must be stuck together
		Map<T, Set<T>> followerCache = new LinkedHashMap<T, Set<T>>();
		Set<T> heads = new LinkedHashSet<T>();
		Set<T> tails = new LinkedHashSet<T>();

		final Set<T> unsequencedElements = new LinkedHashSet<T>();
		unsequencedElements.addAll(data.getSequenceElements());
		if (suggestion != null) {
			for (final ISequence<T> seq : suggestion.getSequences().values()) {
				for (final T element : seq)
					unsequencedElements.remove(element);
			}
		}

		log.info("Elements remaining to be sequenced: " + unsequencedElements);

		for (T element1 : unsequencedElements) {
			Set<T> after1 = new LinkedHashSet<T>();
			followerCache.put(element1, after1);
			for (T element2 : data.getSequenceElements()) {
				if (element1 == element2)
					continue;
				if (checker.allowSequence(element1, element2)) {
					after1.add(element2);
				}
			}
			// part of a chain
			if (after1.size() == 1) {
				if (!tails.contains(element1)) {
					heads.add(element1);
				}
				final T tail = after1.iterator().next();
				tails.add(tail);
				heads.remove(tail);
			}
		}

		// Heads now contains the head of every chunk that has to go together.
		// We need to pull out all the chunks and sort out their rules
		List<IResource> resources = new ArrayList<IResource>(data.getResources());
		{
			Collections.sort(resources, new Comparator<IResource>() {
				@Override
				public int compare(final IResource o1, final IResource o2) {
					final IVessel vessel1 = vesselProvider.getVessel(o1);
					final IVessel vessel2 = vesselProvider.getVessel(o2);
					final VesselInstanceType vit1 = vessel1.getVesselInstanceType();
					final VesselInstanceType vit2 = vessel2.getVesselInstanceType();

					if (vit1.ordinal() < vit2.ordinal()) {
						return -1;
					} else if (vit1.ordinal() > vit2.ordinal()) {
						return 1;
					} else {
						if (vessel1.getVesselClass().getMaxSpeed() > vessel2.getVesselClass().getMaxSpeed()) {
							return -1;
						} else {
							return 1;
						}
					}
				}
			});
		}

		final Map<IResource, SequenceChunk<T>> endChunks = new LinkedHashMap<IResource, SequenceChunk<T>>();

		final IResourceAllocationConstraintDataComponentProvider racdcp = data.getDataComponentProvider(SchedulerConstants.DCP_resourceAllocationProvider,
				IResourceAllocationConstraintDataComponentProvider.class);

		List<SequenceChunk<T>> chunks = new LinkedList<SequenceChunk<T>>();
		for (T head : heads) {
			SequenceChunk<T> chunk = new SequenceChunk<T>();
			if (portTypeProvider.getPortType(head) == PortType.End) {
				chunk.setEndElement(true);
				final Collection<IResource> c = racdcp.getAllowedResources(head);
				assert c.size() == 1;
				endChunks.put(c.iterator().next(), chunk);
			}
			chunk.add(head);
			while (followerCache.get(head).size() == 1) {
				head = followerCache.get(head).iterator().next();
				if (!unsequencedElements.contains(head))
					break;
				chunk.add(head);
				if (portTypeProvider.getPortType(head) == PortType.End) {
					chunk.setEndElement(true);
					final Collection<IResource> c = racdcp.getAllowedResources(head);
					assert c.size() == 1;
					endChunks.put(c.iterator().next(), chunk);
				}
			}
			chunks.add(chunk);

			// get resource count for chunk
			int rc = 0;
			for (IResource resource : resources) {
				boolean ok = true;
				for (int c = 0; c < chunk.size() - 1; c++) {
					if (checker.allowSequence(chunk.get(c), chunk.get(c + 1), resource) == false) {
						ok = false;
						break;
					}
				}
				if (ok)
					rc++;
			}
			chunk.setResourceCount(rc);
		}

		// now add chunks for things with only one element

		for (Map.Entry<T, Set<T>> entry : followerCache.entrySet()) {
			final T place = entry.getKey();
			final PortType portType = portTypeProvider.getPortType(place);
			if (portType.equals(PortType.Start))
				continue;

			if (tails.contains(place))
				continue;

			if (portType.equals(PortType.End)) {
				SequenceChunk<T> guy = new SequenceChunk<T>();
				guy.add(place);
				guy.setEndElement(true);
				chunks.add(guy);
				final Collection<IResource> c = racdcp.getAllowedResources(place);
				assert c.size() == 1;
				endChunks.put(c.iterator().next(), guy);
			} else if (entry.getValue().size() > 1) {
				SequenceChunk<T> guy = new SequenceChunk<T>();
				guy.add(place);
				final Collection<IResource> c = racdcp.getAllowedResources(place);
				guy.setResourceCount(c == null ? resources.size() : c.size());
				chunks.add(guy);
			}
		}

		// These are the things which we need to schedule on routes. We need to
		// sort them
		// first by the number of distinct resources which can take them, and
		// then by their start time.

		// Then we can try and schedule them in that order, and hopefully the
		// most constrained stuff will get done first, etc
		// at the end we can try a final insertion pass for leftover chunks.

		final Comparator<SequenceChunk<T>> comparator = new Comparator<SequenceChunk<T>>() {
			@Override
			public int compare(SequenceChunk<T> chunk1, SequenceChunk<T> chunk2) {
				// end elements go at the end
				if (chunk2.isEndElement()) {
					return -1;
				} else if (chunk1.isEndElement()) {
					return 1;
				}

				// check number of resources for each chunk

				final int rc1 = chunk1.getResourceCount();
				final int rc2 = chunk2.getResourceCount();

				if (Math.abs(rc1 - rc2) > 3) {
					if (rc1 < rc2) {
						return -1;
					} else {
						return 1;
					}
				}

				final T o1 = chunk1.get(0);
				final T o2 = chunk2.get(0);

				final T e1 = chunk1.get(chunk1.size() - 1);
				final T e2 = chunk2.get(chunk2.size() - 1);

				final IPortSlot slot1 = portSlotProvider.getPortSlot(o1);
				final IPortSlot slot2 = portSlotProvider.getPortSlot(o2);

				final IPortSlot slot3 = portSlotProvider.getPortSlot(e1);
				final IPortSlot slot4 = portSlotProvider.getPortSlot(e2);

				final int duration1 = slot3.getTimeWindow().getStart() - slot1.getTimeWindow().getStart();
				final int duration2 = slot4.getTimeWindow().getStart() - slot2.getTimeWindow().getStart();

				// if one is much longer than the other, do it first.
				if (Math.abs(duration1 - duration2) > 24) {
					if (duration1 > duration2) {
						return -1;
					} else {
						return 1;
					}
				}

				// sort by start time
				final int s1 = slot1.getTimeWindow().getStart();
				final int s2 = slot2.getTimeWindow().getStart();

				if (s1 < s2) {
					return -1;
				} else if (s1 > s2) {
					return 1;
				} else {
					return 0;
				}
			}
		};


		Collections.sort(chunks, comparator);

		final ChunkChecker<T> chunkChecker = new ChunkChecker<T>(checker);
		Map<IResource, List<SequenceChunk<T>>> sequences = new LinkedHashMap<IResource, List<SequenceChunk<T>>>();

		if (suggestion == null) {
			log.info("No suggested start solution - constructing one");
			for (IResource resource : resources) {
				final SequenceChunk<T> end = endChunks.get(resource);

				log.info("Scheduling elements for resource " + resource);
				List<SequenceChunk<T>> sequence = new ArrayList<SequenceChunk<T>>();
				sequences.put(resource, sequence);

				// try and schedule chunks
				// first put in the start element
				SequenceChunk<T> start = new SequenceChunk<T>();
				start.add(startEndRequirementProvider.getStartElement(resource));
				sequence.add(start);
				// now assign any chunks which we can to follow it
				Iterator<SequenceChunk<T>> iterator = chunks.iterator();
				SequenceChunk<T> here = start;
				INITIAL_SEQUENCING_LOOP: while (iterator.hasNext()) {
					final SequenceChunk<T> there = iterator.next();
					// check that there can come after here, and that end can come after there.
					if (chunkChecker.canFollow(here, there, resource) && ((there == end) || chunkChecker.canFollow(there, end, resource))) {
						// now check for resource advice
						for (final T element : there) {
							final IResource suggestedResource = resourceSuggestion.get(element);
							if (suggestedResource != null && suggestedResource != resource)
								continue INITIAL_SEQUENCING_LOOP; // process
																	// this
																	// chunk at
																	// a later
																	// date.
						}
						sequence.add(there);
						log.info("Adding chunk " + there);
						here = there;
						iterator.remove();
					}
				}
			}
		} else {
			// copy suggestion state into one-element chunks.
			log.info("Starting with suggested solution");
			for (final IResource resource : suggestion.getResources()) {
				List<SequenceChunk<T>> sequence = new ArrayList<SequenceChunk<T>>();
				sequences.put(resource, sequence);

				final ISequence<T> seq = suggestion.getSequence(resource);
				for (final T element : seq) {
					final SequenceChunk<T> chunk = new SequenceChunk<T>();
					chunk.add(element);
					sequence.add(chunk);
				}

				if (sequence.isEmpty()) {
					// insert a start element (otherwise start element came from
					// elsewhere)
					// end element should be handled by the next step,
					// hopefully.
					SequenceChunk<T> start = new SequenceChunk<T>();
					start.add(startEndRequirementProvider.getStartElement(resource));
					sequence.add(start);
					// spam in any elements which will fit
					Iterator<SequenceChunk<T>> iterator = chunks.iterator();
					SequenceChunk<T> here = start;
					SECOND_INSERT_LOOP: while (iterator.hasNext()) {
						final SequenceChunk<T> there = iterator.next();

						for (final T element : there) {
							final IResource suggestedResource = resourceSuggestion.get(resource);
							if (suggestedResource != null && suggestedResource != resource)
								continue SECOND_INSERT_LOOP;
						}

						if (chunkChecker.canFollow(here, there, resource)) {
							sequence.add(there);
							here = there;
							iterator.remove();
							if (vesselProvider.getVessel(resource).getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER)) {
								break; // only schedule one thing on each spot
										// vessel
							}
						}
					}
				}
			}
		}

		// chunks have been scheduled sequentially as best we can, now try
		// inserting any leftovers
		log.info("Trying to insert " + chunks.size() + " unscheduled elements into solution (" + chunks + ")");
		while (!chunks.isEmpty()) {
			final Iterator<SequenceChunk<T>> iterator = chunks.iterator();
			while (iterator.hasNext()) {
				top: {
					final SequenceChunk<T> here = iterator.next();

					// first check for suggested resource
					IResource suggestedResource = null;
					for (final T element : here) {
						final IResource suggestionForElement = resourceSuggestion.get(element);
						if (suggestionForElement != null && suggestedResource != null && suggestionForElement != suggestedResource) {
							suggestedResource = null;
							break;
						}
						suggestedResource = suggestionForElement;
					}

					if (suggestedResource != null) {
						// try inserting on suggested resource first

						final List<SequenceChunk<T>> sequence = sequences.get(suggestedResource);

						if (tryInsertingChunk(chunkChecker, iterator, here, sequence, suggestedResource))
							break top;
					}

					for (Map.Entry<IResource, List<SequenceChunk<T>>> entry : sequences.entrySet()) {
						final IResource res = entry.getKey();
						final List<SequenceChunk<T>> sequence = entry.getValue();
						if (tryInsertingChunk(chunkChecker, iterator, here, sequence, res))
							break top;
					}
				}
			}
			if (travelTimeChecker == null)
				break;
			// relax constraint
			final int maxLateness = travelTimeChecker.getMaxLateness();
			if (maxLateness == initialMaxLateness)
				break;

			travelTimeChecker.setMaxLateness(maxLateness + 1);
			// log.info("Lateness constraint relaxed to " + maxLateness + " as "
			// + chunks.size() + " elements are unscheduled (" + chunks
			// + ")");
		}

		if (chunks.isEmpty() == false) {
			log.error("Could not schedule the following " + chunks.size() + " elements anywhere: " + chunks);
			throw new RuntimeException("Scenario is too hard for ConstrainedInitialSolutionBuilder. " + chunks.size() + " chunks " + "could not be scheduled anywhere: " + chunks);
		}

		// OK, we have done our best, now build the modifiablesequences
		// from the intermediate gack

		IModifiableSequences<T> result = new ModifiableSequences<T>(resources);
		for (final IResource resource : resources) {
			IModifiableSequence<T> realseq = result.getModifiableSequence(resource);
			final List<SequenceChunk<T>> seq = sequences.get(resource);
			for (SequenceChunk<T> chunk : seq) {
				for (T element : chunk) {
					realseq.add(element);
				}
			}
		}

		return result;
	}

	private boolean tryInsertingChunk(final ChunkChecker<T> chunkChecker, final Iterator<SequenceChunk<T>> iterator, final SequenceChunk<T> here, final List<SequenceChunk<T>> sequence,
			final IResource res) {
		if (here.isEndElement()) {
			if (chunkChecker.canFollow(sequence.get(sequence.size() - 1), here, res)) {
				sequence.add(here);
				iterator.remove();
				return true;
				// break top;
			}
		} else {
			for (int i = 0; i < sequence.size() - 1; i++) {
				if (chunkChecker.canInsert(sequence.get(i), here, sequence.get(i + 1), res)) {
					sequence.add(i + 1, here);
					iterator.remove();
					return true;
					// break top;
				}
			}
		}
		return false;
	}

}
