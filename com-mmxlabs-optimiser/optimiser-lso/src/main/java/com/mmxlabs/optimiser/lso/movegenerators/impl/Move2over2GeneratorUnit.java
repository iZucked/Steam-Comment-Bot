package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.List;

import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.impl.Move2over2;

/**
 * Generator for {@link Move2over2} moves. Currently always generates moves which respect the start and end elements of sequences.
 * @author hinton
 *
 * @param <T>
 */
public class Move2over2GeneratorUnit<T> implements
		IRandomMoveGeneratorUnit<T> {

	@Override
	public IMove<T> generateRandomMove(RandomMoveGenerator<T> moveGenerator,
			ISequences<T> sequences) {
		final RandomHelper random = new RandomHelper(moveGenerator.getRandom());
		
		final List<IResource> resources = sequences.getResources();
		final int resourceCount = resources.size();
		if (resourceCount == 1) return null; //need two sequences at least
		
		//pick two random sequences
		final int resource1 = random.nextInt(resourceCount);
		final int resource2 = random.nextDifferentInt(resourceCount, resource1);

		//generate breakpoints (this should be OK regarding start and end).
		final int resource1Start = moveGenerator.generateBreakPoint(sequences.getSequence(resource1));
		final int resource2Start = moveGenerator.generateBreakPoint(sequences.getSequence(resource2));
		
		//fill out new move object
		Move2over2<T> output = new Move2over2<T>();
		
		output.setResource1(resources.get(resource1));
		output.setResource2(resources.get(resource2));
		output.setPreserveStartAndEnd(true);
		output.setResource1Position(resource1Start);
		output.setResource2Position(resource2Start);
		
		return output;
	}
}
