package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

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
public final class Individual {

	public final byte[] bytes;

	public Individual(final byte[] bytes) {
		this.bytes = bytes;
	}
	
	public final int hashBytes() {
		int hash = 9;
		final int multiplier = 17;
		for (int i = 0; i<bytes.length; i+=2) {
			hash = hash * multiplier + bytes[i];
		}
		//make positive (handy trick)
		return  (hash ^ (hash>>31)) - (hash>>31);
	}
}
