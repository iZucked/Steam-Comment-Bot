/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.concurrent.JobExecutor;
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
	void optimise(@NonNull IOptimisationContext optimisationContext, JobExecutor jobExecutor);

	/**
	 * Returns the {@link IFitnessEvaluator} instance used within the optimisation process.
	 * 
	 * @return
	 */
	@NonNull
	IFitnessEvaluator getFitnessEvaluator();
}
