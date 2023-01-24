/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import static org.ops4j.peaberry.Peaberry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Exposed;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.optimiser.core.modules.FitnessFunctionInstantiatorModule;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;

/**
 * Used to return a simple interface to evaluate a set of sequences.
 * 
 */
public class SequencesEvaluatorModule extends PrivateModule {

	@Override
	protected void configure() {

		if (Platform.isRunning()) {
			bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
		}

		install(new FitnessFunctionInstantiatorModule());

		bind(IFitnessHelper.class).to(FitnessHelper.class);
	}

	@Provides
	// @Singleton
	private IFitnessCombiner createFitnessCombiner(@Named(LinearFitnessEvaluatorModule.LINEAR_FITNESS_WEIGHTS_MAP) final Map<String, Double> weightsMap) {

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);
		return combiner;
	}

	@Provides
	@Singleton
	@Exposed
	private ISequenceEvaluator provideSolutionPair(@NonNull final Injector injector, @NonNull List<IFitnessComponent> fitnessComponents, @NonNull IFitnessHelper fitnessHelper,
			@NonNull IFitnessCombiner fitnessCombiner) {

		return new ISequenceEvaluator() {
			public IMultiStateResult eval(ISequences rawSequences) {
				final ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class);
				try {
					scope.enter();
					final Pair<IAnnotatedSolution, IEvaluationState> p = LNGSchedulerJobUtils.evaluateCurrentState(injector, rawSequences);
					IAnnotatedSolution annotatedSolution = p.getFirst();
					IEvaluationState evaluationState = p.getSecond();

					fitnessHelper.evaluateSequencesFromComponents(annotatedSolution.getFullSequences(), evaluationState, fitnessComponents, null);
					final Map<String, Long> currentFitnesses = new HashMap<>();
					for (final IFitnessComponent fitnessComponent : fitnessComponents) {
						currentFitnesses.put(fitnessComponent.getName(), fitnessComponent.getFitness());
					}

					final Map<String, Object> extraAnnotations = new HashMap<>();
					extraAnnotations.put(OptimiserConstants.G_AI_fitnessComponents, currentFitnesses);
					return new MultiStateResult(rawSequences, extraAnnotations);
				} finally {
					scope.exit();
				}
			}
		};
	}

	public interface ISequenceEvaluator {
		IMultiStateResult eval(ISequences sequences);
	}
}
