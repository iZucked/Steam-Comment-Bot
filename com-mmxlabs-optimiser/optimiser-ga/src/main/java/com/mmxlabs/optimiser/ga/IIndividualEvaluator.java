/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga;


/**
 * The {@link IIndividualEvaluator} calculates the fitness of a given
 * individual.
 * 
 * @author Simon Goodall
 * 
 * @param I
 *            Individual Type
 */
public interface IIndividualEvaluator<I> {

	/**
	 * Calculate individual fitness
	 * 
	 * @param individual
	 * @return
	 */
	long evaluate(I individual);

	/**
	 * Clean up resources
	 */
	void dispose();
}
