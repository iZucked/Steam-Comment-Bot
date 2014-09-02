/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.ILocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

/**
 * Test class to run a "system" test of the LSO to optimise a sequence of elements based upon distance. I.e. find minimum travel distance for each resource.
 * 
 * @author Simon Goodall
 * 
 */
public class SimpleSchedulerTest {

	IOptimisationData createProblem(Injector injector) {

		final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

		// Build XY ports so distance is automatically populated`
		// TODO: Add API to determine which distance provider to use
		final IPort port1 = builder.createPort("port-1", false, null, 0, 0, "UTC");
		final IPort port2 = builder.createPort("port-2", false, null, 0, 5, "UTC");
		final IPort port3 = builder.createPort("port-3", false, null, 5, 0, "UTC");
		final IPort port4 = builder.createPort("port-4", false, null, 5, 5, "UTC");
		final IPort port5 = builder.createPort("port-5", false, null, 0, 10, "UTC");
		final IPort port6 = builder.createPort("port-6", false, null, 5, 10, "UTC");

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
		final InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(keypoints);

		final IVesselClass vesselClass1 = builder.createVesselClass("vesselClass-1", 12000, 20000, 150000000, 0, 7000, 10000, 0, Integer.MAX_VALUE, 0, 0);

		builder.setVesselClassStateParameters(vesselClass1, VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(150), OptimiserUnitConvertor.convertToInternalDailyRate(100),
				OptimiserUnitConvertor.convertToInternalDailyRate(10), consumptionCalculator, 15000, 0);
		builder.setVesselClassStateParameters(vesselClass1, VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(150), OptimiserUnitConvertor.convertToInternalDailyRate(100),
				OptimiserUnitConvertor.convertToInternalDailyRate(10), consumptionCalculator, 15000, 0);

		// TODO: Setup start/end ports correctly
		IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, 150000000);
		IVessel vessel2 = builder.createVessel("vessel-2", vesselClass1, 150000000);
		IVessel vessel3 = builder.createVessel("vessel-3", vesselClass1, 150000000);

		builder.createVesselAvailability(vessel1, new ConstantValueCurve(0), VesselInstanceType.FLEET, builder.createStartRequirement(port1, null, null),
				builder.createEndRequirement(Collections.singleton(port2), null, false, 0));
		builder.createVesselAvailability(vessel2, new ConstantValueCurve(0), VesselInstanceType.FLEET, builder.createStartRequirement(port1, null, null),
				builder.createEndRequirement(Collections.singleton(port2), null, false, 0));
		builder.createVesselAvailability(vessel3, new ConstantValueCurve(0), VesselInstanceType.FLEET, builder.createStartRequirement(port1, null, null),
				builder.createEndRequirement(Collections.singleton(port6), null, false, 0));

		final ITimeWindow tw1 = builder.createTimeWindow(5, 6);
		final ITimeWindow tw2 = builder.createTimeWindow(10, 11);

		final ITimeWindow tw3 = builder.createTimeWindow(15, 16);
		final ITimeWindow tw4 = builder.createTimeWindow(20, 21);

		final ITimeWindow tw5 = builder.createTimeWindow(25, 26);
		final ITimeWindow tw6 = builder.createTimeWindow(30, 31);

		final ITimeWindow tw7 = builder.createTimeWindow(35, 36);

		final ILoadPriceCalculator purchaseCurve = new FixedPriceContract(5);
		final ISalesPriceCalculator salesCurve = new FixedPriceContract(200000);

		final ILoadSlot load1 = builder.createLoadSlot("load1", port1, tw1, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false);
		final ILoadSlot load2 = builder.createLoadSlot("load2", port1, tw3, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false);
		final ILoadSlot load3 = builder.createLoadSlot("load3", port1, tw5, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false);
		final ILoadSlot load4 = builder.createLoadSlot("load4", port1, tw4, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false);
		final ILoadSlot load5 = builder.createLoadSlot("load5", port3, tw2, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false);
		final ILoadSlot load6 = builder.createLoadSlot("load6", port3, tw4, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false);
		final ILoadSlot load7 = builder.createLoadSlot("load7", port5, tw6, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false);

		final IDischargeSlot discharge1 = builder.createDischargeSlot("discharge1", port2, tw2, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false);
		final IDischargeSlot discharge2 = builder.createDischargeSlot("discharge2", port2, tw4, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false);
		final IDischargeSlot discharge3 = builder.createDischargeSlot("discharge3", port2, tw6, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false);
		final IDischargeSlot discharge4 = builder.createDischargeSlot("discharge4", port6, tw6, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false);
		final IDischargeSlot discharge5 = builder.createDischargeSlot("discharge5", port4, tw3, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false);
		final IDischargeSlot discharge6 = builder.createDischargeSlot("discharge6", port4, tw5, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false);
		final IDischargeSlot discharge7 = builder.createDischargeSlot("discharge7", port6, tw7, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false);

