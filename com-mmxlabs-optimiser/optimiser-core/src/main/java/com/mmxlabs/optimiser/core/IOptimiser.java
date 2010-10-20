/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core;

import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;

/**
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IOptimiser<T> {

	/**
	 * Perform an optimisation using the given {@link IOptimisationContext}
	 * 
	 * @param optimisationContext
	 */
	void optimise(IOptimisationContext<T> optimisationContext);

	/**
	 * Returns the {@link IFitnessEvaluator} instance used within the
	 * optimisation process.
	 * 
	 * @return
	 */
	IFitnessEvaluator<T> getFitnessEvaluator();

	void dispose();
}
