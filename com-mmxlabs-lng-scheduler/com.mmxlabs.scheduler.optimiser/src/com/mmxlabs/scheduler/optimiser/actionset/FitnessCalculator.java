package com.mmxlabs.scheduler.optimiser.actionset;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;

public class FitnessCalculator {

	@Inject
	@NonNull
	private List<IFitnessComponent> fitnessComponents;

	@Inject
	@NonNull
	private IFitnessHelper fitnessHelper;

	@Inject
	@NonNull
	private IFitnessCombiner fitnessCombiner;

	public long evaluateSequencesFitness(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {

		// Evaluates the current sequences
		if (affectedResources == null) {
			if (!fitnessHelper.evaluateSequencesFromComponents(sequences, evaluationState, fitnessComponents)) {
				return Long.MAX_VALUE;
			}
		} else {
			if (!fitnessHelper.evaluateSequencesFromComponents(sequences, evaluationState, fitnessComponents, affectedResources)) {
				return Long.MAX_VALUE;
			}
		}

		return fitnessCombiner.calculateFitness(fitnessComponents);
	}
}
