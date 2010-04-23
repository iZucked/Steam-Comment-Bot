package com.mmxlabs.optimiser.lso.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.constraints.impl.ConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.impl.OptimisationContext;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.scenario.impl.OptimisationData;

public class DefaultLocalSearchOptimiserTest {

	@Test
	public void testOptimise() {

		// Initialise random number generator
		final Random random = new Random(1);

		// Optimisation params
		final int numberOfIterations = 100;
		final double temperature = 5.0;

		final IConstraintCheckerRegistry checkerRegistry = TestUtils
				.createConstraintCheckerRegistry();
		final IFitnessFunctionRegistry fitnessRegistry = TestUtils
				.createFitnessRegistry();

		final List<String> constraintCheckerNames = new ArrayList<String>(
				checkerRegistry.getConstraintCheckerNames());
		final ConstraintCheckerInstantiator constraintCheckerInstantiator = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker<Integer>> constraintCheckers = constraintCheckerInstantiator
				.instantiateConstraintCheckers(checkerRegistry,
						constraintCheckerNames);

		final List<String> fitnessComponentNames = new ArrayList<String>(
				fitnessRegistry.getFitnessComponentNames());
		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
		final List<IFitnessComponent<Integer>> fitnessComponents = fitnessComponentInstantiator
				.instantiateFitnesses(fitnessRegistry, fitnessComponentNames);

		final LinearSimulatedAnnealingFitnessEvaluator<Integer> fitnessEvaluator = TestUtils
				.createLinearSAFitnessEvaluator(temperature,
						numberOfIterations, fitnessComponents);
		final IMoveGenerator<Integer> moveGenerator = TestUtils
				.createRandomMoveGenerator(random);

		final DefaultLocalSearchOptimiser<Integer> lso = new DefaultLocalSearchOptimiser<Integer>();

		lso.setNumberOfIterations(numberOfIterations);
		lso.setSequenceManipulator(new NullSequencesManipulator<Integer>());
		lso.setMoveGenerator(moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);

		// Create an initial set of sequences.

		final IResource r1 = OptimiserTestUtil.makeResource();
		final IResource r2 = OptimiserTestUtil.makeResource();

		final Map<IResource, IModifiableSequence<Integer>> map = CollectionsUtil
				.makeHashMap(r1, OptimiserTestUtil.makeSequence(1, 3, 2, 4),
						r2, OptimiserTestUtil.makeSequence(5, 8, 7, 6));

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				CollectionsUtil.makeArrayList(r1, r2), map);

		final OptimisationData<Integer> data = new OptimisationData<Integer>();

		final OptimisationContext<Integer> context = new OptimisationContext<Integer>(
				data, sequences, fitnessComponentNames, fitnessRegistry,
				constraintCheckerNames, checkerRegistry);

		// Perform the optimisation
		lso.optimise(context);

		System.out
				.println("Final fitness " + fitnessEvaluator.getBestFitness());
	}

	@Test(expected = IllegalStateException.class)
	public void testOptimiseInit1() {

		// Test all init failure combinations in several methods.
		// Code coverage utils should help here

		fail("Not yet implemented");
	}

}
