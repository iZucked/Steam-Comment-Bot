/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.booleanarray;

import java.util.Random;

import com.mmxlabs.optimiser.ga.IGeneticAlgorithm;
import com.mmxlabs.optimiser.ga.IIndividualEvaluator;
import com.mmxlabs.optimiser.ga.IIndividualFactory;
import com.mmxlabs.optimiser.ga.impl.AbstractGAAlgorithm;

/**
 * Genetic Algorithm implementation which works on a {@link BooleanArrayIndividual}. Generates an integer array with values between 0 (inclusive) and {@link #maxValue} (exclusive).
 * 
 * @author Simon Goodall
 * 
 */
public final class BooleanArrayGAAlgorithm extends AbstractGAAlgorithm<BooleanArrayIndividual> implements IGeneticAlgorithm<BooleanArrayIndividual> {

	/**
	 * Number of values in each individual.
	 */
	private final int numValues;

	public BooleanArrayGAAlgorithm(final Random random, final IIndividualEvaluator<BooleanArrayIndividual> individualEvaluator, final IIndividualFactory<BooleanArrayIndividual> individualFactory,
			final float mutateThreshold, final int numElements, final int topN, final int numValues) {

		super(random, individualEvaluator, individualFactory, mutateThreshold, numElements, topN);
		this.numValues = numValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#op_mutate(com.mmxlabs .optimiser.ga.bytearray.ByteArrayIndividual, float)
	 */
	@Override
	public final void op_mutate(final BooleanArrayIndividual individual, final float threshold) {
		final boolean[] ints = individual.values;
		final Random random = getRandom();
		for (int i = 0; i < ints.length; ++i) {
			if (random.nextFloat() < threshold) {
				ints[i] = random.nextBoolean();
			}
		}
	}

	@Override
	public final void op_crossover(final BooleanArrayIndividual individual1, final BooleanArrayIndividual individual2, final BooleanArrayIndividual newIndividual1,
			final BooleanArrayIndividual newIndividual2) {

		final boolean[] ints1 = individual1.values;
		final boolean[] ints2 = individual2.values;

		final boolean[] newInts1 = newIndividual1.values;
		final boolean[] newInts2 = newIndividual2.values;

		// Check data matches
		// assert individual1.length == individual2.length;
		// assert newIndividual1.length == newIndividual2.length;
		// assert bytes1.length == bytes2.length;
		// assert newBytes1.length == newBytes2.length;
		// assert bytes1.length == newBytes1.length;
		// assert individual1.length == newIndividual1.length;

		final int numInts = ints1.length;

		// Pick a break point for crossover at byte level (rather than bit
		// level)
		// This could be made better by performing some bit mask ops to
		// crossover at the bit level,
		// at the cost of marginally increased computation
		final int cutoff = getRandom().nextInt(numInts);

		// Copy first part
		for (int i = 0; i < cutoff; ++i) {
			newInts1[i] = ints1[i];
			newInts2[i] = ints2[i];
		}

		// Copy second part, swapping source
		for (int i = cutoff; i < numInts; ++i) {
			newInts1[i] = ints2[i];
			newInts2[i] = ints1[i];
		}
	}

	@Override
	public final void op_crossover(final BooleanArrayIndividual individual1, final BooleanArrayIndividual individual2, final BooleanArrayIndividual newIndividual1) {

		final boolean[] ints1 = individual1.values;
		final boolean[] ints2 = individual2.values;

		final boolean[] newInts = newIndividual1.values;

		// Check data matches
		// assert individual1.length == individual2.length;
		// assert bytes1.length == bytes2.length;
		// assert bytes1.length == newBytes1.length;
		// assert individual1.length == newIndividual1.length;

		final int numInts = ints1.length;

		// Pick a break point for crossover at byte level (rather than bit
		// level)
		// This could be made better by performing some bit mask ops to
		// crossover at the bit level,
		// at the cost of marginally increased computation
		final int cutoff = getRandom().nextInt(numInts);

		// Randomly pick which way round the crossover happens
		if (getRandom().nextBoolean()) {
			// Copy first part
			for (int i = 0; i < cutoff; ++i) {
				newInts[i] = ints1[i];
			}

			// Copy second part, swapping source
			for (int i = cutoff; i < numInts; ++i) {
				newInts[i] = ints2[i];
			}
		} else {
			// Copy first part
			for (int i = 0; i < cutoff; ++i) {
				newInts[i] = ints2[i];
			}

			// Copy second part, swapping source
			for (int i = cutoff; i < numInts; ++i) {
				newInts[i] = ints1[i];
			}
		}
	}

	public final int getNumValues() {
		return numValues;
	}

}
