package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.OptimisationContext;
import com.mmxlabs.optimiser.scenario.impl.OptimisationData;

public class DefaultLocalSearchOptimiserTest {

	@Test
	public void testOptimise() {

		// Initialise random number generator
		final Random random = new Random(1);

		// Optimisation params
		final int numberOfIterations = 100;

		final IConstraintCheckerRegistry checkerRegistry = TestUtils
				.createConstraintCheckerRegistry();
		final IFitnessFunctionRegistry fitnessRegistry = TestUtils
				.createFitnessRegistry();

		final List<String> constraintCheckerNames = new ArrayList<String>(
				checkerRegistry.getConstraintCheckerNames());
//		final ConstraintCheckerInstantiator constraintCheckerInstantiator = new ConstraintCheckerInstantiator();
//		final List<IConstraintChecker<Integer>> constraintCheckers = constraintCheckerInstantiator
//				.instantiateConstraintCheckers(checkerRegistry,
//						constraintCheckerNames);
//
		final List<String> fitnessComponentNames = new ArrayList<String>(
				fitnessRegistry.getFitnessComponentNames());
//		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
//		final List<IFitnessComponent<Integer>> fitnessComponents = fitnessComponentInstantiator
//				.instantiateFitnesses(fitnessRegistry, fitnessComponentNames);
//
//		final LinearSimulatedAnnealingFitnessEvaluator<Integer> fitnessEvaluator = TestUtils
//				.createLinearSAFitnessEvaluator(1, numberOfIterations,
//						fitnessComponents);
//		final IMoveGenerator<Integer> moveGenerator = TestUtils
//				.createRandomMoveGenerator(random);
//
//		final DefaultLocalSearchOptimiser<Integer> lso = new DefaultLocalSearchOptimiser<Integer>();

//		lso.setNumberOfIterations(numberOfIterations);
//		lso.setSequenceManipulator(new NullSequencesManipulator<Integer>());
//		lso.setMoveGenerator(moveGenerator);
//		lso.setFitnessEvaluator(fitnessEvaluator);
//		lso.setConstraintCheckers(constraintCheckers);

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

		
		final LocalSearchOptimiser<Integer> lso = TestUtils.buildOptimiser(context, random, numberOfIterations, 1);
		
		
		LinearSimulatedAnnealingFitnessEvaluator<Integer> fitnessEvaluator = (LinearSimulatedAnnealingFitnessEvaluator<Integer>)lso.getFitnessEvaluator();
		

		fitnessEvaluator.setOptimisationData(context.getOptimisationData());
		fitnessEvaluator.setInitialSequences(context.getInitialSequences());
	
		long initialFitness = fitnessEvaluator.getBestFitness();
		System.out.println("Initial fitness "
				+ initialFitness);

		// Perform the optimisation
		lso.optimise(context);

		long finalFitness = fitnessEvaluator.getBestFitness();
		System.out
				.println("Final fitness " + finalFitness);
		
		Assert.assertTrue(finalFitness < initialFitness);
	}

}
