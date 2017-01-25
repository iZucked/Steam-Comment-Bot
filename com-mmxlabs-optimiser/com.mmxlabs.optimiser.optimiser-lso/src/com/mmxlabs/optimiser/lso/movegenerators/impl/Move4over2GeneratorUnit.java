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
import com.mmxlabs.optimiser.lso.impl.Move4over2;

public final class Move4over2GeneratorUnit implements IRandomMoveGeneratorUnit {

	@Override
	@Nullable
	public IMove generateRandomMove(@NonNull final RandomMoveGenerator moveGenerator, @NonNull final ISequences sequences, Random random) {

		final List<IResource> resources = sequences.getResources();

		final int numResources = resources.size();

		// Requires two resources
		if (numResources < 2) {
			return null;
		}

		final int resource1Idx = random.nextInt(numResources);
		int resource2Idx = random.nextInt(numResources);

		// Ensure different resources
		while (resource2Idx == resource1Idx) {
			resource2Idx = random.nextInt(numResources);
		}

		final ISequence sequence1 = sequences.getSequence(resource1Idx);
		final ISequence sequence2 = sequences.getSequence(resource2Idx);

		final int[] resource1StartEnd = new int[2];
		moveGenerator.generateSortedBreakPoints(sequence1, resource1StartEnd, random);

		final int[] resource2StartEnd = new int[2];
		moveGenerator.generateSortedBreakPoints(sequence2, resource2StartEnd, random);

		// Create new move
		IResource resource1 = resources.get(resource1Idx);
		assert resource1 != null;
		IResource resource2 = resources.get(resource2Idx);
		assert resource2 != null;
		final Move4over2 move = new Move4over2(resource1, resource1StartEnd[0], resource1StartEnd[1], resource2, resource2StartEnd[0], resource2StartEnd[1]);

		return move;
	}
}
