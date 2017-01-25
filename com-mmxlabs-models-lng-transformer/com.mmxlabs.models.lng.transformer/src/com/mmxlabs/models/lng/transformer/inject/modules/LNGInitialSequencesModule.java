/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.transformer.util.OptimisationTransformer;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.modules.FitnessFunctionInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.OptimiserContextModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures. This is a {@link PrivateModule} to avoid "leakage" into the parent injector
 * 
 */
public class LNGInitialSequencesModule extends PrivateModule {
	@NonNull
	public static final String KEY_GENERATED_RAW_SEQUENCES = "generated-raw-sequences";

	@NonNull
	public static final String KEY_GENERATED_SOLUTION_PAIR = "generated-solution-pair";

	@Override
	protected void configure() {
		// if (Platform.isRunning()) {
		// bind(IConstraintCheckerRegistry.class).toProvider(service(IConstraintCheckerRegistry.class).single());
		// }
		//
		// install(new ConstraintCheckerInstantiatorModule());
//		install(new OptimiserContextModule());

		if (Platform.isRunning()) {
			bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
		}

		install(new FitnessFunctionInstantiatorModule());

		// install(new LocalSearchOptimiserModule());
		// install(new LinearFitnessEvaluatorModule());

		bind(IFitnessHelper.class).to(FitnessHelper.class);

		bind(IOptimisationTransformer.class).to(OptimisationTransformer.class).in(Singleton.class);
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
	private IInitialSequenceBuilder provideIInitialSequenceBuilder(@NonNull final Injector injector, @NonNull final List<IPairwiseConstraintChecker> pairwiseCheckers) {
		final IInitialSequenceBuilder builder = new ConstrainedInitialSequenceBuilder(pairwiseCheckers);
		injector.injectMembers(builder);
		return builder;
	}

	@Provides
	@Singleton
	@Named(KEY_GENERATED_RAW_SEQUENCES)
	@Exposed
	private ISequences provideInitialSequences(@NonNull final IOptimisationTransformer optimisationTransformer, @NonNull final IOptimisationData data, @NonNull final ModelEntityMap modelEntityMap) {

		final ISequences sequences = optimisationTransformer.createInitialSequences(data, modelEntityMap);

		return sequences;
	}

	@Provides
	@Singleton
	@Named(KEY_GENERATED_SOLUTION_PAIR)
	@Exposed
	private IMultiStateResult provideSolutionPair(@NonNull final Injector injector, @NonNull @Named(KEY_GENERATED_RAW_SEQUENCES) final ISequences rawSequences,
			@NonNull List<IFitnessComponent> fitnessComponents, @NonNull IFitnessHelper fitnessHelper, @NonNull IFitnessCombiner fitnessCombiner) {

		final Pair<IAnnotatedSolution, IEvaluationState> p = LNGSchedulerJobUtils.evaluateCurrentState(injector, injector.getInstance(IOptimisationData.class), rawSequences);
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
	}
}
