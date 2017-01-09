/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.intarray;

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
public final class IntArrayIndividual implements Individual<IntArrayIndividual> {

	public final int[] ints;

	public IntArrayIndividual(final int[] ints) {
		this.ints = ints;
	}

	@Override
	public final int hashCode() {
		return Arrays.hashCode(ints);
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof IntArrayIndividual) {
			final IntArrayIndividual i = (IntArrayIndividual) obj;
			return Arrays.equals(i.ints, ints);
		}
		return false;
	}

	@Override
	public final IntArrayIndividual clone() {
		return new IntArrayIndividual(ints.clone());
	}

	public final int hashBytes() {
		return externalHashBytes();
	}

	public final int externalHashBytes() {
		final int hash = Arrays.hashCode(ints);
		return (hash ^ (hash >> 31)) - (hash >> 31);
	}

	public final int internalHashBytes() {
		int hash = 9;
		final int multiplier = 17;
		for (int i = 0; i < ints.length; i += 2) {
			hash = (hash * multiplier) + ints[i];
		}
		// make positive (handy trick)
		return (hash ^ (hash >> 31)) - (hash >> 31);
	}

	@Override
	public final String toString() {
		return Arrays.toString(ints);
	}
}
