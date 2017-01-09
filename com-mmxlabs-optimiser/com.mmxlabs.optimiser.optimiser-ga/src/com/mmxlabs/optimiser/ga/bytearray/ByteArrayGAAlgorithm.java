/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.bytearray;

import java.util.Random;

import com.mmxlabs.optimiser.ga.IGeneticAlgorithm;
import com.mmxlabs.optimiser.ga.IIndividualEvaluator;
import com.mmxlabs.optimiser.ga.IIndividualFactory;
import com.mmxlabs.optimiser.ga.impl.AbstractGAAlgorithm;

/**
 * Genetic Algorithm implementation which works on a {@link ByteArrayIndividual}
 * 
 * @author Simon Goodall
 * 
 */
public final class ByteArrayGAAlgorithm extends AbstractGAAlgorithm<ByteArrayIndividual> implements IGeneticAlgorithm<ByteArrayIndividual> {

	public ByteArrayGAAlgorithm(final Random random, final IIndividualEvaluator<ByteArrayIndividual> individualEvaluator, final IIndividualFactory<ByteArrayIndividual> individualFactory,
			final float mutateThreshold, final int numElements, final int topN) {

		super(random, individualEvaluator, individualFactory, mutateThreshold, numElements, topN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#op_mutate(com.mmxlabs .optimiser.ga.bytearray.ByteArrayIndividual, float)
	 */
	@Override
	public final void op_mutate(final ByteArrayIndividual individual, final float threshold) {
		final byte[] storage = new byte[1];
		final byte[] bytes = individual.bytes;
		for (int i = 0; i < bytes.length; ++i) {
			if (getRandom().nextFloat() < threshold) {
				getRandom().nextBytes(storage);
				bytes[i] = storage[0];
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#op_crossover(com .mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual, com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual,
	 * com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual, com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual)
	 */
	@Override
	public final void op_crossover(final ByteArrayIndividual individual1, final ByteArrayIndividual individual2, final ByteArrayIndividual newIndividual1, final ByteArrayIndividual newIndividual2) {

		final byte[] bytes1 = individual1.bytes;
		final byte[] bytes2 = individual2.bytes;

		final byte[] newBytes1 = newIndividual1.bytes;
		final byte[] newBytes2 = newIndividual2.bytes;

		// Check data matches
		// assert individual1.length == individual2.length;
		// assert newIndividual1.length == newIndividual2.length;
		// assert bytes1.length == bytes2.length;
		// assert newBytes1.length == newBytes2.length;
		// assert bytes1.length == newBytes1.length;
		// assert individual1.length == newIndividual1.length;

		final int numBytes = bytes1.length;

		// Pick a break point for crossover at byte level (rather than bit
		// level)
		// This could be made better by performing some bit mask ops to
		// crossover at the bit level,
		// at the cost of marginally increased computation
		final int cutoff = getRandom().nextInt(numBytes);

		// Copy first part
		for (int i = 0; i < cutoff; ++i) {
			newBytes1[i] = bytes1[i];
			newBytes2[i] = bytes2[i];
		}

		// Copy second part, swapping source
		for (int i = cutoff; i < numBytes; ++i) {
			newBytes1[i] = bytes2[i];
			newBytes2[i] = bytes1[i];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#op_crossover(com .mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual, com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual,
	 * com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual)
	 */
	@Override
	public final void op_crossover(final ByteArrayIndividual individual1, final ByteArrayIndividual individual2, final ByteArrayIndividual newIndividual1) {

		final byte[] bytes1 = individual1.bytes;
		final byte[] bytes2 = individual2.bytes;

		final byte[] newBytes = newIndividual1.bytes;

		// Check data matches
		// assert individual1.length == individual2.length;
		// assert bytes1.length == bytes2.length;
		// assert bytes1.length == newBytes1.length;
		// assert individual1.length == newIndividual1.length;

		final int numBytes = bytes1.length;

		// Pick a break point for crossover at byte level (rather than bit
		// level)
		// This could be made better by performing some bit mask ops to
		// crossover at the bit level,
		// at the cost of marginally increased computation
		final int cutoff = getRandom().nextInt(numBytes);

		// Randomly pick which way round the crossover happens
		if (getRandom().nextBoolean()) {
			// Copy first part
			for (int i = 0; i < cutoff; ++i) {
				newBytes[i] = bytes1[i];
			}

			// Copy second part, swapping source
			for (int i = cutoff; i < numBytes; ++i) {
				newBytes[i] = bytes2[i];
			}
		} else {
			// Copy first part
			for (int i = 0; i < cutoff; ++i) {
				newBytes[i] = bytes2[i];
			}

			// Copy second part, swapping source
			for (int i = cutoff; i < numBytes; ++i) {
				newBytes[i] = bytes1[i];
			}
		}
	}
}
