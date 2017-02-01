/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.lso.moves.SwapSingleSequenceElements;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;

/**
 * A module for the {@link ConstrainedMoveGenerator} which swaps slots within a single sequence. This is intended for use with {@link VesselInstanceType#CARGO_SHORTS}. Note this cannot currently swap
 * two elements which are adjacent to each other. This would require additional work around the follower / preceder checks.
 * 
 * @author Simon Goodall
 * 
 */
public class SwapElementsInSequenceMoveGeneratorUnit implements IConstrainedMoveGeneratorUnit {

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	private final ConstrainedMoveGenerator owner;

	public SwapElementsInSequenceMoveGeneratorUnit(final ConstrainedMoveGenerator owner) {
		super();
		this.owner = owner;
	}

	@Override
	public SwapSingleSequenceElements generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager stateManager, @NonNull Random random) {

		final ISequences sequences = rawSequences;

		// Find a random none-empty ISequence
		ISequence sequence = null;
		IResource resource = null;
		for (int i = 0; i < sequences.size(); ++i) {
			final int idx = RandomHelper.nextIntBetween(random, 0, sequences.size() - 1);
			final ISequence s = sequences.getSequence(idx);
			if (s.size() < 2) {
				continue;
			}
			sequence = s;
			resource = sequences.getResources().get(idx);
			break;
		}
		if (sequence == null) {
			return null;
		}
		// Get a list of all the elements for outer loop
		final List<ISequenceElement> sequenceElementsAsList = new ArrayList<ISequenceElement>(sequence.size());
		for (final ISequenceElement e : sequence) {
			sequenceElementsAsList.add(e);
		}

		// Clone list for inner loop
		final List<ISequenceElement> bucket = new ArrayList<ISequenceElement>(sequenceElementsAsList);
		// Randomise orders independently
		Collections.shuffle(sequenceElementsAsList, random);
		Collections.shuffle(bucket, random);

		for (final ISequenceElement e : sequenceElementsAsList) {
			final int eIdx = stateManager.lookup(e).getSecond();
			// Remove element so it is not considered again in the inner loop. This avoids repeating the search e -> f when e has become f.
			bucket.remove(e);

			final ISequenceElement beforeE = eIdx == 0 ? null : sequence.get(eIdx - 1);
			final ISequenceElement afterE = eIdx == sequence.size() - 1 ? null : sequence.get(eIdx + 1);

			// Find possible element to swap - this list will be reduced overtime
			for (final ISequenceElement f : bucket) {
				final int fIdx = stateManager.lookup(f).getSecond();

				final ISequenceElement beforeF = fIdx == 0 ? null : sequence.get(fIdx - 1);
				final ISequenceElement afterF = fIdx == sequence.size() - 1 ? null : sequence.get(fIdx + 1);

				// @formatter:off
				if (checkSequence(beforeE, f, true) 
						&& checkSequence(afterE, f, false)
						&& checkSequence(beforeF, e, true)
						&& checkSequence(afterF, e, false)) {
					// @formatter:on
					// We can swap these elements!
					return new SwapSingleSequenceElements(resource, eIdx, fIdx);
				}
			}

		}
		return null;
	}

	/**
	 * Check whether or not the two elements can be sequenced. A null element a will return true
	 * 
	 * @param a
	 * @param b
	 * @param follower
	 * @return
	 */
	public boolean checkSequence(final ISequenceElement a, final ISequenceElement b, final boolean follower) {
		if (a == null) {
			return true;
		}
		if (follower) {
			return followersAndPreceders.getValidFollowers(a).contains(b);
		} else {
			return followersAndPreceders.getValidPreceders(a).contains(b);
		}
	}
}
