/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.booleanarray;

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
public final class BooleanArrayIndividual implements Individual<BooleanArrayIndividual> {

	public final boolean[] values;

	public BooleanArrayIndividual(final boolean[] ints) {
		this.values = ints;
	}

	@Override
	public final int hashCode() {
		return Arrays.hashCode(values);
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof BooleanArrayIndividual) {
			final BooleanArrayIndividual i = (BooleanArrayIndividual) obj;
			return Arrays.equals(i.values, values);
		}
		return false;
	}

	@Override
	public final BooleanArrayIndividual clone() {
		return new BooleanArrayIndividual(values.clone());
	}

	public final int hashBytes() {
		return externalHashBytes();
	}

	public final int externalHashBytes() {
		final int hash = Arrays.hashCode(values);
		return (hash ^ (hash >> 31)) - (hash >> 31);
	}

	public final int internalHashBytes() {
		int hash = 9;
		final int multiplier = 17;
		for (int i = 0; i < values.length; i += 2) {
			hash = (hash * multiplier) + (values[i] ? 1 : 0);
		}
		// make positive (handy trick)
		return (hash ^ (hash >> 31)) - (hash >> 31);
	}

	@Override
	public final String toString() {
		return Arrays.toString(values);
	}
}
