package com.mmxlabs.optimiser;

import java.util.List;

import com.mmxlabs.optimiser.fitness.IFitnessComponent;

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

	List<IFitnessComponent<T>> getFitnessComponents();

	/**
	 * Returns the initial sequences state - i.e. the starting point of the
	 * optimisation process.
	 * 
	 * @return
	 */
	ISequences<T> getInitialSequences();

}
