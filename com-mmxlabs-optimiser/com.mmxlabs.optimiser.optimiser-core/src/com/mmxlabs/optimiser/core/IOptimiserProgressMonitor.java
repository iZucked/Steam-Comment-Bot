/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A callback interface to monitor progress of an optimisation. An {@link IOptimiser} instance will report the initial and ending states and report back updates during the optimisation process.
 * 
 * @author Simon Goodall
 * 
 */
public interface IOptimiserProgressMonitor {

	/**
	 * Notify that the optimisation is starting with the given initial state.
	 * 
	 * @param optimiser
	 * @param initialFitness
	 * @param initialState
	 */
	void begin(@NonNull IOptimiser optimiser, long initialFitness, @Nullable IAnnotatedSolution annotatedSolution);

	/**
	 * Report back the current and best state found at the given iteration number. This method will be called from the main optimisation loop so implementations should return as quickly as possible.
	 * 
	 * @param optimiser
	 * @param iteration
	 * @param currentFitness
	 * @param bestFitness
	 * @param currentState
	 * @param bestState
	 */
	void report(@NonNull IOptimiser optimiser, int iteration, long currentFitness, long bestFitness, @Nullable IAnnotatedSolution currentSolution, @Nullable IAnnotatedSolution bestSolution);

	/**
	 * Notify the optimisation has finished with the given solution as the best found.
	 * 
	 * @param optimiser
	 * @param bestFitness
	 * @param bestState
	 */
	void done(@NonNull IOptimiser optimiser, long bestFitness, @Nullable IAnnotatedSolution annotatedSolution);
}
