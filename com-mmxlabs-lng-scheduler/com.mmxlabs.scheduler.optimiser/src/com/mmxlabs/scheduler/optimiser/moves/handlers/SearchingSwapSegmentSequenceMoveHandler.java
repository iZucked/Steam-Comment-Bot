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
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.moves.impl.Move4over2;
import com.mmxlabs.optimiser.lso.moves.impl.NullMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IBreakPointHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;

/**
 * Alternative {@link SwapSegmentSequenceMoveHandler} implementation performing a more intensive search over possible move options.
 * 
 * @author Simon Goodall
 *
 */
public class SearchingSwapSegmentSequenceMoveHandler implements IMoveGenerator {

	private static final String MOVE_NAME = "SwapSegment";

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

			// Now loop forwards to find valid segments to swap. We only loop through one sequences as the search the all followers and the position checks covers the second sequence.
			// TODO: Is it better to do a second loop? How does the scaling work as scenario size increases?
			final List<Pair<Integer, Integer>> viableSecondBreaks = new ArrayList<>();

			final int position1 = pos1.getSecond();
			final int position2 = pos2.getSecond();

			LOOP_SEQUENCE: for (int position2_end = position2; position2_end < (sequence2.size() - 1); position2_end++) { // ignore last element
				final ISequenceElement here = sequence2.get(position2_end);
				final ISequenceElement possibleSegment2AfterElement = sequence2.get(position2_end + 1);

				LOOP_FOLLOWERS: for (final ISequenceElement elt : followersAndPreceders.getValidFollowers(here)) {
					final Pair<IResource, Integer> loc = lookupManager.lookup(elt);
					final IResource first = loc.getFirst();
					if (first == null) {
						// Most likely an optional element no longer in sequences
						continue LOOP_FOLLOWERS;
					}
					if (first != resource1) {
						// Not on target resource
						continue LOOP_FOLLOWERS;
					}
					if (loc.getSecond() <= position1 + 1) {
						// Element is before (or is the) segment start
						continue LOOP_FOLLOWERS;
					}

					// Check reverse swap is valid
					@NonNull
					final ISequenceElement possibleSegment1EndElement = sequence1.get(loc.getSecond() - 1);

					if (!followersAndPreceders.getValidFollowers(possibleSegment1EndElement).contains(possibleSegment2AfterElement)) {
						continue LOOP_FOLLOWERS;
					}

					// Check if the full segment can be swapped
					final List<ISequenceElement> e1 = new LinkedList<ISequenceElement>();
					for (int idx = position1 + 1; idx < loc.getSecond(); ++idx) {
						final ISequenceElement e = sequence1.get(idx);
						e1.add(e);
						if (!moveHelper.checkResource(e, resource2)) {
							continue LOOP_FOLLOWERS;
						}
					}
					final List<ISequenceElement> e2 = new LinkedList<ISequenceElement>();

					for (int idx = position2; idx <= position2_end; ++idx) {
						final ISequenceElement e = sequence2.get(idx);
						e2.add(e);
						if (!moveHelper.checkResource(e, resource1)) {
							continue LOOP_FOLLOWERS;
						}
					}

					viableSecondBreaks.add(new Pair<Integer, Integer>(position2_end, loc.getSecond()));
				}
			}

			if (viableSecondBreaks.size() > 0) {
				final Pair<Integer, Integer> selectedSecondBreak = RandomHelper.chooseElementFrom(random, viableSecondBreaks);
				final int secondPosition1 = selectedSecondBreak.getSecond();
				final int secondPosition2 = selectedSecondBreak.getFirst();

				// 4opt2
				final Move4over2 result = new Move4over2(resource1, position1 + 1, secondPosition1, resource2, position2, secondPosition2 + 1);
				return result;
			}
		}
		return new NullMove(MOVE_NAME, "No valid options found");
	}
}
