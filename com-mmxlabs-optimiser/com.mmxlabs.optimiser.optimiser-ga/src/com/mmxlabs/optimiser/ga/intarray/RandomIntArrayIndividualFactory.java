/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.intarray;

import java.util.Random;

import com.mmxlabs.optimiser.ga.IIndividualFactory;

public final class RandomIntArrayIndividualFactory implements IIndividualFactory<IntArrayIndividual> {

	private final int numInts;

	private final int maxValue;
	private final Random random;

	public RandomIntArrayIndividualFactory(final int numInts, final int maxValue, final Random random) {
		this.numInts = numInts;
		this.maxValue = maxValue;
		this.random = random;
	}

	@Override
	public final IntArrayIndividual createIndividual() {
		final int[] ints = new int[numInts];
		for (int i = 0; i < numInts; ++i) {
			ints[i] = random.nextInt(maxValue);
		}
		return new IntArrayIndividual(ints);
	}

}