		builder.createCargo(Lists.<IPortSlot> newArrayList(load1, discharge1), false);
		builder.createCargo(Lists.<IPortSlot> newArrayList(load2, discharge2), false);
		builder.createCargo(Lists.<IPortSlot> newArrayList(load3, discharge3), false);
		builder.createCargo(Lists.<IPortSlot> newArrayList(load4, discharge4), false);
		builder.createCargo(Lists.<IPortSlot> newArrayList(load5, discharge5), false);
		builder.createCargo(Lists.<IPortSlot> newArrayList(load6, discharge6), false);
		builder.createCargo(Lists.<IPortSlot> newArrayList(load7, discharge7), false);

		// TODO: Set port durations

		// ....

		builder.buildXYDistances();

		// Generate the optimisation data
		final IOptimisationData data = builder.getOptimisationData();

		builder.dispose();

		return data;
	}

	@Test
	public void testLSO() {

		final long seed = 1;
		final Injector parentInjector = Guice.createInjector(new DataComponentProviderModule());

		final IOptimisationData data = createProblem(parentInjector);
		// Build opt data
		ScheduleTestModule m = new ScheduleTestModule(data);
		Injector injector = parentInjector.createChildInjector(m);

		// Generate initial state
		// final IInitialSequenceBuilder sequenceBuilder = injector.getInstance(IInitialSequenceBuilder.class);

		// final ISequences initialSequences = sequenceBuilder.createInitialSequences(data, null, null, Collections.<ISequenceElement, ISequenceElement> emptyMap());

		// final OptimisationContext context = new OptimisationContext(data, initialSequences, new ArrayList<String>(fitnessRegistry.getFitnessComponentNames()), fitnessRegistry, new
		// ArrayList<String>(
		// constraintRegistry.getConstraintCheckerNames()), constraintRegistry, new ArrayList<String>(evaluationProcessRegistry.getEvaluationProcessNames()), evaluationProcessRegistry);

		IOptimisationContext context = injector.getInstance(IOptimisationContext.class);

		final IOptimiserProgressMonitor monitor = new IOptimiserProgressMonitor() {

			@Override
			public void begin(final IOptimiser optimiser, final long initialFitness, final IAnnotatedSolution initialState) {
				System.out.println("Initial Fitness: " + initialFitness);
			}

			@Override
			public void report(final IOptimiser optimiser, final int iteration, final long currentFitness, final long bestFitness, final IAnnotatedSolution currentState,
					final IAnnotatedSolution bestState) {
				System.out.println("Iter: " + iteration + " Fitness: " + bestFitness);
			}

			@Override
			public void done(final IOptimiser optimiser, final long bestFitness, final IAnnotatedSolution bestState) {
				System.out.println("Final Fitness: " + bestFitness);
			}
		};

		final ILocalSearchOptimiser optimiser = GeneralTestUtils.buildOptimiser(context, new Random(seed), 1000, 5, monitor);

		for (IConstraintChecker c : optimiser.getConstraintCheckers()) {
			injector.injectMembers(c);
		}

		for (IFitnessComponent c : optimiser.getFitnessEvaluator().getFitnessComponents()) {
			injector.injectMembers(c);
			injector.injectMembers(c.getFitnessCore());
		}

		final IFitnessEvaluator fitnessEvaluator = optimiser.getFitnessEvaluator();

		final LinearSimulatedAnnealingFitnessEvaluator linearFitnessEvaluator = (LinearSimulatedAnnealingFitnessEvaluator) fitnessEvaluator;

		linearFitnessEvaluator.setOptimisationData(context.getOptimisationData());
		linearFitnessEvaluator.setInitialSequences(context.getInitialSequences());

		printSequences(context.getInitialSequences());

		final long initialFitness = linearFitnessEvaluator.getBestFitness();
		System.out.println("Initial fitness " + initialFitness);

		Assert.assertFalse(initialFitness == Long.MAX_VALUE);

		optimiser.optimise(context);

		final long finalFitness = linearFitnessEvaluator.getBestFitness();
		System.out.println("Final fitness " + finalFitness);
		Assert.assertFalse(finalFitness == Long.MAX_VALUE);

		printSequences(fitnessEvaluator.getBestSequences());

		// TODO: How to verify result?
	}

	void printSequences(final Collection<ISequences> sequences) {
		for (final ISequences s : sequences) {
			printSequences(s);
		}
	}

	void printSequences(final ISequences sequences) {
		for (final ISequence seq : sequences.getSequences().values()) {
			System.out.print("[");
			for (final ISequenceElement t : seq) {
				System.out.print(t.getName());
				System.out.print(",");
			}
			System.out.println("]");
		}
	}
}
