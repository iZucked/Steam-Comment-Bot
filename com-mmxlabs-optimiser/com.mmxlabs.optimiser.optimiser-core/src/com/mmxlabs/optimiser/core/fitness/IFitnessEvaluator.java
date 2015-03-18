/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * The {@link IFitnessEvaluator} interface defines objects which can be used in an optimisation process to determine whether or not the current fitness is a suitable improvement. The
 * {@link #init(ISequences)} method needs to be called at the start of the process and further invocations of this class should use {@link #checkSequences(ISequences)}. The {@link IFitnessComponent}
 * passed to the {@link #setFitnessComponents(List)} will be used to evaluate fitnesses. As such they should not be used while using this instance.
 * 
 * @author Simon Goodall
 * 
 */
public interface IFitnessEvaluator {

	/**
	 * Returns the {@link List} of {@link IFitnessComponent}s used by this {@link IFitnessEvaluator}
	 * 
	 * @return
	 */
	@NonNull
	List<IFitnessComponent> getFitnessComponents();

	@NonNull
	List<IEvaluationProcess> getEvaluationProcesses();

	/**
	 * Set the list of {@link IFitnessComponent}s to be used by this {@link IFitnessEvaluator}. Note these {@link IFitnessComponent}s instances should not be elsewhere while using this object.
	 * 
	 * @param fitnessComponents
	 */
	void setFitnessComponents(@NonNull List<IFitnessComponent> fitnessComponents);

	/**
	 * Initialise {@link IFitnessEvaluator} and ensure all the relevant setters have been called.
	 */
	void init();

	/**
	 * Initialise the {@link IFitnessEvaluator} with {@link IOptimisationData} object. This should be called once at the beginning of an optimisation process.
	 * 
	 * @param data
	 */
	void setOptimisationData(@NonNull IOptimisationData data);

	/**
	 * Set the initial sequences. These should have had any manipulation from {@link ISequencesManipulator} already applied.
	 * 
	 * @param sequences
	 */
	void setInitialSequences(@NonNull ISequences sequences, @NonNull IEvaluationState evaluationState);

	/**
	 * Evaluate the given {@link ISequences} to determine whether or not they are accepted as a better state than the previously accepted state. If accepted, then the {@link ISequences} "state" will
	 * be recorded and used as the basis for future checks. {@link #setInitialSequences(ISequences)} must have been called previously to setup the initial state.
	 * 
	 * @param sequences
	 * @return
	 */
	boolean evaluateSequences(@NonNull ISequences sequences, @NonNull IEvaluationState evaluationState, @NonNull Collection<IResource> affectedResources);

	/**
	 * Returns the best {@link ISequences} instance seen by this {@link IFitnessEvaluator}. The value of best is determined by the implementation.
	 * 
	 * @return
	 */
	@Nullable
	Pair<ISequences, IEvaluationState> getBestSequences();

	/**
	 * Returns the fitness value of the best {@link ISequences} instance returned by {@link #getBestSequences()};
	 * 
	 * @return
	 */
	long getBestFitness();

	/**
	 * Returns the current {@link ISequences} instance in use by this {@link IFitnessEvaluator}.
	 * 
	 * @return
	 */
	@Nullable
	Pair<ISequences, IEvaluationState> getCurrentSequences();

	/**
	 * Returns the fitness value of the current {@link ISequences} instance returned by {@link #getCurrentSequences()};
	 * 
	 * @return
	 */
	long getCurrentFitness();

	/**
	 * Returns an annotated solution for the best sequences so far
	 * 
	 * @param context
	 * @return
	 */
	@Nullable
	IAnnotatedSolution getBestAnnotatedSolution(@NonNull final IOptimisationContext context);

	/**
	 * Returns an annotated solution for the current sequences.
	 * 
	 * @param context
	 * @return
	 */
	@Nullable
	IAnnotatedSolution getCurrentAnnotatedSolution(@NonNull final IOptimisationContext context);
}
