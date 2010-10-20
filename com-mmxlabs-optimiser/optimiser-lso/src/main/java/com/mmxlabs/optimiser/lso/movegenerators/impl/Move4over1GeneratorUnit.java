/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.List;
import java.util.Random;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.impl.Move4over1;

public final class Move4over1GeneratorUnit<T> implements IRandomMoveGeneratorUnit<T> {

	@Override
	public IMove<T> generateRandomMove(RandomMoveGenerator<T> moveGenerator,
			ISequences<T> sequences) {

		Random random = moveGenerator.getRandom();
		
		final List<IResource> resources = sequences.getResources();

		final int numResources = resources.size();

		if (numResources < 1) {
			return null;
		}

		final int resource = random.nextInt(numResources);

		final ISequence<T> sequence = sequences.getSequence(resource);

		final int[] segmentBreakPoints= new int[4];
		moveGenerator.generateSortedBreakPoints(sequence, segmentBreakPoints);

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

}
