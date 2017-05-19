/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.insertion;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

@NonNullByDefault
public class SequencesHelper {
	@Inject
	private IMoveHandlerHelper moveHandlerHelper;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private ISpotMarketSlotsProvider spotMarketSlotsProvider;

	public ISequences undoUnrelatedChanges(final ISequences source, final ISequences target, final Collection<ISequenceElement> initialElements) {

		final ILookupManager sourceLookup = new LookupManager(source);
		final ILookupManager targetLookup = new LookupManager(target);

		// The collection of elements to include
		final List<ISequenceElement> queue = new LinkedList<>();

		// Elements already checked
		final Collection<ISequenceElement> seen = new HashSet<>();

		// Resources that have been checked
		final Collection<IResource> seenResource = new HashSet<>();

		queue.addAll(initialElements);

		// Gather the set of "sequenced" resources - basically anything not a nominal. For sequenced resources, we can just replace the sequence.
		// For nominals, the sequencing is arbitrary. Nominals can either be on a specific nominal ship or on a sequenced resource, or unused. Thus we can remove them from the nominal if the resource
		// is different.
		// Note - if we do nominal vessel optimisation, this will not work as well.
		final Set<ISequenceElement> evictedElements = new HashSet<>();

		while (!queue.isEmpty()) {
			final ISequenceElement e = queue.remove(0);
			seen.add(e);

			// Skip spot slots to avoid unnecessary link ups. It will be included by dependent changes if need be.
			if (spotMarketSlotsProvider.isSpotMarketSlot(e)) {
				continue;
			}

			// Find original position of element
			@Nullable
			final Pair<@Nullable IResource, Integer> a = sourceLookup.lookup(e);

			// Is the element in the sequence?
			if (a != null && a.getFirst() != null) {
				final IResource r = a.getFirst();
				assert r != null;

				// Pull out the whole cargo
				final List<ISequenceElement> seg = moveHandlerHelper.extractSegment(source.getSequence(r), e);

				// Add all the elements of the cargo to the queue.
				queue.addAll(seg);

				if (isSequencedResource(r)) {
					// Add every element in this resource to the queue. We cannot determine whether or not they really are linked, so assume linked change.
					Iterables.addAll(queue, source.getSequence(r));
					// Mark resource as one to replace
					seenResource.add(r);
				} else {
					assert isNominalResource(r);

					// Nominal cargo, just remove the cargo if it has moved from the nominal.
					// Note: We assume nominal cargoes are fixed pairing (on the nominal) and can only be assigned to one nominal vessel instance
					final Pair<@Nullable IResource, Integer> b = targetLookup.lookup(e);
					if (b != null && b.getFirst() != null) {
						final IResource rb = b.getFirst();
						if (rb != r) {
							evictedElements.addAll(seg);
							Iterables.addAll(queue, seg);

						}
					} else {
						evictedElements.addAll(seg);
						Iterables.addAll(queue, seg);
					}
				}
			}

			// Is the element in the target sequence?
			final Pair<@Nullable IResource, Integer> b = targetLookup.lookup(e);
			if (b != null && b.getFirst() != null) {
				final IResource r = b.getFirst();
				assert r != null;
				// Extract the new cargo paring
				final List<ISequenceElement> seg = moveHandlerHelper.extractSegment(target.getSequence(r), e);
				queue.addAll(seg);
				if (isSequencedResource(r)) {
					// Add in the new resource.
					Iterables.addAll(queue, target.getSequence(r));
					seenResource.add(r);
				} else {
					assert isNominalResource(r);
					// If it is nominal in the target, then it must have been so in the source and thus no change.
				}
			}

			queue.removeAll(seen);
		}

		// Here we should have *seen* any valid element changes. Therefore any other changes are not linked and can be considered to be a hitch hiker
		{
			// TODO: Revert other changes.

			final IModifiableSequences revertedSeq = new ModifiableSequences(source);

			// Remove existing evicted elements (should all be nominals).
			for (final ISequenceElement e : evictedElements) {
				@Nullable
				final Pair<@Nullable IResource, @NonNull Integer> p = sourceLookup.lookup(e);
				@Nullable
				final IResource r = p.getFirst();
				if (p != null && r != null) {
					revertedSeq.getModifiableSequence(r).remove(e);
				}
			}

			// How? Shipped resource/DES, copy values over. For Nominals, iterate
			final Set<ISequenceElement> insertedElements = new HashSet<>();

			for (final IResource r : source.getResources()) {
				if (!seenResource.contains(r)) {
					continue;
				}
				// // Replace sequence contents.
				final IModifiableSequence s = revertedSeq.getModifiableSequence(r);
				Iterables.addAll(evictedElements, s);
				s.replaceAll(target.getSequence(r));
				Iterables.addAll(insertedElements, s);
			}
			// Finally, update unused elements list.
			evictedElements.removeAll(insertedElements);
			revertedSeq.getModifiableUnusedElements().removeAll(insertedElements);
			revertedSeq.getModifiableUnusedElements().addAll(evictedElements);

			// Sanity check output
			assert checkValidSequences(revertedSeq);

			return revertedSeq;
		}

	}

	/**
	 * Checks to ensure each element exists only once in the sequences. Multiple occurrences indicates an error in the creation of the sequences.
	 * 
	 * @param sequences
	 */
	public static boolean checkValidSequences(final @NonNull ISequences sequences) {
		final Set<ISequenceElement> seenElements = new HashSet<>();

		for (final IResource r : sequences.getResources()) {
			final ISequence s = sequences.getSequence(r);
			for (final ISequenceElement element : s) {
				if (!seenElements.add(element)) {
					return false;
				}
			}
		}
		for (final ISequenceElement e : sequences.getUnusedElements()) {
			if (!seenElements.add(e)) {
				return false;
			}
		}
		return true;
	}

	private boolean isNominalResource(final IResource resource) {

		@NonNull
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		return vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;
	}

	private boolean isSequencedResource(final IResource resource) {
		return !isNominalResource(resource);
	}
}
