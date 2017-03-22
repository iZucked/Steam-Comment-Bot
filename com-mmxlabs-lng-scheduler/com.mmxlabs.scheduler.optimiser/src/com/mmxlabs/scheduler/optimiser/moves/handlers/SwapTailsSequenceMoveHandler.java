/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.handlers;

import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.Move2over2;
import com.mmxlabs.optimiser.lso.impl.NullMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IBreakPointHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;

public class SwapTailsSequenceMoveHandler implements IMoveGenerator {

	@Inject
	private IMoveHandlerHelper moveHelper;

	@Inject
	private IBreakPointHelper breakHelper;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	@Override
	public IMove generateMove(@NonNull final ISequences rawSequences, @NonNull final ILookupManager lookupManager, @NonNull final Random random) {

		final Triple<String, Pair<IResource, Integer>, Pair<IResource, Integer>> initialPositions = moveHelper.selectInitialPositions(breakHelper.getValidBreaks(), lookupManager, random);

		// Catch and break at null entries
		if (initialPositions.getFirst() != null) {
			return new NullMove("SwapTails", initialPositions.getFirst());
		}

		final Pair<IResource, Integer> pos1 = initialPositions.getSecond();
		final Pair<IResource, Integer> pos2 = initialPositions.getThird();

		if (!moveHelper.checkPositions(pos1, pos2)) {
			return new NullMove("SwapTails", "One or both selections indexed at -1.");
		}

		final IResource sequence1 = pos1.getFirst();
		final IResource sequence2 = pos2.getFirst();
		final int position1 = pos1.getSecond();
		final int position2 = pos2.getSecond();

		if (sequence1 == sequence2) {
			return new NullMove("SwapTails", "Same sequence selected.");
		}

		final ISequence seq1 = rawSequences.getSequence(sequence1);
		final ISequence seq2 = rawSequences.getSequence(sequence2);

		boolean valid2opt2 = followersAndPreceders.getValidFollowers(seq2.get(position2 - 1)).contains(seq1.get(position1 + 1));

		if (valid2opt2) {
			return swapTailsMove(sequence1, sequence2, position1, position2);
		}

		return new NullMove("SwapTails", "Cannot swap tails.");
	}

	public IMove swapTailsMove(final IResource sequence1, final IResource sequence2, final int position1, final int position2) {
		// make 2opt2
		final Move2over2 result = new Move2over2();

		result.setResource1(sequence1);
		result.setResource2(sequence2);
		// add 1 because the positions are inclusive, and we need to cut
		// after the first element
		result.setResource1Position(position1 + 1);
		result.setResource2Position(position2);
		return result;
	}
}
