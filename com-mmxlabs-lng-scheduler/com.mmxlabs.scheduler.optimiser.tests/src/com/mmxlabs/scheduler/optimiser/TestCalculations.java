/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import javax.inject.Singleton;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IEvaluationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselStartDateCharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.ILoadEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.ExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.DefaultEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.schedule.VoyagePlanAnnotator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Set of unit tests to check that the expected costs are calculated from an input set of data.
 * 
 */
@SuppressWarnings({ "unused", "null" })
public class TestCalculations {

	/**
	 * 
	 * 
	 * TODO: Need to test NBO speed (lets increase rate so we get e.g. 13/14 knots) *** Case 1, but with higher NBO speed/rates Need to trigger Base_Supplemental calculations Need to test
	 * load/discharge volumes + cost/revenue calcs
	 */

	public static final boolean DEFAULT_VOLUME_LIMIT_IS_M3 = true;

	/**
	 * This tests the fuel costs + consumptions when travelling at slowest speed with no idle time.
	 */
	@Test
	public void testCalculations1() {
		final IVolumeAllocator volumeAllocator = Mockito.mock(IVolumeAllocator.class);
		final Injector injector = createTestInjector(volumeAllocator);

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);) {
			scope.enter();

			final IVesselBaseFuelCalculator v = injector.getInstance(IVesselBaseFuelCalculator.class);

			final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

			final IPort port1 = builder.createPortForTest("port-1", false, null, "UTC");
			final IPort port2 = builder.createPortForTest("port-2", false, null, "UTC");
			final IPort port3 = builder.createPortForTest("port-3", false, null, "UTC");
			final IPort port4 = builder.createPortForTest("port-4", false, null, "UTC");

			final int minSpeed = 12000;
			final int maxSpeed = 20000;
			final int capacity = 150000000;
			// 2 / 4 == 0.5 equivalence
			final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(2.0);
			final int baseFuelEquivalence = OptimiserUnitConvertor.convertToInternalConversionFactor(4);
			final int baseFuelUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(400);
			final IBaseFuel baseFuel = new BaseFuel("test");
			baseFuel.setEquivalenceFactor(baseFuelEquivalence);
			final IVesselClass vesselClass1 = builder.createVesselClass("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuel, 0, Integer.MAX_VALUE, 0, 0, false);

			final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<Integer, Long>();
			ladenKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.6));
			ladenKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.4));
			final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ladenKeypoints);
			final int laden_nboRateInM3PerDay = OptimiserUnitConvertor.convertToInternalDailyRate(1.2);
			final int laden_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int laden_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.5);
			builder.setVesselClassStateParameters(vesselClass1, VesselState.Laden, laden_nboRateInM3PerDay, laden_idleNBORateInM3PerHour, laden_idleConsumptionRateInMTPerHour,
					ladenConsumptionCalculator, 0);
			final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<Integer, Long>();
			ballastKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.5));
			ballastKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.3));
			final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ballastKeypoints);

			final int ballast_nboRateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int ballast_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.8);
			final int ballast_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.4);
			builder.setVesselClassStateParameters(vesselClass1, VesselState.Ballast, ballast_nboRateInM3PerHour, ballast_idleNBORateInM3PerHour, ballast_idleConsumptionRateInMTPerHour,
					ballastConsumptionCalculator, 0);
			final IStartRequirement startRequirement = builder.createStartRequirement(port1, builder.createTimeWindow(0, 0), null);
			final IEndRequirement endRequirement = builder.createEndRequirement(Collections.singleton(port4), builder.createTimeWindow(75, 75), false, 0, false);

			final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, capacity);
			final IVesselAvailability vesselAvailability1 = builder.createVesselAvailability(vessel1, new ConstantValueLongCurve(0), VesselInstanceType.FLEET, startRequirement, endRequirement);

			final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
			final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2, loadWindow, 0, 150000000, new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), cargoCVValue, 1,
					false, false, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);

			final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
			final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-1", port3, dischargeWindow, 0, 150000000, 0, Long.MAX_VALUE,
					new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), 1, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false,
					DEFAULT_VOLUME_LIMIT_IS_M3);

			final ICargo cargo1 = builder.createCargo(Lists.newArrayList(loadSlot, dischargeSlot), false);

			builder.setPortToPortDistance(port1, port2, ERouteOption.DIRECT, 12 * 24);
			builder.setPortToPortDistance(port2, port3, ERouteOption.DIRECT, 12 * 24);
			builder.setPortToPortDistance(port3, port4, ERouteOption.DIRECT, 12 * 24);

			final IOptimisationData data = builder.getOptimisationData();

			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IStartEndRequirementProvider startEndProvider = injector.getInstance(IStartEndRequirementProvider.class);

			final IResource resource = vesselProvider.getResource(vesselAvailability1);

			final ISequenceElement startElement = startEndProvider.getStartElement(resource);
			assert startElement != null;
			final ISequenceElement endElement = startEndProvider.getEndElement(resource);
			assert endElement != null;

			final ISequenceElement dischargeElement = portSlotProvider.getElement(dischargeSlot);
			assert dischargeElement != null;
			final ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
			assert loadElement != null;
			final List<ISequenceElement> sequenceList = CollectionsUtil.makeArrayList(startElement, loadElement, dischargeElement, endElement);

			final ISequence sequence = new ListSequence(sequenceList);

			final IAllocationAnnotation allocationAnnotation = Mockito.mock(IAllocationAnnotation.class);
			{
				final PortTimesRecord expectedPTR = new PortTimesRecord();
				expectedPTR.setSlotTime(loadSlot, 25);
				expectedPTR.setSlotTime(dischargeSlot, 50);
				expectedPTR.setSlotDuration(loadSlot, 1);
				expectedPTR.setSlotDuration(dischargeSlot, 1);
				expectedPTR.setReturnSlotTime(portSlotProvider.getPortSlot(endElement), 75);

				Mockito.when(volumeAllocator.allocate(Matchers.<IVesselAvailability> any(), Matchers.anyInt(), Matchers.<VoyagePlan> any(), Matchers.<IPortTimesRecord> eq(expectedPTR)))
						.thenReturn(allocationAnnotation);
			}

			Mockito.when(allocationAnnotation.getSlotTime(loadSlot)).thenReturn(25);
			Mockito.when(allocationAnnotation.getSlotDuration(loadSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(dischargeSlot)).thenReturn(50);
			Mockito.when(allocationAnnotation.getSlotDuration(dischargeSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(portSlotProvider.getPortSlot(endElement))).thenReturn(75);

			Mockito.when(allocationAnnotation.getRemainingHeelVolumeInM3()).thenReturn(0L);
			// Load enough to cover boil-off
			Mockito.when(allocationAnnotation.getSlotVolumeInM3(loadSlot)).thenReturn(2200L);
			Mockito.when(allocationAnnotation.getSlotVolumeInM3(dischargeSlot)).thenReturn(0L);

			// Schedule sequence
			final int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
			final ISequences sequences = new Sequences(Collections.singletonList(resource), CollectionsUtil.<IResource, ISequence> makeHashMap(resource, sequence));

			final IEvaluationContext context = Mockito.mock(IEvaluationContext.class);
			final IEvaluationState state = Mockito.mock(IEvaluationState.class);

			final AnnotatedSolution annotatedSolution = new AnnotatedSolution(sequences, context, state);

			// try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			// scope.enter();
			final ScheduleCalculator scheduler = injector.getInstance(ScheduleCalculator.class);

			final VolumeAllocatedSequences volumeAllocatedSequences = scheduler.schedule(sequences, new int[][] { expectedArrivalTimes }, annotatedSolution);
			// }
			Assert.assertNotNull(volumeAllocatedSequences);
			// TODO: Start checking results
			{
				Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
				Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class));
				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(0, event.getDuration());
				Assert.assertEquals(1, event.getStartTime());
				Assert.assertEquals(1, event.getEndTime());
				Assert.assertEquals(startElement, event.getSequenceElement());
			}

			{

				final IJourneyEvent journeyEvent = annotatedSolution.getElementAnnotations().getAnnotation(loadElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
				Assert.assertNotNull(journeyEvent);
				Assert.assertEquals(1, journeyEvent.getStartTime());

				Assert.assertEquals(24 * 12, journeyEvent.getDistance());
				Assert.assertEquals(24, journeyEvent.getDuration());

				Assert.assertEquals(12000, journeyEvent.getSpeed());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyEvent.getVesselState());

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(500, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals((500L * baseFuelUnitPrice) / Calculator.HighScaleFactor, journeyEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));

				final IIdleEvent idleEvent = annotatedSolution.getElementAnnotations().getAnnotation(loadElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
				Assert.assertNotNull(idleEvent);
				Assert.assertSame(loadElement, idleEvent.getSequenceElement());
				Assert.assertSame(port2, idleEvent.getPort());
				Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
				Assert.assertEquals(0, idleEvent.getDuration());
				Assert.assertEquals(25, idleEvent.getStartTime());
				Assert.assertEquals(25, idleEvent.getEndTime());

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleBase));

				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(loadElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(1, event.getDuration());
				Assert.assertEquals(25, event.getStartTime());
				Assert.assertEquals(26, event.getEndTime());
				Assert.assertEquals(loadElement, event.getSequenceElement());
			}

			{

				final IJourneyEvent journeyEvent = annotatedSolution.getElementAnnotations().getAnnotation(dischargeElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
				Assert.assertNotNull(journeyEvent);
				Assert.assertEquals(26, journeyEvent.getStartTime());
				Assert.assertEquals(24 * 12, journeyEvent.getDistance());
				Assert.assertEquals(24, journeyEvent.getDuration());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Laden, journeyEvent.getVesselState());

				Assert.assertEquals(12000, journeyEvent.getSpeed());

				Assert.assertEquals(laden_nboRateInM3PerDay, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals((1200L * 500000) / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals((long) laden_nboRateInM3PerDay * (long) cargoCVValue / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
				// Assert.assertEquals(25 * 1200 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(laden_nboRateInM3PerDay * 2 * 5, journeyEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));

				final IIdleEvent idleEvent = annotatedSolution.getElementAnnotations().getAnnotation(dischargeElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
				Assert.assertNotNull(idleEvent);
				Assert.assertSame(dischargeElement, idleEvent.getSequenceElement());
				Assert.assertSame(port3, idleEvent.getPort());
				Assert.assertSame(VesselState.Laden, idleEvent.getVesselState());
				Assert.assertEquals(0, idleEvent.getDuration());
				Assert.assertEquals(50, idleEvent.getStartTime());
				Assert.assertEquals(50, idleEvent.getEndTime());

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleBase));

				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(dischargeElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(1, event.getDuration());
				Assert.assertEquals(50, event.getStartTime());
				Assert.assertEquals(51, event.getEndTime());
				Assert.assertEquals(dischargeElement, event.getSequenceElement());
			}

			{

				final IJourneyEvent journeyEvent = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
				Assert.assertNotNull(journeyEvent);

				Assert.assertEquals(24 * 12, journeyEvent.getDistance());
				Assert.assertEquals(24, journeyEvent.getDuration());
				Assert.assertEquals(51, journeyEvent.getStartTime());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyEvent.getVesselState());

				Assert.assertEquals(12000, journeyEvent.getSpeed());

				Assert.assertEquals(24 * ballast_nboRateInM3PerHour / 24L, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals((24L * ballast_nboRateInM3PerHour * cargoCVValue / baseFuelEquivalence) / 24L, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(24L * ballast_nboRateInM3PerHour * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
				// Assert.assertEquals(25 * 1000 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(24 * ballast_nboRateInM3PerHour * 2 * 5 / 24L, journeyEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));

				final IIdleEvent idleEvent = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
				Assert.assertNotNull(idleEvent);
				Assert.assertSame(endElement, idleEvent.getSequenceElement());
				Assert.assertSame(port4, idleEvent.getPort());
				Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
				Assert.assertEquals(0, idleEvent.getDuration());
				Assert.assertEquals(75, idleEvent.getStartTime());
				Assert.assertEquals(75, idleEvent.getEndTime());

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleBase));

				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(0, event.getDuration());
				Assert.assertEquals(75, event.getStartTime());
				Assert.assertEquals(75, event.getEndTime());
				Assert.assertEquals(endElement, event.getSequenceElement());
			}
		}
	}

	/**
	 * Like case 1, but force a higher travelling speed to get idle time + FBO
	 */
	@SuppressWarnings("unused")
	@Test
	public void testCalculations2() {

		final IVolumeAllocator volumeAllocator = Mockito.mock(IVolumeAllocator.class);
		final Injector injector = createTestInjector(volumeAllocator);

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);) {
			scope.enter();

			final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

			final IPort port1 = builder.createPortForTest("port-1", false, null, "UTC");
			final IPort port2 = builder.createPortForTest("port-2", false, null, "UTC");
			final IPort port3 = builder.createPortForTest("port-3", false, null, "UTC");
			final IPort port4 = builder.createPortForTest("port-4", false, null, "UTC");

			final int minSpeed = 16000;
			final int maxSpeed = 20000;
			final int capacity = 150000000;
			final int baseFuelUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(400);

			// 2 / 4 == 0.5 equivalence
			final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(2.0);
			final int baseFuelUnitEquivalence = OptimiserUnitConvertor.convertToInternalConversionFactor(4.0);
			final IBaseFuel baseFuel = new BaseFuel("test");
			baseFuel.setEquivalenceFactor(baseFuelUnitEquivalence);

			final IVesselClass vesselClass1 = builder.createVesselClass("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuel, 0, Integer.MAX_VALUE, 0, 0, false);

			final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<Integer, Long>();
			ladenKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.6));
			ladenKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.4));
			final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ladenKeypoints);

			final int laden_nboRateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.2);
			final int laden_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int laden_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.5);

			builder.setVesselClassStateParameters(vesselClass1, VesselState.Laden, laden_nboRateInM3PerHour, laden_idleNBORateInM3PerHour, laden_idleConsumptionRateInMTPerHour,
					ladenConsumptionCalculator, 0);

			final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<Integer, Long>();
			ballastKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.5));
			ballastKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.3));
			final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ballastKeypoints);

			final int ballast_nboRateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int ballast_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.8);
			final int ballast_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.4);

			builder.setVesselClassStateParameters(vesselClass1, VesselState.Ballast, ballast_nboRateInM3PerHour, ballast_idleNBORateInM3PerHour, ballast_idleConsumptionRateInMTPerHour,
					ballastConsumptionCalculator, 0);

			final IStartRequirement startRequirement = builder.createStartRequirement(port1, builder.createTimeWindow(0, 0), null);
			final IEndRequirement endRequirement = builder.createEndRequirement(Collections.singleton(port4), builder.createTimeWindow(75, 75), false, 0, false);

			final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, capacity);
			final IVesselAvailability vesselAvailability1 = builder.createVesselAvailability(vessel1, new ConstantValueLongCurve(0), VesselInstanceType.FLEET, startRequirement, endRequirement);

			final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
			final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2, loadWindow, 0, 150000000, new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), cargoCVValue, 1,
					false, false, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);

			final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
			final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-1", port3, dischargeWindow, 0, 150000000, 0, Long.MAX_VALUE,
					new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), 1, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false,
					DEFAULT_VOLUME_LIMIT_IS_M3);

			final ICargo cargo1 = builder.createCargo(Lists.newArrayList(loadSlot, dischargeSlot), false);

			builder.setPortToPortDistance(port1, port2, ERouteOption.DIRECT, 12 * 25);
			builder.setPortToPortDistance(port2, port3, ERouteOption.DIRECT, 12 * 25);
			builder.setPortToPortDistance(port3, port4, ERouteOption.DIRECT, 12 * 25);

			final IOptimisationData data = builder.getOptimisationData();

			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IStartEndRequirementProvider startEndProvider = injector.getInstance(IStartEndRequirementProvider.class);
			final VoyagePlanAnnotator annotator = injector.getInstance(VoyagePlanAnnotator.class);

			final ScheduleCalculator scheduler = injector.getInstance(ScheduleCalculator.class);

			final IResource resource = vesselProvider.getResource(vesselAvailability1);

			final ISequenceElement startElement = startEndProvider.getStartElement(resource);
			final ISequenceElement endElement = startEndProvider.getEndElement(resource);

			final ISequenceElement dischargeElement = portSlotProvider.getElement(dischargeSlot);
			final ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
			final List<ISequenceElement> sequenceList = CollectionsUtil.makeArrayList(startElement, loadElement, dischargeElement, endElement);

			final ISequence sequence = new ListSequence(sequenceList);
			final ISequences sequences = new Sequences(Collections.singletonList(resource), CollectionsUtil.<IResource, ISequence> makeHashMap(resource, sequence));

			final IAllocationAnnotation allocationAnnotation = Mockito.mock(IAllocationAnnotation.class);
			{
				final PortTimesRecord expectedPTR = new PortTimesRecord();
				expectedPTR.setSlotTime(loadSlot, 25);
				expectedPTR.setSlotTime(dischargeSlot, 50);
				expectedPTR.setSlotDuration(loadSlot, 1);
				expectedPTR.setSlotDuration(dischargeSlot, 1);
				expectedPTR.setReturnSlotTime(portSlotProvider.getPortSlot(endElement), 75);

				Mockito.when(volumeAllocator.allocate(Matchers.<IVesselAvailability> any(), Matchers.anyInt(), Matchers.<VoyagePlan> any(), Matchers.<IPortTimesRecord> eq(expectedPTR)))
						.thenReturn(allocationAnnotation);
			}

			Mockito.when(allocationAnnotation.getSlotTime(loadSlot)).thenReturn(25);
			Mockito.when(allocationAnnotation.getSlotDuration(loadSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(dischargeSlot)).thenReturn(50);
			Mockito.when(allocationAnnotation.getSlotDuration(dischargeSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(portSlotProvider.getPortSlot(endElement))).thenReturn(75);

			Mockito.when(allocationAnnotation.getRemainingHeelVolumeInM3()).thenReturn(0L);
			// Load enough to cover boil-off
			Mockito.when(allocationAnnotation.getSlotVolumeInM3(loadSlot)).thenReturn(3300L);
			Mockito.when(allocationAnnotation.getSlotVolumeInM3(dischargeSlot)).thenReturn(0L);
			Mockito.when(allocationAnnotation.getSlotCargoCV(Matchers.<IPortSlot> any())).thenReturn(cargoCVValue);

			// Schedule sequence
			final int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
			final IEvaluationContext context = Mockito.mock(IEvaluationContext.class);
			final IEvaluationState state = Mockito.mock(IEvaluationState.class);

			final AnnotatedSolution annotatedSolution = new AnnotatedSolution(sequences, context, state);

			final VolumeAllocatedSequences volumeAllocatedSequences = scheduler.schedule(sequences, new int[][] { expectedArrivalTimes }, annotatedSolution);
			Assert.assertNotNull(volumeAllocatedSequences);

			// TODO: Start checking results
			{
				Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
				Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class));
				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(0, event.getDuration());
				Assert.assertEquals(1, event.getStartTime());
				Assert.assertEquals(1, event.getEndTime());
				Assert.assertEquals(startElement, event.getSequenceElement());
			}

			{

				final IJourneyEvent journeyEvent = annotatedSolution.getElementAnnotations().getAnnotation(loadElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
				Assert.assertNotNull(journeyEvent);

				Assert.assertEquals(1, journeyEvent.getStartTime());
				Assert.assertEquals(25 * 12, journeyEvent.getDistance());
				Assert.assertEquals(18, journeyEvent.getDuration());

				Assert.assertEquals(16000, journeyEvent.getSpeed());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyEvent.getVesselState());

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(900 * 18 / 24, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals((900L * 18L / 24 * baseFuelUnitPrice) / Calculator.HighScaleFactor, journeyEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));

				final IIdleEvent idleEvent = annotatedSolution.getElementAnnotations().getAnnotation(loadElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
				Assert.assertNotNull(idleEvent);
				Assert.assertSame(loadElement, idleEvent.getSequenceElement());
				Assert.assertSame(port2, idleEvent.getPort());
				Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
				Assert.assertEquals(6, idleEvent.getDuration());
				Assert.assertEquals(19, idleEvent.getStartTime());
				Assert.assertEquals(25, idleEvent.getEndTime());

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(6 * 400 / 24L, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals((6L * 400 * baseFuelUnitPrice) / 24L / Calculator.HighScaleFactor, idleEvent.getFuelCost(FuelComponent.IdleBase));

				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(loadElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(1, event.getDuration());
				Assert.assertEquals(25, event.getStartTime());
				Assert.assertEquals(26, event.getEndTime());
				Assert.assertEquals(loadElement, event.getSequenceElement());
			}

			{

				final IJourneyEvent journeyEvent = annotatedSolution.getElementAnnotations().getAnnotation(dischargeElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
				Assert.assertNotNull(journeyEvent);

				Assert.assertEquals(25 * 12, journeyEvent.getDistance());
				Assert.assertEquals(18, journeyEvent.getDuration());
				Assert.assertEquals(26, journeyEvent.getStartTime());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Laden, journeyEvent.getVesselState());

				Assert.assertEquals(16000, journeyEvent.getSpeed());

				Assert.assertEquals(18 * laden_nboRateInM3PerHour / 24L, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals((18L * laden_nboRateInM3PerHour * cargoCVValue / baseFuelUnitEquivalence) / 24L, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(18L * laden_nboRateInM3PerHour * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
				// Assert.assertEquals(25 * 1200 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));

				Assert.assertEquals(18 * 800 / 24L, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals((18L * 800 * cargoCVValue / baseFuelUnitEquivalence) / 24L, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(18L * 800 * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(18L * 1200 * 2 * 5 / 24L, journeyEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(18 * 800 * 2 * 5 / 24L, journeyEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));

				final IIdleEvent idleEvent = annotatedSolution.getElementAnnotations().getAnnotation(dischargeElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
				Assert.assertNotNull(idleEvent);
				Assert.assertSame(dischargeElement, idleEvent.getSequenceElement());
				Assert.assertSame(port3, idleEvent.getPort());
				Assert.assertSame(VesselState.Laden, idleEvent.getVesselState());
				Assert.assertEquals(6, idleEvent.getDuration());
				Assert.assertEquals(44, idleEvent.getStartTime());
				Assert.assertEquals(50, idleEvent.getEndTime());

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(6 * 1000 / 24L, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals((6L * 1000 * 500) / 24L / Calculator.ScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(6L * 1000 * cargoCVValue / 24L / Calculator.HighScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(6 * 1000 * 2 * 5 / 24L, idleEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleBase));

				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(dischargeElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(1, event.getDuration());
				Assert.assertEquals(50, event.getStartTime());
				Assert.assertEquals(51, event.getEndTime());
				Assert.assertEquals(dischargeElement, event.getSequenceElement());
			}

			{

				final IJourneyEvent journeyEvent = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
				Assert.assertNotNull(journeyEvent);

				Assert.assertEquals(25 * 12, journeyEvent.getDistance());
				Assert.assertEquals(18, journeyEvent.getDuration());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyEvent.getVesselState());

				Assert.assertEquals(16000, journeyEvent.getSpeed());

				Assert.assertEquals(18 * 1000 / 24L, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals((18L * 1000L * 500) / 24L / Calculator.ScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(18L * 1000 * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
				// Assert.assertEquals(25 * 1000 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));
				//
				Assert.assertEquals(18 * 800 / 24L, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals((18L * 800L * 500) / 24L / Calculator.ScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(18L * 800 * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(18 * 1000 * 2 * 5 / 24L, journeyEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(18 * 800 * 2 * 5 / 24L, journeyEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));

				final IIdleEvent idleEvent = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
				Assert.assertNotNull(idleEvent);
				Assert.assertSame(endElement, idleEvent.getSequenceElement());
				Assert.assertSame(port4, idleEvent.getPort());
				Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
				Assert.assertEquals(6, idleEvent.getDuration());
				Assert.assertEquals(69, idleEvent.getStartTime());
				Assert.assertEquals(75, idleEvent.getEndTime());

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(6 * 800 / 24L, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals((6 * 800 * 500) / 24L / Calculator.ScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(6L * 800 * cargoCVValue / 24L / Calculator.HighScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(6 * 800 * 2 * 5 / 24L, idleEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleBase));

				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(0, event.getDuration());
				Assert.assertEquals(75, event.getStartTime());
				Assert.assertEquals(75, event.getEndTime());
				Assert.assertEquals(endElement, event.getSequenceElement());
			}
		}
	}

	/**
	 * Like case 1, but force a higher travelling speed to get idle time + Base_Supplemental (make NBO more costly than Base)
	 */
	@SuppressWarnings("unused")
	@Test
	public void testCalculations3() {

		final IVolumeAllocator volumeAllocator = Mockito.mock(IVolumeAllocator.class);
		final int baseFuelUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(1);
		final Injector injector = createTestInjector(volumeAllocator, baseFuelUnitPrice);

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);) {
			scope.enter();

			final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

			final IPort port1 = builder.createPortForTest("port-1", false, null, "UTC");
			final IPort port2 = builder.createPortForTest("port-2", false, null, "UTC");
			final IPort port3 = builder.createPortForTest("port-3", false, null, "UTC");
			final IPort port4 = builder.createPortForTest("port-4", false, null, "UTC");

			final int minSpeed = 16000;
			final int maxSpeed = 20000;
			final int capacity = 150000000;

			// 2 / 4 == 0.5 equivalence
			final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(2.0);
			final int baseFuelEquivalence = OptimiserUnitConvertor.convertToInternalConversionFactor(4);

			final IBaseFuel baseFuel = new BaseFuel("test");
			baseFuel.setEquivalenceFactor(baseFuelEquivalence);
			final IVesselClass vesselClass1 = builder.createVesselClass("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuel, 0, Integer.MAX_VALUE, 0, 0, false);

			final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<Integer, Long>();
			ladenKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.6));
			ladenKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.4));
			final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ladenKeypoints);

			final int laden_nboRateInM3PerDay = OptimiserUnitConvertor.convertToInternalDailyRate(1.2);
			final int laden_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int laden_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.5);

			builder.setVesselClassStateParameters(vesselClass1, VesselState.Laden, laden_nboRateInM3PerDay, laden_idleNBORateInM3PerHour, laden_idleConsumptionRateInMTPerHour,
					ladenConsumptionCalculator, 0);

			final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<Integer, Long>();
			ballastKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.5));
			ballastKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.3));
			final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ballastKeypoints);

			final int ballast_nboRateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int ballast_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.8);
			final int ballast_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.4);
			builder.setVesselClassStateParameters(vesselClass1, VesselState.Ballast, ballast_nboRateInM3PerHour, ballast_idleNBORateInM3PerHour, ballast_idleConsumptionRateInMTPerHour,
					ballastConsumptionCalculator, 0);

			final IStartRequirement startRequirement = builder.createStartRequirement(port1, builder.createTimeWindow(0, 0), null);
			final IEndRequirement endRequirement = builder.createEndRequirement(Collections.singleton(port4), builder.createTimeWindow(75, 75), false, 0, false);

			final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, capacity);
			final IVesselAvailability vesselAvailability1 = builder.createVesselAvailability(vessel1, new ConstantValueLongCurve(0), VesselInstanceType.FLEET, startRequirement, endRequirement);

			final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
			final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2, loadWindow, 0, 150000000, new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), cargoCVValue, 1,
					false, false, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);

			final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
			final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-1", port3, dischargeWindow, 0, 150000000, 0, Long.MAX_VALUE,
					new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(200)), 1, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false,
					DEFAULT_VOLUME_LIMIT_IS_M3);

			final ICargo cargo1 = builder.createCargo(Lists.newArrayList(loadSlot, dischargeSlot), false);

			builder.setPortToPortDistance(port1, port2, ERouteOption.DIRECT, 12 * 25);
			builder.setPortToPortDistance(port2, port3, ERouteOption.DIRECT, 12 * 25);
			builder.setPortToPortDistance(port3, port4, ERouteOption.DIRECT, 12 * 25);

			final IOptimisationData data = builder.getOptimisationData();

			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IStartEndRequirementProvider startEndProvider = injector.getInstance(IStartEndRequirementProvider.class);

			final ScheduleCalculator scheduler = injector.getInstance(ScheduleCalculator.class);

			final IResource resource = vesselProvider.getResource(vesselAvailability1);

			final ISequenceElement startElement = startEndProvider.getStartElement(resource);
			final ISequenceElement endElement = startEndProvider.getEndElement(resource);

			final ISequenceElement dischargeElement = portSlotProvider.getElement(dischargeSlot);
			final ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
			final List<ISequenceElement> sequenceList = CollectionsUtil.makeArrayList(startElement, loadElement, dischargeElement, endElement);

			final ISequence sequence = new ListSequence(sequenceList);

			final IAllocationAnnotation allocationAnnotation = Mockito.mock(IAllocationAnnotation.class);
			{
				final PortTimesRecord expectedPTR = new PortTimesRecord();
				expectedPTR.setSlotTime(loadSlot, 25);
				expectedPTR.setSlotTime(dischargeSlot, 50);
				expectedPTR.setSlotDuration(loadSlot, 1);
				expectedPTR.setSlotDuration(dischargeSlot, 1);
				expectedPTR.setReturnSlotTime(portSlotProvider.getPortSlot(endElement), 75);

				Mockito.when(volumeAllocator.allocate(Matchers.<IVesselAvailability> any(), Matchers.anyInt(), Matchers.<VoyagePlan> any(), Matchers.<IPortTimesRecord> eq(expectedPTR)))
						.thenReturn(allocationAnnotation);
			}

			Mockito.when(allocationAnnotation.getSlotTime(loadSlot)).thenReturn(25);
			Mockito.when(allocationAnnotation.getSlotDuration(loadSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(dischargeSlot)).thenReturn(50);
			Mockito.when(allocationAnnotation.getSlotDuration(dischargeSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(portSlotProvider.getPortSlot(endElement))).thenReturn(75);

			Mockito.when(allocationAnnotation.getRemainingHeelVolumeInM3()).thenReturn(0L);
			// Load enough to cover boil-off
			Mockito.when(allocationAnnotation.getSlotVolumeInM3(loadSlot)).thenReturn(1150L);
			Mockito.when(allocationAnnotation.getSlotVolumeInM3(dischargeSlot)).thenReturn(0L);

			// Schedule sequence
			final int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
			final ISequences sequences = new Sequences(Collections.singletonList(resource), CollectionsUtil.<IResource, ISequence> makeHashMap(resource, sequence));

			final IEvaluationContext context = Mockito.mock(IEvaluationContext.class);
			final IEvaluationState state = Mockito.mock(IEvaluationState.class);

			final AnnotatedSolution annotatedSolution = new AnnotatedSolution(sequences, context, state);

			final VolumeAllocatedSequences volumeAllocatedSequences = scheduler.schedule(sequences, new int[][] { expectedArrivalTimes }, annotatedSolution);
			Assert.assertNotNull(volumeAllocatedSequences);
			// TODO: Start checking results
			{
				Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
				Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class));
				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(0, event.getDuration());
				Assert.assertEquals(1, event.getStartTime());
				Assert.assertEquals(1, event.getEndTime());
				Assert.assertEquals(startElement, event.getSequenceElement());
			}

			{

				final IJourneyEvent journeyEvent = annotatedSolution.getElementAnnotations().getAnnotation(loadElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
				Assert.assertNotNull(journeyEvent);

				Assert.assertEquals(25 * 12, journeyEvent.getDistance());
				Assert.assertEquals(18, journeyEvent.getDuration());
				Assert.assertEquals(1, journeyEvent.getStartTime());

				Assert.assertEquals(16000, journeyEvent.getSpeed());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyEvent.getVesselState());

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(900 * 18 / 24, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals((900L * 18L / 24 * baseFuelUnitPrice) / Calculator.HighScaleFactor, journeyEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));

				final IIdleEvent idleEvent = annotatedSolution.getElementAnnotations().getAnnotation(loadElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
				Assert.assertNotNull(idleEvent);
				Assert.assertSame(loadElement, idleEvent.getSequenceElement());
				Assert.assertSame(port2, idleEvent.getPort());
				Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
				Assert.assertEquals(6, idleEvent.getDuration());
				Assert.assertEquals(19, idleEvent.getStartTime());
				Assert.assertEquals(25, idleEvent.getEndTime());

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(6 * 400 / 24L, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals((6L * 400 * baseFuelUnitPrice) / 24L / Calculator.HighScaleFactor, idleEvent.getFuelCost(FuelComponent.IdleBase));

				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(loadElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(1, event.getDuration());
				Assert.assertEquals(25, event.getStartTime());
				Assert.assertEquals(26, event.getEndTime());
				Assert.assertEquals(loadElement, event.getSequenceElement());

				Assert.assertTrue(event instanceof ILoadEvent);
			}

			{

				final IJourneyEvent journeyEvent = annotatedSolution.getElementAnnotations().getAnnotation(dischargeElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
				Assert.assertNotNull(journeyEvent);

				Assert.assertEquals(25 * 12, journeyEvent.getDistance());
				Assert.assertEquals(18, journeyEvent.getDuration());
				Assert.assertEquals(26, journeyEvent.getStartTime());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Laden, journeyEvent.getVesselState());

				Assert.assertEquals(16000, journeyEvent.getSpeed());

				Assert.assertEquals(18 * 1200 / 24L, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals((18L * 1200L * 500) / 24L / Calculator.ScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				// Not yet set
				Assert.assertEquals(18L * laden_nboRateInM3PerDay * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
				// Assert.assertEquals(25 * 1200 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(18 * 1200 * 2 * 200 / 24L, journeyEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(18 * 400 / 24L, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals((18L * 400 * baseFuelUnitPrice) / 24L / Calculator.HighScaleFactor, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));

				final IIdleEvent idleEvent = annotatedSolution.getElementAnnotations().getAnnotation(dischargeElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
				Assert.assertNotNull(idleEvent);
				Assert.assertSame(dischargeElement, idleEvent.getSequenceElement());
				Assert.assertSame(port3, idleEvent.getPort());
				Assert.assertSame(VesselState.Laden, idleEvent.getVesselState());
				Assert.assertEquals(6, idleEvent.getDuration());
				Assert.assertEquals(44, idleEvent.getStartTime());
				Assert.assertEquals(50, idleEvent.getEndTime());

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(6 * 1000 / 24L, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals((6L * 1000 * 500) / 24L / Calculator.ScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(6L * 1000 * cargoCVValue / 24L / Calculator.HighScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(6 * 1000 * 2 * 200 / 24L, idleEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleBase));

				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(dischargeElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(1, event.getDuration());
				Assert.assertEquals(50, event.getStartTime());
				Assert.assertEquals(51, event.getEndTime());
				Assert.assertEquals(dischargeElement, event.getSequenceElement());

				// Assert.assertTrue(event instanceof IDischargeEvent);
				//
				// final IDischargeEvent discharge = (IDischargeEvent) event;
				// Assert.assertEquals(loadSlot.getMaxLoadVolume() - (18 * 1200 + 6 * 1000), discharge.getDischargeVolume());
				//
				// Assert.assertEquals((loadSlot.getMaxLoadVolume() - (18 * 1200 + 6 * 1000)) * 2 * 200, discharge.getSalesPrice());
			}

			{

				final IJourneyEvent journeyEvent = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
				Assert.assertNotNull(journeyEvent);

				Assert.assertEquals(25 * 12, journeyEvent.getDistance());
				Assert.assertEquals(18, journeyEvent.getDuration());
				Assert.assertEquals(51, journeyEvent.getStartTime());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyEvent.getVesselState());

				Assert.assertEquals(16000, journeyEvent.getSpeed());

				// Why still nbo? NEED TO CHECK vpo COST CALS. -- Basefuel should be
				// the cheaper option!
				// -- is base fuel being evaluated or is the choice not being
				// allowed?

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				// Not yet set
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
				// Assert.assertEquals(25 * 1000 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));
				//
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(18 * 900 / 24L, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals((18L * 900 * baseFuelUnitPrice) / 24L / Calculator.HighScaleFactor, journeyEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));

				final IIdleEvent idleEvent = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
				Assert.assertNotNull(idleEvent);
				Assert.assertSame(endElement, idleEvent.getSequenceElement());
				Assert.assertSame(port4, idleEvent.getPort());
				Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
				Assert.assertEquals(6, idleEvent.getDuration());
				Assert.assertEquals(69, idleEvent.getStartTime());
				Assert.assertEquals(75, idleEvent.getEndTime());

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
				Assert.assertEquals(6 * 400 / 24L, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
				Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
				Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
				Assert.assertEquals((6L * 400 * baseFuelUnitPrice) / 24L / Calculator.HighScaleFactor, idleEvent.getFuelCost(FuelComponent.IdleBase));

				final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				Assert.assertNotNull(event);
				Assert.assertEquals(0, event.getDuration());
				Assert.assertEquals(75, event.getStartTime());
				Assert.assertEquals(75, event.getEndTime());
				Assert.assertEquals(endElement, event.getSequenceElement());
			}
		}
	}

	private Injector createTestInjector(final IVolumeAllocator volumeAllocator, final int baseFuelUnitPrice) {

		final Injector injector = Guice.createInjector(new PerChainUnitScopeModule(), new DataComponentProviderModule(), new AbstractModule() {

			@Provides
			@Singleton
			private ILatenessComponentParameters provideLatenessComponentParameters() {
				final LatenessComponentParameters lcp = new LatenessComponentParameters();

				lcp.setThreshold(Interval.PROMPT, 48);
				lcp.setLowWeight(Interval.PROMPT, 250000);
				lcp.setHighWeight(Interval.PROMPT, 1000000);

				lcp.setThreshold(Interval.MID_TERM, 72);
				lcp.setLowWeight(Interval.MID_TERM, 250000);
				lcp.setHighWeight(Interval.MID_TERM, 1000000);

				lcp.setThreshold(Interval.BEYOND, 72);
				lcp.setLowWeight(Interval.BEYOND, 250000);
				lcp.setHighWeight(Interval.BEYOND, 1000000);

				return lcp;
			}

			@Provides
			@Singleton
			private IExcessIdleTimeComponentParameters provideIdleComponentParameters() {
				final ExcessIdleTimeComponentParameters idleParams = new ExcessIdleTimeComponentParameters();
				int highPeriodInDays = 15;
				int lowPeriodInDays = Math.max(0, highPeriodInDays - 2);
				idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, lowPeriodInDays * 24);
				idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, highPeriodInDays * 24);
				idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, 2_500);
				idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, 10_000);
				idleParams.setEndWeight(10_000);

				return idleParams;
			}

			@Override
			protected void configure() {

				bind(VoyagePlanAnnotator.class);
				bind(VoyagePlanner.class);
				bind(ScheduleCalculator.class);
				bind(ICharterRateCalculator.class).to(VesselStartDateCharterRateCalculator.class);
				bind(IVolumeAllocator.class).toInstance(volumeAllocator);
				bind(SchedulerBuilder.class);
				bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
				bind(IVoyagePlanOptimiser.class).to(VoyagePlanOptimiser.class);

				bind(IEndEventScheduler.class).to(DefaultEndEventScheduler.class);
				
				bind(IVesselBaseFuelCalculator.class).to(VesselBaseFuelCalculator.class);
				final VesselBaseFuelCalculator baseFuelCalculator = Mockito.mock(VesselBaseFuelCalculator.class);
				Mockito.when(baseFuelCalculator.getBaseFuelPrice(Matchers.any(IVessel.class), Matchers.any(IPortTimesRecord.class))).thenReturn(baseFuelUnitPrice);
				Mockito.when(baseFuelCalculator.getBaseFuelPrice(Matchers.any(IVessel.class), Matchers.anyInt())).thenReturn(baseFuelUnitPrice);

				bind(VesselBaseFuelCalculator.class).toInstance(baseFuelCalculator);

				bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocationCache)).toInstance(Boolean.FALSE);
				bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocatedSequenceCache)).toInstance(Boolean.FALSE);
				bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_ProfitandLossCache)).toInstance(Boolean.FALSE);

			}
		});
		return injector;
	}

	private Injector createTestInjector(final IVolumeAllocator volumeAllocator) {
		return createTestInjector(volumeAllocator, OptimiserUnitConvertor.convertToInternalPrice(400));
	}
}
