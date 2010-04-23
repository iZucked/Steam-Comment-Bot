package com.mmxlabs.optimiser;

import java.util.List;

import com.mmxlabs.optimiser.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

/**
 * Interface defining an optimisation context. This ties together static
 * optimisation data, initial state, parameters and fitness components that will
 * compose an optimisation run.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IOptimisationContext<T> {

	/**
	 * Return static input data to the optimisation.
	 * 
	 * @return
	 */
	IOptimisationData<T> getOptimisationData();

	/**
	 * Returns the initial sequences state - i.e. the starting point of the
	 * optimisation process.
	 * 
	 * @return
	 */
	ISequences<T> getInitialSequences();

	/**
	 * Returns the {@link IFitnessFunctionRegistry} instance to be used to
	 * obtain {@link IFitnessComponent}s. @see {@link #getFitnessComponents()}.
	 * 
	 * @return
	 */
	IFitnessFunctionRegistry getFitnessFunctionRegistry();

	/**
	 * Returns a list of {@link IFitnessComponent} names to be used to evaluate
	 * this optimisation.
	 * 
	 * @return
	 */
	List<String> getFitnessComponents();

	/**
	 * Returns the {@link IConstraintCheckerRegistry} instance to be used to
	 * obtain {@link IConstraintChecker} instances. @see
	 * {@link #getConstraintCheckers()}.
	 * 
	 * @return
	 */
	IConstraintCheckerRegistry getConstraintCheckerRegistry();

	/**
	 * Returns a list of {@link IConstraintChecker} names to be used in this
	 * optimisation.
	 * 
	 * @return
	 */
	List<String> getConstraintCheckers();

}
