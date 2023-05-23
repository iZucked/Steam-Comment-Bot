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
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.CargoModelToScheduleSpecification;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
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
import com.mmxlabs.optimiser.core.modules.FitnessFunctionInstantiatorModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures. This is a {@link PrivateModule} to avoid "leakage" into the parent injector
 * 
 */
public class LNGInitialSequencesModule extends PrivateModule {
	@NonNull
	public static final String KEY_GENERATED_RAW_SEQUENCES = "generated-raw-sequences";

	@NonNull
	public static final String KEY_GENERATED_SOLUTION_PAIR = "generated-solution-pair";

	private static final String QUICK_SOLUTION = "quick";
	private static final String FULL_SOLUTION = "full";

	@Override
	protected void configure() {

		if (Platform.isRunning()) {
			bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
		}

		install(new FitnessFunctionInstantiatorModule());

		bind(IFitnessHelper.class).to(FitnessHelper.class);

		bind(CargoModelToScheduleSpecification.class).in(Singleton.class);
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
	@Named(KEY_GENERATED_RAW_SEQUENCES)
	@Exposed
	private ISequences provideInitialSequences(
			IScenarioDataProvider sdp,
			@NonNull final CargoModelToScheduleSpecification builder,
			@NonNull final ScheduleSpecificationTransformer scheduleSpecificationTransformer, 
			@NonNull final IPhaseOptimisationData data,
			@NonNull final IOptimisationData optiData,
			Injector injector,
			@NonNull final ModelEntityMap modelEntityMap) {

		
		var spec = builder.generateScheduleSpecifications(sdp, ScenarioModelUtil.getCargoModel(sdp));
		return scheduleSpecificationTransformer.createSequences(spec, modelEntityMap, optiData, injector, true);
//		final ISequences sequences = optimisationTransformer.createInitialSequences(data, modelEntityMap);

//		return sequences;
	}

	@Provides
	@Singleton
	@Named(KEY_GENERATED_SOLUTION_PAIR)
	@Exposed
	private IMultiStateResult provideSolutionPair(@NonNull final Injector injector, @Named(LNGTransformerHelper.HINT_EVALUATION_ONLY) boolean evaluationMode) {
		// If we are just evaluating a scenario, then do not run the initial evaluation here as it is redundant.
		// Split into multiple methods to avoid unnecessary fitness etc object instantiation for the quick code path
		if (evaluationMode) {
			return injector.getInstance(Key.get(IMultiStateResult.class, Names.named(QUICK_SOLUTION)));
		} else {
			return injector.getInstance(Key.get(IMultiStateResult.class, Names.named(FULL_SOLUTION)));
		}

	}

	@Provides
	@Singleton
	@Named(FULL_SOLUTION)
	private IMultiStateResult provideEvaluatedSolutionPair(@NonNull final Injector injector, @NonNull @Named(KEY_GENERATED_RAW_SEQUENCES) final ISequences rawSequences,
			@NonNull final List<IFitnessComponent> fitnessComponents, @NonNull final IFitnessHelper fitnessHelper, @NonNull final IFitnessCombiner fitnessCombiner) {

		final Map<String, Object> extraAnnotations = new HashMap<>();

		final Pair<IAnnotatedSolution, IEvaluationState> p = LNGSchedulerJobUtils.evaluateCurrentState(injector, rawSequences);
		final IAnnotatedSolution annotatedSolution = p.getFirst();
		final IEvaluationState evaluationState = p.getSecond();

		fitnessHelper.evaluateSequencesFromComponents(annotatedSolution.getFullSequences(), evaluationState, fitnessComponents, null);
		final Map<String, Long> currentFitnesses = new HashMap<>();
		for (final IFitnessComponent fitnessComponent : fitnessComponents) {
			currentFitnesses.put(fitnessComponent.getName(), fitnessComponent.getFitness());
		}

		extraAnnotations.put(OptimiserConstants.G_AI_fitnessComponents, currentFitnesses);
		return new MultiStateResult(rawSequences, extraAnnotations);
	}

	@Provides
	@Singleton
	@Named(QUICK_SOLUTION)
	private IMultiStateResult provideQuickSolutionPair(@NonNull @Named(KEY_GENERATED_RAW_SEQUENCES) final ISequences rawSequences) {
		// If we are just evaluating a scenario, then do not run the initial evaluation here as it is redundant.
		return new MultiStateResult(rawSequences, new HashMap<>());
	}

}
