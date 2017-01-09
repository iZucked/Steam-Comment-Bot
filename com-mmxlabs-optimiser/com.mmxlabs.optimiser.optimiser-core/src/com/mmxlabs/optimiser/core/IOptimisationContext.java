/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;

/**
 * Interface defining an optimisation context. This ties together static optimisation data, initial state, parameters and fitness components that will compose an optimisation run.
 * 
 * @author Simon Goodall
 * 
 */
public interface IOptimisationContext extends IEvaluationContext {

	/**
	 * Returns the {@link IFitnessFunctionRegistry} instance to be used to obtain {@link IFitnessComponent}s. @see {@link #getFitnessComponents()}.
	 * 
	 * @return
	 */
	@NonNull
	IFitnessFunctionRegistry getFitnessFunctionRegistry();

	/**
	 * Returns a list of {@link IFitnessComponent} names to be used to evaluate this optimisation.
	 * 
	 * @return
	 */
	@NonNull
	List<@NonNull String> getFitnessComponents();

	/**
	 * Returns the {@link IConstraintCheckerRegistry} instance to be used to obtain {@link IConstraintChecker} instances. @see {@link #getConstraintCheckers()}.
	 * 
	 * @return
	 */
	@NonNull
	IConstraintCheckerRegistry getConstraintCheckerRegistry();

	/**
	 * Returns a list of {@link IConstraintChecker} names to be used in this optimisation.
	 * 
	 * @return
	 */
	@NonNull
	List<@NonNull String> getConstraintCheckers();

	@NonNull
	IEvaluatedStateConstraintCheckerRegistry getEvaluatedStateConstraintCheckerRegistry();

	@NonNull
	List<@NonNull String> getEvaluatedStateConstraintCheckers();
}
