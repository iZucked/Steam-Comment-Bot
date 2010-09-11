package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.ILocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.impl.GeneralTestUtils;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;

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

		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(12000, 12000l);
		keypoints.put(13000, 13000l);
		keypoints.put(14000, 14000l);
		keypoints.put(15000, 15000l);
		keypoints.put(16000, 16000l);
		keypoints.put(17000, 17000l);
		keypoints.put(18000, 18000l);
		keypoints.put(19000, 19000l);
		keypoints.put(20000, 20000l);
		final InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(
				keypoints);

		final IVesselClass vesselClass1 = builder.createVesselClass("vesselClass-1",
				12000, 20000, 150000000, 0, 7000, 10000);

		builder.setVesselClassStateParamaters(vesselClass1, VesselState.Laden,
				150 * Calculator.ScaleFactor, 100 * Calculator.ScaleFactor,
				10 * Calculator.ScaleFactor, consumptionCalculator, 15000);
		builder.setVesselClassStateParamaters(vesselClass1,
				VesselState.Ballast, 150 * Calculator.ScaleFactor, 100 * Calculator.ScaleFactor, 10 * Calculator.ScaleFactor,
				consumptionCalculator, 15000);

		// TODO: Setup start/end ports correctly
		builder.createVessel("vessel-1", vesselClass1,
				builder.createStartEndRequirement(port1),
				builder.createStartEndRequirement(port2));
		builder.createVessel("vessel-2", vesselClass1,
				builder.createStartEndRequirement(port1),
				builder.createStartEndRequirement(port2));
		builder.createVessel("vessel-3", vesselClass1,
				builder.createStartEndRequirement(port1),
				builder.createStartEndRequirement(port6));

		final ITimeWindow tw1 = builder.createTimeWindow(5, 6);
		final ITimeWindow tw2 = builder.createTimeWindow(10, 11);

		final ITimeWindow tw3 = builder.createTimeWindow(15, 16);
		final ITimeWindow tw4 = builder.createTimeWindow(20, 21);

		final ITimeWindow tw5 = builder.createTimeWindow(25, 26);
		final ITimeWindow tw6 = builder.createTimeWindow(30, 31);

		final ITimeWindow tw7 = builder.createTimeWindow(35, 36);

		final ILoadSlot load1 = builder.createLoadSlot("load1", port1, tw1, 0,
				150000 * Calculator.ScaleFactor, 5, 22800);
		final ILoadSlot load2 = builder.createLoadSlot("load2", port1, tw3, 0,
				150000 * Calculator.ScaleFactor, 5, 22800);
		final ILoadSlot load3 = builder.createLoadSlot("load3", port1, tw5, 0,
				150000 * Calculator.ScaleFactor, 5, 22800);
		final ILoadSlot load4 = builder.createLoadSlot("load4", port1, tw4, 0,
				150000 * Calculator.ScaleFactor, 5, 22800);
		final ILoadSlot load5 = builder.createLoadSlot("load5", port3, tw2, 0,
				150000 * Calculator.ScaleFactor, 5, 22800);
		final ILoadSlot load6 = builder.createLoadSlot("load6", port3, tw4, 0,
				150000 * Calculator.ScaleFactor, 5, 22800);
		final ILoadSlot load7 = builder.createLoadSlot("load7", port5, tw6, 0,
				150000 * Calculator.ScaleFactor, 5, 22800);

		final IDischargeSlot discharge1 = builder.createDischargeSlot("discharge1",
				port2, tw2, 0, 100000 * Calculator.ScaleFactor, 200000);
		final IDischargeSlot discharge2 = builder.createDischargeSlot("discharge2",
				port2, tw4, 0, 100000 * Calculator.ScaleFactor, 200000);
		final IDischargeSlot discharge3 = builder.createDischargeSlot("discharge3",
				port2, tw6, 0, 100000 * Calculator.ScaleFactor, 200000);
		final IDischargeSlot discharge4 = builder.createDischargeSlot("discharge4",
				port6, tw6, 0, 100000 * Calculator.ScaleFactor, 200000);
		final IDischargeSlot discharge5 = builder.createDischargeSlot("discharge5",
				port4, tw3, 0, 100000 * Calculator.ScaleFactor, 200000);
		final IDischargeSlot discharge6 = builder.createDischargeSlot("discharge6",
				port4, tw5, 0, 100000 * Calculator.ScaleFactor, 200000);
		final IDischargeSlot discharge7 = builder.createDischargeSlot("discharge7",
				port6, tw7, 0, 100000 * Calculator.ScaleFactor, 200000);

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

	@Test
	public void testLSO() {

		final long seed = 1;

		// Build opt data
		final IOptimisationData<ISequenceElement> data = createProblem();

		final IFitnessFunctionRegistry fitnessRegistry = createFitnessRegistry();
		final IConstraintCheckerRegistry constraintRegistry = createConstraintRegistry();

		// Generate initial state
		final IInitialSequenceBuilder<ISequenceElement> sequenceBuilder = new ConstrainedInitialSequenceBuilder<ISequenceElement>(constraintRegistry.getConstraintCheckerFactories());
		final ISequences<ISequenceElement> initialSequences =  sequenceBuilder.createInitialSequences(data);

		final OptimisationContext<ISequenceElement> context = new OptimisationContext<ISequenceElement>(
				data, initialSequences, new ArrayList<String>(
						fitnessRegistry.getFitnessComponentNames()),
				fitnessRegistry, new ArrayList<String>(
						constraintRegistry.getConstraintCheckerNames()),
				constraintRegistry);

		
		final IOptimiserProgressMonitor<ISequenceElement> monitor = new IOptimiserProgressMonitor<ISequenceElement>() {

			@Override
			public void begin(final IOptimiser<ISequenceElement> optimiser,
					final long initialFitness,
					final ISequences<ISequenceElement> initialState) {
				System.out.println("Initial Fitness: " + initialFitness);
			}

			@Override
			public void report(final IOptimiser<ISequenceElement> optimiser,
					final int iteration, final long currentFitness,
					final long bestFitness,
					final ISequences<ISequenceElement> currentState,
					final ISequences<ISequenceElement> bestState) {
				System.out.println("Iter: " + iteration + " Fitness: "
						+ bestFitness);
			}

			@Override
			public void done(final IOptimiser<ISequenceElement> optimiser,
					final long bestFitness,
					final ISequences<ISequenceElement> bestState) {
				System.out.println("Final Fitness: " + bestFitness);
			}
		};

		final ILocalSearchOptimiser<ISequenceElement> optimiser = GeneralTestUtils
				.buildOptimiser(context, new Random(seed), 1000, 5, monitor);

		final IFitnessEvaluator<ISequenceElement> fitnessEvaluator = optimiser
				.getFitnessEvaluator();

		final LinearSimulatedAnnealingFitnessEvaluator<ISequenceElement> linearFitnessEvaluator = (LinearSimulatedAnnealingFitnessEvaluator<ISequenceElement>) fitnessEvaluator;

		linearFitnessEvaluator.setOptimisationData(context
				.getOptimisationData());
		linearFitnessEvaluator.setInitialSequences(context
				.getInitialSequences());

		printSequences(context.getInitialSequences());

		final long initialFitness = linearFitnessEvaluator.getBestFitness();
		System.out.println("Initial fitness "
				+ initialFitness);

		Assert.assertFalse(initialFitness == Long.MAX_VALUE);
		
		optimiser.optimise(context);

		final long finalFitness = linearFitnessEvaluator.getBestFitness();
		System.out.println("Final fitness "
				+ finalFitness);
		Assert.assertFalse(finalFitness == Long.MAX_VALUE);

		printSequences(fitnessEvaluator.getBestSequences());

		// TODO: How to verify result?
	}

	private IConstraintCheckerRegistry createConstraintRegistry() {
		final IConstraintCheckerRegistry constraintRegistry = new ConstraintCheckerRegistry();
		
		final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory(
				SchedulerConstants.DCP_orderedElementsProvider);
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory);
		
		final ResourceAllocationConstraintCheckerFactory constraintFactory2 = new ResourceAllocationConstraintCheckerFactory(SchedulerConstants.DCP_resourceAllocationProvider);
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory2);
		
		final PortTypeConstraintCheckerFactory constraintFactory3 = new  PortTypeConstraintCheckerFactory(SchedulerConstants.DCP_portTypeProvider, 
				SchedulerConstants.DCP_vesselProvider);
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory3);
		
		return constraintRegistry;
	}

	private FitnessFunctionRegistry createFitnessRegistry() {
		final FitnessFunctionRegistry fitnessRegistry = new FitnessFunctionRegistry();

		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();

		fitnessRegistry.registerFitnessCoreFactory(factory);
		return fitnessRegistry;
	}

	void printSequences(final Collection<ISequences<ISequenceElement>> sequences) {
		for (final ISequences<ISequenceElement> s : sequences) {
			printSequences(s);
		}
	}

	void printSequences(final ISequences<ISequenceElement> sequences) {
		for (final ISequence<ISequenceElement> seq : sequences.getSequences()
				.values()) {
			System.out.print("[");
			for (final ISequenceElement t : seq) {
				System.out.print(t.getName());
				System.out.print(",");
			}
			System.out.println("]");
		}
	}
}
