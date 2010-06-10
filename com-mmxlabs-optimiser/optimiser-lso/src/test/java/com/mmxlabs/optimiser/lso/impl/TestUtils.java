package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.jws.Oneway;

import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.constraints.impl.ConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.fitness.impl.SortingFitnessFactory;
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
public final class TestUtils {

	public static <T> IMoveGenerator<T> createRandomMoveGenerator(
			final Random random) {
		final RandomMoveGenerator<T> moveGenerator = new RandomMoveGenerator<T>();
		moveGenerator.setRandom(random);

		// Register RNG move generator units
		moveGenerator.addMoveGeneratorUnit(new Move3over2GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new Move4over1GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new Move4over2GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new MoveSnakeGeneratorUnit<T>());
		// moveGenerator.addMoveGeneratorUnit(new MoveHydraGeneratorUnit<T>());

		return moveGenerator;
	}

	public static IFitnessFunctionRegistry createFitnessRegistry() {
		final FitnessFunctionRegistry registry = new FitnessFunctionRegistry();

		registry.registerFitnessCoreFactory(new SortingFitnessFactory());

		return registry;
	}

	public static IConstraintCheckerRegistry createConstraintCheckerRegistry() {

		final ConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();

		return registry;
	}

	public static <T> LinearSimulatedAnnealingFitnessEvaluator<T> createLinearSAFitnessEvaluator(
			final int stepSize, final int numIterations,
			final List<IFitnessComponent<T>> fitnessComponents) {
		final FitnessHelper<T> fitnessHelper = new FitnessHelper<T>();

		final LinearSimulatedAnnealingFitnessEvaluator<T> fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator<T>();

		fitnessEvaluator.setFitnessComponents(fitnessComponents);
		fitnessEvaluator.setFitnessHelper(fitnessHelper);

		final Map<String, Double> weightsMap = new HashMap<String, Double>();
		for (final IFitnessComponent<T> component : fitnessComponents) {
			weightsMap.put(component.getName(), 1.0);
		}

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);

		fitnessEvaluator.setFitnessCombiner(combiner);

		// Thresholder params
		final int initialThreshold = stepSize * numIterations;

		final StepThresholder thresholder = new StepThresholder();
		thresholder.setStepSize(stepSize);
		thresholder.setInitialThreshold(initialThreshold);

		fitnessEvaluator.setThresholder(thresholder);

		fitnessEvaluator.init();

		return fitnessEvaluator;
	}

	public static <T> LocalSearchOptimiser<T> buildOptimiser(
			final IOptimisationContext<T> context, final Random random,
			final int numberOfIterations, final int stepSize) {
		
		final ConstraintCheckerInstantiator constraintCheckerInstantiator = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker<T>> constraintCheckers = constraintCheckerInstantiator
				.instantiateConstraintCheckers(context.getConstraintCheckerRegistry(),
						context.getConstraintCheckers());

		
		for (final IConstraintChecker<T> checker : constraintCheckers) {
			checker.setOptimisationData(context.getOptimisationData());
		}
		
		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
		final List<IFitnessComponent<T>> fitnessComponents = fitnessComponentInstantiator
				.instantiateFitnesses(context.getFitnessFunctionRegistry(), context.getFitnessComponents());

		final LinearSimulatedAnnealingFitnessEvaluator<T> fitnessEvaluator = TestUtils
				.createLinearSAFitnessEvaluator(stepSize, numberOfIterations,
						fitnessComponents);
		final IMoveGenerator<T> moveGenerator = TestUtils
				.createRandomMoveGenerator(random);

		final DefaultLocalSearchOptimiser<T> lso = new DefaultLocalSearchOptimiser<T>();

		lso.setNumberOfIterations(numberOfIterations);
		lso.setSequenceManipulator(new NullSequencesManipulator<T>());
		lso.setMoveGenerator(moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);

		lso.init();
		
		return lso;
	}

}
