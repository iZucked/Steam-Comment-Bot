/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.print.attribute.Size2DSyntax;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IMultiObjectiveFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.lso.IThresholder;

public class MultiObjectiveFitnessEvaluator extends LinearSimulatedAnnealingFitnessEvaluator implements IMultiObjectiveFitnessEvaluator {

	public MultiObjectiveFitnessEvaluator(@NonNull IThresholder thresholder,
			@NonNull List<IFitnessComponent> fitnessComponents, @NonNull List<IEvaluationProcess> evaluationProcesses) {
		super(thresholder, fitnessComponents, evaluationProcesses);
	}

	@Override
	public Map<IFitnessComponent, Long> getObjectiveValues(@NonNull ISequences rawSequences,
			@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState,
			@NonNull Collection<IResource> affectedResources) {
		Map<IFitnessComponent,Long> map = 
			    IntStream.range(0,getFitnessComponents().size())
	             .boxed()
	             .collect(Collectors.toMap (i -> getFitnessComponents().get(i), i -> getFitnessComponents().get(i).getFitness()));
		return map;
		}

	@Override
	public long[] getObjectiveValuesForComponentClasses(@NonNull ISequences rawSequences,
			@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState,
			@NonNull Collection<IResource> affectedResources, @NonNull List<IFitnessComponent> fitnessComponents) {
		evaluateComponents(fullSequences, evaluationState, affectedResources);
		long[] fitnesses  = new long[fitnessComponents.size()];
		for (int i = 0; i < fitnessComponents.size(); i++) {
			fitnesses[i] = fitnessComponents.get(i).getFitness();
		}
		return fitnesses;
	}
	
	@Override
	public long[] getCombinedFitnessAndObjectiveValuesForComponentClasses(@NonNull ISequences rawSequences,
			@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState,
			Collection<IResource> affectedResources, @NonNull List<IFitnessComponent> selectedFitnessComponents) {
		if (!evaluateComponents(fullSequences, evaluationState, affectedResources)) {
			return null;
		}
		long[] fitnesses  = new long[selectedFitnessComponents.size() + 1];
		fitnesses[0] = getFitnessCombiner().calculateFitness(getFitnessComponents());
		for (int i = 1; i < selectedFitnessComponents.size() + 1; i++) {
			fitnesses[i] = selectedFitnessComponents.get(i-1).getFitness();
		}
		return fitnesses;
	}

	private boolean evaluateComponents(ISequences fullSequences, IEvaluationState evaluationState,
			Collection<IResource> affectedResources) {
		// Evaluates the current sequences
		if (affectedResources == null) {
			if (!fitnessHelper.evaluateSequencesFromComponents(fullSequences, evaluationState, getFitnessComponents())) {
				return false;
			}
		} else {
			if (!fitnessHelper.evaluateSequencesFromComponents(fullSequences, evaluationState, getFitnessComponents(), affectedResources)) {
				return false;
			}
		}
		return true;
	}


	@Override
	public void updateBest(@NonNull ISequences rawSequences, ISequences fullSequences,
			@NonNull IEvaluationState evaluationState) {
		// Store current fitness and sequences
		bestSequences = new Triple<ISequences, ISequences, IEvaluationState>(new Sequences(rawSequences), new Sequences(fullSequences), evaluationState);

			for (final IFitnessComponent component : fitnessComponents) {
				bestFitnesses.put(component.getName(), component.getFitness());
			}
	}

}
