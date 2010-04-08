package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.List;
import java.util.Random;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.impl.Move4over2;

public final class Move4over2GeneratorUnit<T> implements
		IRandomMoveGeneratorUnit<T> {

	@Override
	public IMove<T> generateRandomMove(
			final RandomMoveGenerator<T> moveGenerator,
			final ISequences<T> sequences) {

		final Random random = moveGenerator.getRandom();

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
		moveGenerator.generateSortedBreakPoints(sequence1, resource1StartEnd);

		final int[] resource2StartEnd = new int[2];
		moveGenerator.generateSortedBreakPoints(sequence2, resource2StartEnd);

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

}
