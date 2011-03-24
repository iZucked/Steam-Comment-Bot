/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;
import java.util.Set;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

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
	void initFitnessCores(Collection<IFitnessCore<T>> fitnessCores,
			IOptimisationData<T> data);

	/**
	 * Initialise {@link IFitnessCore}s based upon the a {@link Collection} of
	 * {@link IFitnessComponent}s
	 * 
	 * @param fitnessCores
	 */
	void initFitnessComponents(
			Collection<IFitnessComponent<T>> fitnessComponents,
			IOptimisationData<T> data);

	/**
	 * Evaluate the fitness of the given sequences using the given
	 * {@link IFitnessCores}s
	 * 
	 * @param sequences
	 * @param fitnessFunctions
	 * @return
	 */

	boolean evaluateSequencesFromCores(ISequences<T> sequences,
			Collection<IFitnessCore<T>> fitnessCores);

	/**
	 * Evaluate the fitness of the given sequences using the given
	 * {@link IFitnessCores}s
	 * 
	 * @param sequences
	 * @param fitnessFunctions
	 * @param affectedResources
	 * @return
	 */

	boolean evaluateSequencesFromCores(ISequences<T> sequences,
			Collection<IFitnessCore<T>> fitnessCores,
			Collection<IResource> affectedResources);

	/**
	 * Evaluate the fitness of the given sequences using the given
	 * {@link IFitnessCores}s
	 * 
	 * @param sequences
	 * @param fitnessFunctions
	 * @return
	 */

	boolean evaluateSequencesFromComponents(ISequences<T> sequences,
			Collection<IFitnessComponent<T>> fitnessComponents);

	/**
	 * Evaluate the fitness of the given sequences using the given
	 * {@link IFitnessCores}s
	 * 
	 * @param sequences
	 * @param fitnessFunctions
	 * @return
	 */

	boolean evaluateSequencesFromComponents(ISequences<T> sequences,
			Collection<IFitnessComponent<T>> fitnessComponents,
			Collection<IResource> affectedResources);

	/**
	 * The {@link #accept(ISequences, Collection)} method is to be invoked when
	 * a {@link ISequences} object is accepted as the new state. The
	 * {@link ISequences} object must have been passed to the
	 * {@link IFitnessCore#evaluate(ISequences, Collection) method previously.
	 * This could be directly or via the @link
	 * #evaluateSequencesFromComponents(ISequences, Collection, Collection)} or
	 * 
	 * @link{#evaluateSequencesFromCores(ISequences, Collection, Collection)}
	 *                                               methods.
	 * 
	 * @param sequences
	 * @param affectedResources
	 */
	void acceptFromCores(Collection<IFitnessCore<T>> fitnessCores,
			ISequences<T> sequences, Collection<IResource> affectedResources);

/**
	 * The {@link #accept(ISequences, Collection)} method is to be invoked when
	 * a {@link ISequences} object is accepted as the new state. The
	 * {@link ISequences} object must have been passed to the
	 * {@link IFitnessCore#evaluate(ISequences, Collection) method previously.
	 * This could be directly or via the
	 * 
	 * @link #evaluateSequencesFromComponents(ISequences, Collection,
	 *       Collection)} or
	 *       {@link #evaluateSequencesFromCores(ISequences, Collection, Collection)}
	 *       methods.
	 * 
	 * @param sequences
	 * @param affectedResources
	 */
	void acceptFromComponents(
			Collection<IFitnessComponent<T>> fitnessComponents,
			ISequences<T> sequences, Collection<IResource> affectedResources);

	/**
	 * Returns the set of {@link IFitnessCore}s that are used by the given
	 * {@link IFitnessComponent}s
	 * 
	 * @param fitnessComponents
	 * @return
	 */
	Set<IFitnessCore<T>> getFitnessCores(
			Collection<IFitnessComponent<T>> fitnessComponents);

	/**
	 * Construct an annotated solution for the given state. Performs an
	 * evaluation with the given components as well, so watch out for that.
	 * 
	 * @param context
	 * @param state
	 * @param fitnessComponents
	 * @return
	 */
	public IAnnotatedSolution<T> buildAnnotatedSolution(
			IOptimisationContext<T> context, ISequences<T> state,
			Collection<IFitnessComponent<T>> fitnessComponents,
			final boolean forExport);
}
