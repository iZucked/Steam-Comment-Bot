/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.modules;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
import com.mmxlabs.optimiser.core.fitness.IMultiObjectiveFitnessEvaluator;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.SimilarityFitnessMode;
import com.mmxlabs.optimiser.lso.impl.ArbitraryStateLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.RestartingLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.SimpleMultiObjectiveOptimiser;
import com.mmxlabs.optimiser.lso.impl.thresholders.GreedyThresholder;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.ProcessorAgnosticParallelLSO;

/**
 * A {@link Guice} module to provide a Local Search optimiser
 * 
 * @author Simon Goodall, Tom Hinton
 * @since 2.0
 */
public class LocalSearchOptimiserModule extends AbstractModule {

	public static final String CLEAN_STATE_NUMBER_OF_ITERATIONS = "CleanState-NumberOfIterations";
	public static final String LSO_NUMBER_OF_ITERATIONS = "LSO-NumberOfIterations";
	public static final String SOLUTION_IMPROVER_NUMBER_OF_ITERATIONS = "SOLUTION_IMPROVER-NumberOfIterations";
	public static final String USE_RESTARTING_OPTIMISER = "useRestartingOptimiser";
	public static final String RANDOM_SEED = "RandomSeed";
	public static final String MULTIOBJECTIVE_OBJECTIVE_NAMES = "MULTIOBJECTIVE_OBJECTIVE_NAMES";
	public static final String SIMILARITY_SETTING = "LSO_MODULE_SIMILARITY_SETTING";
	public static final String NEW_SIMILARITY_OPTIMISER = "LSO_MODULE_SIMILARITY_OPTIMISER";
	public static final String MULTIOBJECTIVE_OBJECTIVE_FITNESS_COMPONENTS = "MULTIOBJECTIVE_FITNESS_COMPONENTS";
	public static final String OPTIMISER_DEBUG_MODE = "OPTIMISER_DEBUG_MODE";

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	LocalSearchOptimiser buildDefaultOptimiser(@NonNull final Injector injector, @Named(USE_RESTARTING_OPTIMISER) final boolean isRestarting,
			@Named(SIMILARITY_SETTING) final SimilarityFitnessMode similarityFitnessMode, @Named(NEW_SIMILARITY_OPTIMISER) final boolean isUsingNewSimilarityOptimiser) {
		if (isUsingNewSimilarityOptimiser) {
			if (similarityFitnessMode == SimilarityFitnessMode.OFF) {
				return injector.getInstance(DefaultLocalSearchOptimiser.class);
			} else {
				return injector.getInstance(SimpleMultiObjectiveOptimiser.class);
			}
		} else {
			if (isRestarting) {
				return injector.getInstance(RestartingLocalSearchOptimiser.class);
			} else {
				return injector.getInstance(DefaultLocalSearchOptimiser.class);
			}

		}
	}

	@Provides
	@Singleton
	DefaultLocalSearchOptimiser buildDefaultOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final IFitnessEvaluator fitnessEvaluator, @Named(LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations, 	@Named(LocalSearchOptimiserModule.RANDOM_SEED) final long randomSeed,
			@NonNull final List<@NonNull IConstraintChecker> constraintCheckers, @NonNull final List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers,
			@NonNull final List<@NonNull IEvaluationProcess> evaluationProcesses) {

		final DefaultLocalSearchOptimiser lso = new DefaultLocalSearchOptimiser();
		setLSO(injector, context, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso, randomSeed);

		return lso;
	}

	@Provides
	@Singleton
	RestartingLocalSearchOptimiser buildRestartingOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final IFitnessEvaluator fitnessEvaluator, @Named(LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations,
			@Named(LocalSearchOptimiserModule.RANDOM_SEED) final long randomSeed, @NonNull final List<@NonNull IConstraintChecker> constraintCheckers,
			@NonNull final List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers, @NonNull final List<@NonNull IEvaluationProcess> evaluationProcesses) {

		final RestartingLocalSearchOptimiser lso = new RestartingLocalSearchOptimiser();
		setLSO(injector, context, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso, randomSeed);

		return lso;
	}

	@Provides
	@Singleton
	SimpleMultiObjectiveOptimiser buildMultiObjectiveOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final IMultiObjectiveFitnessEvaluator fitnessEvaluator, @Named(LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations,
			@Named(MULTIOBJECTIVE_OBJECTIVE_NAMES) final List<String> objectiveNames, @NonNull final List<IConstraintChecker> constraintCheckers,
			@NonNull final List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers, @NonNull final List<IEvaluationProcess> evaluationProcesses,
			@Named(LocalSearchOptimiserModule.RANDOM_SEED) final long randomSeed) {
		final List<IFitnessComponent> objectives = fitnessEvaluator.getFitnessComponents().stream().filter(f -> objectiveNames.contains(f.getName())).collect(Collectors.toList());
		assert (objectives.size() == objectiveNames.size());
		final SimpleMultiObjectiveOptimiser lso = new SimpleMultiObjectiveOptimiser(objectives, new Random(randomSeed));

		setLSO(injector, context, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso, randomSeed);
		lso.setMultiObjectiveFitnessEvaluator(fitnessEvaluator);
		return lso;
	}

	@Provides
	@Singleton
	ArbitraryStateLocalSearchOptimiser buildSolutionImprovingOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @Named(SOLUTION_IMPROVER_NUMBER_OF_ITERATIONS) final int numberOfIterations,
			@Named(LocalSearchOptimiserModule.RANDOM_SEED) final long randomSeed, @NonNull final List<@NonNull IConstraintChecker> constraintCheckers,
			@NonNull final List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers, @NonNull final List<@NonNull IEvaluationProcess> evaluationProcesses,
			@NonNull final List<@NonNull IFitnessComponent> fitnessComponents) {
	
		final ArbitraryStateLocalSearchOptimiser lso = new ArbitraryStateLocalSearchOptimiser();
	
		// TODO: Put in the LinearFitnessEvaluatorModule as named provider
		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator(new GreedyThresholder(), fitnessComponents, evaluationProcesses);
		injector.injectMembers(fitnessEvaluator);
	
		setLSO(injector, context, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso, randomSeed);
	
		return lso;
	}

	@Provides
	@Singleton
	ProcessorAgnosticParallelLSO buildParallelLSOOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final IFitnessEvaluator fitnessEvaluator, @Named(LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations,
			@NonNull final List<IConstraintChecker> constraintCheckers, @NonNull final List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers,
			@NonNull final List<IEvaluationProcess> evaluationProcesses, @Named(LocalSearchOptimiserModule.RANDOM_SEED) final long seed) {

		final ProcessorAgnosticParallelLSO lso = new ProcessorAgnosticParallelLSO();
		setLSO(injector, context, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso, seed);

		return lso;
	}

	private void setLSO(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator, @NonNull final IMoveGenerator moveGenerator,
			@NonNull final IFitnessEvaluator fitnessEvaluator, final int numberOfIterations, @NonNull final List<@NonNull IConstraintChecker> constraintCheckers,
			@NonNull final List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers, @NonNull final List<@NonNull IEvaluationProcess> evaluationProcesses,
			@NonNull final LocalSearchOptimiser lso, final long seed) {
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

}