package com.mmxlabs.optimiser.lso;

import com.mmxlabs.optimiser.IOptimiser;
import com.mmxlabs.optimiser.ISequences;

/**
 * A callback interface to monitor progress of an optimisation. An
 * {@link IOptimiser} instance will report the initial and ending states and
 * report back updates during the optimisation process.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public interface IOptimiserProgressMonitor<T> {

	/**
	 * Notify that the optimisation is starting with the given initial state.
	 * 
	 * @param optimiser
	 * @param initialFitness
	 * @param initialState
	 */
	void begin(IOptimiser<T> optimiser, long initialFitness,
			ISequences<T> initialState);

	/**
	 * Report back the current and best state found at the given iteration
	 * number. This method will be called from the main optimisation loop so
	 * implementations should return as quickly as possible.
	 * 
	 * @param optimiser
	 * @param iteration
	 * @param currentFitness
	 * @param bestFitness
	 * @param currentState
	 * @param bestState
	 */
	void report(IOptimiser<T> optimiser, int iteration, long currentFitness,
			long bestFitness, ISequences<T> currentState,
			ISequences<T> bestState);

	/**
	 * Notify the optimisation has finished with the given solution as the best
	 * found.
	 * 
	 * @param optimiser
	 * @param bestFitness
	 * @param bestState
	 */
	void done(IOptimiser<T> optimiser, long bestFitness, ISequences<T> bestState);
}
