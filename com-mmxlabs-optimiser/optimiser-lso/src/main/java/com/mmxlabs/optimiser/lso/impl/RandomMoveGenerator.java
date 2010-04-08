package com.mmxlabs.optimiser.lso.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

/**
 * Move generator to randomly generate moves with an equal probability
 * distribution.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */

public final class RandomMoveGenerator<T> implements IMoveGenerator<T> {

	private Random random;

	private ISequences<T> sequences;

	private static enum MoveTypes {
		Move_3over2, Move_4over1, Move_4over2
	};

	public RandomMoveGenerator() {

	}

	@Override
	public IMove<T> generateMove() {

		final int newMove = random.nextInt(MoveTypes.values().length);
		final MoveTypes moveType = MoveTypes.values()[newMove];

		final IMove<T> move;
		switch (moveType) {
		case Move_4over1:
			move = createMove4over1();
			break;
		case Move_4over2:
			move = createMove4over2();
			break;
		case Move_3over2:
			move = createMove3over2();
			break;
		// TODO: Add other move types here
		default:
			move = null;
		}

		return move;
	}

	public void init() {

		if (random == null) {
			throw new IllegalStateException("Random is not set");
		}

		if (sequences == null) {
			throw new IllegalStateException("Sequences is not set");
		}
	}

	public void setRandom(final Random random) {
		this.random = random;
	}

	public Random getRandom() {
		return random;
	}

	public void setSequences(final ISequences<T> sequences) {
		this.sequences = sequences;
	}

	public ISequences<T> getSequences() {
		return sequences;
	}

	/**
	 * Randomly generate a break point in the given sequence.
	 * 
	 * @param sequence
	 * @return
	 */
	private int generateBreakPoint(final ISequence<T> sequence) {
		final int breakPoint = random.nextInt(1 + sequence.size());

		// Validate break point -- should it include start/end elements?

		return breakPoint;
	}

	/**
	 * Generate a list of break points in linear order. The number of break
	 * points is defined by the size of the input array.
	 * 
	 * @param sequence
	 * @param breakPoints
	 */
	private void generateSortedBreakPoints(final ISequence<T> sequence,
			final int[] breakPoints) {

		// Generate the break points
		for (int i = 0; i < breakPoints.length; ++i) {
			breakPoints[i] = generateBreakPoint(sequence);
		}

		// Sort the list
		Arrays.sort(breakPoints);
	}

	/**
	 * Generate a 4over1 move
	 * 
	 * @return
	 */
	private IMove<T> createMove4over1() {

		final List<IResource> resources = sequences.getResources();

		final int numResources = resources.size();

		if (numResources < 1) {
			return null;
		}

		final int resource = random.nextInt(numResources);

		final ISequence<T> sequence = sequences.getSequence(resource);

		final int[] segmentBreakPoints= new int[4];
		generateSortedBreakPoints(sequence, segmentBreakPoints);


		// Create new move
		final Move4over1<T> move = new Move4over1<T>();

		// Set resources
		move.setResource(resources.get(resource));

		// Set break points
		move.setSegment1Start(segmentBreakPoints[0]);
		move.setSegment1End(segmentBreakPoints[1]);
		move.setSegment2Start(segmentBreakPoints[2]);
		move.setSegment2End(segmentBreakPoints[3]);

		return move;
	}

	
	/**
	 * Generate a 4over2 move
	 * 
	 * @return
	 */
	private IMove<T> createMove4over2() {

		final List<IResource> resources = sequences.getResources();

		final int numResources = resources.size();

		if (numResources == 1) {
			return null;
		}

		final int resource1 = random.nextInt(numResources);
		int resource2 = random.nextInt(numResources);

		// Ensure different resources
		while (resource2 != resource1) {
			resource2 = random.nextInt(numResources);
		}

		final ISequence<T> sequence1 = sequences.getSequence(resource1);
		final ISequence<T> sequence2 = sequences.getSequence(resource2);

		final int[] resource1StartEnd = new int[2];
		generateSortedBreakPoints(sequence1, resource1StartEnd);

		final int[] resource2StartEnd = new int[2];
		generateSortedBreakPoints(sequence2, resource2StartEnd);

		// Create new move
		final Move4over2<T> move = new Move4over2<T>();

		// Set resources
		move.setResource1(resources.get(resource1));
		move.setResource2(resources.get(resource2));

		// Set break points
		move.setResource1Start(resource1StartEnd[0]);
		move.setResource1End(resource1StartEnd[1]);

		move.setResource2Start(resource2StartEnd[0]);
		move.setResource2End(resource2StartEnd[1]);

		return move;
	}

	/**
	 * Generate a {@link Move3over2} move type
	 * 
	 * @return
	 */
	private IMove<T> createMove3over2() {

		final List<IResource> resources = sequences.getResources();

		final int numResources = resources.size();

		if (numResources == 1) {
			return null;
		}

		final int resource1 = random.nextInt(numResources);
		int resource2 = random.nextInt(numResources);

		// Ensure different resources
		while (resource2 == resource1) {
			resource2 = random.nextInt(numResources);
		}

		final ISequence<T> sequence1 = sequences.getSequence(resource1);
		final ISequence<T> sequence2 = sequences.getSequence(resource2);

		final int[] resource1StartEnd = new int[2];
		generateSortedBreakPoints(sequence1, resource1StartEnd);

		final int resource2Potision = generateBreakPoint(sequence2);

		// Create new move
		final Move3over2<T> move = new Move3over2<T>();

		// Set resources
		move.setResource1(resources.get(resource1));
		move.setResource2(resources.get(resource2));

		// Set break points
		move.setResource1Start(resource1StartEnd[0]);
		move.setResource1End(resource1StartEnd[1]);

		move.setResource2Position(resource2Potision);

		return move;
	}

}
