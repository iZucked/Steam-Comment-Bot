/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;

/**
 * The {@link IMultiObjectiveFitnessEvaluator} interface defines objects which can be used in a multiobjective optimisation process to evaluate fitnesses linked to multiple objectives.
 * @author Alex Churchill
 * 
 */
public interface IMultiObjectiveFitnessEvaluator extends IFitnessEvaluator{

	/**
	 * Evaluate a sequence and return a map of the fitnesses for each IFitnessComponent.
	 * @param rawSequences
	 * @param fullSequences
	 * @param evaluationState
	 * @param affectedResources
	 * @return
	 */
	Map<IFitnessComponent, Long> getObjectiveValues(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull Collection<IResource> affectedResources);

	long[] getObjectiveValuesForComponentClasses(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull Collection<IResource> affectedResources, @NonNull List<IFitnessComponent> fitnessComponents);

	void updateBest(@NonNull final ISequences rawSequences, final ISequences fullSequences, @NonNull final IEvaluationState evaluationState);

	long[] getCombinedFitnessAndObjectiveValuesForComponentClasses(@NonNull ISequences rawSequences,
			@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState,
			@Nullable Collection<IResource> affectedResources, @NonNull List<IFitnessComponent> fitnessComponents);
}
