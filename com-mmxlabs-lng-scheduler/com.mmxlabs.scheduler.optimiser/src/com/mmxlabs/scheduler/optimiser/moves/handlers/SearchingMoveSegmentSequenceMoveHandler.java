/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.moves.impl.NullMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertSegmentMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IBreakPointHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

/**
 * Alternative {@link MoveSegmentSequenceMoveHandler} implementation performing a more intensive search over possible move options.
 * 
 * @author Simon Goodall
 *
 */
public class SearchingMoveSegmentSequenceMoveHandler implements IMoveGenerator {

	private static final String MOVE_NAME = "MoveSegment";

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

			if (resource1 != resource2) {
				continue LOOP_BREAK_POINTS;
			}
			// Ensure shipped resources
			if (!moveHelper.isShippedResource(resource1)) {
				continue LOOP_BREAK_POINTS;
			}
			if (!moveHelper.isShippedResource(resource2)) {
				continue LOOP_BREAK_POINTS;
			}
			final IMove move = segmentMoveCheck(resource1, pos1.getSecond(), pos2.getSecond(), lookupManager, random);
			if (move != null) {
				return move;
			}
		}
		return new NullMove(MOVE_NAME, "No options found");
	}

	public @Nullable IMove segmentMoveCheck(final IResource resource, final int position1, final int position2, final ILookupManager lookupManager, final Random random) {

		final ISequences sequences = lookupManager.getRawSequences();
		final ISequence sequence1 = sequences.getSequence(resource);
		final int beforeFirstCut = Math.min(position1, position2);
		final int beforeSecondCut = Math.max(position1, position2) - 1;
		// Zero length segment
		if (beforeFirstCut == beforeSecondCut) {
			return null;
		}

		final ISequenceElement firstElementInSegment = sequence1.get(beforeFirstCut + 1);
		final ISequenceElement lastElementInSegment = sequence1.get(beforeSecondCut);
		final ISequenceElement nextElementInSegment = sequence1.get(beforeSecondCut + 1);

		// Collect the elements which can go after the segment we are cutting out
		final Followers<ISequenceElement> followers = followersAndPreceders.getValidFollowers(lastElementInSegment);

		// Pick one of these followers and find where it is at the moment
		if (followers.size() == 0) {
			return null;
		}

		final List<ISequenceElement> elements = new LinkedList<>();
		for (int idx = beforeFirstCut + 1; idx <= beforeSecondCut; ++idx) {
			final ISequenceElement e = sequence1.get(idx);
			elements.add(e);
		}
		assert elements.size() > 0;

		final List<Pair<IResource, ISequenceElement>> insertionTargets = new LinkedList<>();

		final List<ISequenceElement> randomFollowers = new LinkedList<>(followers.asList());
		Collections.shuffle(randomFollowers, random);
		LOOP_FOLLOWERS: for (final ISequenceElement possibleInsertBeforeThisElement : randomFollowers) {

			if (possibleInsertBeforeThisElement == nextElementInSegment) {
				// Current end element!
				continue LOOP_FOLLOWERS;
			}

			final Pair<IResource, Integer> posPrecursor = lookupManager.lookup(possibleInsertBeforeThisElement);

			// Unused element?
			final IResource targetResource = posPrecursor.getFirst();
			if (targetResource == null) {
				continue LOOP_FOLLOWERS;
			}
			// now check whether the element before the precursor can precede
			// the first element in the segment
			final ISequenceElement beforeInsert = lookupManager.getRawSequences().getSequence(targetResource).get(posPrecursor.getSecond() - 1);
			if (followersAndPreceders.getValidFollowers(beforeInsert).contains(firstElementInSegment)) {
				// we have a legal 3opt2, so do that. It might be a 3opt1
				// really, but that's OK
				// so long as we don't insert a segment into itself.
				if (targetResource == resource) {
					// check for stupidity
					final int position3 = posPrecursor.getSecond();
					if ((position3 >= beforeFirstCut) && (position3 <= beforeSecondCut)) {
						continue LOOP_FOLLOWERS;
					}
				} else {
					// Check if the full segment can be moved
					for (final ISequenceElement e : elements) {
						if (!moveHelper.checkResource(e, targetResource)) {
							continue LOOP_FOLLOWERS;
						}
					}
				}
				insertionTargets.add(new Pair<>(targetResource, possibleInsertBeforeThisElement));
			}
		}

		if (insertionTargets.size() > 0) {
			final Pair<IResource, ISequenceElement> target = RandomHelper.chooseElementFrom(random, insertionTargets);
			return InsertSegmentMove.Builder.newMove() //
					.withElements(resource, elements) //
					.withInsertBefore(target.getFirst(), target.getSecond()) //
					.create();
		}

		return null;
	}
}
