package com.mmxlabs.optimiser.lso.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IConstraintChecker;
import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.lso.fitness.impl.SortingFitnessFactory;

public class DefaultLocalSearchOptimiserTest {

	@Test
	public void testOptimise() {

		// Initialise random number generator
		final Random random = new Random(1);

		// Optimisation params
		final int numberOfIterations = 100;
		final double temperature = 5.0;

		final SortingFitnessFactory factory = new SortingFitnessFactory();
		final IFitnessCore<Integer> core = factory.instantiate();

		final List<IFitnessComponent<Integer>> fitnessComponents = new ArrayList<IFitnessComponent<Integer>>(
				1);
		fitnessComponents.addAll(core.getFitnessComponents());

		final FitnessHelper<Integer> fitnessHelper = new FitnessHelper<Integer>();
		final LinearSimulatedAnnealingFitnessEvaluator<Integer> fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator<Integer>();
		fitnessEvaluator.setFitnessHelper(fitnessHelper);

		fitnessEvaluator.setTemperature(temperature);
		fitnessEvaluator.setNumberOfIterations(numberOfIterations);
		fitnessEvaluator.setFitnessComponents(fitnessComponents);

		final Map<String, Double> weightsMap = CollectionsUtil.makeHashMap(
				factory.getFitnessComponentNames().iterator().next(), 1.0);

		fitnessEvaluator.setFitnessComponentWeights(weightsMap);

		fitnessEvaluator.init();

		final RandomMoveGenerator<Integer> moveGenerator = new RandomMoveGenerator<Integer>();
		moveGenerator.setRandom(random);

		final DefaultLocalSearchOptimiser<Integer> lso = new DefaultLocalSearchOptimiser<Integer>();

		final List<IConstraintChecker<Integer>> constraintCheckers = Collections
				.emptyList();
		lso.setConstraintCheckers(constraintCheckers);
		lso.setNumberOfIterations(numberOfIterations);
		lso.setSequenceManipulator(new NullSequencesManipulator<Integer>());
		lso.setMoveGenerator(moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);

		// Create an initial set of sequences.

		final IResource r1 = OptimiserTestUtil.makeResource();
		final IResource r2 = OptimiserTestUtil.makeResource();

		final Map<IResource, IModifiableSequence<Integer>> map = CollectionsUtil
				.makeHashMap(r1, OptimiserTestUtil.makeSequence(1, 3, 2, 4),
						r2, OptimiserTestUtil.makeSequence(5, 8, 7, 6));

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				CollectionsUtil.makeArrayList(r1, r2), map);

		// TODO: Need to think about better API to keep move generator up to
		// date with current sequences.
		moveGenerator.setSequences(sequences);

		// TODO: Create a default opt. context object
		final IOptimisationContext<Integer> context = new IOptimisationContext<Integer>() {

			@Override
			public ISequences<Integer> getInitialSequences() {
				return sequences;
			}

			@Override
			public List<IFitnessComponent<Integer>> getFitnessComponents() {
				return fitnessComponents;
			}
		};

		// Perform the optimisation
		lso.optimise(context, null, null);

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
