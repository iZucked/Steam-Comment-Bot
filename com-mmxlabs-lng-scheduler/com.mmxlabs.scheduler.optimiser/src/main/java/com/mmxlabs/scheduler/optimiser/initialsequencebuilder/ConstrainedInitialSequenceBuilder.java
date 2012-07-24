/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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

import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Resource;
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
 * @param
 */
public class ConstrainedInitialSequenceBuilder implements IInitialSequenceBuilder {

	private static final Logger log = LoggerFactory.getLogger(ConstrainedInitialSequenceBuilder.class);

	/**
	 * The initial maximum acceptable lateness; every cargo in the solution should be feasible on the fastest ship in the fleet with this much slack.
	 */
	private static final int INITIAL_MAX_LATENESS = 48;

	private final List<IPairwiseConstraintChecker> pairwiseCheckers;
	private TravelTimeConstraintChecker travelTimeChecker;

	class ChunkChecker {
		private final LegalSequencingChecker checker;

		public ChunkChecker(final LegalSequencingChecker realChecker) {
			this.checker = realChecker;
		}

		public boolean canFollow(final SequenceChunk chunk1, final SequenceChunk chunk2, final IResource resource) {
			final ISequenceElement tail1 = chunk1.last();
			final ISequenceElement head2 = chunk2.first();
			return checker.allowSequence(tail1, head2, resource);
		}

		public boolean canInsert(final SequenceChunk before, final SequenceChunk added, final SequenceChunk after, final IResource resource) {
			return canFollow(before, added, resource) && canFollow(added, after, resource);
		}
	}

	class SequenceChunk extends ArrayList<ISequenceElement> {
		int resourceCount;
		private boolean endElement;

		public ISequenceElement first() {
			return get(0);
		}

		public ISequenceElement last() {
			return get(size() - 1);
		}

		public int getResourceCount() {
			return resourceCount;
		}

		public void setResourceCount(final int resourceCount) {
			this.resourceCount = resourceCount;
		}

		public void setEndElement(final boolean b) {
			this.endElement = b;
		}

		public boolean isEndElement() {
			return this.endElement;
		}
	}

	public ConstrainedInitialSequenceBuilder(final Collection<IConstraintCheckerFactory> factories) {
		this.pairwiseCheckers = new ArrayList<IPairwiseConstraintChecker>();
		for (final IConstraintCheckerFactory factory : factories) {
			final IConstraintChecker checker = factory.instantiate();
			if (checker instanceof IPairwiseConstraintChecker) {
				pairwiseCheckers.add((IPairwiseConstraintChecker) checker);
			}
			if (checker instanceof TravelTimeConstraintChecker) {
				this.travelTimeChecker = (TravelTimeConstraintChecker) checker;
			}
		}
	}

	public ConstrainedInitialSequenceBuilder(final List<IPairwiseConstraintChecker> pairwiseCheckers) {
		this.pairwiseCheckers = pairwiseCheckers;
	}

	@Override
	public ISequences createInitialSequences(final IOptimisationData data, final ISequences suggestion, Map<ISequenceElement, IResource> resourceSuggestion) {

		if (resourceSuggestion == null) {
			resourceSuggestion = Collections.emptyMap();
		}

		final IPortTypeProvider portTypeProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);

