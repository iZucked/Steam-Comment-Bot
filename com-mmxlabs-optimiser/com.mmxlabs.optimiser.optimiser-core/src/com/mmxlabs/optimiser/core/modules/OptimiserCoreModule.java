/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessInstantiator;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public class OptimiserCoreModule extends AbstractModule {

	public static final String ENABLED_EVALUATION_PROCESS_NAMES = "EnabledEvaluationProcessNames";
	public static final String ENABLED_CONSTRAINT_NAMES = "EnabledConstraintNames";
	public static final String ENABLED_FITNESS_NAMES = "EnabledFitnessNames";

	@Override
	protected void configure() {

	}

	@Provides
	private List<IEvaluationProcess> provideEvaluationProcesses(@NonNull final Injector injector, @NonNull final IEvaluationProcessRegistry evaluationProcessRegistry,
			@NonNull @Named(ENABLED_EVALUATION_PROCESS_NAMES) final List<String> enabledProcessNames, @NonNull final IOptimisationData optimisationData) {
		final EvaluationProcessInstantiator evaluationProcessInstantiator = new EvaluationProcessInstantiator();
		final List<IEvaluationProcess> evaluationProcesses = evaluationProcessInstantiator.instantiateEvaluationProcesses(evaluationProcessRegistry, enabledProcessNames, optimisationData);

		final List<IEvaluationProcess> result = new ArrayList<IEvaluationProcess>(evaluationProcesses.size());
		for (final IEvaluationProcess process : evaluationProcesses) {
			if (process != null) {
				result.add(process);
				injector.injectMembers(process);
			}
		}
		return result;
	}

	@Provides
	private List<IConstraintChecker> provideConstraintCheckers(@NonNull final Injector injector, @NonNull final IConstraintCheckerRegistry constraintCheckerRegistry,
			@NonNull @Named(ENABLED_CONSTRAINT_NAMES) final List<String> enabledConstraintNames, @NonNull final IOptimisationData optimisationData) {
		final ConstraintCheckerInstantiator constraintCheckerInstantiator = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker> constraintCheckers = constraintCheckerInstantiator.instantiateConstraintCheckers(constraintCheckerRegistry, enabledConstraintNames, optimisationData);

		final List<IConstraintChecker> result = new ArrayList<IConstraintChecker>(constraintCheckers.size());
		for (final IConstraintChecker checker : constraintCheckers) {
			if (checker != null) {
				result.add(checker);
				injector.injectMembers(checker);
			}
		}
		return result;
	}

	@Provides
	private List<IPairwiseConstraintChecker> providePairwiseConstraintCheckers(final List<IConstraintChecker> contraintCheckers) {
		final ArrayList<IPairwiseConstraintChecker> pairwiseCheckers = new ArrayList<IPairwiseConstraintChecker>();
		for (final IConstraintChecker checker : contraintCheckers) {
			if (checker instanceof IPairwiseConstraintChecker) {
				pairwiseCheckers.add((IPairwiseConstraintChecker) checker);
			}
		}

		return pairwiseCheckers;
	}

	@Provides
	@Singleton
	private List<IFitnessComponent> provideFitnessComponents(@NonNull final Injector injector, @NonNull final IFitnessFunctionRegistry fitnessFunctionRegistry,
			@Named(ENABLED_FITNESS_NAMES) final List<String> enabledFitnessNames) {

		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
		final List<IFitnessComponent> fitnessComponents = fitnessComponentInstantiator.instantiateFitnesses(fitnessFunctionRegistry, enabledFitnessNames);
		final Set<IFitnessCore> cores = new HashSet<IFitnessCore>();

		final List<IFitnessComponent> result = new ArrayList<IFitnessComponent>(fitnessComponents.size());
		for (final IFitnessComponent c : fitnessComponents) {
			if (c != null) {
				result.add(c);
				injector.injectMembers(c);
				cores.add(c.getFitnessCore());
			}
		}

		for (final IFitnessCore c : cores) {
			injector.injectMembers(c);
		}

		return result;
	}

	@Provides
	@Singleton
	private IOptimisationContext createOptimisationContext(@NonNull final IOptimisationData data, @NonNull @Named("Initial") final ISequences sequences,
			@NonNull final IFitnessFunctionRegistry fitnessFunctionRegistry, @NonNull final IConstraintCheckerRegistry constraintCheckerRegistry,
			@NonNull final IEvaluationProcessRegistry evaluationProcessRegistry, @NonNull @Named(ENABLED_CONSTRAINT_NAMES) final List<String> enabledConstraintNames,
			@NonNull @Named(ENABLED_FITNESS_NAMES) final List<String> enabledFitnessNames) {

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

}
