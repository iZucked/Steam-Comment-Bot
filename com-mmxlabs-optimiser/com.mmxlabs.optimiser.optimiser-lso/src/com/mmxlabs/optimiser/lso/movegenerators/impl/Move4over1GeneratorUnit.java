/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.moves.impl.Move4over1;

public final class Move4over1GeneratorUnit implements IRandomMoveGeneratorUnit {

	@Override
	@Nullable
	public IMove generateRandomMove(@NonNull final RandomMoveGenerator moveGenerator, @NonNull final ISequences sequences, Random random) {

		final List<IResource> resources = sequences.getResources();

		final int numResources = resources.size();

		if (numResources < 1) {
			return null;
		}

		final int resourceIdx = random.nextInt(numResources);
		final IResource resource = resources.get(resourceIdx);

		final ISequence sequence = sequences.getSequence(resource);

		final int[] segmentBreakPoints = new int[4];
		moveGenerator.generateSortedBreakPoints(sequence, segmentBreakPoints, random);

		// Create new move
		final Move4over1 move = new Move4over1();

		// Set resources
		move.setResource(resource);

		// Set break points
		move.setSegment1Start(segmentBreakPoints[0]);
		move.setSegment1End(segmentBreakPoints[1]);
		move.setSegment2Start(segmentBreakPoints[2]);
		move.setSegment2End(segmentBreakPoints[3]);

		return move;

	}

}
