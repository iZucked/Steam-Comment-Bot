/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.modules;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.fitness.IMultiObjectiveFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.MultiObjectiveFitnessEvaluator;

/**
 * A {@link Guice} module to provide a Local Search optimiser
 * 
 * @author Simon Goodall, Tom Hinton
 * @since 2.0
 */
public class LinearFitnessEvaluatorModule extends AbstractModule {

	public static final String LINEAR_FITNESS_WEIGHTS_MAP = "LinearFitnessWeights";
	public static final String MULTI_OBJECTIVE_FITNESS_EVALUATOR = "MULTI_OBJECTIVE_FITNESS_EVALUATOR";

	@Override
	protected void configure() {
		bind(IFitnessHelper.class).to(FitnessHelper.class);
	}

	@Provides
	// @Singleton
	private IFitnessCombiner createFitnessCombiner(@Named(LINEAR_FITNESS_WEIGHTS_MAP) final Map<String, Double> weightsMap) {

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);
		return combiner;
	}

	@Provides
	// @Singleton
	private IFitnessEvaluator createFitnessEvaluator(@NonNull final Injector injector, @NonNull final IThresholder thresholder, @NonNull final List<IFitnessComponent> fitnessComponents,
			@NonNull final List<IEvaluationProcess> evaluationProcesses) {
		// create a linear Fitness evaluator.

		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator(thresholder, fitnessComponents, evaluationProcesses);
		injector.injectMembers(fitnessEvaluator);

		return fitnessEvaluator;
	}

	@Provides
	private IMultiObjectiveFitnessEvaluator createMultiObjectiveFitnessEvaluator(@NonNull final Injector injector, @NonNull final IThresholder thresholder,
			@NonNull final List<IFitnessComponent> fitnessComponents, @NonNull final List<IEvaluationProcess> evaluationProcesses) {

		final MultiObjectiveFitnessEvaluator fitnessEvaluator = new MultiObjectiveFitnessEvaluator(thresholder, fitnessComponents, evaluationProcesses);
		injector.injectMembers(fitnessEvaluator);

		return fitnessEvaluator;
	}

}
