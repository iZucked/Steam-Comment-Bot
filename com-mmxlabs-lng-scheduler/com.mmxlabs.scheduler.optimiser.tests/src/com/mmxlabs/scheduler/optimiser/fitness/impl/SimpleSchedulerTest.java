/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.ILocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.impl.TimeWindowMaker;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelCurveProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

/**
 * Test class to run a "system" test of the LSO to optimise a sequence of elements based upon distance. I.e. find minimum travel distance for each resource.
 * 
 * @author Simon Goodall
 * 
 */
public class SimpleSchedulerTest {

	private static final boolean DEFAULT_VOLUME_LIMIT_IS_M3 = true;

	IOptimisationData createProblem(final Injector injector) {

		final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);
		final IBaseFuelCurveProviderEditor baseFuelCurveProvider = injector.getInstance(IBaseFuelCurveProviderEditor.class);

		final IBaseFuelCurveProviderEditor baseFuCurveProviderEditor = injector.getInstance(IBaseFuelCurveProviderEditor.class);

		// Build XY ports so distance is automatically populated`
		// TODO: Add API to determine which distance provider to use
		final IPort port1 = builder.createPortForTest("port-1", false, null, 0, 0, "UTC");
		final IPort port2 = builder.createPortForTest("port-2", false, null, 0, 5, "UTC");
		final IPort port3 = builder.createPortForTest("port-3", false, null, 5, 0, "UTC");
		final IPort port4 = builder.createPortForTest("port-4", false, null, 5, 5, "UTC");
		final IPort port5 = builder.createPortForTest("port-5", false, null, 0, 10, "UTC");
		final IPort port6 = builder.createPortForTest("port-6", false, null, 5, 10, "UTC");

		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(12000, 12000L);
		keypoints.put(13000, 13000L);
		keypoints.put(14000, 14000L);
		keypoints.put(15000, 15000L);
		keypoints.put(16000, 16000L);
		keypoints.put(17000, 17000L);
		keypoints.put(18000, 18000L);
		keypoints.put(19000, 19000L);
		keypoints.put(20000, 20000L);
		final InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(keypoints);

		// CV is 22800, divide by the following to get m3 to MT of 0.1 as used by this test.
		final int baseFuelEquivalence = 228000;
		final IBaseFuel baseFuel = builder.createBaseFuel("test", baseFuelEquivalence);
		baseFuCurveProviderEditor.setBaseFuelCurve(baseFuel, new ConstantValueCurve(7000));
		final IVesselClass vesselClass1 = builder.createVesselClass("vesselClass-1", 12000, 20000, 150000000, 0, baseFuel, 0, Integer.MAX_VALUE, 0, 0, false);

		// set up basefuel curve
		final StepwiseIntegerCurve baseFuelCurve = new StepwiseIntegerCurve();
		baseFuelCurve.setValueAfter(0, 7000);
		baseFuelCurveProvider.setBaseFuelCurve(baseFuel, baseFuelCurve);

		builder.setVesselClassStateParameters(vesselClass1, VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(150), OptimiserUnitConvertor.convertToInternalDailyRate(100),
				OptimiserUnitConvertor.convertToInternalDailyRate(10), consumptionCalculator, 0, OptimiserUnitConvertor.convertToInternalDailyRate(100));
		builder.setVesselClassStateParameters(vesselClass1, VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(150), OptimiserUnitConvertor.convertToInternalDailyRate(100),
				OptimiserUnitConvertor.convertToInternalDailyRate(10), consumptionCalculator, 0, OptimiserUnitConvertor.convertToInternalDailyRate(100));

