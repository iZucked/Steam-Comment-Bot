/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * The {@link IFitnessCore} is the main fitness function object. A single {@link IFitnessCore} can represent multiple {@link IFitnessComponent}s. The {@link IFitnessCore} is used to evaluate
 * {@link ISequences} instances either in whole, or progressively throughout an optimisation process.
 * 
 * @author Simon Goodall
 * 
 */
public interface IFitnessCore {

	/**
	 * Initialise or re-initialise the fitness core. This will reset the state associated with the {@link #evaluate(ISequences, List)} method.
	 */
	void init(@NonNull IOptimisationData data);

	/**
	 * Return a {@link Collection} of {@link IFitnessComponent} instances that are represented by this {@link IFitnessCore}.
	 * 
	 * @return
	 */
	@NonNull
	Collection<IFitnessComponent> getFitnessComponents();

	/**
	 * Evaluates the full fitness of the given sequences. If this returns false, the sequences are unacceptably bad, and the caller should back out of any more evaluation immediately
	 * 
	 * @param fullSequences
	 */
	boolean evaluate(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState);

	/**
	 * Evaluates the fitness of the given sequence. This method takes a list of affected resources used to indicate that only the given resources have changed since the previous evaluation. If this is
	 * the first invocation of this method since the last {@link #init()} call, then a full evaluation must be performed and subsequent calls can take advantage of the affected list. Likewise, passing
	 * null into the affectedResources will perform a full evaluation.
	 * 
	 * @param fullSequences
	 */
	boolean evaluate(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @Nullable Collection<IResource> affectedResources);

	/**
	 * Notify the fitness core that the given sequences have been accepted. These parameters should have been given to the last invocation of {@link #evaluate(ISequences, Collection)}.
	 * 
	 * @param fullSequences
	 * @param affectedResources
	 */
	void accepted(@NonNull ISequences fullSequences, @Nullable Collection<IResource> affectedResources);

	/**
	 * Clean up resources once the {@link IFitnessCore} is no longer required.
	 */
	void dispose();

	/**
	 * Add annotations of the given sequences to the associated {@link IAnnotatedSolution}. Performs a full evaluation, but should <em>not</em> change the fitness stored in the associated components
	 * 
	 * @param fullSequences
	 *            sequences to evaluate
	 * @param solution
	 *            annotated solution for these sequences
	 */
	void annotate(@NonNull final ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution);
}