		final IPortSlotProvider portSlotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);

		final IStartEndRequirementProvider startEndRequirementProvider = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);

		final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);

		final IOptionalElementsProvider optionalElements = data.getDataComponentProvider(SchedulerConstants.DCP_optionalElementsProvider, IOptionalElementsProvider.class);

		final LegalSequencingChecker checker = new LegalSequencingChecker(data, pairwiseCheckers);

		final int initialMaxLateness = (travelTimeChecker == null) ? 0 : travelTimeChecker.getMaxLateness();

		if (travelTimeChecker != null) {
			travelTimeChecker.setMaxLateness(INITIAL_MAX_LATENESS);
		}

		// stick together elements which must be stuck together
		final Map<ISequenceElement, Set<ISequenceElement>> followerCache = new LinkedHashMap<ISequenceElement, Set<ISequenceElement>>();
		final Set<ISequenceElement> heads = new LinkedHashSet<ISequenceElement>();
		final Set<ISequenceElement> tails = new LinkedHashSet<ISequenceElement>();

		final Set<ISequenceElement> unsequencedElements = new LinkedHashSet<ISequenceElement>();
		unsequencedElements.addAll(data.getSequenceElements());
		if (suggestion != null) {
			for (final ISequence seq : suggestion.getSequences().values()) {
				for (final ISequenceElement element : seq) {
					if (unsequencedElements.contains(element) == false)
						log.warn("Element " + element + " is already sequenced");
					unsequencedElements.remove(element);
				}
			}
		}

		final List<ISequenceElement> unusedElements = new ArrayList<ISequenceElement>();
		unusedElements.addAll(optionalElements.getOptionalElements());
		unusedElements.retainAll(unsequencedElements);
		unsequencedElements.removeAll(optionalElements.getOptionalElements());

		log.info("Elements remaining to be sequenced: " + unsequencedElements);

		for (final ISequenceElement element1 : unsequencedElements) {
			final Set<ISequenceElement> after1 = new LinkedHashSet<ISequenceElement>();
			followerCache.put(element1, after1);
			for (final ISequenceElement element2 : data.getSequenceElements()) {
				if (element1 == element2) {
					continue;
				}
				if (checker.allowSequence(element1, element2)) {
					after1.add(element2);
				}
			}
			// part of a chain
			if ((after1.size() == 1) && (portTypeProvider.getPortType(element1) != PortType.Start)) {
				if (!tails.contains(element1)) {
					heads.add(element1);
				}
				final ISequenceElement tail = after1.iterator().next();
				tails.add(tail);
				heads.remove(tail);
			}
		}

		// Heads now contains the head of every chunk that has to go together.
		// We need to pull out all the chunks and sort out their rules
		final List<IResource> resources = new ArrayList<IResource>(data.getResources());
		{
			Collections.sort(resources, new Comparator<IResource>() {
				@Override
				public int compare(final IResource o1, final IResource o2) {
					final IVessel vessel1 = vesselProvider.getVessel(o1);
					final IVessel vessel2 = vesselProvider.getVessel(o2);
					final VesselInstanceType vit1 = vessel1.getVesselInstanceType();
					final VesselInstanceType vit2 = vessel2.getVesselInstanceType();

					int x = vit1.compareTo(vit2);
					if (x == 0) {
						x = ((Integer) vessel1.getVesselClass().getMaxSpeed()).compareTo(vessel2.getVesselClass().getMaxSpeed());
					}
					return x;
				}
			});
		}

		final Map<IResource, SequenceChunk> endChunks = new LinkedHashMap<IResource, SequenceChunk>();

		final IResourceAllocationConstraintDataComponentProvider racdcp = data.getDataComponentProvider(SchedulerConstants.DCP_resourceAllocationProvider,
				IResourceAllocationConstraintDataComponentProvider.class);

		final List<SequenceChunk> chunks = new LinkedList<SequenceChunk>();
		for (ISequenceElement head : heads) {
			final SequenceChunk chunk = new SequenceChunk();
			if (portTypeProvider.getPortType(head) == PortType.End) {
				chunk.setEndElement(true);
				for (final IResource r : data.getResources()) {
					if (head == startEndRequirementProvider.getEndElement(r)) {
						endChunks.put(r, chunk);
						break;
					}
				}
			}
			chunk.add(head);
			while (followerCache.get(head).size() == 1) {
				head = followerCache.get(head).iterator().next();
				if (!unsequencedElements.contains(head)) {
					break;
				}
				chunk.add(head);
				if (portTypeProvider.getPortType(head) == PortType.End) {
					chunk.setEndElement(true);
					for (final IResource r : data.getResources()) {
						if (head == startEndRequirementProvider.getEndElement(r)) {
							endChunks.put(r, chunk);
							break;
						}
					}
				}
			}
			chunks.add(chunk);

			// get resource count for chunk
			int rc = 0;
			for (final IResource resource : resources) {
				boolean ok = true;
				for (int c = 0; c < (chunk.size() - 1); c++) {
					if (checker.allowSequence(chunk.get(c), chunk.get(c + 1), resource) == false) {
						ok = false;
						break;
					}
				}
				if (ok) {
					rc++;
				}
			}
			chunk.setResourceCount(rc);
		}

		// now add chunks for things with only one element

		for (final Map.Entry<ISequenceElement, Set<ISequenceElement>> entry : followerCache.entrySet()) {
			final ISequenceElement place = entry.getKey();
			final PortType portType = portTypeProvider.getPortType(place);
			if (portType.equals(PortType.Start)) {
				continue;
			}

			if (tails.contains(place)) {
				continue;
			}

			if (portType.equals(PortType.End)) {
				final SequenceChunk guy = new SequenceChunk();
				guy.add(place);
				guy.setEndElement(true);
				chunks.add(guy);
				for (final IResource r : data.getResources()) {
					if (place == startEndRequirementProvider.getEndElement(r)) {
						endChunks.put(r, guy);
						break;
					}
				}
			} else if (entry.getValue().size() > 1) {
				final SequenceChunk guy = new SequenceChunk();
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

		final Comparator<SequenceChunk> comparator = new Comparator<SequenceChunk>() {
			@Override
			public int compare(final SequenceChunk chunk1, final SequenceChunk chunk2) {
				// check if either chunk is optional

				// end elements go at the end
				if (chunk2.isEndElement()) {
					return -1;
				} else if (chunk1.isEndElement()) {
					return 1;
				}

				// check number of resources for each chunk

				final int rc1 = chunk1.getResourceCount();
				final int rc2 = chunk2.getResourceCount();

				if (rc1 < rc2) {
					return -1;
				} else if (rc1 > rc2) {
					return 1;
				}

				final ISequenceElement o1 = chunk1.get(0);
				final ISequenceElement o2 = chunk2.get(0);

				final ISequenceElement e1 = chunk1.get(chunk1.size() - 1);
				final ISequenceElement e2 = chunk2.get(chunk2.size() - 1);

				final IPortSlot slot1 = portSlotProvider.getPortSlot(o1);
				final IPortSlot slot2 = portSlotProvider.getPortSlot(o2);

				final IPortSlot slot3 = portSlotProvider.getPortSlot(e1);
				final IPortSlot slot4 = portSlotProvider.getPortSlot(e2);

				final int duration1 = slot3.getTimeWindow().getStart() - slot1.getTimeWindow().getStart();
				final int duration2 = slot4.getTimeWindow().getStart() - slot2.getTimeWindow().getStart();

				// if one is much longer than the other, do it first.

				if (duration1 > duration2) {
					return -1;
				} else if (duration1 < duration2) {
					return 1;
				}

				// sort by start time
				final int s1 = slot1.getTimeWindow().getStart();
				final int s2 = slot2.getTimeWindow().getStart();

				if (s1 < s2) {
					return -1;
				} else if (s1 > s2) {
					return 1;
				}

				return chunk1.toString().compareTo(chunk2.toString());
			}
		};

		Collections.sort(chunks, comparator);

		log.debug(chunks + "");

		final ChunkChecker chunkChecker = new ChunkChecker(checker);
		final Map<IResource, List<SequenceChunk>> sequences = new LinkedHashMap<IResource, List<SequenceChunk>>();

		if (suggestion == null) {
			log.info("No suggested start solution - constructing one");
			for (final IResource resource : resources) {
				final SequenceChunk end = endChunks.get(resource);

				log.info("Scheduling elements for resource " + resource);
				final List<SequenceChunk> sequence = new ArrayList<SequenceChunk>();
				sequences.put(resource, sequence);

				// try and schedule chunks
				// first put in the start element
				final SequenceChunk start = new SequenceChunk();
				start.add(startEndRequirementProvider.getStartElement(resource));
				sequence.add(start);
				// now assign any chunks which we can to follow it
				final Iterator<SequenceChunk> iterator = chunks.iterator();
				SequenceChunk here = start;
				INITIAL_SEQUENCING_LOOP: while (iterator.hasNext()) {
					final SequenceChunk there = iterator.next();
					// check that there can come after here, and that end can come after there.
					if (chunkChecker.canFollow(here, there, resource) && ((there == end) || chunkChecker.canFollow(there, end, resource))) {
						// now check for resource advice
						for (final ISequenceElement element : there) {
							final IResource suggestedResource = resourceSuggestion.get(element);
							if ((suggestedResource != null) && (suggestedResource != resource)) {
								continue INITIAL_SEQUENCING_LOOP; // process
																	// this
																	// chunk at
																	// a later
																	// date.
							}
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
				final List<SequenceChunk> sequence = new ArrayList<SequenceChunk>();
				sequences.put(resource, sequence);

				final ISequence seq = suggestion.getSequence(resource);
				for (final ISequenceElement element : seq) {
					final SequenceChunk chunk = new SequenceChunk();
					chunk.add(element);
					sequence.add(chunk);
				}

				if (sequence.isEmpty()) {
					// insert a start element (otherwise start element came from
					// elsewhere)
					// end element should be handled by the next step,
					// hopefully.
					final SequenceChunk start = new SequenceChunk();
					start.add(startEndRequirementProvider.getStartElement(resource));
					sequence.add(start);
					// spam in any elements which will fit
					final Iterator<SequenceChunk> iterator = chunks.iterator();
					SequenceChunk here = start;
					SECOND_INSERT_LOOP: while (iterator.hasNext()) {
						final SequenceChunk there = iterator.next();

						for (final ISequenceElement element : there) {
							final IResource suggestedResource = resourceSuggestion.get(resource);
							if ((suggestedResource != null) && (suggestedResource != resource)) {
								continue SECOND_INSERT_LOOP;
							}
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
		int numTries = 1000 + chunks.size();
		while (!chunks.isEmpty() && numTries-- != 0) {
			final Iterator<SequenceChunk> iterator = chunks.iterator();
			while (iterator.hasNext()) {
				top: {
					final SequenceChunk here = iterator.next();

					// first check for suggested resource
					IResource suggestedResource = null;
					for (final ISequenceElement element : here) {
						final IResource suggestionForElement = resourceSuggestion.get(element);
						log.debug("Suggestion for " + element + " = " + suggestionForElement);
						if (suggestionForElement != null) {
							suggestedResource = suggestionForElement;
							break;
						}
					}

					if (suggestedResource != null) {
						// try inserting on suggested resource first
						log.info("Trying to insert on " + suggestedResource);
						final List<SequenceChunk> sequence = sequences.get(suggestedResource);

						if (tryInsertingChunk(chunkChecker, iterator, here, sequence, suggestedResource)) {
							break top;
						} else {
							log.info("Could not insert at suggested location");
						}
					}

					for (final Map.Entry<IResource, List<SequenceChunk>> entry : sequences.entrySet()) {
						final IResource res = entry.getKey();
						final List<SequenceChunk> sequence = entry.getValue();
						if (tryInsertingChunk(chunkChecker, iterator, here, sequence, res)) {
							break top;
						}
					}
				}
			}
			if (travelTimeChecker == null) {
				break;
			}
			// relax constraint
			final int maxLateness = travelTimeChecker.getMaxLateness();
			if (maxLateness == initialMaxLateness) {
				break;
			}

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

		final IModifiableSequences result = new ModifiableSequences(resources);
		for (final IResource resource : resources) {
			final IModifiableSequence realseq = result.getModifiableSequence(resource);
			final List<SequenceChunk> seq = sequences.get(resource);
			for (final SequenceChunk chunk : seq) {
				for (final ISequenceElement element : chunk) {
					realseq.add(element);
				}
			}
		}

		result.getModifiableUnusedElements().clear();
		result.getModifiableUnusedElements().addAll(unusedElements);

		return result;
	}

	private boolean tryInsertingChunk(final ChunkChecker chunkChecker, final Iterator<SequenceChunk> iterator, final SequenceChunk here, final List<SequenceChunk> sequence, final IResource res) {
		if (here.isEndElement()) {
			if (chunkChecker.canFollow(sequence.get(sequence.size() - 1), here, res)) {
				sequence.add(here);
				iterator.remove();
				return true;
				// break top;
			}
		} else {
			for (int i = 0; i < (sequence.size() - 1); i++) {
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
