package com.mmxlabs.optimiser.fitness;

import java.util.Collection;
import java.util.Set;

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
	 * Initialise a {@link Collection} of {@link IFitnessCore}s
	 * 
	 * @param fitnessCores
	 */
	void initFitnessCores(Collection<IFitnessCore<T>> fitnessCores);

	/**
	 * Initialise {@link IFitnessCore}s based upon the a {@link Collection} of
	 * {@link IFitnessComponent}s
	 * 
	 * @param fitnessCores
	 */
	void initFitnessComponents(
			Collection<IFitnessComponent<T>> fitnessComponents);

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
			Collection<IResource> affectedResources);

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
			Collection<IResource> affectedResources);

	/**
	 * Returns the set of {@link IFitnessCore}s that are used by the given
	 * {@link IFitnessComponent}s
	 * 
	 * @param fitnessComponents
	 * @return
	 */
	Set<IFitnessCore<T>> getFitnessCores(
			Collection<IFitnessComponent<T>> fitnessComponents);
}
