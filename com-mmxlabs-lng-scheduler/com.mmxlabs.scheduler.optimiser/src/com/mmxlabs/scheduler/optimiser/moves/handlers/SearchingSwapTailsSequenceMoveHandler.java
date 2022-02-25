/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.moves.impl.Move2over2;
import com.mmxlabs.optimiser.lso.moves.impl.NullMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IBreakPointHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;

/**
 * Alternative {@link SwapTailsSequenceMoveHandler} implementation performing a more intensive search over possible move options.
 * 
 * @author Simon Goodall
 *
 */
public class SearchingSwapTailsSequenceMoveHandler implements IMoveGenerator {

	private static final String MOVE_NAME = "SwapTails";

	@Inject
	private IMoveHelper moveHelper;

	@Inject
	private IBreakPointHelper breakHelper;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	@Override
	public IMove generateMove(@NonNull final ISequences rawSequences, @NonNull final ILookupManager lookupManager, @NonNull final Random random) {

		final List<Pair<ISequenceElement, ISequenceElement>> validBreaks = breakHelper.getValidBreaks();

		if (validBreaks.isEmpty()) {
			return new NullMove(MOVE_NAME, "No break points found.");
		}
		final List<Pair<ISequenceElement, ISequenceElement>> randomisedValidBreaks = new ArrayList<>(breakHelper.getValidBreaks());
		Collections.shuffle(randomisedValidBreaks, random);

		LOOP_BREAK_POINTS: for (final Pair<ISequenceElement, ISequenceElement> newPair : randomisedValidBreaks) {

			final ISequenceElement seq1Before = newPair.getFirst();
			final ISequenceElement seq2After = newPair.getSecond();
			assert seq1Before != null;
			assert seq2After != null;

			final Pair<IResource, Integer> pos1 = lookupManager.lookup(seq1Before);
			final Pair<IResource, Integer> pos2 = lookupManager.lookup(seq2After);

			// Are both elements in use?
			final IResource resource2 = pos2.getFirst();
			final IResource resource1 = pos1.getFirst();
			if ((resource1 == null) || (resource2 == null)) {
				continue LOOP_BREAK_POINTS;
			}

			if (resource1 == resource2) {
				continue LOOP_BREAK_POINTS;
			}

			// Ensure shipped resources
			if (!moveHelper.isShippedResource(resource1)) {
				continue LOOP_BREAK_POINTS;
			}
			if (!moveHelper.isShippedResource(resource2)) {
				continue LOOP_BREAK_POINTS;
			}

			// By virtue of the break point we know the second resource tail can follow the head of the first resource. Now check the reverse is true.
			final ISequence sequence1 = rawSequences.getSequence(resource1);
			final ISequence sequence2 = rawSequences.getSequence(resource2);

			final ISequenceElement seq1After = sequence1.get(pos1.getSecond() + 1);
			final ISequenceElement seq2Before = sequence2.get(pos2.getSecond() - 1);

			if (!followersAndPreceders.getValidFollowers(seq2Before).contains(seq1After)) {
				continue LOOP_BREAK_POINTS;
			}

			// Check if the full tail can be swapped
			final List<ISequenceElement> e1 = new LinkedList<ISequenceElement>();

			for (int i = pos1.getSecond() + 1; i < sequence1.size() - 1; ++i) {
				final ISequenceElement e = sequence1.get(i);
				e1.add(e);
				if (!moveHelper.checkResource(e, resource2)) {
					continue LOOP_BREAK_POINTS;
				}
			}
			final List<ISequenceElement> e2 = new LinkedList<ISequenceElement>();

			for (int i = pos2.getSecond(); i < sequence2.size() - 1; ++i) {
				final ISequenceElement e = sequence2.get(i);
				e2.add(e);
				if (!moveHelper.checkResource(e, resource1)) {
					continue LOOP_BREAK_POINTS;
				}
			}
			if (e1.size() == 0 && e2.size() == 0) {
				continue LOOP_BREAK_POINTS;
			}
			return makeSwapTailsMove(resource1, resource2, pos1.getSecond(), pos2.getSecond());

		}

		return new NullMove(MOVE_NAME, "No valid break found.");
	}

	public IMove makeSwapTailsMove(final IResource sequence1, final IResource sequence2, final int position1, final int position2) {
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
