/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Interface defining an evaluation context. This ties together static optimisation data and initial state.
 * 
 * @author Simon Goodall
 * 
 */
public interface IEvaluationContext {

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
	ISequences getInputSequences();

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
	List<@NonNull String> getEvaluationProcesses();
}
