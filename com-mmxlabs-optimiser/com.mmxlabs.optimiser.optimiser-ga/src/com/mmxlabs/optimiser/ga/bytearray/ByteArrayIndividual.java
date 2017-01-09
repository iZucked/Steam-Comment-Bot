/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.bytearray;

import java.util.Arrays;

import com.mmxlabs.optimiser.ga.IIndividualEvaluator;
import com.mmxlabs.optimiser.ga.Individual;

/**
 * 
 * Represents a single individual in the GA population. This is purely a byte array. The {@link IIndividualEvaluator} determines how to encode/decode this array.
 * 
 * @author Simon Goodall
 * 
 */
public final class ByteArrayIndividual implements Individual<ByteArrayIndividual> {

	public final byte[] bytes;

	public ByteArrayIndividual(final byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public final int hashCode() {
		return Arrays.hashCode(bytes);
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof ByteArrayIndividual) {
			final ByteArrayIndividual i = (ByteArrayIndividual) obj;
			return Arrays.equals(i.bytes, bytes);
		}
		return false;
	}

	@Override
	public final ByteArrayIndividual clone() {
		return new ByteArrayIndividual(bytes.clone());
	}

	public final int hashBytes() {
		return externalHashBytes();
	}

	public final int externalHashBytes() {
		final int hash = Arrays.hashCode(bytes);
		return (hash ^ (hash >> 31)) - (hash >> 31);
	}

	public final int internalHashBytes() {
		int hash = 9;
		final int multiplier = 17;
		for (int i = 0; i < bytes.length; i += 2) {
			hash = (hash * multiplier) + bytes[i];
		}
		// make positive (handy trick)
		return (hash ^ (hash >> 31)) - (hash >> 31);
	}

}
