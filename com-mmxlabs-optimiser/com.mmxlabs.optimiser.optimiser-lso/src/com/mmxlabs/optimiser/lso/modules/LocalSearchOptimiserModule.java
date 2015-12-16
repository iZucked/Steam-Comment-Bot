/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.ArbitraryStateLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.RestartingLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.thresholders.GreedyThresholder;
import com.mmxlabs.optimiser.lso.movegenerators.impl.CompoundMoveGenerator;
import com.mmxlabs.optimiser.lso.movegenerators.impl.InstrumentingMoveGenerator;

/**
 * A {@link Guice} module to provide a Local Search optimiser
 * 
 * @author Simon Goodall, Tom Hinton
 * @since 2.0
 */
public class LocalSearchOptimiserModule extends AbstractModule {

	public static final boolean instrumenting = true;

	public static final String LSO_NUMBER_OF_ITERATIONS = "LSO-NumberOfIterations";
	public static final String SOLUTION_IMPROVER_NUMBER_OF_ITERATIONS = "SOLUTION_IMPROVER-NumberOfIterations";
	public static final String USE_RESTARTING_OPTIMISER = "useRestartingOptimiser";
	public static final String RANDOM_SEED = "RandomSeed";

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	LocalSearchOptimiser buildDefaultOptimiser(@NonNull final Injector injector, @Named(USE_RESTARTING_OPTIMISER) final boolean isRestarting) {
		if (isRestarting) {
			return injector.getInstance(RestartingLocalSearchOptimiser.class);
		} else {
			return injector.getInstance(DefaultLocalSearchOptimiser.class);
		}
	}
	
	@Provides
	@Singleton
	DefaultLocalSearchOptimiser buildDefaultOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final InstrumentingMoveGenerator instrumentingMoveGenerator, @NonNull final IFitnessEvaluator fitnessEvaluator,
			@Named(RANDOM_SEED) final long seed, @Named(LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations, @NonNull final List<IConstraintChecker> constraintCheckers,
			@NonNull final List<IEvaluationProcess> evaluationProcesses) {

		final DefaultLocalSearchOptimiser lso = new DefaultLocalSearchOptimiser();
		setLSO(injector, context, manipulator, moveGenerator, instrumentingMoveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluationProcesses, lso);

		return lso;
	}
	
	@Provides
	@Singleton
	RestartingLocalSearchOptimiser buildRestartingOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final InstrumentingMoveGenerator instrumentingMoveGenerator, @NonNull final IFitnessEvaluator fitnessEvaluator,
			@Named(RANDOM_SEED) final long seed, @Named(LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations, @NonNull final List<IConstraintChecker> constraintCheckers,
			@NonNull final List<IEvaluationProcess> evaluationProcesses) {
		
		final RestartingLocalSearchOptimiser lso = new RestartingLocalSearchOptimiser();
		setLSO(injector, context, manipulator, moveGenerator, instrumentingMoveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluationProcesses, lso);
		
		return lso;
	}

	private void setLSO(final Injector injector, final IOptimisationContext context, final ISequencesManipulator manipulator, final IMoveGenerator moveGenerator,
			final InstrumentingMoveGenerator instrumentingMoveGenerator, final IFitnessEvaluator fitnessEvaluator, final int numberOfIterations, final List<IConstraintChecker> constraintCheckers,
			final List<IEvaluationProcess> evaluationProcesses, final LocalSearchOptimiser lso) {
		injector.injectMembers(lso);
		lso.setNumberOfIterations(numberOfIterations);

		lso.setSequenceManipulator(manipulator);

		lso.setMoveGenerator(instrumenting ? instrumentingMoveGenerator : moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);
		lso.setEvaluationProcesses(evaluationProcesses);

		lso.setReportInterval(Math.max(10, numberOfIterations / 100));
	}
	
	@Provides
	@Singleton
	ArbitraryStateLocalSearchOptimiser buildSolutionImprovingOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
			@NonNull final IMoveGenerator moveGenerator, @NonNull final InstrumentingMoveGenerator instrumentingMoveGenerator,
			@Named(RANDOM_SEED) final long seed, @Named(SOLUTION_IMPROVER_NUMBER_OF_ITERATIONS) final int numberOfIterations, @NonNull final List<IConstraintChecker> constraintCheckers,
			@NonNull final List<IEvaluationProcess> evaluationProcesses, @NonNull List<IFitnessComponent> fitnessComponents) {

		final ArbitraryStateLocalSearchOptimiser lso = new ArbitraryStateLocalSearchOptimiser();
		
		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator();
		injector.injectMembers(fitnessEvaluator);
		fitnessEvaluator.setThresholder(new GreedyThresholder());
		fitnessEvaluator.setFitnessComponents(fitnessComponents);
		fitnessEvaluator.setEvaluationProcesses(evaluationProcesses);
		fitnessEvaluator.init();

		setLSO(injector, context, manipulator, moveGenerator, instrumentingMoveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluationProcesses, lso);

		return lso;
	}

}