/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.impl.Move3over2;

public final class Move3over2GeneratorUnit implements IRandomMoveGeneratorUnit {

	@Override
	@Nullable
	public IMove generateRandomMove(@NonNull final RandomMoveGenerator moveGenerator, @NonNull final ISequences sequences, Random random) {

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

		final ISequence sequence1 = sequences.getSequence(resource1);
		final ISequence sequence2 = sequences.getSequence(resource2);

		final int[] resource1StartEnd = new int[2];
		moveGenerator.generateSortedBreakPoints(sequence1, resource1StartEnd, random);

		final int resource2Position = moveGenerator.generateBreakPoint(sequence2, random);

		// Create new move
		final Move3over2 move = new Move3over2();

		// Set resources
		move.setResource1(resources.get(resource1));
		move.setResource2(resources.get(resource2));

		// Set break points
		move.setResource1Start(resource1StartEnd[0]);
		move.setResource1End(resource1StartEnd[1]);

		move.setResource2Position(resource2Position);

		return move;
	}

}
