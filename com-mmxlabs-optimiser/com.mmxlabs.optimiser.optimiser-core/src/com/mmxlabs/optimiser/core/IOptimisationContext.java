/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Interface defining an optimisation context. This ties together static optimisation data, initial state, parameters and fitness components that will compose an optimisation run.
 * 
 * @author Simon Goodall
 * 
 */
public interface IOptimisationContext {

	/**
	 * Return static input data to the optimisation.
	 * 
	 * @return
	 */
	@NonNull
	IOptimisationData getOptimisationData();

	/**
	 * Returns the initial sequences state - i.e. the starting point of the optimisation process.
	 * 
	 * @return
	 */
	@NonNull
	ISequences getInitialSequences();

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
	List<String> getFitnessComponents();

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
	List<String> getConstraintCheckers();

	/**
	 * Returns the {@link IEvaluationProcessRegistry} instance to be used to obtain {@link IEvaluationProcess} instances. @see {@link #getEvaluationProcesses()}.
	 * 
	 * @return
	 */
	@NonNull
	IEvaluationProcessRegistry getEvaluationProcessRegistry();

	/**
	 * Returns a list of {@link IEvaluationProcess} names to be used in this optimisation.
	 * 
	 * @return
	 */
	@NonNull
	List<String> getEvaluationProcesses();
}
