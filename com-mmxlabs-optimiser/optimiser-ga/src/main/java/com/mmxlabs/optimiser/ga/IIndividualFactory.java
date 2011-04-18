package com.mmxlabs.optimiser.ga;

/**
 * Factory interface to generate new {@link Individual}
 * 
 * @author Simon Goodall
 * 
 * @param <I>
 *            Individual type
 */
public interface IIndividualFactory<I extends Individual<I>> {

	/**
	 * Create a new {@link Individual} implementation.
	 * 
	 * @return
	 */
	I createIndividual();
}