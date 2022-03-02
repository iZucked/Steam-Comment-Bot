/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.google.inject.Provider;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

@NonNullByDefault
public class SequencesHitchHikerHelper {

	@Inject
	private IMoveHandlerHelper moveHandlerHelper;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPhaseOptimisationData phaseOptimisationData;

	@Inject
	private Provider<LookupManager> lookupManagerProvider;

	private ISequences __undoUnrelatedChanges(final ISequences source, final ISequences target, final Collection<ISequenceElement> initialElements) {

		final ILookupManager sourceLookup = lookupManagerProvider.get();
		sourceLookup.createLookup(source);

		final ILookupManager targetLookup = lookupManagerProvider.get();
		targetLookup.createLookup(target);

		// The collection of elements to include
		final List<ISequenceElement> queue = new LinkedList<>();

		// Elements already checked
		final Collection<ISequenceElement> seen = new HashSet<>();

		// Resources that have been checked
		final Collection<IResource> seenResource = new HashSet<>();

		queue.addAll(initialElements);

		// Gather the set of "sequenced" resources - basically anything not a nominal.
		// For sequenced resources, we can just replace the sequence.
		// For nominals, the sequencing is arbitrary. Nominals can either be on a
		// specific nominal ship or on a sequenced resource, or unused. Thus we can
		// remove them from the nominal if the resource
		// is different.
		// Note - if we do nominal vessel optimisation, this will not work as well.
		final Set<ISequenceElement> evictedElements = new HashSet<>();

		while (!queue.isEmpty()) {
			final ISequenceElement e = queue.remove(0);
			assert e != null;

			seen.add(e);

			// 2017-08-22 - SG - Disable this check as is can cause spot slot to be used
			// multiple time now with change to only mark the segment as evicted rather than
			// whole sequence. Keep an eye on
			// spot results!

			// // Skip spot slots to avoid unnecessary link ups. It will be included by
			// dependent changes if need be.
			// if (spotMarketSlotsProvider.isSpotMarketSlot(e)) {
			// continue;
			// }

			// Find original position of element
			final @Nullable Pair<@Nullable IResource, Integer> a = sourceLookup.lookup(e);

			// Is the element in the sequence?
			if (a != null && a.getFirst() != null) {
				final IResource r = a.getFirst();
				assert r != null;

				// Pull out the whole cargo
				final List<ISequenceElement> seg = moveHandlerHelper.extractSegment(source.getSequence(r), e);

				// Add all the elements of the cargo to the queue.
				queue.addAll(seg);

				if (isSequencedResource(r)) {
					// For spot charter's just pull the cargo off and ignore the rest. If a related
					// cargo goes back on then we consider it.
					// This stops hitch-hikers which make use of the shipping length, but are
					// otherwise unrelated.
					// Note, if we have a sequence a->b->c and remove cargo b, we may introduce a
					// violation that is fixed by using this shipping length.
					// Potentially we should see if this is the first or last cargo otherwise
					// fallback to general method.
					if (true || isSpotCharterResource(r)) {
						evictedElements.addAll(seg);
						Iterables.addAll(queue, seg);
					} else {
						// Add every element in this resource to the queue. We cannot determine whether
						// or not they really are linked, so assume linked change.
						Iterables.addAll(queue, source.getSequence(r));
						// Mark resource as one to replace
						seenResource.add(r);
					}
				} else {
					assert isNominalResource(r);

					// Nominal cargo, just remove the cargo if it has moved from the nominal.
					// Note: We assume nominal cargoes are fixed pairing (on the nominal) and can
					// only be assigned to one nominal vessel instance
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
					// If it is nominal in the target, then it must have been so in the source and
					// thus no change.
				}
			}

			queue.removeAll(seen);
		}

		// Here we should have *seen* any valid element changes. Therefore any other
		// changes are not linked and can be considered to be a hitch hiker
		{
			// TODO: Revert other changes.

			final IModifiableSequences revertedSeq = new ModifiableSequences(source);

			// Remove existing evicted elements (should all be nominals).
			for (final ISequenceElement e : evictedElements) {
				assert e != null;

				final @Nullable Pair<@Nullable IResource, @NonNull Integer> p = sourceLookup.lookup(e);
				final @Nullable IResource r = p.getFirst();
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

	public ISequences undoUnrelatedChanges(final ISequences source, final ISequences target, final Collection<ISequenceElement> initialElements) {

		ISequences revertedSeq = __undoUnrelatedChanges(source, target, initialElements);

		// Loop a few times to make sure we include everything. After first pass it is
		// possible elements moved out of a sequence have not been re-included.
		for (int i = 0; i < 4; ++i) {
			final List<ISequenceElement> recheck = new LinkedList<>();
			for (final ISequenceElement slot : revertedSeq.getUnusedElements()) {
				assert slot != null;
				
				if (phaseOptimisationData.isElementRequired(slot) || phaseOptimisationData.getSoftRequiredElements().contains(slot)) {
					if (!target.getUnusedElements().contains(slot)) {
						recheck.add(slot);
					}
				}
			}
			if (recheck.isEmpty()) {
				return revertedSeq;
			}
			revertedSeq = __undoUnrelatedChanges(revertedSeq, target, recheck);

		}
		// failed
		return target;

	}

	/**
	 * Checks to ensure each element exists only once in the sequences. Multiple occurrences indicates an error in the creation of the sequences.
	 * 
	 * @param sequences
	 */
	public static boolean checkValidSequences(final ISequences sequences) {
		final Set<ISequenceElement> seenElements = new HashSet<>();

		for (final IResource r : sequences.getResources()) {
			if (r == null) {
				return false;
			}
			final ISequence s = sequences.getSequence(r);
			for (final ISequenceElement element : s) {
				if (element == null) {
					return false;
				}

				if (!seenElements.add(element)) {
					return false;
				}
			}
		}
		for (final ISequenceElement e : sequences.getUnusedElements()) {
			if (e == null) {
				return false;
			}
			if (!seenElements.add(e)) {
				return false;
			}
		}
		return true;
	}

	private boolean isNominalResource(final IResource resource) {

		final @NonNull IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		return vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;
	}

	private boolean isSpotCharterResource(final IResource resource) {

		final @NonNull IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		return vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER;
	}

	private boolean isSequencedResource(final IResource resource) {
		return !isNominalResource(resource);
	}
}
