/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.insertion;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

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
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;

@NonNullByDefault
public class SequencesUndoSpotHelper {
	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private ISpotMarketSlotsProvider spotMarketSlotsProvider;

	@Inject
	private Provider<LookupManager> lookupManagerProvider;

	public ISequences undoSpotMarketSwaps(final ISequences original, final ISequences current) {

		final ILookupManager originalLookup = lookupManagerProvider.get();
		originalLookup.createLookup(original);
		final ILookupManager currentLookup = lookupManagerProvider.get();
		currentLookup.createLookup(current);

		final IModifiableSequences revertedSeq = new ModifiableSequences(current);

		IResource[] changedResources = new IResource[2];

		for (final IResource r : revertedSeq.getResources()) {
			final IModifiableSequence seq = revertedSeq.getModifiableSequence(r);

			for (int i = 0; i < seq.size(); ++i) {
				final ISequenceElement e = seq.get(i);
				if (spotMarketSlotsProvider.isSpotMarketSlot(e)) {
					// Load or discharge?
					int offset = 0;
					if (isLoadSlot(e)) {
						offset = +1;
					} else {
						assert isDischargeSlot(e);
						offset = -1;
					}
					// This is the other half of the deal.
					// NOTE: LD cargoes only!
					final ISequenceElement currentLinkedSlot = seq.get(i + offset);
					final Pair<@Nullable IResource, Integer> p = originalLookup.lookup(currentLinkedSlot);
					if (p != null) {
						final IResource originalResource = p.getFirst();
						if (originalResource != null) {
							final ISequence originalSeq = original.getSequence(originalResource);
							// If e is a load, this is the original load for the linked discharge.
							// Likewise if e is a discharge, this is the original discharge for the linked load
							final ISequenceElement originalElement = originalSeq.get(p.getSecond() - offset);
							if (!spotMarketSlotsProvider.isSpotMarketSlot(originalElement)) {
								continue;
							}

							if (originalElement == e) {
								// Same element, no change
							} else if (spotMarketSlotsProvider.isEquivalentOption(originalElement, e)) {
								// Same market and month, just another instance!
								// Swap the current for the original.
								seq.set(i, originalElement);
								changedResources[0] = r;

								final Pair<@Nullable IResource, Integer> lookup = currentLookup.lookup(originalElement);
								if (lookup != null) {
									final IResource newResource = lookup.getFirst();
									if (newResource == null) {
										revertedSeq.getModifiableUnusedElements().set(lookup.getSecond(), e);
									} else {
										revertedSeq.getModifiableSequence(newResource).set(lookup.getSecond(), e);
									}
									changedResources[1] = newResource;
								} else {
									changedResources[1] = null;
								}
								// Re-generate the lookup table
								currentLookup.updateLookup(revertedSeq, changedResources);
							}
						}
					}
				}
			}
		}

		if (!SequencesHitchHikerHelper.checkValidSequences(revertedSeq)) {
			// Error!
			return current;
		}

		return revertedSeq;
	}

	public boolean isLoadSlot(@NonNull final ISequenceElement element) {

		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return portSlot instanceof ILoadOption;
	}

	public boolean isDischargeSlot(@NonNull final ISequenceElement element) {

		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return portSlot instanceof IDischargeOption;
	}

}
