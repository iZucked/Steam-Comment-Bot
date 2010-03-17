package com.mmxlabs.optimiser.fitness;

import java.util.Map;

import com.mmxlabs.optimiser.ISequences;

/**
 * {@link IFitnessHelper} implementations provide helper methods to evaluate the
 * fitness of a set of {@link ISequences}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public interface IFitnessHelper<T> {

	/**
	 * Evaluate the full fitness of the given sequences using the given
	 * {@link IFitnessComponent}s
	 * 
	 * @param sequences
	 * @param fitnessFunctions
	 */

	void evaluateSequences(ISequences<T> sequences,
			Map<IFitnessComponent<T>, Double> fitnessFunctions);
}
