/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess.Phase;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Basic implementation of {@link IFitnessHelper}.
 * 
 * @author Simon Goodall
 */
public final class FitnessHelper implements IFitnessHelper {

	@Override
	public boolean evaluateSequencesFromComponents(@NonNull final ISequences fullSequences, @NonNull final IEvaluationState evaluationState,
			@NonNull final Collection<IFitnessComponent> fitnessComponents) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);

		return evaluateSequencesFromCores(fullSequences, evaluationState, fitnessCores);
	}

	@Override
	public boolean evaluateSequencesFromCores(@NonNull final ISequences fullSequences, @NonNull final IEvaluationState evaluationState, @NonNull final Collection<IFitnessCore> fitnessCores) {

		for (final IFitnessCore core : fitnessCores) {
			if (!core.evaluate(fullSequences, evaluationState)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean evaluateSequencesFromComponents(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final Collection<IFitnessComponent> fitnessComponents,
			@Nullable final Collection<IResource> affectedResources) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);

		return evaluateSequencesFromCores(sequences, evaluationState, fitnessCores, affectedResources);

	}

	@Override
	public boolean evaluateSequencesFromCores(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final Collection<IFitnessCore> fitnessCores,
			@Nullable final Collection<IResource> affectedResources) {
		for (final IFitnessCore core : fitnessCores) {
			if (!core.evaluate(sequences, evaluationState, affectedResources)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void initFitnessComponents(@NonNull final Collection<IFitnessComponent> fitnessComponents, @NonNull final IOptimisationData data) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);
		initFitnessCores(fitnessCores, data);

	}

	@NonNull
	@Override
	public Set<IFitnessCore> getFitnessCores(@NonNull final Collection<IFitnessComponent> fitnessComponents) {
		final Set<IFitnessCore> fitnessCores = new HashSet<IFitnessCore>();
		for (final IFitnessComponent component : fitnessComponents) {
			fitnessCores.add(component.getFitnessCore());
		}
		return fitnessCores;
	}

	@Override
	public void initFitnessCores(@NonNull final Collection<IFitnessCore> fitnessCores, @NonNull final IOptimisationData data) {
		for (final IFitnessCore core : fitnessCores) {
			core.init(data);
		}
	}

	@Override
	public void acceptFromComponents(@NonNull final Collection<IFitnessComponent> fitnessComponents, @NonNull final ISequences fullSequences, @Nullable final Collection<IResource> affectedResources) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);
		acceptFromCores(fitnessCores, fullSequences, affectedResources);
	}

	@Override
	public void acceptFromCores(@NonNull final Collection<IFitnessCore> fitnessCores, @NonNull final ISequences fullSequences, @Nullable final Collection<IResource> affectedResources) {
		for (final IFitnessCore core : fitnessCores) {
			core.accepted(fullSequences, affectedResources);
		}
	}

	@NonNull
	@Override
	public IAnnotatedSolution buildAnnotatedSolution(@NonNull final IOptimisationContext context, @NonNull final ISequences fullSequences, @NonNull final IEvaluationState evaluationState,
			@NonNull final Collection<IFitnessComponent> fitnessComponents, @NonNull final Collection<IEvaluationProcess> evaluationProcesses) {

		final AnnotatedSolution result = new AnnotatedSolution(fullSequences, context, evaluationState);

		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			evaluationProcess.annotate(Phase.Checked_Evaluation, fullSequences, evaluationState, result);
			evaluationProcess.annotate(Phase.Final_Evaluation, fullSequences, evaluationState, result);
		}

		final Set<IFitnessCore> cores = getFitnessCores(fitnessComponents);
		for (final IFitnessCore core : cores) {
			core.annotate(fullSequences, evaluationState, result);
		}

		return result;
	}
}
