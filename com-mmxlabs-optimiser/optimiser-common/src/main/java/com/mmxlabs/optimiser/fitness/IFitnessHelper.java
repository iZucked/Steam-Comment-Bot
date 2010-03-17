package com.mmxlabs.optimiser.fitness;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.IResource;
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
	 * Evaluate the fitness of the given sequences using the given
	 * {@link IFitnessCores}s
	 * 
	 * @param sequences
	 * @param fitnessFunctions
	 */

	void evaluateSequencesFromCores(ISequences<T> sequences,
			Collection<IFitnessCore<T>> fitnessCores);

	/**
	 * Evaluate the fitness of the given sequences using the given
	 * {@link IFitnessCores}s
	 * 
	 * @param sequences
	 * @param fitnessFunctions
	 * @param affectedResources
	 */

	void evaluateSequencesFromCores(ISequences<T> sequences,
			Collection<IFitnessCore<T>> fitnessCores,
			List<IResource> affectedResources);

	/**
	 * Evaluate the fitness of the given sequences using the given
	 * {@link IFitnessCores}s
	 * 
	 * @param sequences
	 * @param fitnessFunctions
	 */

	void evaluateSequencesFromComponents(ISequences<T> sequences,
			Collection<IFitnessComponent<T>> fitnessComponents);

	/**
	 * Evaluate the fitness of the given sequences using the given
	 * {@link IFitnessCores}s
	 * 
	 * @param sequences
	 * @param fitnessFunctions
	 */

	void evaluateSequencesFromComponents(ISequences<T> sequences,
			Collection<IFitnessComponent<T>> fitnessComponents,
			List<IResource> affectedResources);
}
