/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.impl.Move2over2;

/**
 * Generator for {@link Move2over2} moves. Currently always generates moves which respect the start and end elements of sequences.
 * 
 * @author hinton
 * 
 */
public class Move2over2GeneratorUnit implements IRandomMoveGeneratorUnit {

	@Override
	@Nullable
	public IMove generateRandomMove(@NonNull final RandomMoveGenerator moveGenerator, @NonNull final ISequences sequences, Random random) {

		final List<IResource> resources = sequences.getResources();
		final int resourceCount = resources.size();
		if (resourceCount == 1) {
			return null; // need two sequences at least
		}

		// pick two random sequences
		final int resource1 = random.nextInt(resourceCount);
		final int resource2 = RandomHelper.nextDifferentInt(random, resourceCount, resource1);

		// generate breakpoints (this should be OK regarding start and end).
		final int resource1Start = moveGenerator.generateBreakPoint(sequences.getSequence(resource1), random);
		final int resource2Start = moveGenerator.generateBreakPoint(sequences.getSequence(resource2), random);

		// fill out new move object
		final Move2over2 output = new Move2over2();

		output.setResource1(resources.get(resource1));
		output.setResource2(resources.get(resource2));
		output.setPreserveStartAndEnd(true);
		output.setResource1Position(resource1Start);
		output.setResource2Position(resource2Start);

		return output;
	}
}
