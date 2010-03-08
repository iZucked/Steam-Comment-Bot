package com.acme.optimiser;

import java.util.Collection;

import com.acme.optimiser.fitness.IFitnessFunction;

/**
 * Interface defining an optimisation context. This tes together static
 * optimisation data, initial state, parameters and fitness components that will
 * compose an optimisation run.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IOptimisationContext<T> {

	Collection<IFitnessFunction> getFitnessFunctions();

	/**
	 * Returns the initial sequences state - i.e. the starting point of the
	 * optimisation process.
	 * 
	 * @return
	 */
	ISequences<T> getInitialSequences();

}
