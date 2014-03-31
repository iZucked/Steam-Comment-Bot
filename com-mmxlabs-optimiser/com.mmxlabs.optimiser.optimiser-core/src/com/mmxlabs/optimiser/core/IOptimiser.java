/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;

/**
 * 
 * @author Simon Goodall
 * 
 */
public interface IOptimiser {

	/**
	 * Perform an optimisation using the given {@link IOptimisationContext}
	 * 
	 * @param optimisationContext
	 */
	void optimise(IOptimisationContext optimisationContext);

	/**
	 * Returns the {@link IFitnessEvaluator} instance used within the optimisation process.
	 * 
	 * @return
	 */
	IFitnessEvaluator getFitnessEvaluator();

	void dispose();
}
