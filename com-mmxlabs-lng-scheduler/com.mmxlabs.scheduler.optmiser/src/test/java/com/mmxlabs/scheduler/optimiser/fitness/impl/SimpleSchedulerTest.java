package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.junit.Test;

import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.OptimisationContext;
import com.mmxlabs.optimiser.lso.ILocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.TestUtils;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
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

		// Build XY ports so distance is automatically populated`
		// TODO: Add API to determine which distance provider to use
		final IPort port1 = builder.createPort("port-1", 0, 0);
		final IPort port2 = builder.createPort("port-2", 0, 5);
		final IPort port3 = builder.createPort("port-3", 5, 0);
		final IPort port4 = builder.createPort("port-4", 5, 5);
		final IPort port5 = builder.createPort("port-5", 0, 10);
		final IPort port6 = builder.createPort("port-6", 5, 10);

		TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(12000, 12000l);
		keypoints.put(13000, 13000l);
		keypoints.put(14000, 14000l);
		keypoints.put(15000, 15000l);
		keypoints.put(16000, 16000l);
		keypoints.put(17000, 17000l);
		keypoints.put(18000, 18000l);
		keypoints.put(19000, 19000l);
		keypoints.put(20000, 20000l);
		InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(
				keypoints);

		IVesselClass vesselClass1 = builder.createVesselClass("vesselClass-1",
				12000, 20000, 150000000, 0);

		builder.setVesselClassStateParamaters(vesselClass1, VesselState.Laden,
				15000, 10000, 10000, consumptionCalculator, 15000);
		builder.setVesselClassStateParamaters(vesselClass1,
				VesselState.Ballast, 15000, 10000, 10000,
				consumptionCalculator, 15000);

		builder.createVessel("vessel-1", vesselClass1);
		builder.createVessel("vessel-2", vesselClass1);
		builder.createVessel("vessel-3", vesselClass1);

		final ITimeWindow tw1 = builder.createTimeWindow(5, 6);
		final ITimeWindow tw2 = builder.createTimeWindow(10, 11);

		final ITimeWindow tw3 = builder.createTimeWindow(15, 16);
		final ITimeWindow tw4 = builder.createTimeWindow(20, 21);

		final ITimeWindow tw5 = builder.createTimeWindow(25, 26);
		final ITimeWindow tw6 = builder.createTimeWindow(30, 31);

		final ITimeWindow tw7 = builder.createTimeWindow(35, 36);

		ILoadSlot load1 = builder.createLoadSlot("load1", port1, tw1, 0,
				1000000, 5);
		ILoadSlot load2 = builder.createLoadSlot("load2", port1, tw3, 0,
				1000000, 5);
		ILoadSlot load3 = builder.createLoadSlot("load3", port1, tw5, 0,
				1000000, 5);
		ILoadSlot load4 = builder.createLoadSlot("load4", port1, tw4, 0,
				1000000, 5);
		ILoadSlot load5 = builder.createLoadSlot("load5", port3, tw2, 0,
				1000000, 5);
		ILoadSlot load6 = builder.createLoadSlot("load6", port3, tw4, 0,
				1000000, 5);
		ILoadSlot load7 = builder.createLoadSlot("load7", port5, tw6, 0,
				1000000, 5);

		IDischargeSlot discharge1 = builder.createDischargeSlot("discharge1",
				port2, tw2, 0, 1000000, 6);
		IDischargeSlot discharge2 = builder.createDischargeSlot("discharge2",
				port2, tw4, 0, 1000000, 6);
		IDischargeSlot discharge3 = builder.createDischargeSlot("discharge3",
				port2, tw6, 0, 1000000, 6);
		IDischargeSlot discharge4 = builder.createDischargeSlot("discharge4",
				port6, tw6, 0, 1000000, 6);
		IDischargeSlot discharge5 = builder.createDischargeSlot("discharge5",
				port4, tw3, 0, 1000000, 6);
		IDischargeSlot discharge6 = builder.createDischargeSlot("discharge6",
				port4, tw5, 0, 1000000, 6);
		IDischargeSlot discharge7 = builder.createDischargeSlot("discharge7",
				port6, tw7, 0, 1000000, 6);

		builder.createCargo("cargo1", load1, discharge1);
		builder.createCargo("cargo2", load2, discharge2);
		builder.createCargo("cargo3", load3, discharge3);
		builder.createCargo("cargo4", load4, discharge4);
		builder.createCargo("cargo5", load5, discharge5);
		builder.createCargo("cargo6", load6, discharge6);
		builder.createCargo("cargo7", load7, discharge7);

		// TODO: Set port durations

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

		final long seed = 1;

		// Build opt data
		final IOptimisationData<ISequenceElement> data = createProblem();
		// Generate initial state
		final ISequences<ISequenceElement> initialSequences = createInitialSequences(
				data, seed);

		final IFitnessFunctionRegistry fitnessRegistry = createFitnessRegistry();
		final IConstraintCheckerRegistry constraintRegistry = createConstraintRegistry();

		final OptimisationContext<ISequenceElement> context = new OptimisationContext<ISequenceElement>(
				data, initialSequences, new ArrayList<String>(
						fitnessRegistry.getFitnessComponentNames()),
				fitnessRegistry, new ArrayList<String>(
						constraintRegistry.getConstraintCheckerNames()),
				constraintRegistry);

		final ILocalSearchOptimiser<ISequenceElement> optimiser = TestUtils
				.buildOptimiser(context, new Random(seed), 1000, 5);

		final IFitnessEvaluator<ISequenceElement> fitnessEvaluator = optimiser
				.getFitnessEvaluator();

		final LinearSimulatedAnnealingFitnessEvaluator<ISequenceElement> linearFitnessEvaluator = (LinearSimulatedAnnealingFitnessEvaluator<ISequenceElement>) fitnessEvaluator;

		linearFitnessEvaluator.setOptimisationData(context
				.getOptimisationData());
		linearFitnessEvaluator.setInitialSequences(context
				.getInitialSequences());

		printSequences(context.getInitialSequences());

		System.out.println("Initial fitness "
				+ linearFitnessEvaluator.getBestFitness());

		optimiser.optimise(context);

		System.out.println("Final fitness "
				+ linearFitnessEvaluator.getBestFitness());

		printSequences(fitnessEvaluator.getBestSequences());

		// TODO: How to verify result?
	}

	private IConstraintCheckerRegistry createConstraintRegistry() {
		final IConstraintCheckerRegistry constraintRegistry = new ConstraintCheckerRegistry();
		final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory(
				SchedulerConstants.DCP_orderedElementsProvider);
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory);
		return constraintRegistry;
	}

	private FitnessFunctionRegistry createFitnessRegistry() {
		final FitnessFunctionRegistry fitnessRegistry = new FitnessFunctionRegistry();

		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();

		fitnessRegistry.registerFitnessCoreFactory(factory);
		return fitnessRegistry;
	}

	<T> void printSequences(final Collection<ISequences<T>> sequences) {
		for (ISequences<T> s : sequences) {
			printSequences(s);
		}
	}

	<T> void printSequences(final ISequences<T> sequences) {
		for (ISequence<T> seq : sequences.getSequences().values()) {
			System.out.print("[");
			for (T t : seq) {
				System.out.print(t);
				System.out.print(",");
			}
			System.out.println("]");
		}
	}
}
