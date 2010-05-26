package com.mmxlabs.optimiser;

import com.mmxlabs.optimiser.fitness.IFitnessEvaluator;

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
}
