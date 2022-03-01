/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.constraints.impl.EvaluatedStateConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessInstantiator;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.core.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.SingleThreadLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.thresholders.StepThresholder;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move2over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move3over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over1GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.MoveSnakeGeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.RandomMoveGenerator;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;

/**
 * Utility class to help build up test components.
 * 
 * @author Simon Goodall
 * 
 */
public final class GeneralTestUtils {

	public static IMoveGenerator createRandomMoveGenerator() {
		final RandomMoveGenerator moveGenerator = new RandomMoveGenerator();

		// Register RNG move generator units
		moveGenerator.addMoveGeneratorUnit(new Move3over2GeneratorUnit());
		moveGenerator.addMoveGeneratorUnit(new Move4over1GeneratorUnit());
		moveGenerator.addMoveGeneratorUnit(new Move4over2GeneratorUnit());
		moveGenerator.addMoveGeneratorUnit(new Move2over2GeneratorUnit());
		moveGenerator.addMoveGeneratorUnit(new MoveSnakeGeneratorUnit());
		// moveGenerator.addMoveGeneratorUnit(new MoveHydraGeneratorUnit());

		return moveGenerator;
	}

	public static LinearSimulatedAnnealingFitnessEvaluator createLinearSAFitnessEvaluator(Injector parent, final int stepSize, final int numIterations, final List<IFitnessComponent> fitnessComponents,
			@NonNull final List<IEvaluationProcess> evaluationProcesses, final IOptimisationData optimisationData, IPhaseOptimisationData phaseOptimisationData) {

		final Map<String, Double> weightsMap = new HashMap<>();
		for (final IFitnessComponent component : fitnessComponents) {
			weightsMap.put(component.getName(), 1.0);
		}

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);

		// Thresholder params
		final int initialThreshold = stepSize * numIterations;

		final StepThresholder thresholder = new StepThresholder();
		thresholder.setStepSize(stepSize);
		thresholder.setInitialThreshold(initialThreshold);

		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = createFitnessEvaluatorInstance(parent, combiner, thresholder, fitnessComponents, evaluationProcesses);

		return fitnessEvaluator;
	}

	public static LocalSearchOptimiser buildOptimiser(Injector parent, @NonNull final IOptimisationContext context, final IOptimisationData data, final IPhaseOptimisationData phaseOptimisationData,
			@NonNull final Random random, final int numberOfIterations, final int stepSize, final IOptimiserProgressMonitor monitor) {

		final EvaluationProcessInstantiator evaluationProcessInstantiator = new EvaluationProcessInstantiator();
		final List<IEvaluationProcess> evaluationProcesses = evaluationProcessInstantiator.instantiateEvaluationProcesses(context.getEvaluationProcessRegistry(), context.getEvaluationProcesses(),
				phaseOptimisationData);

		final ConstraintCheckerInstantiator constraintCheckerInstantiator = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker> constraintCheckers = constraintCheckerInstantiator.instantiateConstraintCheckers(context.getConstraintCheckerRegistry(), context.getConstraintCheckers(),
				phaseOptimisationData);

		final EvaluatedStateConstraintCheckerInstantiator evaluatedStateConstraintCheckerInstantiator = new EvaluatedStateConstraintCheckerInstantiator();
		final List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers = evaluatedStateConstraintCheckerInstantiator
				.instantiateConstraintCheckers(context.getEvaluatedStateConstraintCheckerRegistry(), context.getEvaluatedStateConstraintCheckers());

		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
		final List<IFitnessComponent> fitnessComponents = fitnessComponentInstantiator.instantiateFitnesses(context.getFitnessFunctionRegistry(), context.getFitnessComponents());

		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = GeneralTestUtils.createLinearSAFitnessEvaluator(parent, stepSize, numberOfIterations, fitnessComponents, evaluationProcesses,
				data, phaseOptimisationData);
		final IMoveGenerator moveGenerator = GeneralTestUtils.createRandomMoveGenerator();

		final SingleThreadLocalSearchOptimiser lso = new SingleThreadLocalSearchOptimiser();

		Injector c = parent.createChildInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ILookupManager.class).to(LookupManager.class);
				bind(ISequencesManipulator.class).to(NullSequencesManipulator.class);
				bind(boolean.class).annotatedWith(Names.named(LocalSearchOptimiserModule.USE_RESTARTING_OPTIMISER)).toInstance(Boolean.FALSE);
				bind(int.class).annotatedWith(Names.named(SingleThreadLocalSearchOptimiser.RESTART_ITERATIONS_THRESHOLD)).toInstance(-1);
				bind(IFitnessCombiner.class).toInstance(fitnessEvaluator.getFitnessCombiner());
			}

		});

		c.injectMembers(lso);

//		lso.setLookupManager(new LookupManager());

		lso.setNumberOfIterations(numberOfIterations);
		lso.setSequenceManipulator(new NullSequencesManipulator());
		lso.setMoveGenerator(moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);
		lso.setEvaluatedStateConstraintCheckers(evaluatedStateConstraintCheckers);
		lso.setEvaluationProcesses(evaluationProcesses);

		lso.setProgressMonitor(monitor);

		lso.setReportInterval(Math.max(10, numberOfIterations / 100));

		lso.init();

		lso.setRandom(random);

		return lso;
	}

	private static LinearSimulatedAnnealingFitnessEvaluator createFitnessEvaluatorInstance(Injector parent, @NonNull final IFitnessCombiner fitnessCombiner, @NonNull final IThresholder thresholder,
			@NonNull final List<IFitnessComponent> fitnessComponents, @NonNull final List<IEvaluationProcess> evaluationProcesses) {
		return parent.createChildInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IFitnessCombiner.class).toInstance(fitnessCombiner);
				bind(IThresholder.class).toInstance(thresholder);
			}

			@Provides
			LinearSimulatedAnnealingFitnessEvaluator providerFitnessEvaluator(@NonNull final Injector injector) {
				final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator(thresholder, fitnessComponents, evaluationProcesses);
				injector.injectMembers(evaluator);
				return evaluator;
			}

		}).getInstance(LinearSimulatedAnnealingFitnessEvaluator.class);
	}

	public static int[] makeBaseFuelPrices(final int price) {
		final int[] m = new int[10];
		Arrays.fill(m, price);
		// Mockito.when(m.get(ArgumentMatchers.any(in[].class))).thenReturn(price);
		return m;
	}
}
