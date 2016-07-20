/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;

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
	 * Set the initial sequences. These should have had any manipulation from {@link ISequencesManipulator} already applied.
	 * 
	 * @param sequences
	 */
	void setInitialSequences(@NonNull ISequences initialRawSequences, @NonNull ISequences initialFullSequences, @NonNull IEvaluationState evaluationState);

	/**
	 * Evaluate the given {@link ISequences} to determine whether or not they are accepted as a better state than the previously accepted state. If accepted, then the {@link ISequences} "state" will
	 * be recorded and used as the basis for future checks. {@link #setInitialSequences(ISequences)} must have been called previously to setup the initial state.
	 * 
	 * @param sequences
	 * @param potentialFullSequences
	 * @return
	 */
	boolean evaluateSequences(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull Collection<IResource> affectedResources);

	/**
	 * Returns the best {@link ISequences} instances (raw then full) seen by this {@link IFitnessEvaluator}. The value of best is determined by the implementation.
	 * 
	 * @return
	 */
	@NonNull
	Triple<ISequences, ISequences, IEvaluationState> getBestSequences();

	/**
	 * Returns the fitness value of the best {@link ISequences} instance returned by {@link #getBestSequences()};
	 * 
	 * @return
	 */
	long getBestFitness();

	/**
	 * Returns the current {@link ISequences} (raw then full) instance in use by this {@link IFitnessEvaluator}.
	 * 
	 * @return
	 */

	@NonNull
	Triple<ISequences, ISequences, IEvaluationState> getCurrentSequences();

	/**
	 * Returns the fitness value of the current {@link ISequences} instance returned by {@link #getCurrentSequences()};
	 * 
	 * @return
	 */
	long getCurrentFitness();

	/**
	 * Returns the fitness value of the last {@link ISequences} instance returned by {@link #getCurrentSequences()};
	 * 
	 * @return
	 */
	long getLastFitness();

	/**
	 * Returns an annotated solution for the best sequences so far
	 * 
	 * @param context
	 * @return
	 */
	@NonNull
	IAnnotatedSolution getBestAnnotatedSolution();

	/**
	 * Returns an annotated solution for the current sequences.
	 * 
	 * @param context
	 * @return
	 */
	@NonNull
	IAnnotatedSolution getCurrentAnnotatedSolution();

//	@NonNull
//	IAnnotatedSolution createAnnotatedSolution(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState);

	void step();

	void restart();

}
