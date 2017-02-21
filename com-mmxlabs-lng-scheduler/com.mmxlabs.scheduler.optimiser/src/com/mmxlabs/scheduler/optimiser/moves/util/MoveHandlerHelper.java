package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.ArrayList;
import java.util.LinkedList;
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
import com.mmxlabs.scheduler.optimiser.providers.Followers;

public class MoveHandlerHelper implements IMoveHandlerHelper {

	@Inject
	private @NonNull MoveHelper helper;

	@Override
	public MoveHelper getMoveHelper() {
		return helper;
	}

	@Override
	public List<ISequenceElement> extractSegment(final ISequence fromSequence, final ISequenceElement element) {
		final List<ISequenceElement> orderedCargoElements = new LinkedList<>();
		//// Find full cargo sequence based on element

		// Part 1, find the segment start
		int segmentStart = -1;
		boolean lastElementWasLoad = false;
		for (int i = 0; i < fromSequence.size(); ++i) {
			final ISequenceElement e = fromSequence.get(i);

			if (helper.isLoadSlot(e)) {
				if (!lastElementWasLoad || segmentStart == -1) {
					segmentStart = i;
				}
				lastElementWasLoad = true;
			} else if (!helper.isDischargeSlot(e)) {
				lastElementWasLoad = false;
				segmentStart = -1;
			} else {
				lastElementWasLoad = false;
			}

			if (e == element) {
				if (segmentStart == -1) {
					segmentStart = i;
				}
				break;
			}
		}
		if (segmentStart == -1) {
			throw new IllegalStateException();
			// return null;
		}

		// Part 2 - find full segment extent
		boolean foundDischarge = false;
		for (int i = segmentStart; i < fromSequence.size(); ++i) {
			final ISequenceElement e = fromSequence.get(i);
			// Iterate through elements (they should be loads) until we reach a discharge.
			// The segment ends at the first non-discharge element.
			if (helper.isStartOrEndSlot(e)) {
				break;
			}
			if (!foundDischarge) {
				if (helper.isDischargeSlot(e)) {
					foundDischarge = true;
				} else {
					assert helper.isLoadSlot(e);
				}
			} else {
				if (!helper.isDischargeSlot(e)) {
					break;
				}
			}
			orderedCargoElements.add(e);
		}
		return orderedCargoElements;
	}

	@Override
	public Triple<String, Pair<IResource, Integer>, Pair<IResource, Integer>> selectInitialPositions(final List<Pair<ISequenceElement, ISequenceElement>> validBreaks,
			final ILookupManager lookupManager, final Random random) {

		final Triple<String, Pair<IResource, Integer>, Pair<IResource, Integer>> result = new Triple<>();

		if (validBreaks.isEmpty()) {
			result.setFirst("No Valid Breaks.");
			return result;
		}

		// get resources for a random edge
		final Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> positions = findEdge(validBreaks, lookupManager, random);
		Pair<IResource, Integer> pos1 = null;
		Pair<IResource, Integer> pos2 = null;

		// if no resource found then we have a part of the edge in the unused list
		// exit! (let another move solve this)
		if (positions != null) {
			pos1 = positions.getFirst();
			pos2 = positions.getSecond();
			if ((pos1.getFirst() == null) || (pos2.getFirst() == null)) {
				result.setFirst("Null IResource in pair.");
			}
		} else {
			result.setFirst("Failed to find appropriate edge.");
		}

		result.setSecond(pos1);
		result.setThird(pos2);

		return result;
	}

	@Override
	public Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> findEdge(final List<Pair<ISequenceElement, ISequenceElement>> validBreaks, final ILookupManager lookupManager,
			final Random random) {

		Pair<IResource, Integer> pos1 = null;
		Pair<IResource, Integer> pos2 = null;

		final Pair<ISequenceElement, ISequenceElement> newPair = RandomHelper.chooseElementFrom(random, validBreaks);
		pos1 = lookupManager.lookup(newPair.getFirst());
		pos2 = lookupManager.lookup(newPair.getSecond());

		final Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> positions = new Pair(pos1, pos2);
		return positions;
	}

	@Override
	public boolean checkPositions(final Pair<IResource, Integer> pos1, final Pair<IResource, Integer> pos2) {
		final int position1 = pos1.getSecond();
		final int position2 = pos2.getSecond();

		if (position1 == -1 || position2 == -1) {
			return false;
		}
		return true;
	}

	@Override
	public List<Pair<Integer, Integer>> determineViableSecondBreaks(final IResource sequence1, final int position1, final int position2, final ISequence seq2, final boolean valid2opt2,
			final ILookupManager lookupManager, final IFollowersAndPreceders followersAndPreceders) {

		final Followers<ISequenceElement> followersOfSecondElementsPredecessor = followersAndPreceders.getValidFollowers(seq2.get(position2 - 1));
		final List<Pair<Integer, Integer>> viableSecondBreaks = new ArrayList<Pair<Integer, Integer>>();

		for (int i = position2; i < (seq2.size() - 1); i++) { // ignore last element
			final ISequenceElement here = seq2.get(i);
			for (final ISequenceElement elt : followersAndPreceders.getValidFollowers(here)) {
				final Pair<IResource, Integer> loc = lookupManager.lookup(elt);
				final IResource first = loc.getFirst();
				if (first == null) {
					// Most likely an optional element no longer in sequences
					continue;
				}
				if (first == sequence1) {
					// it can be adjacent to something in sequence 1,
					// that's good
					if (loc.getSecond() > position1) {
						// it's something after A, that's even better!
						// now we need to check that we can put the
						// chunk cut out of S1 into S2 here

						if (loc.getSecond() == (position1 + 1)) {
							// 3opt1 check
							if (followersOfSecondElementsPredecessor.contains(seq2.get(i + 1))) {
								viableSecondBreaks.add(new Pair<Integer, Integer>(i, loc.getSecond()));
							}
						} else {
							// 4opt2 check
							if (valid2opt2 && followersAndPreceders.getValidFollowers(lookupManager.getRawSequences().getSequence(first).get(loc.getSecond() - 1)).contains(seq2.get(i + 1))) {
								viableSecondBreaks.add(new Pair<Integer, Integer>(i, loc.getSecond()));
							}
						}

					}
				}
			}
		}
		return viableSecondBreaks;

	}

	@Override
	public boolean valid2opt2(final IResource sequence1, final IResource sequence2, final int position1, int position2, final ISequences sequences,
			final IFollowersAndPreceders followersAndPreceders) {

		final ISequence seq1 = sequences.getSequence(sequence1);
		final ISequence seq2 = sequences.getSequence(sequence2);

		boolean valid2opt2 = followersAndPreceders.getValidFollowers(seq2.get(position2 - 1)).contains(seq1.get(position1 + 1));

		while (!valid2opt2 && (position2 > 1)) {
			// rewind position 2? after all if we don't have a valid 2opt2
			// we probably won't get a valid 4opt2 out of it either?
			position2--;
			valid2opt2 = followersAndPreceders.getValidFollowers(seq2.get(position2 - 1)).contains(seq1.get(position1 + 1));
		}

		return valid2opt2;

	}
}
