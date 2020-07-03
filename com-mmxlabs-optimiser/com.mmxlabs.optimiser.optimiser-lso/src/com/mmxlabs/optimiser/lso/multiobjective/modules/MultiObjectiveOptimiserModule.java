/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.multiobjective.modules;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.IMultiObjectiveFitnessEvaluator;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.SequentialParallelSimpleMultiObjectiveOptimiser;

public class MultiObjectiveOptimiserModule extends AbstractModule {
	public static final String MULTIOBJECTIVE_ADDITIONAL_OBJECTIVE_NAMES = "MULTIOBJECTIVE_OBJECTIVE_NAMES";
	public static final String MULTIOBJECTIVE_OBJECTIVE_EPSILON_DOMINANCE_VALUES = "MULTIOBJECTIVE_OBJECTIVE_EPSILON_DOMINANCE_VALUES";

	private CleanableExecutorService executorService;

	public MultiObjectiveOptimiserModule(CleanableExecutorService executorService) {
		this.executorService = executorService;
	}
	
	@Override
	protected void configure() {
		bind(CleanableExecutorService.class).toInstance(executorService);
	}

	@Provides
	@Singleton
	SequentialParallelSimpleMultiObjectiveOptimiser buildMultiObjectiveOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final IMultiObjectiveFitnessEvaluator fitnessEvaluator,
			@Named(LocalSearchOptimiserModule.LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations,
			@Named(MultiObjectiveOptimiserModule.MULTIOBJECTIVE_ADDITIONAL_OBJECTIVE_NAMES) final List<String> objectiveNames, @NonNull final List<IConstraintChecker> constraintCheckers,
			@NonNull final List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers, @NonNull final List<IEvaluationProcess> evaluationProcesses,
			@Named(LocalSearchOptimiserModule.RANDOM_SEED) final long randomSeed) {
		final List<IFitnessComponent> objectives = fitnessEvaluator.getFitnessComponents().stream().filter(f -> objectiveNames.contains(f.getName())).collect(Collectors.toList());
		assert (objectives.size() == objectiveNames.size());

		final SequentialParallelSimpleMultiObjectiveOptimiser lso = new SequentialParallelSimpleMultiObjectiveOptimiser(objectives, new Random(randomSeed));
		setLSO(injector, context, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso, randomSeed);
		lso.setMultiObjectiveFitnessEvaluator(fitnessEvaluator);
		return lso;
	}
	
	private void setLSO(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final IFitnessEvaluator fitnessEvaluator, final int numberOfIterations,
			@NonNull final List<@NonNull IConstraintChecker> constraintCheckers, @NonNull final List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers,
			@NonNull final List<@NonNull IEvaluationProcess> evaluationProcesses, @NonNull final LocalSearchOptimiser lso, final long seed) {
		injector.injectMembers(lso);
		lso.setNumberOfIterations(numberOfIterations);

		lso.setSequenceManipulator(manipulator);

		lso.setMoveGenerator(moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);
		lso.setEvaluatedStateConstraintCheckers(evaluatedStateConstraintCheckers);
		lso.setEvaluationProcesses(evaluationProcesses);

		lso.setReportInterval(Math.max(10, numberOfIterations / 100));

		lso.setRandom(new Random(seed));
	}

	public static List<IFitnessComponent> getMultiObjectiveFitnessComponents(final IMultiObjectiveFitnessEvaluator fitnessEvaluator, final List<String> objectiveNames) {
		final List<IFitnessComponent> objectives = fitnessEvaluator.getFitnessComponents().stream().filter(f -> objectiveNames.contains(f.getName())).collect(Collectors.toList());
		assert (objectives.size() == objectiveNames.size());
		return objectives;
	}

}
