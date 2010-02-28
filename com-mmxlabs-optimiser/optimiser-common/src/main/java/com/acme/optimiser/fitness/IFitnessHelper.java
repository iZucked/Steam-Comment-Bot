package com.acme.optimiser.fitness;

import java.util.List;

import com.acme.optimiser.ISequences;
import com.acme.optimiser.fitness.IFitnessFunction;

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
	 * {@link IFitnessFunction}s
	 * 
	 * @param sequences
	 * @param fitnessFunctions
	 */
	void evaluateSequences(ISequences<T> sequences,
			List<IFitnessFunction<T>> fitnessFunctions);
}
