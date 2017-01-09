/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * {@link IFitnessHelper} implementations provide helper methods to evaluate the fitness of a set of {@link ISequences}.
 * 
 * @author Simon Goodall
 * 
 */
public interface IFitnessHelper {

	/**
	 * Initialise a {@link Collection} of {@link IFitnessCore}s
	 * 
	 * @param fitnessCores
	 */
	void initFitnessCores(@NonNull Collection<IFitnessCore> fitnessCores, @NonNull IOptimisationData data);

	/**
	 * Initialise {@link IFitnessCore}s based upon the a {@link Collection} of {@link IFitnessComponent}s
	 * 
	 * @param fitnessCores
	 */
	void initFitnessComponents(@NonNull Collection<IFitnessComponent> fitnessComponents, @NonNull IOptimisationData data);

	/**
	 * Evaluate the fitness of the given sequences using the given {@link IFitnessCores}s
	 * 
	 * @param fullSequences
	 * @param fitnessFunctions
	 * @return
	 */

	boolean evaluateSequencesFromCores(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull Collection<IFitnessCore> fitnessCores);

	/**
	 * Evaluate the fitness of the given sequences using the given {@link IFitnessCores}s
	 * 
	 * @param fullSequences
	 * @param fitnessFunctions
	 * @param affectedResources
	 * @return
	 */

	boolean evaluateSequencesFromCores(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationStates, @NonNull Collection<IFitnessCore> fitnessCores,
			@Nullable Collection<IResource> affectedResources);

	/**
	 * Evaluate the fitness of the given sequences using the given {@link IFitnessCores}s
	 * 
	 * @param fullSequences
	 * @param fitnessFunctions
	 * @return
	 */

	boolean evaluateSequencesFromComponents(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull Collection<IFitnessComponent> fitnessComponents);

	/**
	 * Evaluate the fitness of the given sequences using the given {@link IFitnessCores}s
	 * 
	 * @param fullSequences
	 * @param fitnessFunctions
	 * @return
	 */

	boolean evaluateSequencesFromComponents(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull Collection<IFitnessComponent> fitnessComponents,
			@Nullable Collection<IResource> affectedResources);

	/**
	 * The {@link #accept(ISequences, Collection)} method is to be invoked when a {@link ISequences} object is accepted as the new state. The {@link ISequences} object must have been passed to the
	 * {@link IFitnessCore#evaluate(ISequences, Collection) method previously. This could be directly or via the @link #evaluateSequencesFromComponents(ISequences, Collection, Collection)} or
	 * 
	 * @link{#evaluateSequencesFromCores(ISequences, Collection, Collection)} methods.
	 * 
	 * @param fullSequences
	 * @param affectedResources
	 */
	void acceptFromCores(@NonNull Collection<IFitnessCore> fitnessCores, @NonNull ISequences fullSequences, @Nullable Collection<IResource> affectedResources);

	/**
	 * The {@link #accept(ISequences, Collection)} method is to be invoked when a {@link ISequences} object is accepted as the new state. The {@link ISequences} object must have been passed to the
	 * {@link IFitnessCore#evaluate(ISequences, Collection) method previously. This could be directly or via the
	 * 
	 * @link #evaluateSequencesFromComponents(ISequences, Collection, Collection)} or {@link #evaluateSequencesFromCores(ISequences, Collection, Collection)} methods.
	 * 
	 * @param fullSequences
	 * @param affectedResources
	 */
	void acceptFromComponents(@NonNull Collection<IFitnessComponent> fitnessComponents, @NonNull ISequences fullSequences, @Nullable Collection<IResource> affectedResources);

	/**
	 * Returns the set of {@link IFitnessCore}s that are used by the given {@link IFitnessComponent}s
	 * 
	 * @param fitnessComponents
	 * @return
	 */
	@NonNull
	Set<IFitnessCore> getFitnessCores(@NonNull Collection<IFitnessComponent> fitnessComponents);

	/**
	 * Construct an annotated solution for the given fullSequences. Performs an evaluation with the given components as well, so watch out for that.
	 * 
	 * @param context
	 * @param fullSequences
	 * @param fitnessComponents
	 * @return
	 */
	@NonNull
	IAnnotatedSolution buildAnnotatedSolution(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull Collection<IFitnessComponent> fitnessComponents,
			@NonNull Collection<IEvaluationProcess> evaluationProcesses);

	void addToAnnotatedSolution(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull Collection<IFitnessComponent> fitnessComponents,
			@NonNull AnnotatedSolution annotatedSolution);
}
