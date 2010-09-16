package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import java.util.Arrays;

/**
 * Obsolete as just a byte array?
 * 
 * Represents a single individual in the GA population. This is purely a byte
 * array. The {@link IIndividualEvaluator} determines how to encode/decode this
 * array.
 * 
 * @author Simon Goodall
 * 
 */
public final class Individual implements Cloneable {

	public final byte[] bytes;

	public Individual(final byte[] bytes) {
		this.bytes = bytes;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(bytes);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Individual) {
			final Individual i = (Individual) obj;
			return Arrays.equals(i.bytes, bytes);
		}
		return false;
	}

	@Override
	protected Object clone() {
		return new Individual(bytes.clone());
	}

	public final int hashBytes() {
		return externalHashBytes();
	}
	
	public final int externalHashBytes() {
		final int hash = Arrays.hashCode(bytes);
		return  (hash ^ (hash>>31)) - (hash>>31);
	}
	
	
	public final int internalHashBytes() {
		int hash = 9;
		final int multiplier = 17;
		for (int i = 0; i<bytes.length; i+=2) {
			hash = hash * multiplier + bytes[i];
		}
		//make positive (handy trick)
		return  (hash ^ (hash>>31)) - (hash>>31);
	}
	
}
