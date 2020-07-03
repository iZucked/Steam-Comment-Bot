/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.booleanarray;

import java.util.Random;

import com.mmxlabs.optimiser.ga.IIndividualFactory;

public final class RandomBooleanArrayIndividualFactory implements IIndividualFactory<BooleanArrayIndividual> {

	private final int numValues;

	private final Random random;

	public RandomBooleanArrayIndividualFactory(final int numValues, final Random random) {
		this.numValues = numValues;
		this.random = random;
	}

	@Override
	public final BooleanArrayIndividual createIndividual() {
		final boolean[] values = new boolean[numValues];
		for (int i = 0; i < numValues; ++i) {
			values[i] = random.nextBoolean();
		}
		return new BooleanArrayIndividual(values);
	}

}
