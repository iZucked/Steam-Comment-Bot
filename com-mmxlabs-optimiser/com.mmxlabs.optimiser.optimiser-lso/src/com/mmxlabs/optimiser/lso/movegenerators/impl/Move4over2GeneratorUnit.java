/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.List;
import java.util.Random;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.impl.Move4over2;

public final class Move4over2GeneratorUnit implements IRandomMoveGeneratorUnit {

	@Override
	public IMove generateRandomMove(final RandomMoveGenerator moveGenerator, final ISequences sequences) {

		final Random random = moveGenerator.getRandom();

		final List<IResource> resources = sequences.getResources();

		final int numResources = resources.size();

		// Requires two resources
		if (numResources < 2) {
			return null;
		}

		final int resource1 = random.nextInt(numResources);
		int resource2 = random.nextInt(numResources);

		// Ensure different resources
		while (resource2 == resource1) {
			resource2 = random.nextInt(numResources);
		}

		final ISequence sequence1 = sequences.getSequence(resource1);
		final ISequence sequence2 = sequences.getSequence(resource2);

		final int[] resource1StartEnd = new int[2];
		moveGenerator.generateSortedBreakPoints(sequence1, resource1StartEnd);

		final int[] resource2StartEnd = new int[2];
		moveGenerator.generateSortedBreakPoints(sequence2, resource2StartEnd);

		// Create new move
		final Move4over2 move = new Move4over2();

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
