package com.mmxlabs.optimiser.lso.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.fitness.impl.FitnessHelper;
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

	public static <T> IMoveGenerator<T> createRandomMoveGenerator(Random random) {
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
		FitnessFunctionRegistry registry = new FitnessFunctionRegistry();

		registry.registerFitnessCoreFactory(new SortingFitnessFactory());

		return registry;
	}

	public static IConstraintCheckerRegistry createConstraintCheckerRegistry() {

		ConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();

		return registry;
	}

	public static <T> LinearSimulatedAnnealingFitnessEvaluator<T> createLinearSAFitnessEvaluator(
			double temperature, int numIterations,
			List<IFitnessComponent<T>> fitnessComponents) {
		FitnessHelper<T> fitnessHelper = new FitnessHelper<T>();

		LinearSimulatedAnnealingFitnessEvaluator<T> fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator<T>();

		fitnessEvaluator.setFitnessComponents(fitnessComponents);
		fitnessEvaluator.setFitnessHelper(fitnessHelper);

		final Map<String, Double> weightsMap = new HashMap<String, Double>();
		for (IFitnessComponent<T> component : fitnessComponents) {
			weightsMap.put(component.getName(), 1.0);
		}

		LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);
		
		fitnessEvaluator.setFitnessCombiner(combiner);

		// Thresholder params
		int stepSize = 1;
		int initialThreshold = stepSize * numIterations;
		
		StepThresholder thresholder = new StepThresholder();
		thresholder.setStepSize(stepSize);
		thresholder.setInitialThreshold(initialThreshold);

		fitnessEvaluator.setThresholder(thresholder);
		
		fitnessEvaluator.init();

		return fitnessEvaluator;
	}

}
