/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.modules;

import java.util.List;
import java.util.Random;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LSOMover;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.ProcessorAgnosticParallelLSO;
import com.mmxlabs.optimiser.lso.impl.SingleThreadLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.thresholders.GreedyThresholder;

/**
 * A {@link Guice} module to provide a Local Search optimiser
 * 
 * @author Simon Goodall, Tom Hinton
 * @since 2.0
 */
public class LocalSearchOptimiserModule extends AbstractModule {

	public static final String GREEDY_THRESHOLDER = "greedy-thresholder";

	public static final String CLEAN_STATE_NUMBER_OF_ITERATIONS = "CleanState-NumberOfIterations";
	public static final String LSO_NUMBER_OF_ITERATIONS = "LSO-NumberOfIterations";
	public static final String SOLUTION_IMPROVER_NUMBER_OF_ITERATIONS = "SOLUTION_IMPROVER-NumberOfIterations";
	public static final String USE_RESTARTING_OPTIMISER = "useRestartingOptimiser";
	public static final String RANDOM_SEED = "RandomSeed";
	public static final String SIMILARITY_SETTING = "LSO_MODULE_SIMILARITY_SETTING";
	public static final String OPTIMISER_DEBUG_MODE = "OPTIMISER_DEBUG_MODE";

	@Override
	protected void configure() {
		bind(LSOMover.class).in(ThreadLocalScope.class);
	}

	@Provides
	@Singleton
	private SingleThreadLocalSearchOptimiser buildDefaultOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final IFitnessEvaluator fitnessEvaluator, @Named(LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations,
			@Named(LocalSearchOptimiserModule.RANDOM_SEED) final long randomSeed, @NonNull final List<@NonNull IConstraintChecker> constraintCheckers,
			@NonNull final List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers, @NonNull final List<@NonNull IEvaluationProcess> evaluationProcesses) {

		final SingleThreadLocalSearchOptimiser lso = new SingleThreadLocalSearchOptimiser();
		setLSO(injector, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso);
		lso.setRandom(new Random(randomSeed));

		return lso;
	}

	@Provides
	@Singleton
	private ProcessorAgnosticParallelLSO buildParallelLSOOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final IFitnessEvaluator fitnessEvaluator, @Named(LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations,
			@NonNull final List<IConstraintChecker> constraintCheckers, @NonNull final List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers,
			@NonNull final List<IEvaluationProcess> evaluationProcesses) {

		final ProcessorAgnosticParallelLSO lso = new ProcessorAgnosticParallelLSO();
		setLSO(injector, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso);

		return lso;
	}

	@Provides
	@Singleton
	@Named(GREEDY_THRESHOLDER)
	private ProcessorAgnosticParallelLSO buildParallelGreedyLSOOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context,
			@NonNull final ISequencesManipulator manipulator, final List<@NonNull IFitnessComponent> fitnessComponents, @NonNull final IMoveGenerator moveGenerator,
			@Named(LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations, @NonNull final List<IConstraintChecker> constraintCheckers,
			@NonNull final List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers, @NonNull final List<IEvaluationProcess> evaluationProcesses) {

		final ProcessorAgnosticParallelLSO lso = new ProcessorAgnosticParallelLSO();
		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator(new GreedyThresholder(), fitnessComponents, evaluationProcesses);
		injector.injectMembers(fitnessEvaluator);

		setLSO(injector, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso);

		return lso;
	}

	private void setLSO(@NonNull final Injector injector, @NonNull final ISequencesManipulator manipulator, @NonNull final IMoveGenerator moveGenerator,
			@NonNull final IFitnessEvaluator fitnessEvaluator, final int numberOfIterations, @NonNull final List<@NonNull IConstraintChecker> constraintCheckers,
			@NonNull final List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers, @NonNull final List<@NonNull IEvaluationProcess> evaluationProcesses,
			@NonNull final LocalSearchOptimiser lso) {
		injector.injectMembers(lso);
		lso.setNumberOfIterations(numberOfIterations);

		lso.setSequenceManipulator(manipulator);

		lso.setMoveGenerator(moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);
		lso.setEvaluatedStateConstraintCheckers(evaluatedStateConstraintCheckers);
		lso.setEvaluationProcesses(evaluationProcesses);

		lso.setReportInterval(Math.max(10, numberOfIterations / 100));

	}

}