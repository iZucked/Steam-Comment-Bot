package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.IOptimiser;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.impl.OptimisationContext;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move3over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over1GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.MoveSnakeGeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.RandomMoveGenerator;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;

/**
 * Test class to run a "system" test of the LSO to optimise a sequence of
 * elements based upon distance. I.e. find minimum travel distance for each
 * resource.
 * 
 * @author Simon Goodall
 * 
 */
public class SimpleSchedulerTest {

	IOptimisationData<ISequenceElement> createProblem() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port1 = builder.createPort("port-1", 0, 0);
		final IPort port2 = builder.createPort("port-2", 0, 5);
		final IPort port3 = builder.createPort("port-3", 5, 0);
		final IPort port4 = builder.createPort("port-4", 5, 5);
		final IPort port5 = builder.createPort("port-5", 0, 10);
		final IPort port6 = builder.createPort("port-6", 5, 10);

		// TODO: Build distance table
		// builder.setPortToPortDistance(from, to, distance)

		final IVessel vessel1 = builder.createVessel("vessel-1");
		final IVessel vessel2 = builder.createVessel("vessel-2");
		final IVessel vessel3 = builder.createVessel("vessel-3");

		final ITimeWindow tw1 = builder.createTimeWindow(5, 6);
		final ITimeWindow tw2 = builder.createTimeWindow(10, 11);
		final ITimeWindow tw3 = builder.createTimeWindow(15, 16);
		final ITimeWindow tw4 = builder.createTimeWindow(20, 21);
		final ITimeWindow tw5 = builder.createTimeWindow(25, 26);
		final ITimeWindow tw6 = builder.createTimeWindow(30, 31);
		
		final ITimeWindow tw7 = builder.createTimeWindow(35, 36);
		final ITimeWindow tw8 = builder.createTimeWindow(40, 41);
		final ITimeWindow tw9 = builder.createTimeWindow(45, 46);
		final ITimeWindow tw10 = builder.createTimeWindow(50, 51);

		builder.createCargo(port1, tw1, port2, tw2);
		builder.createCargo(port1, tw3, port2, tw4);
		builder.createCargo(port1, tw5, port2, tw6);

		builder.createCargo(port1, tw4, port6, tw6);

		
		builder.createCargo(port3, tw5, port4, tw6);
		builder.createCargo(port3, tw7, port4, tw8);
		builder.createCargo(port5, tw9, port6, tw10);

		// ....

		// Generate the optimisation data
		final IOptimisationData<ISequenceElement> data = builder
				.getOptimisationData();

		builder.dispose();

		return data;
	}

	/**
	 * Create a randomly generated {@link ISequences} object using the given
	 * seed
	 * 
	 * @param <T>
	 * @param data
	 * @param seed
	 * @return
	 */
	<T> ISequences<T> createInitialSequences(final IOptimisationData<T> data,
			final long seed) {
		final Random random = new Random(seed);
		final Collection<T> sequenceElements = data.getSequenceElements();
		final List<T> shuffledElements = new ArrayList<T>(sequenceElements);
		Collections.shuffle(shuffledElements, random);

		final List<IResource> resources = data.getResources();
		final IModifiableSequences<T> sequences = new ModifiableSequences<T>(
				resources);
		for (final T e : shuffledElements) {
			final int nextInt = random.nextInt(resources.size());
			sequences.getModifiableSequence(nextInt).add(e);
		}
		return sequences;
	}

	@Test
	public void testLSO() {
		// Build opt data
		final IOptimisationData<ISequenceElement> data = createProblem();
		// Generate initial state
		final ISequences<ISequenceElement> initialSequences = createInitialSequences(
				data, 1);

		final FitnessFunctionRegistry fitnessRegistry = new FitnessFunctionRegistry();

		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();

		fitnessRegistry.registerFitnessCoreFactory(factory);

		final IConstraintCheckerRegistry constraintRegistry = new ConstraintCheckerRegistry();
		final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory(
				SchedulerConstants.DCP_orderedElementsProvider);
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory);

		final OptimisationContext<ISequenceElement> context = new OptimisationContext<ISequenceElement>(
				data, initialSequences, new ArrayList<String>(factory
						.getFitnessComponentNames()), fitnessRegistry,
				Collections.singletonList(constraintFactory.getName()),
				constraintRegistry);

		final IOptimiser<ISequenceElement> optimiser = buildOptimiser(1000,
				50.0, context);

		final LinearSimulatedAnnealingFitnessEvaluator<ISequenceElement> fitnessEvaluator = (LinearSimulatedAnnealingFitnessEvaluator<ISequenceElement>) ((DefaultLocalSearchOptimiser<ISequenceElement>) optimiser)
				.getFitnessEvaluator();

		optimiser.optimise(context);
		System.out
				.println("Final fitness " + fitnessEvaluator.getBestFitness());

		// TODO: How to verify result?
	}

	<T> IOptimiser<T> buildOptimiser(final int numIterations,
			final double temperature, final IOptimisationContext<T> context) {

		// Initialise random number generator
		final Random random = new Random(1);

		final FitnessHelper<T> fitnessHelper = new FitnessHelper<T>();
		final LinearSimulatedAnnealingFitnessEvaluator<T> fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator<T>();
		fitnessEvaluator.setFitnessHelper(fitnessHelper);

		fitnessEvaluator.setTemperature(temperature);
		fitnessEvaluator.setNumberOfIterations(numIterations);

		final FitnessComponentInstantiator fci = new FitnessComponentInstantiator();
		final List<IFitnessComponent<T>> fitnessComponents = fci
				.instantiateFitnesses(context.getFitnessFunctionRegistry());

		fitnessEvaluator.setFitnessComponents(fitnessComponents);

		final Map<String, Double> weightsMap = new HashMap<String, Double>();
		for (final IFitnessComponent<T> fc : fitnessComponents) {
			weightsMap.put(fc.getName(), 1.0);
		}
		fitnessEvaluator.setFitnessComponentWeights(weightsMap);
		fitnessEvaluator.init();

		final IMoveGenerator<T> moveGenerator = createMoveGenerator(random);

		final LocalSearchOptimiser<T> lso = new DefaultLocalSearchOptimiser<T>();

		final List<IConstraintChecker<T>> constraintCheckers = Collections
				.emptyList();
		lso.setConstraintCheckers(constraintCheckers);
		lso.setNumberOfIterations(numIterations);
		lso.setSequenceManipulator(new NullSequencesManipulator<T>());
		lso.setMoveGenerator(moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);

		lso.init();

		return lso;
	}

	private <T> RandomMoveGenerator<T> createMoveGenerator(final Random random) {
		final RandomMoveGenerator<T> moveGenerator = new RandomMoveGenerator<T>();
		moveGenerator.setRandom(random);

		// Register RNG move generator units
		moveGenerator.addMoveGeneratorUnit(new Move3over2GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new Move4over1GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new Move4over2GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new MoveSnakeGeneratorUnit<T>());

		return moveGenerator;
	}
}
