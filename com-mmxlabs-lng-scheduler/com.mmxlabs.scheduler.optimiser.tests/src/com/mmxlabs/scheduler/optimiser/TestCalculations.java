/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
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
import com.mmxlabs.scheduler.optimiser.builder.impl.TimeWindowMaker;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselStartDateCharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.ExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.DefaultEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.GeneralTestUtils;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanner;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.scheduling.IArrivalTimeScheduler;
import com.mmxlabs.scheduler.optimiser.shared.SharedDataModule;
import com.mmxlabs.scheduler.optimiser.shared.SharedPortDistanceDataBuilder;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
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
		final SimpleIndexingContext indexingContext = new SimpleIndexingContext();
		indexingContext.registerType(IBaseFuel.class);
		indexingContext.assignIndex(IBaseFuel.LNG);

		final IVolumeAllocator volumeAllocator = Mockito.mock(IVolumeAllocator.class);
		final int baseFuelUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(400);

		final IBaseFuel baseFuel = new BaseFuel(indexingContext, "test");

		final int[] baseFuelPrices = GeneralTestUtils.makeBaseFuelPrices(baseFuelUnitPrice);

		final Injector injector = createTestInjector(volumeAllocator, baseFuelPrices);

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);) {
			scope.enter();

			final IVesselBaseFuelCalculator v = injector.getInstance(IVesselBaseFuelCalculator.class);

			final SharedPortDistanceDataBuilder portBuilder = injector.getInstance(SharedPortDistanceDataBuilder.class);

			final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

			final IPort port1 = portBuilder.createPort("port-1", "port-1", "UTC");
			final IPort port2 = portBuilder.createPort("port-2", "port-2", "UTC");
			final IPort port3 = portBuilder.createPort("port-3", "port-3", "UTC");
			final IPort port4 = portBuilder.createPort("port-4", "port-4", "UTC");

			final int minSpeed = 12000;
			final int maxSpeed = 20000;
			final int capacity = 150000000;
			// 2 / 4 == 0.5 equivalence
			final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(2.0);
			final int baseFuelEquivalence = OptimiserUnitConvertor.convertToInternalConversionFactor(4);
			baseFuel.setEquivalenceFactor(baseFuelEquivalence);
			final IVessel vessel1 = builder.createVessel("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuel, baseFuel, baseFuel, baseFuel, 0, Integer.MAX_VALUE, 0, 0, false);

			final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<>();
			ladenKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.6));
			ladenKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.4));
			final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ladenKeypoints);
			final int laden_nboRateInM3PerDay = OptimiserUnitConvertor.convertToInternalDailyRate(1.2);
			final int laden_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int laden_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.5);
			final int laden_inPortNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(48);
			builder.setVesselStateParameters(vessel1, VesselState.Laden, laden_nboRateInM3PerDay, laden_idleNBORateInM3PerHour, laden_idleConsumptionRateInMTPerHour, ladenConsumptionCalculator, 0,
					laden_inPortNBORateInM3PerHour);
			final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<>();
			ballastKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.5));
			ballastKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.3));
			final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ballastKeypoints);

			final int ballast_nboRateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int ballast_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.8);
			final int ballast_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.4);
			final int ballast_inPortNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(24);
			builder.setVesselStateParameters(vessel1, VesselState.Ballast, ballast_nboRateInM3PerHour, ballast_idleNBORateInM3PerHour, ballast_idleConsumptionRateInMTPerHour,
					ballastConsumptionCalculator, 0, ballast_inPortNBORateInM3PerHour);
			final IStartRequirement startRequirement = builder.createStartRequirement(port1, true, TimeWindowMaker.createInclusiveInclusive(0, 0), null);

			IHeelOptionConsumer heelOptionConsumer = new HeelOptionConsumer(0, 0, VesselTankState.MUST_BE_WARM, new ConstantHeelPriceCalculator(0));
			final IEndRequirement endRequirement = builder.createEndRequirement(Collections.singleton(port4), true, TimeWindowMaker.createInclusiveInclusive(75, 75), heelOptionConsumer, false);

			final IVesselAvailability vesselAvailability1 = builder.createVesselAvailability(vessel1, new ConstantValueLongCurve(0), VesselInstanceType.FLEET, startRequirement, endRequirement, null,
					new ConstantValueLongCurve(0), false);

			final ITimeWindow loadWindow = TimeWindowMaker.createInclusiveInclusive(25, 25, 0, false);
			final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2, loadWindow, 0, 150000000, new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), cargoCVValue, 1,
					false, false, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);

			final ITimeWindow dischargeWindow = TimeWindowMaker.createInclusiveInclusive(50, 50);
			final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-1", port3, dischargeWindow, 0, 150000000, 0, Long.MAX_VALUE,
					new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), 1, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false,
					DEFAULT_VOLUME_LIMIT_IS_M3);

			final ICargo cargo1 = builder.createCargo(Lists.newArrayList(loadSlot, dischargeSlot), false);

			portBuilder.setPortToPortDistance(port1, port2, ERouteOption.DIRECT, 12 * 24);
			portBuilder.setPortToPortDistance(port2, port3, ERouteOption.DIRECT, 12 * 24);
			portBuilder.setPortToPortDistance(port3, port4, ERouteOption.DIRECT, 12 * 24);

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
			final PortTimesRecord expectedPTR1 = new PortTimesRecord();
			{
				expectedPTR1.setSlotTime(portSlotProvider.getPortSlot(startElement), 1);
				expectedPTR1.setSlotDuration(portSlotProvider.getPortSlot(startElement), 0);
				expectedPTR1.setReturnSlotTime(loadSlot, 25);
			}

			final PortTimesRecord expectedPTR2 = new PortTimesRecord();
			{
				expectedPTR2.setSlotTime(loadSlot, 25);
				expectedPTR2.setSlotTime(dischargeSlot, 50);
				expectedPTR2.setSlotDuration(loadSlot, 1);
				expectedPTR2.setSlotDuration(dischargeSlot, 1);
				expectedPTR2.setReturnSlotTime(portSlotProvider.getPortSlot(endElement), 75);

				Mockito.when(volumeAllocator.allocate(Matchers.<IVesselAvailability> any(), Matchers.anyInt(), Matchers.<VoyagePlan> any(), Matchers.<IPortTimesRecord> eq(expectedPTR2)))
						.thenReturn(allocationAnnotation);
			}

			Mockito.when(allocationAnnotation.getSlotCargoCV(loadSlot)).thenReturn(2_000_000);
			Mockito.when(allocationAnnotation.getSlotCargoCV(dischargeSlot)).thenReturn(2_000_000);
			Mockito.when(allocationAnnotation.getSlotTime(loadSlot)).thenReturn(25);
			Mockito.when(allocationAnnotation.getSlotDuration(loadSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(dischargeSlot)).thenReturn(50);
			Mockito.when(allocationAnnotation.getSlotDuration(dischargeSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(portSlotProvider.getPortSlot(endElement))).thenReturn(75);

			Mockito.when(allocationAnnotation.getRemainingHeelVolumeInM3()).thenReturn(0L);
			// Load enough to cover boil-off
			Mockito.when(allocationAnnotation.getCommercialSlotVolumeInM3(loadSlot)).thenReturn(5200L);
			Mockito.when(allocationAnnotation.getPhysicalSlotVolumeInM3(loadSlot)).thenReturn(3200L);
			Mockito.when(allocationAnnotation.getCommercialSlotVolumeInM3(dischargeSlot)).thenReturn(0L);
			Mockito.when(allocationAnnotation.getPhysicalSlotVolumeInM3(dischargeSlot)).thenReturn(0L);

			Mockito.when(allocationAnnotation.getFuelVolumeInM3()).thenReturn(5200L);

			// Schedule sequence
			final int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
			final ISequences sequences = new Sequences(Collections.singletonList(resource), CollectionsUtil.<IResource, ISequence> makeHashMap(resource, sequence));

			final IEvaluationContext context = Mockito.mock(IEvaluationContext.class);
			final IEvaluationState state = Mockito.mock(IEvaluationState.class);

			final AnnotatedSolution annotatedSolution = new AnnotatedSolution(sequences, state);

			// try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			// scope.enter();
			final ScheduleCalculator scheduler = injector.getInstance(ScheduleCalculator.class);

			Map<IResource, List<IPortTimesRecord>> allPortTimeRecords = CollectionsUtil.makeHashMap(resource, Lists.newArrayList(expectedPTR1, expectedPTR2));

			final VolumeAllocatedSequences volumeAllocatedSequences = scheduler.schedule(sequences, allPortTimeRecords, annotatedSolution);
			final VolumeAllocatedSequence volumeAllocatedSequence = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
			// }
			Assert.assertNotNull(volumeAllocatedSequences);
			// TODO: Start checking results
			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(startElement);
				Assert.assertEquals(1, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(0, volumeAllocatedSequence.getVisitDuration(portSlot));
			}

			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(startElement);

				final VoyageDetails journeyDetails = volumeAllocatedSequence.getVoyageDetailsFrom(portSlot);
				Assert.assertNotNull(journeyDetails);

				Assert.assertEquals(24 * 12, journeyDetails.getOptions().getDistance());
				Assert.assertEquals(24, journeyDetails.getTravelTime());

				Assert.assertEquals(12000, journeyDetails.getSpeed());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyDetails.getOptions().getVesselState());

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_mmBtu));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_mmBtu));

				Assert.assertEquals(500, journeyDetails.getFuelConsumption(vessel1.getTravelBaseFuelInMT()));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getSupplementalTravelBaseFuelInMT()));

				Assert.assertEquals(baseFuelUnitPrice, journeyDetails.getFuelUnitPrice(FuelComponent.Base));

				Assert.assertSame(port2, journeyDetails.getOptions().getToPortSlot().getPort());
				Assert.assertEquals(0, journeyDetails.getIdleTime());

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_mmBtu));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getIdleBaseFuelInMT()));
			}
			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(loadElement);

				Assert.assertEquals(25, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(1, volumeAllocatedSequence.getVisitDuration(portSlot));
			}

			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(loadElement);

				final VoyageDetails journeyDetails = volumeAllocatedSequence.getVoyageDetailsFrom(portSlot);
				Assert.assertNotNull(journeyDetails);
				Assert.assertEquals(24 * 12, journeyDetails.getOptions().getDistance());
				Assert.assertEquals(24, journeyDetails.getTravelTime());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Laden, journeyDetails.getOptions().getVesselState());

				Assert.assertEquals(12000, journeyDetails.getSpeed());

				Assert.assertEquals(laden_nboRateInM3PerDay, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
				Assert.assertEquals((long) laden_nboRateInM3PerDay * (long) cargoCVValue / Calculator.HighScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_mmBtu));
				// Assert.assertEquals(25 * 1200 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_mmBtu));

				Assert.assertEquals(5_000_000, journeyDetails.getFuelUnitPrice(FuelComponent.NBO));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getTravelBaseFuelInMT()));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getSupplementalTravelBaseFuelInMT()));

				Assert.assertEquals(0, journeyDetails.getIdleTime());

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_mmBtu));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getIdleBaseFuelInMT()));
			}
			{

				final IPortSlot portSlot = portSlotProvider.getPortSlot(dischargeElement);

				Assert.assertEquals(50, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(1, volumeAllocatedSequence.getVisitDuration(portSlot));
			}

			{

				final IPortSlot portSlot = portSlotProvider.getPortSlot(dischargeElement);

				final VoyageDetails journeyDetails = volumeAllocatedSequence.getVoyageDetailsFrom(portSlot);
				Assert.assertNotNull(journeyDetails);
				Assert.assertEquals(24 * 12, journeyDetails.getOptions().getDistance());
				Assert.assertEquals(24, journeyDetails.getTravelTime());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyDetails.getOptions().getVesselState());

				Assert.assertEquals(12000, journeyDetails.getSpeed());

				Assert.assertEquals(24 * ballast_nboRateInM3PerHour / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
				Assert.assertEquals((24L * ballast_nboRateInM3PerHour * cargoCVValue / baseFuelEquivalence) / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_MT));
				Assert.assertEquals(24L * ballast_nboRateInM3PerHour * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_mmBtu));
				// Assert.assertEquals(25 * 1000 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_mmBtu));

				Assert.assertEquals(5_000_000, journeyDetails.getFuelUnitPrice(FuelComponent.NBO));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getTravelBaseFuelInMT()));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getSupplementalTravelBaseFuelInMT()));

				Assert.assertEquals(0, journeyDetails.getIdleTime());

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_mmBtu));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getIdleBaseFuelInMT()));
			}
			{

				final IPortSlot portSlot = portSlotProvider.getPortSlot(endElement);

				Assert.assertEquals(75, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(0, volumeAllocatedSequence.getVisitDuration(portSlot));
			}
		}
	}

	/**
	 * Like case 1, but force a higher travelling speed to get idle time + FBO
	 */
	@Test
	public void testCalculations2() {
		final SimpleIndexingContext indexingContext = new SimpleIndexingContext();
		indexingContext.registerType(IBaseFuel.class);
		indexingContext.assignIndex(IBaseFuel.LNG);

		final int baseFuelUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(400);

		final IBaseFuel baseFuel = new BaseFuel(indexingContext, "test");

		final int[] baseFuelPrices = GeneralTestUtils.makeBaseFuelPrices(baseFuelUnitPrice);
		final IVolumeAllocator volumeAllocator = Mockito.mock(IVolumeAllocator.class);
		final Injector injector = createTestInjector(volumeAllocator, baseFuelPrices);

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);) {
			scope.enter();

			final SharedPortDistanceDataBuilder portBuilder = injector.getInstance(SharedPortDistanceDataBuilder.class);

			final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

			final IPort port1 = portBuilder.createPort("port-1", "port-1", "UTC");
			final IPort port2 = portBuilder.createPort("port-2", "port-2", "UTC");
			final IPort port3 = portBuilder.createPort("port-3", "port-3", "UTC");
			final IPort port4 = portBuilder.createPort("port-4", "port-4", "UTC");

			final int minSpeed = 16000;
			final int maxSpeed = 20000;
			final int capacity = 150000000;

			// 2 / 4 == 0.5 equivalence
			final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(2.0);
			final int baseFuelUnitEquivalence = OptimiserUnitConvertor.convertToInternalConversionFactor(4.0);
			baseFuel.setEquivalenceFactor(baseFuelUnitEquivalence);

			final IVessel vessel1 = builder.createVessel("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuel, baseFuel, baseFuel, baseFuel, 0, Integer.MAX_VALUE, 0, 0, false);

			final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<>();
			ladenKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.6));
			ladenKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.4));
			final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ladenKeypoints);

			final int laden_nboRateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.2);
			final int laden_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int laden_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.5);
			final int laden_inPortNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(48.0);

			builder.setVesselStateParameters(vessel1, VesselState.Laden, laden_nboRateInM3PerHour, laden_idleNBORateInM3PerHour, laden_idleConsumptionRateInMTPerHour, ladenConsumptionCalculator, 0,
					laden_inPortNBORateInM3PerHour);

			final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<>();
			ballastKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.5));
			ballastKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.3));
			final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ballastKeypoints);

			final int ballast_nboRateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int ballast_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.8);
			final int ballast_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.4);
			final int ballast_inPortNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(24.0);

			builder.setVesselStateParameters(vessel1, VesselState.Ballast, ballast_nboRateInM3PerHour, ballast_idleNBORateInM3PerHour, ballast_idleConsumptionRateInMTPerHour,
					ballastConsumptionCalculator, 0, ballast_inPortNBORateInM3PerHour);

			final IStartRequirement startRequirement = builder.createStartRequirement(port1, true, TimeWindowMaker.createInclusiveInclusive(0, 0, 0, false), null);

			IHeelOptionConsumer heelOptionConsumer = new HeelOptionConsumer(0, 0, VesselTankState.MUST_BE_WARM, new ConstantHeelPriceCalculator(0));
			final IEndRequirement endRequirement = builder.createEndRequirement(Collections.singleton(port4), true, TimeWindowMaker.createInclusiveInclusive(75, 75, 0, false), heelOptionConsumer,
					false);

			final IVesselAvailability vesselAvailability1 = builder.createVesselAvailability(vessel1, new ConstantValueLongCurve(0), VesselInstanceType.FLEET, startRequirement, endRequirement, null,
					new ConstantValueLongCurve(0), false);

			final ITimeWindow loadWindow = TimeWindowMaker.createInclusiveInclusive(25, 25, 0, false);
			final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2, loadWindow, 0, 150000000, new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), cargoCVValue, 1,
					false, false, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);

			final ITimeWindow dischargeWindow = TimeWindowMaker.createInclusiveInclusive(50, 50, 0, false);
			final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-1", port3, dischargeWindow, 0, 150000000, 0, Long.MAX_VALUE,
					new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), 1, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false,
					DEFAULT_VOLUME_LIMIT_IS_M3);

			final ICargo cargo1 = builder.createCargo(Lists.newArrayList(loadSlot, dischargeSlot), false);

			portBuilder.setPortToPortDistance(port1, port2, ERouteOption.DIRECT, 12 * 25);
			portBuilder.setPortToPortDistance(port2, port3, ERouteOption.DIRECT, 12 * 25);
			portBuilder.setPortToPortDistance(port3, port4, ERouteOption.DIRECT, 12 * 25);

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
			final ISequences sequences = new Sequences(Collections.singletonList(resource), CollectionsUtil.<IResource, ISequence> makeHashMap(resource, sequence));

			final IAllocationAnnotation allocationAnnotation = Mockito.mock(IAllocationAnnotation.class);
			final PortTimesRecord expectedPTR1 = new PortTimesRecord();
			{
				expectedPTR1.setSlotTime(portSlotProvider.getPortSlot(startElement), 1);
				expectedPTR1.setSlotDuration(portSlotProvider.getPortSlot(startElement), 0);
				expectedPTR1.setReturnSlotTime(loadSlot, 25);

			}
			final PortTimesRecord expectedPTR2 = new PortTimesRecord();
			{
				expectedPTR2.setSlotTime(loadSlot, 25);
				expectedPTR2.setSlotTime(dischargeSlot, 50);
				expectedPTR2.setSlotDuration(loadSlot, 1);
				expectedPTR2.setSlotDuration(dischargeSlot, 1);
				expectedPTR2.setReturnSlotTime(portSlotProvider.getPortSlot(endElement), 75);

				Mockito.when(volumeAllocator.allocate(Matchers.<IVesselAvailability> any(), Matchers.anyInt(), Matchers.<VoyagePlan> any(), Matchers.<IPortTimesRecord> eq(expectedPTR2)))
						.thenReturn(allocationAnnotation);
			}

			Mockito.when(allocationAnnotation.getSlotTime(loadSlot)).thenReturn(25);
			Mockito.when(allocationAnnotation.getSlotDuration(loadSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(dischargeSlot)).thenReturn(50);
			Mockito.when(allocationAnnotation.getSlotDuration(dischargeSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(portSlotProvider.getPortSlot(endElement))).thenReturn(75);

			Mockito.when(allocationAnnotation.getRemainingHeelVolumeInM3()).thenReturn(0L);
			// Load enough to cover boil-off
			Mockito.when(allocationAnnotation.getCommercialSlotVolumeInM3(loadSlot)).thenReturn(6300L);
			Mockito.when(allocationAnnotation.getCommercialSlotVolumeInM3(dischargeSlot)).thenReturn(0L);
			Mockito.when(allocationAnnotation.getPhysicalSlotVolumeInM3(loadSlot)).thenReturn(4300L);
			Mockito.when(allocationAnnotation.getPhysicalSlotVolumeInM3(dischargeSlot)).thenReturn(0L);
			Mockito.when(allocationAnnotation.getSlotCargoCV(Matchers.<IPortSlot> any())).thenReturn(cargoCVValue);
			Mockito.when(allocationAnnotation.getFuelVolumeInM3()).thenReturn(6300L);

			// Schedule sequence
			final int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
			final IEvaluationContext context = Mockito.mock(IEvaluationContext.class);
			final IEvaluationState state = Mockito.mock(IEvaluationState.class);

			final AnnotatedSolution annotatedSolution = new AnnotatedSolution(sequences, state);
			Map<IResource, List<IPortTimesRecord>> allPortTimeRecords = CollectionsUtil.makeHashMap(resource, Lists.newArrayList(expectedPTR1, expectedPTR2));

			final VolumeAllocatedSequences volumeAllocatedSequences = scheduler.schedule(sequences, allPortTimeRecords, annotatedSolution);
			Assert.assertNotNull(volumeAllocatedSequences);
			final VolumeAllocatedSequence volumeAllocatedSequence = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(startElement);
				Assert.assertEquals(1, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(0, volumeAllocatedSequence.getVisitDuration(portSlot));
			}

			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(startElement);

				final VoyageDetails journeyDetails = volumeAllocatedSequence.getVoyageDetailsFrom(portSlot);
				Assert.assertNotNull(journeyDetails);
				Assert.assertEquals(25 * 12, journeyDetails.getOptions().getDistance());
				Assert.assertEquals(18, journeyDetails.getTravelTime());

				Assert.assertEquals(16000, journeyDetails.getSpeed());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyDetails.getOptions().getVesselState());

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_mmBtu));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_mmBtu));

				Assert.assertEquals(900 * 18 / 24, journeyDetails.getFuelConsumption(vessel1.getTravelBaseFuelInMT()));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getSupplementalTravelBaseFuelInMT()));

				Assert.assertEquals(baseFuelUnitPrice, journeyDetails.getFuelUnitPrice(FuelComponent.Base));

				Assert.assertEquals(6, journeyDetails.getIdleTime());

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_mmBtu));

				Assert.assertEquals(6 * 400 / 24L, journeyDetails.getFuelConsumption(vessel1.getIdleBaseFuelInMT()));

				Assert.assertEquals(baseFuelUnitPrice, journeyDetails.getFuelUnitPrice(FuelComponent.IdleBase));
			}
			{

				final IPortSlot portSlot = portSlotProvider.getPortSlot(loadElement);
				Assert.assertEquals(25, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(1, volumeAllocatedSequence.getVisitDuration(portSlot));
			}

			{

				final IPortSlot portSlot = portSlotProvider.getPortSlot(loadElement);

				final VoyageDetails journeyDetails = volumeAllocatedSequence.getVoyageDetailsFrom(portSlot);
				Assert.assertNotNull(journeyDetails);
				Assert.assertEquals(25 * 12, journeyDetails.getOptions().getDistance());
				Assert.assertEquals(18, journeyDetails.getTravelTime());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Laden, journeyDetails.getOptions().getVesselState());

				Assert.assertEquals(16000, journeyDetails.getSpeed());

				Assert.assertEquals(18 * laden_nboRateInM3PerHour / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
				Assert.assertEquals((18L * laden_nboRateInM3PerHour * cargoCVValue / baseFuelUnitEquivalence) / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_MT));
				Assert.assertEquals(18L * laden_nboRateInM3PerHour * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_mmBtu));
				// Assert.assertEquals(25 * 1200 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));

				Assert.assertEquals(18 * 800 / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
				Assert.assertEquals((18L * 800 * cargoCVValue / baseFuelUnitEquivalence) / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_MT));
				Assert.assertEquals(18L * 800 * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_mmBtu));

				Assert.assertEquals(5_000_000, journeyDetails.getFuelUnitPrice(FuelComponent.NBO));
				Assert.assertEquals(5_000_000, journeyDetails.getFuelUnitPrice(FuelComponent.FBO));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getTravelBaseFuelInMT()));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getSupplementalTravelBaseFuelInMT()));

				Assert.assertEquals(6, journeyDetails.getIdleTime());

				Assert.assertEquals(6 * 1000 / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
				Assert.assertEquals((6L * 1000 * 500) / 24L / Calculator.ScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_MT));
				Assert.assertEquals(6L * 1000 * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_mmBtu));

				Assert.assertEquals(5_000_000, journeyDetails.getFuelUnitPrice(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getIdleBaseFuelInMT()));
			}
			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(dischargeElement);
				Assert.assertEquals(50, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(1, volumeAllocatedSequence.getVisitDuration(portSlot));
			}
			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(dischargeElement);

				final VoyageDetails journeyDetails = volumeAllocatedSequence.getVoyageDetailsFrom(portSlot);
				Assert.assertNotNull(journeyDetails);
				Assert.assertEquals(25 * 12, journeyDetails.getOptions().getDistance());
				Assert.assertEquals(18, journeyDetails.getTravelTime());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyDetails.getOptions().getVesselState());

				Assert.assertEquals(16000, journeyDetails.getSpeed());

				Assert.assertEquals(18 * 1000 / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
				Assert.assertEquals((18L * 1000L * 500) / 24L / Calculator.ScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_MT));
				Assert.assertEquals(18L * 1000 * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_mmBtu));
				// Assert.assertEquals(25 * 1000 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));
				//
				Assert.assertEquals(18 * 800 / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
				Assert.assertEquals((18L * 800L * 500) / 24L / Calculator.ScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_MT));
				Assert.assertEquals(18L * 800 * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_mmBtu));

				Assert.assertEquals(5_000_000, journeyDetails.getFuelUnitPrice(FuelComponent.NBO));
				Assert.assertEquals(5_000_000, journeyDetails.getFuelUnitPrice(FuelComponent.FBO));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getTravelBaseFuelInMT()));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getSupplementalTravelBaseFuelInMT()));

				Assert.assertEquals(6, journeyDetails.getIdleTime());

				Assert.assertEquals(6 * 800 / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
				Assert.assertEquals((6 * 800 * 500) / 24L / Calculator.ScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_MT));
				Assert.assertEquals(6L * 800 * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_mmBtu));

				Assert.assertEquals(5_000_000, journeyDetails.getFuelUnitPrice(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getIdleBaseFuelInMT()));

			}
			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(endElement);
				Assert.assertEquals(75, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(0, volumeAllocatedSequence.getVisitDuration(portSlot));
			}
		}
	}

	/**
	 * Like case 1, but force a higher travelling speed to get idle time + Base_Supplemental (make NBO more costly than Base)
	 */
	@Test
	public void testCalculations3() {
		test3Logic(false);
	}

	/**
	 * Like testCalculations3 but with optional vessel availabilities
	 */
	@Test
	public void testCalculations3_optional() {
		test3Logic(true);
	}

	private void test3Logic(final boolean isOptional) {

		final SimpleIndexingContext indexingContext = new SimpleIndexingContext();
		indexingContext.registerType(IBaseFuel.class);
		indexingContext.assignIndex(IBaseFuel.LNG);

		final IVolumeAllocator volumeAllocator = Mockito.mock(IVolumeAllocator.class);
		final IBaseFuel baseFuel = new BaseFuel(indexingContext, "test");
		final int baseFuelUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(1);

		final int[] baseFuelPrices = GeneralTestUtils.makeBaseFuelPrices(baseFuelUnitPrice);

		final Injector injector = createTestInjector(volumeAllocator, baseFuelPrices);

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);) {
			scope.enter();

			final SharedPortDistanceDataBuilder portBuilder = injector.getInstance(SharedPortDistanceDataBuilder.class);

			final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

			final IPort port1 = portBuilder.createPort("port-1", "port-1", "UTC");
			final IPort port2 = portBuilder.createPort("port-2", "port-2", "UTC");
			final IPort port3 = portBuilder.createPort("port-3", "port-3", "UTC");
			final IPort port4 = portBuilder.createPort("port-4", "port-4", "UTC");

			final int minSpeed = 16000;
			final int maxSpeed = 20000;
			final int capacity = 150000000;

			// 2 / 4 == 0.5 equivalence
			final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(2.0);
			final int baseFuelEquivalence = OptimiserUnitConvertor.convertToInternalConversionFactor(4);

			baseFuel.setEquivalenceFactor(baseFuelEquivalence);
			final IVessel vessel1 = builder.createVessel("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuel, baseFuel, baseFuel, baseFuel, 0, Integer.MAX_VALUE, 0, 0, false);

			final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<>();
			ladenKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.6));
			ladenKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.4));
			final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ladenKeypoints);

			final int laden_nboRateInM3PerDay = OptimiserUnitConvertor.convertToInternalDailyRate(1.2);
			final int laden_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int laden_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.5);
			final int laden_inPortNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(48.0);

			builder.setVesselStateParameters(vessel1, VesselState.Laden, laden_nboRateInM3PerDay, laden_idleNBORateInM3PerHour, laden_idleConsumptionRateInMTPerHour, ladenConsumptionCalculator, 0,
					laden_inPortNBORateInM3PerHour);

			final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<>();
			ballastKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.5));
			ballastKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.3));
			final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ballastKeypoints);

			final int ballast_nboRateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
			final int ballast_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.8);
			final int ballast_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.4);
			final int ballast_inPortNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(24.0);
			builder.setVesselStateParameters(vessel1, VesselState.Ballast, ballast_nboRateInM3PerHour, ballast_idleNBORateInM3PerHour, ballast_idleConsumptionRateInMTPerHour,
					ballastConsumptionCalculator, 0, ballast_inPortNBORateInM3PerHour);

			final IStartRequirement startRequirement = builder.createStartRequirement(port1, true, TimeWindowMaker.createInclusiveInclusive(0, 0), null);
			IHeelOptionConsumer heelOptionConsumer = new HeelOptionConsumer(0, 0, VesselTankState.MUST_BE_WARM, new ConstantHeelPriceCalculator(0));

			final IEndRequirement endRequirement = builder.createEndRequirement(Collections.singleton(port4), true, TimeWindowMaker.createInclusiveInclusive(75, 75), heelOptionConsumer, false);

			final IVesselAvailability vesselAvailability1 = builder.createVesselAvailability(vessel1, new ConstantValueLongCurve(0), VesselInstanceType.FLEET, startRequirement, endRequirement, null,
					new ConstantValueLongCurve(0), isOptional);

			final ITimeWindow loadWindow = TimeWindowMaker.createInclusiveInclusive(25, 25);
			final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2, loadWindow, 0, 150000000, new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), cargoCVValue, 1,
					false, false, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);

			final ITimeWindow dischargeWindow = TimeWindowMaker.createInclusiveInclusive(50, 50);
			final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-1", port3, dischargeWindow, 0, 150000000, 0, Long.MAX_VALUE,
					new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(200)), 1, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false,
					DEFAULT_VOLUME_LIMIT_IS_M3);

			final ICargo cargo1 = builder.createCargo(Lists.newArrayList(loadSlot, dischargeSlot), false);

			portBuilder.setPortToPortDistance(port1, port2, ERouteOption.DIRECT, 12 * 25);
			portBuilder.setPortToPortDistance(port2, port3, ERouteOption.DIRECT, 12 * 25);
			portBuilder.setPortToPortDistance(port3, port4, ERouteOption.DIRECT, 12 * 25);

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
			final PortTimesRecord expectedPTR1 = new PortTimesRecord();
			{
				expectedPTR1.setSlotTime(portSlotProvider.getPortSlot(startElement), 1);
				expectedPTR1.setSlotDuration(portSlotProvider.getPortSlot(startElement), 0);
				expectedPTR1.setReturnSlotTime(loadSlot, 25);
			}
			final PortTimesRecord expectedPTR2 = new PortTimesRecord();
			{
				expectedPTR2.setSlotTime(loadSlot, 25);
				expectedPTR2.setSlotTime(dischargeSlot, 50);
				expectedPTR2.setSlotDuration(loadSlot, 1);
				expectedPTR2.setSlotDuration(dischargeSlot, 1);
				expectedPTR2.setReturnSlotTime(portSlotProvider.getPortSlot(endElement), 75);

				Mockito.when(volumeAllocator.allocate(Matchers.<IVesselAvailability> any(), Matchers.anyInt(), Matchers.<VoyagePlan> any(), Matchers.<IPortTimesRecord> eq(expectedPTR2)))
						.thenReturn(allocationAnnotation);
			}

			Mockito.when(allocationAnnotation.getSlotTime(loadSlot)).thenReturn(25);
			Mockito.when(allocationAnnotation.getSlotDuration(loadSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(dischargeSlot)).thenReturn(50);
			Mockito.when(allocationAnnotation.getSlotDuration(dischargeSlot)).thenReturn(1);

			Mockito.when(allocationAnnotation.getSlotTime(portSlotProvider.getPortSlot(endElement))).thenReturn(75);

			Mockito.when(allocationAnnotation.getRemainingHeelVolumeInM3()).thenReturn(0L);
			// Load enough to cover boil-off
			Mockito.when(allocationAnnotation.getCommercialSlotVolumeInM3(loadSlot)).thenReturn(4150L);
			Mockito.when(allocationAnnotation.getCommercialSlotVolumeInM3(dischargeSlot)).thenReturn(0L);
			Mockito.when(allocationAnnotation.getPhysicalSlotVolumeInM3(loadSlot)).thenReturn(2150L);
			Mockito.when(allocationAnnotation.getPhysicalSlotVolumeInM3(dischargeSlot)).thenReturn(0L);
			Mockito.when(allocationAnnotation.getFuelVolumeInM3()).thenReturn(4150L);

			// Schedule sequence
			final ISequences sequences = new Sequences(Collections.singletonList(resource), CollectionsUtil.<IResource, ISequence> makeHashMap(resource, sequence));

			Map<IResource, List<IPortTimesRecord>> allPortTimeRecords = CollectionsUtil.makeHashMap(resource, Lists.newArrayList(expectedPTR1, expectedPTR2));

			final IEvaluationContext context = Mockito.mock(IEvaluationContext.class);
			final IEvaluationState state = Mockito.mock(IEvaluationState.class);

			final AnnotatedSolution annotatedSolution = new AnnotatedSolution(sequences, state);
			// TODO: Fix arrival time feed in.
			final VolumeAllocatedSequences volumeAllocatedSequences = scheduler.schedule(sequences, allPortTimeRecords, annotatedSolution);
			Assert.assertNotNull(volumeAllocatedSequences);

			final VolumeAllocatedSequence volumeAllocatedSequence = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

			// TODO: Start checking results
			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(startElement);
				Assert.assertEquals(1, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(0, volumeAllocatedSequence.getVisitDuration(portSlot));
			}

			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(startElement);

				final VoyageDetails journeyDetails = volumeAllocatedSequence.getVoyageDetailsFrom(portSlot);
				Assert.assertNotNull(journeyDetails);
				Assert.assertEquals(25 * 12, journeyDetails.getOptions().getDistance());
				Assert.assertEquals(18, journeyDetails.getTravelTime());

				Assert.assertEquals(16000, journeyDetails.getSpeed());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyDetails.getOptions().getVesselState());

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_mmBtu));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_mmBtu));

				Assert.assertEquals(900 * 18 / 24, journeyDetails.getFuelConsumption(vessel1.getTravelBaseFuelInMT()));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getSupplementalTravelBaseFuelInMT()));

				Assert.assertEquals(baseFuelUnitPrice, journeyDetails.getFuelUnitPrice(FuelComponent.Base));

				Assert.assertEquals(6, journeyDetails.getIdleTime());

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_mmBtu));

				Assert.assertEquals(6 * 400 / 24L, journeyDetails.getFuelConsumption(vessel1.getIdleBaseFuelInMT()));

				Assert.assertEquals(baseFuelUnitPrice, journeyDetails.getFuelUnitPrice(FuelComponent.IdleBase));
			}
			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(loadElement);
				Assert.assertEquals(25, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(1, volumeAllocatedSequence.getVisitDuration(portSlot));
			}

			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(loadElement);

				final VoyageDetails journeyDetails = volumeAllocatedSequence.getVoyageDetailsFrom(portSlot);
				Assert.assertNotNull(journeyDetails);
				Assert.assertEquals(25 * 12, journeyDetails.getOptions().getDistance());
				Assert.assertEquals(18, journeyDetails.getTravelTime());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Laden, journeyDetails.getOptions().getVesselState());

				Assert.assertEquals(16000, journeyDetails.getSpeed());

				Assert.assertEquals(18 * 1200 / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
				Assert.assertEquals((18L * 1200L * 500) / 24L / Calculator.ScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_MT));
				// Not yet set
				Assert.assertEquals(18L * laden_nboRateInM3PerDay * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_mmBtu));
				// Assert.assertEquals(25 * 1200 * 2,
				// journeyEvent.getFuelConsumption(FuelComponent.NBO,
				// FuelUnit.MMBTu));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_mmBtu));

				Assert.assertEquals(200_000_000, journeyDetails.getFuelUnitPrice(FuelComponent.NBO));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getTravelBaseFuelInMT()));

				Assert.assertEquals(18 * 400 / 24L, journeyDetails.getFuelConsumption(vessel1.getSupplementalTravelBaseFuelInMT()));

				Assert.assertEquals(baseFuelUnitPrice, journeyDetails.getFuelUnitPrice(FuelComponent.Base_Supplemental));

				Assert.assertEquals(6, journeyDetails.getIdleTime());

				Assert.assertEquals(6 * 1000 / 24L, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
				Assert.assertEquals((6L * 1000 * 500) / 24L / Calculator.ScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_MT));
				Assert.assertEquals(6L * 1000 * cargoCVValue / 24L / Calculator.HighScaleFactor, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_mmBtu));

				Assert.assertEquals(200_000_000, journeyDetails.getFuelUnitPrice(FuelComponent.IdleNBO));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getIdleBaseFuelInMT()));

			}
			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(dischargeElement);
				Assert.assertEquals(50, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(1, volumeAllocatedSequence.getVisitDuration(portSlot));
			}

			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(dischargeElement);

				final VoyageDetails journeyDetails = volumeAllocatedSequence.getVoyageDetailsFrom(portSlot);
				Assert.assertNotNull(journeyDetails);
				Assert.assertEquals(25 * 12, journeyDetails.getOptions().getDistance());
				Assert.assertEquals(18, journeyDetails.getTravelTime());

				// Expect only base fuel use on this ballast leg

				Assert.assertSame(VesselState.Ballast, journeyDetails.getOptions().getVesselState());

				Assert.assertEquals(16000, journeyDetails.getSpeed());

				// Why still nbo? NEED TO CHECK vpo COST CALS. -- Basefuel should be
				// the cheaper option!
				// -- is base fuel being evaluated or is the choice not being
				// allowed?

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_MT));
				// Not yet set
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.NBO_In_mmBtu));
				//
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.FBO_In_mmBtu));

				Assert.assertEquals(18 * 900 / 24L, journeyDetails.getFuelConsumption(vessel1.getTravelBaseFuelInMT()));

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(vessel1.getSupplementalTravelBaseFuelInMT()));

				Assert.assertEquals(baseFuelUnitPrice, journeyDetails.getFuelUnitPrice(FuelComponent.Base));

				Assert.assertEquals(6, journeyDetails.getIdleTime());

				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_MT));
				Assert.assertEquals(0, journeyDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_mmBtu));

				Assert.assertEquals(6 * 400 / 24L, journeyDetails.getFuelConsumption(vessel1.getIdleBaseFuelInMT()));

				Assert.assertEquals(baseFuelUnitPrice, journeyDetails.getFuelUnitPrice(FuelComponent.IdleBase));
			}
			{
				final IPortSlot portSlot = portSlotProvider.getPortSlot(endElement);
				Assert.assertEquals(75, volumeAllocatedSequence.getArrivalTime(portSlot));
				Assert.assertEquals(0, volumeAllocatedSequence.getVisitDuration(portSlot));
			}
		}
	}

	private Injector createTestInjector(final IVolumeAllocator volumeAllocator, final int[] baseFuelUnitPrices) {

		final Injector injector = Guice.createInjector(new PerChainUnitScopeModule(), new DataComponentProviderModule(), new AbstractModule() {

			@Provides
			@Named(VoyagePlanOptimiser.VPO_SPEED_STEPPING)
			private boolean isVPOSpeedStepping() {
				return true;
			}

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
				final int highPeriodInDays = 15;
				final int lowPeriodInDays = Math.max(0, highPeriodInDays - 2);
				idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, lowPeriodInDays * 24);
				idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, highPeriodInDays * 24);
				idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, 2_500);
				idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, 10_000);
				idleParams.setEndWeight(10_000);

				return idleParams;
			}

			@Override
			protected void configure() {

				install(new SharedDataModule());
				bind(CalendarMonthMapper.class).toInstance(Mockito.mock(CalendarMonthMapper.class));
				bind(ScheduleCalculator.class);
				bind(ICharterRateCalculator.class).to(VesselStartDateCharterRateCalculator.class);
				bind(IVolumeAllocator.class).toInstance(volumeAllocator);
				bind(SchedulerBuilder.class);
				bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
				bind(IVoyagePlanOptimiser.class).to(VoyagePlanOptimiser.class);

				bind(IVoyagePlanner.class).to(VoyagePlanner.class);

				bind(IArrivalTimeScheduler.class).toInstance(Mockito.mock(IArrivalTimeScheduler.class));

				bind(IEndEventScheduler.class).to(DefaultEndEventScheduler.class);

				bind(IVesselBaseFuelCalculator.class).to(VesselBaseFuelCalculator.class);
				final VesselBaseFuelCalculator baseFuelCalculator = Mockito.mock(VesselBaseFuelCalculator.class);

				Mockito.when(baseFuelCalculator.getBaseFuelPrices(Matchers.any(IVessel.class), Matchers.any(IPortTimesRecord.class))).thenReturn(baseFuelUnitPrices);
				Mockito.when(baseFuelCalculator.getBaseFuelPrices(Matchers.any(IVessel.class), Matchers.anyInt())).thenReturn(baseFuelUnitPrices);

				bind(VesselBaseFuelCalculator.class).toInstance(baseFuelCalculator);

				bind(boolean.class).annotatedWith(Names.named(IEndEventScheduler.ENABLE_HIRE_COST_ONLY_END_RULE)).toInstance(Boolean.TRUE);

				bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_ArrivalTimeCache)).toInstance(CacheMode.Off);
				bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocationCache)).toInstance(CacheMode.Off);
				bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocatedSequenceCache)).toInstance(CacheMode.Off);
				bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_ProfitandLossCache)).toInstance(CacheMode.Off);
				bind(boolean.class).annotatedWith(Names.named("hint-lngtransformer-disable-caches")).toInstance(Boolean.TRUE);
				bind(boolean.class).annotatedWith(Names.named(IEndEventScheduler.ENABLE_HIRE_COST_ONLY_END_RULE)).toInstance(Boolean.TRUE);
				bind(int.class).annotatedWith(Names.named(SchedulerConstants.CHARTER_LENGTH_MIN_IDLE_HOURS)).toInstance(Integer.MAX_VALUE);
				bind(int.class).annotatedWith(Names.named(SchedulerConstants.COOLDOWN_MIN_IDLE_HOURS)).toInstance(Integer.MAX_VALUE);

			}
		});
		return injector;
	}
}
