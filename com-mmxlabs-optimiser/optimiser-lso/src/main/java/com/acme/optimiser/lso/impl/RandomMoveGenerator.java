package com.acme.optimiser.lso.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.acme.optimiser.IResource;
import com.acme.optimiser.ISequence;
import com.acme.optimiser.ISequences;
import com.acme.optimiser.lso.IMove;
import com.acme.optimiser.lso.IMoveGenerator;

/**
 * Move generator to randomly generate moves with an equal probability
 * distribution.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */

public class RandomMoveGenerator<T> implements IMoveGenerator<T> {

	private Random random;

	private ISequences<T> sequences;

	private static enum MoveTypes {
		Move_2opt1, Move_2opt2, Move_3opt1, Move_3op2, Move_4opt1, Move_4opt2
	};

	public RandomMoveGenerator() {

	}

	public RandomMoveGenerator(final Random random,
			final ISequences<T> sequences) {
		this.random = random;
	}

	@Override
	public IMove<T> generateMove() {

		final int newMove = random.nextInt(MoveTypes.values().length);
		final MoveTypes moveType = MoveTypes.values()[newMove];

		final IMove<T> move;
		switch (moveType) {
		case Move_4opt2:
			move = createMove4opt2();
			break;
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
		final int breakPoint = random.nextInt(sequence.size());

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
	 * Generate a 4opt2 move
	 * 
	 * @return
	 */
	private IMove<T> createMove4opt2() {

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
		final Move4opt2<T> move = new Move4opt2<T>();

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
}
