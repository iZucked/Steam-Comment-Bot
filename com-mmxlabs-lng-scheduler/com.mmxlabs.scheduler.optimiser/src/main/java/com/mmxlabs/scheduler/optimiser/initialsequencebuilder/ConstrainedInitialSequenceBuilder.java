package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
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
import com.mmxlabs.scheduler.optimiser.lso.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A pairwise constraint respecting initial sequence builder. This sorts
 * elements by their earliest arrival time and then constructs a sequence for
 * each resource by picking the earliest element which can be appended to the
 * route without violating the consequent pairwise constraint.
 * 
 * @author hinton
 * 
 * @param <T>
 */
public class ConstrainedInitialSequenceBuilder<T> implements
		IInitialSequenceBuilder<T> {
	private List<IPairwiseConstraintChecker<T>> pairwiseCheckers;

	class ChunkChecker<T> {
		private LegalSequencingChecker<T> checker;

		public ChunkChecker(LegalSequencingChecker<T> realChecker) {
			this.checker = realChecker;
		}

		public boolean canFollow(SequenceChunk<T> chunk1,
				SequenceChunk<T> chunk2, IResource resource) {
			final T tail1 = chunk1.last();
			final T head2 = chunk2.first();
			return checker.allowSequence(tail1, head2, resource);
		}

		public boolean canInsert(SequenceChunk<T> before,
				SequenceChunk<T> added, SequenceChunk<T> after,
				IResource resource) {
			return canFollow(before, added, resource)
					&& canFollow(added, after, resource);
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

	public ConstrainedInitialSequenceBuilder(
			Collection<IConstraintCheckerFactory> factories) {
		this.pairwiseCheckers = new ArrayList<IPairwiseConstraintChecker<T>>();
		for (IConstraintCheckerFactory factory : factories) {
			IConstraintChecker<T> checker = factory.instantiate();
			if (checker instanceof IPairwiseConstraintChecker) {
				pairwiseCheckers.add((IPairwiseConstraintChecker<T>) checker);
			}
		}
	}

	public ConstrainedInitialSequenceBuilder(
			List<IPairwiseConstraintChecker<T>> pairwiseCheckers) {
		this.pairwiseCheckers = pairwiseCheckers;
	}

	@Override
	public ISequences<T> createInitialSequences(IOptimisationData<T> data) {
		@SuppressWarnings("unchecked")
		final IPortTypeProvider<T> portTypeProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portTypeProvider,
						IPortTypeProvider.class);

		@SuppressWarnings("unchecked")
		final IPortSlotProvider<T> portSlotProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProvider.class);

		@SuppressWarnings("unchecked")
		final IStartEndRequirementProvider<T> startEndRequirementProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_startEndRequirementProvider,
						IStartEndRequirementProvider.class);

		LegalSequencingChecker<T> checker = new LegalSequencingChecker<T>(data,
				pairwiseCheckers);

		// stick together elements which must be stuck together
		Map<T, Set<T>> followerCache = new HashMap<T, Set<T>>();
		Set<T> heads = new HashSet<T>();
		Set<T> tails = new HashSet<T>();
		for (T element1 : data.getSequenceElements()) {
			Set<T> after1 = new HashSet<T>();
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
		List<IResource> resources = new ArrayList<IResource>(
				data.getResources());
		{
			final IVesselProvider vesselProvider = data
					.getDataComponentProvider(
							SchedulerConstants.DCP_vesselProvider,
							IVesselProvider.class);
			Collections.sort(resources, new Comparator<IResource>() {
				@Override
				public int compare(IResource o1, IResource o2) {
					final IVessel vessel1 = vesselProvider.getVessel(o1);
					final IVessel vessel2 = vesselProvider.getVessel(o2);
					final VesselInstanceType vit1 = vessel1
							.getVesselInstanceType();
					final VesselInstanceType vit2 = vessel2
							.getVesselInstanceType();

					if (vit1.ordinal() < vit2.ordinal()) {
						return -1;
					} else if (vit1.ordinal() > vit2.ordinal()) {
						return 1;
					} else {
						if (vessel1.getVesselClass().getMaxSpeed() > vessel2
								.getVesselClass().getMaxSpeed()) {
							return -1;
						} else {
							return 1;
						}
					}
				}
			});
		}

		List<SequenceChunk<T>> chunks = new LinkedList<SequenceChunk<T>>();
		for (T head : heads) {
			SequenceChunk<T> chunk = new SequenceChunk<T>();
			chunk.add(head);
			while (followerCache.get(head).size() == 1) {
				head = followerCache.get(head).iterator().next();
				chunk.add(head);
			}
			chunks.add(chunk);

			// get resource count for chunk
			int rc = 0;
			for (IResource resource : resources) {
				boolean ok = true;
				for (int c = 0; c < chunk.size() - 1; c++) {
					if (checker.allowSequence(chunk.get(c), chunk.get(c + 1),
							resource) == false) {
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

		final IResourceAllocationConstraintDataComponentProvider racdcp = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_resourceAllocationProvider,
						IResourceAllocationConstraintDataComponentProvider.class);
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
			} else if (entry.getValue().size() > 1) {
				SequenceChunk<T> guy = new SequenceChunk<T>();
				guy.add(place);
				final Collection<IResource> c = racdcp
						.getAllowedResources(place);
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

				if (rc1 < rc2)
					return -1;
				else if (rc1 > rc2)
					return 1;

				final T o1 = chunk1.get(0);
				final T o2 = chunk2.get(0);

				final IPortSlot slot1 = portSlotProvider.getPortSlot(o1);
				final IPortSlot slot2 = portSlotProvider.getPortSlot(o2);
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
		Map<IResource, List<SequenceChunk<T>>> sequences = new HashMap<IResource, List<SequenceChunk<T>>>();

		for (IResource resource : resources) {
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
			while (iterator.hasNext()) {
				final SequenceChunk<T> there = iterator.next();
				if (chunkChecker.canFollow(here, there, resource)) {
					sequence.add(there);
					here = there;
					iterator.remove();
				}
			}
		}

		// chunks have been scheduled sequentially as best we can, now try
		// inserting any leftovers

		Iterator<SequenceChunk<T>> iterator = chunks.iterator();
		while (iterator.hasNext()) {
			final SequenceChunk<T> here = iterator.next();
			for (Map.Entry<IResource, List<SequenceChunk<T>>> entry : sequences
					.entrySet()) {
				final IResource res = entry.getKey();
				final List<SequenceChunk<T>> sequence = entry.getValue();
				for (int i = 0; i < sequence.size() - 1; i++) {
					if (chunkChecker.canInsert(sequence.get(i), here,
							sequence.get(i + 1), res)) {
						sequence.add(i + 1, here);
						iterator.remove();
					}
				}
			}
		}

		// OK, we have done our best, now build the modifiablesequences
		// from the intermediate gack

		IModifiableSequences<T> result = new ModifiableSequences<T>(resources);
		for (final IResource resource : resources) {
			IModifiableSequence<T> realseq = result
					.getModifiableSequence(resource);
			final List<SequenceChunk<T>> seq = sequences.get(resource);
			for (SequenceChunk<T> chunk : seq) {
				for (T element : chunk) {
					realseq.add(element);
				}
			}
		}

		System.err.println("Leftover chunks: " + chunks);

		return result;
	}

}