		// TODO: Setup start/end ports correctly
		final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, 150000000);
		final IVessel vessel2 = builder.createVessel("vessel-2", vesselClass1, 150000000);
		final IVessel vessel3 = builder.createVessel("vessel-3", vesselClass1, 150000000);

		IHeelOptionConsumer heelOptionConsumer = new HeelOptionConsumer(0, 0, VesselTankState.MUST_BE_WARM, new ConstantHeelPriceCalculator(0));

		builder.createVesselAvailability(vessel1, new ConstantValueLongCurve(0), VesselInstanceType.FLEET, builder.createStartRequirement(port1, false, new TimeWindow(0, 1), null),
				builder.createEndRequirement(Collections.singleton(port2), false, new MutableTimeWindow(0, Integer.MAX_VALUE), heelOptionConsumer, false), new ConstantValueLongCurve(0),
				false);
		builder.createVesselAvailability(vessel2, new ConstantValueLongCurve(0), VesselInstanceType.FLEET, builder.createStartRequirement(port1, false, new TimeWindow(0, 1), null),
				builder.createEndRequirement(Collections.singleton(port2), false, new MutableTimeWindow(0, Integer.MAX_VALUE), heelOptionConsumer, false), new ConstantValueLongCurve(0),
				false);
		builder.createVesselAvailability(vessel3, new ConstantValueLongCurve(0), VesselInstanceType.FLEET, builder.createStartRequirement(port1, false, new TimeWindow(0, 1), null),
				builder.createEndRequirement(Collections.singleton(port6), false, new MutableTimeWindow(0, Integer.MAX_VALUE), heelOptionConsumer, false), new ConstantValueLongCurve(0),
				false);

		final ITimeWindow tw1 = TimeWindowMaker.createInclusiveExclusive(5, 6, 0, false);
		final ITimeWindow tw2 = TimeWindowMaker.createInclusiveExclusive(10, 11, 0, false);

		final ITimeWindow tw3 = TimeWindowMaker.createInclusiveExclusive(15, 16, 0, false);
		final ITimeWindow tw4 = TimeWindowMaker.createInclusiveExclusive(20, 21, 0, false);

		final ITimeWindow tw5 = TimeWindowMaker.createInclusiveExclusive(25, 26, 0, false);
		final ITimeWindow tw6 = TimeWindowMaker.createInclusiveExclusive(30, 31, 0, false);

		final ITimeWindow tw7 = TimeWindowMaker.createInclusiveExclusive(35, 36, 0, false);

		final ILoadPriceCalculator purchaseCurve = new FixedPriceContract(5);
		final ISalesPriceCalculator salesCurve = new FixedPriceContract(200000);

		final ILoadSlot load1 = builder.createLoadSlot("load1", port1, tw1, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final ILoadSlot load2 = builder.createLoadSlot("load2", port1, tw3, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final ILoadSlot load3 = builder.createLoadSlot("load3", port1, tw5, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final ILoadSlot load4 = builder.createLoadSlot("load4", port1, tw4, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final ILoadSlot load5 = builder.createLoadSlot("load5", port3, tw2, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final ILoadSlot load6 = builder.createLoadSlot("load6", port3, tw4, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final ILoadSlot load7 = builder.createLoadSlot("load7", port5, tw6, 0, OptimiserUnitConvertor.convertToInternalVolume(150000), purchaseCurve, 22800, 24, false, false,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);

		final IDischargeSlot discharge1 = builder.createDischargeSlot("discharge1", port2, tw2, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final IDischargeSlot discharge2 = builder.createDischargeSlot("discharge2", port2, tw4, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final IDischargeSlot discharge3 = builder.createDischargeSlot("discharge3", port2, tw6, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final IDischargeSlot discharge4 = builder.createDischargeSlot("discharge4", port6, tw6, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final IDischargeSlot discharge5 = builder.createDischargeSlot("discharge5", port4, tw3, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final IDischargeSlot discharge6 = builder.createDischargeSlot("discharge6", port4, tw5, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
		final IDischargeSlot discharge7 = builder.createDischargeSlot("discharge7", port6, tw7, 0, OptimiserUnitConvertor.convertToInternalVolume(100000), 0, Long.MAX_VALUE, salesCurve, 24,
				IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);

		builder.createCargo(Lists.<@NonNull IPortSlot> newArrayList(load1, discharge1), false);
		builder.createCargo(Lists.<@NonNull IPortSlot> newArrayList(load2, discharge2), false);
		builder.createCargo(Lists.<@NonNull IPortSlot> newArrayList(load3, discharge3), false);
		builder.createCargo(Lists.<@NonNull IPortSlot> newArrayList(load4, discharge4), false);
		builder.createCargo(Lists.<@NonNull IPortSlot> newArrayList(load5, discharge5), false);
		builder.createCargo(Lists.<@NonNull IPortSlot> newArrayList(load6, discharge6), false);
		builder.createCargo(Lists.<@NonNull IPortSlot> newArrayList(load7, discharge7), false);

		// TODO: Set port durations

		// ....

		final List<IPort> portsList = Lists.newArrayList(port1, port2, port3, port4, port5, port6);
		for (final IPort from : portsList) {
			if (from instanceof IXYPort) {
				final IXYPort xyFrom = (IXYPort) from;
				for (final IPort to : portsList) {
					if (to instanceof IXYPort) {
						final IXYPort xyTo = (IXYPort) to;
						final float diffX = xyFrom.getX() - xyTo.getX();
						final float diffY = xyFrom.getY() - xyTo.getY();

						final double distance = Math.sqrt((diffX * diffX) + (diffY * diffY));
						builder.setPortToPortDistance(from, to, ERouteOption.DIRECT, (int) distance);
					}
				}
			}
		}

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
		final ScheduleTestModule m = new ScheduleTestModule(data);
		final Injector injector = parentInjector.createChildInjector(m);
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			// Generate initial state
			// final IInitialSequenceBuilder sequenceBuilder = injector.getInstance(IInitialSequenceBuilder.class);

			// final ISequences initialSequences = sequenceBuilder.createInitialSequences(data, null, null, Collections.<ISequenceElement, ISequenceElement> emptyMap());

			// final OptimisationContext context = new OptimisationContext(data, initialSequences, new ArrayList<String>(fitnessRegistry.getFitnessComponentNames()), fitnessRegistry, new
			// ArrayList<String>(
			// constraintRegistry.getConstraintCheckerNames()), constraintRegistry, new ArrayList<String>(evaluationProcessRegistry.getEvaluationProcessNames()), evaluationProcessRegistry);

			final IOptimisationContext context = injector.getInstance(IOptimisationContext.class);

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

			final ILocalSearchOptimiser optimiser = GeneralTestUtils.buildOptimiser(context, data, new Random(seed), 1000, 5, monitor);

			for (final IConstraintChecker c : optimiser.getConstraintCheckers()) {
				injector.injectMembers(c);
			}

			for (final IFitnessComponent c : optimiser.getFitnessEvaluator().getFitnessComponents()) {
				injector.injectMembers(c);
				injector.injectMembers(c.getFitnessCore());
			}

			for (final IEvaluationProcess c : optimiser.getFitnessEvaluator().getEvaluationProcesses()) {
				injector.injectMembers(c);
			}

			final IFitnessEvaluator fitnessEvaluator = optimiser.getFitnessEvaluator();

			final LinearSimulatedAnnealingFitnessEvaluator linearFitnessEvaluator = (LinearSimulatedAnnealingFitnessEvaluator) fitnessEvaluator;

			final IEvaluationState evaluationState = new EvaluationState();

			for (final IEvaluationProcess c : optimiser.getFitnessEvaluator().getEvaluationProcesses()) {
				c.evaluate(context.getInputSequences(), evaluationState);
			}

			linearFitnessEvaluator.setInitialSequences(context.getInputSequences(), context.getInputSequences(), evaluationState);
			printSequences(context.getInputSequences());

			final long initialFitness = linearFitnessEvaluator.getBestFitness();
			System.out.println("Initial fitness " + initialFitness);

			Assert.assertFalse(initialFitness == Long.MAX_VALUE);

			optimiser.optimise(context);

			final long finalFitness = linearFitnessEvaluator.getBestFitness();
			System.out.println("Final fitness " + finalFitness);
			Assert.assertFalse(finalFitness == Long.MAX_VALUE);

			final Triple<ISequences, ISequences, IEvaluationState> bestSequences = fitnessEvaluator.getBestSequences();
			Assert.assertNotNull(bestSequences);
			printSequences(bestSequences.getFirst());

			// TODO: How to verify result?
		}
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
