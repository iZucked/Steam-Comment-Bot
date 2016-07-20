/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.constraints.impl.EvaluatedStateConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessInstantiator;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.core.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.thresholders.StepThresholder;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move2over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move3over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over1GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.MoveSnakeGeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.RandomMoveGenerator;

/**
 * Utility class to help build up test components.
 * 
 * @author Simon Goodall
 * 
 */
public final class GeneralTestUtils {

	public static IMoveGenerator createRandomMoveGenerator(@NonNull final Random random) {
		final RandomMoveGenerator moveGenerator = new RandomMoveGenerator();
		moveGenerator.setRandom(random);

		// Register RNG move generator units
		moveGenerator.addMoveGeneratorUnit(new Move3over2GeneratorUnit());
		moveGenerator.addMoveGeneratorUnit(new Move4over1GeneratorUnit());
		moveGenerator.addMoveGeneratorUnit(new Move4over2GeneratorUnit());
		moveGenerator.addMoveGeneratorUnit(new Move2over2GeneratorUnit());
		moveGenerator.addMoveGeneratorUnit(new MoveSnakeGeneratorUnit());
		// moveGenerator.addMoveGeneratorUnit(new MoveHydraGeneratorUnit());

		return moveGenerator;
	}

	public static LinearSimulatedAnnealingFitnessEvaluator createLinearSAFitnessEvaluator(final int stepSize, final int numIterations, final List<IFitnessComponent> fitnessComponents,
			@NonNull final List<IEvaluationProcess> evaluationProcesses, final IOptimisationData optimisationData) {
		final Map<String, Double> weightsMap = new HashMap<String, Double>();
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

		final FitnessHelper fitnessHelper = new FitnessHelper();
		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = createFitnessEvaluatorInstance(combiner, thresholder, fitnessHelper, fitnessComponents, evaluationProcesses,
				optimisationData);

		return fitnessEvaluator;
	}

	public static LocalSearchOptimiser buildOptimiser(@NonNull final IOptimisationContext context, IOptimisationData data, @NonNull final Random random, final int numberOfIterations,
			final int stepSize, final IOptimiserProgressMonitor monitor) {

		final EvaluationProcessInstantiator evaluationProcessInstantiator = new EvaluationProcessInstantiator();
		final List<IEvaluationProcess> evaluationProcesses = evaluationProcessInstantiator.instantiateEvaluationProcesses(context.getEvaluationProcessRegistry(), context.getEvaluationProcesses(),
				data);

		final ConstraintCheckerInstantiator constraintCheckerInstantiator = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker> constraintCheckers = constraintCheckerInstantiator.instantiateConstraintCheckers(context.getConstraintCheckerRegistry(), context.getConstraintCheckers(), data);

		final EvaluatedStateConstraintCheckerInstantiator evaluatedStateConstraintCheckerInstantiator = new EvaluatedStateConstraintCheckerInstantiator();
		final List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers = evaluatedStateConstraintCheckerInstantiator
				.instantiateConstraintCheckers(context.getEvaluatedStateConstraintCheckerRegistry(), context.getEvaluatedStateConstraintCheckers());

		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
		final List<IFitnessComponent> fitnessComponents = fitnessComponentInstantiator.instantiateFitnesses(context.getFitnessFunctionRegistry(), context.getFitnessComponents());

		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = GeneralTestUtils.createLinearSAFitnessEvaluator(stepSize, numberOfIterations, fitnessComponents, evaluationProcesses, data);
		final IMoveGenerator moveGenerator = GeneralTestUtils.createRandomMoveGenerator(random);

		final DefaultLocalSearchOptimiser lso = new DefaultLocalSearchOptimiser();

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

		return lso;
	}

	private static LinearSimulatedAnnealingFitnessEvaluator createFitnessEvaluatorInstance(@NonNull final IFitnessCombiner fitnessCombiner, @NonNull final IThresholder thresholder,
			@NonNull final IFitnessHelper fitnessHelper, @NonNull final List<IFitnessComponent> fitnessComponents, @NonNull final List<IEvaluationProcess> evaluationProcesses,
			final IOptimisationData optimisationData) {
		return Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IFitnessCombiner.class).toInstance(fitnessCombiner);
				bind(IThresholder.class).toInstance(thresholder);
				bind(IFitnessHelper.class).toInstance(fitnessHelper);
				bind(IOptimisationData.class).toInstance(optimisationData);
			}

			@Provides
			LinearSimulatedAnnealingFitnessEvaluator providerFitnessEvaluator(@NonNull final Injector injector) {
				final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator(thresholder, fitnessComponents, evaluationProcesses);
				injector.injectMembers(evaluator);
				return evaluator;
			}

		}).getInstance(LinearSimulatedAnnealingFitnessEvaluator.class);
	}

}
