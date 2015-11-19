/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.modules;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.IEvaluationContext;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.impl.EvaluationContext;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public class OptimiserContextModule extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides

	private IOptimisationContext createOptimisationContext(@NonNull final IOptimisationData data, @NonNull @Named("Input") final ISequences sequences,

			@NonNull final IFitnessFunctionRegistry fitnessFunctionRegistry, @NonNull final IConstraintCheckerRegistry constraintCheckerRegistry,
			@NonNull final IEvaluationProcessRegistry evaluationProcessRegistry,
			@NonNull @Named(ConstraintCheckerInstantiatorModule.ENABLED_CONSTRAINT_NAMES) final List<String> enabledConstraintNames,
			@NonNull @Named(FitnessFunctionInstantiatorModule.ENABLED_FITNESS_NAMES) final List<String> enabledFitnessNames) {

		final List<String> components = new ArrayList<String>(enabledFitnessNames);
		components.retainAll(fitnessFunctionRegistry.getFitnessComponentNames());

		final List<String> checkers = new ArrayList<String>(enabledConstraintNames);
		checkers.retainAll(constraintCheckerRegistry.getConstraintCheckerNames());

		// Enable all processes
		// final List<String> evaluationProcesses = getEnabledEvaluationProcessNames();
		// log.debug("Available evaluation processes: " + evaluationProcesses);
		// evaluationProcesses.retainAll(evaluationProcessRegistry.getEvaluationProcessNames());

		final List<String> evaluationProcesses = new ArrayList<String>(evaluationProcessRegistry.getEvaluationProcessNames());

		return new OptimisationContext(data, sequences, components, fitnessFunctionRegistry, checkers, constraintCheckerRegistry, evaluationProcesses, evaluationProcessRegistry);
	}

	@Provides
	private IEvaluationContext createEvaluationContext(@NonNull final IOptimisationData data, @NonNull @Named("Input") final ISequences sequences,
			@NonNull final IFitnessFunctionRegistry fitnessFunctionRegistry, @NonNull final IConstraintCheckerRegistry constraintCheckerRegistry,
			@NonNull final IEvaluationProcessRegistry evaluationProcessRegistry) {

		// Enable all processes
		// final List<String> evaluationProcesses = getEnabledEvaluationProcessNames();
		// log.debug("Available evaluation processes: " + evaluationProcesses);
		// evaluationProcesses.retainAll(evaluationProcessRegistry.getEvaluationProcessNames());

		final List<String> evaluationProcesses = new ArrayList<String>(evaluationProcessRegistry.getEvaluationProcessNames());

		return new EvaluationContext(data, sequences, evaluationProcesses, evaluationProcessRegistry);
	}

}
