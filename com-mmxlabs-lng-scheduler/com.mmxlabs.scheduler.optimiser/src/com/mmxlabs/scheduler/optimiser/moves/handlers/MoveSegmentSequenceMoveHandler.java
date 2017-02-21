package com.mmxlabs.scheduler.optimiser.moves.handlers;

import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.Move3over2;
import com.mmxlabs.optimiser.lso.impl.NullMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IBreakPointHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

public class MoveSegmentSequenceMoveHandler implements IMoveGenerator {

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
			return new NullMove("MoveSegment", initialPositions.getFirst());
		}

		final Pair<IResource, Integer> pos1 = initialPositions.getSecond();
		final Pair<IResource, Integer> pos2 = initialPositions.getThird();

		if (!moveHelper.checkPositions(pos1, pos2)) {
			return new NullMove("MoveSegment", "One or both selections indexed at -1.");
		}

		final IResource sequence1 = pos1.getFirst();
		final IResource sequence2 = pos2.getFirst();
		final int position1 = pos1.getSecond();
		final int position2 = pos2.getSecond();

		if (sequence1 == sequence2) {
			return segmentMoveCheck(sequence1, sequence2, position1, position2, lookupManager, random);
		} else {
			// Not able to MoveSegment
			return new NullMove("MoveSegment", "Selections from differing sequences.");
		}
	}

	public IMove segmentMoveCheck(final IResource sequence1, final IResource sequence2, final int position1, final int position2, final ILookupManager lookupManager, final Random random) {

		// we have found a segment which we can legally excise from a route;
		// now we must
		// choose somewhere to insert it.
		// the only two (currently implemented) options here are 3opt2 and
		// 4opt1
		// I think 3opt2 is worth looking for first, as more requirements =>
		// less feasible.

		final ISequences sequences = lookupManager.getRawSequences();
		final ISequence sequence = sequences.getSequence(sequence1);
		final int beforeFirstCut = Math.min(position1, position2);
		final int beforeSecondCut = Math.max(position1, position2) - 1;
		// Zero length segment
		if (beforeFirstCut == beforeSecondCut) {
			return new NullMove("MoveSegment", "Zero length segment.");
		}

		final ISequenceElement firstElementInSegment = sequence.get(beforeFirstCut + 1);
		final ISequenceElement lastElementInSegment = sequence.get(beforeSecondCut);

		// Collect the elements which can go after the segment we are cutting out
		final Followers<ISequenceElement> followers = followersAndPreceders.getValidFollowers(lastElementInSegment);

		// Pick one of these followers and find where it is at the moment
		if (followers.size() == 0) {
			return new NullMove("MoveSegment", "No valid followers.");
		}
		final ISequenceElement precursor = followers.get(random.nextInt(followers.size()));
		final Pair<IResource, Integer> posPrecursor = lookupManager.lookup(precursor);

		// now check whether the element before the precursor can precede
		// the first element in the segment
		final IResource first = posPrecursor.getFirst();
		if (first == null) {
			return new NullMove("MoveSegment", "Null first precursor.");
		}
		final ISequenceElement beforeInsert = lookupManager.getRawSequences().getSequence(first).get(posPrecursor.getSecond() - 1);
		if (followersAndPreceders.getValidFollowers(beforeInsert).contains(firstElementInSegment)) {
			// we have a legal 3opt2, so do that. It might be a 3opt1
			// really, but that's OK
			// so long as we don't insert a segment into itself.
			if (first.equals(sequence1)) {
				// check for stupidity
				final int position3 = posPrecursor.getSecond();
				if ((position3 >= beforeFirstCut) && (position3 <= beforeSecondCut)) {
					return new NullMove("MoveSegment", "Attempting to insert segment into itself."); // stupidity has happened.
				}
			}
			return moveSegmentMove(sequence1, first, beforeFirstCut + 1, beforeSecondCut, beforeFirstCut + 1);
		} else {
			// we chose a bad place to insert ; the segment will not fit
			// TODO could stick this in a loop and try a few times before
			// bailing out
			// maybe search for a 4opt1 in here? but will a 4opt1 work if a
			// 3opt1 won't? probably not!
			return new NullMove("MoveSegment", "Invalid move segement attempt.");
		}

	}

	public IMove moveSegmentMove(final IResource resource1, final IResource resource2, final int res1Start, final int res1End, final int res2Pos) {

		final Move3over2 result = new Move3over2();
		result.setResource1(resource1);
		result.setResource1Start(res1Start);
		result.setResource1End(res1End);

		result.setResource2(resource2);
		result.setResource2Position(res2Pos);
		return result;
	}

}
