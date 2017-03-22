/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.Move4over2;
import com.mmxlabs.optimiser.lso.impl.NullMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IBreakPointHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;

public class SwapSegmentSequenceMoveHandler implements IMoveGenerator {

	@Inject
	private IMoveHandlerHelper moveHelper;

	@Inject
	private IBreakPointHelper breakHelper;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	@Override
	public IMove generateMove(@NonNull final ISequences rawSequences, @NonNull final ILookupManager stateManager, @NonNull final Random random) {

		final Triple<String, Pair<IResource, Integer>, Pair<IResource, Integer>> initialPositions = moveHelper.selectInitialPositions(breakHelper.getValidBreaks(), stateManager, random);

		// Catch and break at null entries
		if (initialPositions.getFirst() != null) {
			return new NullMove("SwapSegment", initialPositions.getFirst());
		}

		final Pair<IResource, Integer> pos1 = initialPositions.getSecond();
		final Pair<IResource, Integer> pos2 = initialPositions.getThird();

		if (!moveHelper.checkPositions(pos1, pos2)) {
			return new NullMove("SwapSegment", "Selection(s) Indexed at -1");
		}

		final IResource sequence1 = pos1.getFirst();
		final IResource sequence2 = pos2.getFirst();
		final int position1 = pos1.getSecond();
		int position2 = pos2.getSecond();

		if (sequence1 == sequence2) {
			return new NullMove("SwapSegment", "Same Sequence Selected");
		} else if (position2 > 0) {

			final ISequence seq1 = stateManager.getRawSequences().getSequence(sequence1);
			final ISequence seq2 = stateManager.getRawSequences().getSequence(sequence2);

			boolean valid2opt2 = followersAndPreceders.getValidFollowers(seq2.get(position2 - 1)).contains(seq1.get(position1 + 1));

			while (!valid2opt2 && (position2 > 1)) {
				// rewind position 2? after all if we don't have a valid 2opt2
				// we probably won't get a valid 4opt2 out of it either?
				position2--;
				// FIXME: This changes the original break point and we can longer assume the reverse position is still valid
				valid2opt2 = followersAndPreceders.getValidFollowers(seq2.get(position2 - 1)).contains(seq1.get(position1 + 1));
			}

			if (!valid2opt2) {
				return new NullMove("SwapSegment", "No Valid 2opt2.");
			}

			final List<Pair<Integer, Integer>> viableSecondBreaks = new ArrayList<Pair<Integer, Integer>>();

			for (int i = position2; i < (seq2.size() - 1); i++) { // ignore last element
				final ISequenceElement here = seq2.get(i);
				for (final ISequenceElement elt : followersAndPreceders.getValidFollowers(here)) {
					final Pair<IResource, Integer> loc = stateManager.lookup(elt);
					final IResource first = loc.getFirst();
					if (first == null) {
						// Most likely an optional element no longer in sequences
						continue;
					}
					if (first == sequence1) {
						// it can be adjacent to something in sequence 1,
						// that's good
						if (loc.getSecond() > position1 + 1) {
							// it's something after A, that's even better!
							// now we need to check that we can put the
							// chunk cut out of S1 into S2 here

							// 4opt2 check
							if (valid2opt2 && followersAndPreceders.getValidFollowers(stateManager.getRawSequences().getSequence(first).get(loc.getSecond() - 1)).contains(seq2.get(i + 1))) {
								viableSecondBreaks.add(new Pair<Integer, Integer>(i, loc.getSecond()));
							}
						}
					}
				}
			}

			if (viableSecondBreaks.size() < 1) {
				return new NullMove("SwapSegment", "No Valid Second Breaks");
			}

			final Pair<Integer, Integer> selectedSecondBreak = RandomHelper.chooseElementFrom(random, viableSecondBreaks);
			final int secondPosition1 = selectedSecondBreak.getSecond();
			final int secondPosition2 = selectedSecondBreak.getFirst();

			if (secondPosition1 != (position1 + 1)) {
				// 4opt2
				final Move4over2 result = new Move4over2(sequence1, position1 + 1, secondPosition1, sequence2, position2, secondPosition2 + 1);
				return result;
			}

			return new NullMove("SwapSegment", "Sequential Position Selected");
		}
		return new NullMove("SwapSegment", "Zero index on Second Position");
	}

}
