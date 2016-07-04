/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.bytearray;

import java.util.Random;

import com.mmxlabs.optimiser.ga.IIndividualFactory;

public final class RandomByteArrayIndividualFactory implements IIndividualFactory<ByteArrayIndividual> {

	/**
	 * Number of bytes in each individual.
	 */
	private final int numBytes;

	private final Random random;

	public RandomByteArrayIndividualFactory(final int numBytes, final Random random) {
		this.numBytes = numBytes;
		this.random = random;
	}

	@Override
	public final ByteArrayIndividual createIndividual() {
		final byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return new ByteArrayIndividual(bytes);
	}
}
