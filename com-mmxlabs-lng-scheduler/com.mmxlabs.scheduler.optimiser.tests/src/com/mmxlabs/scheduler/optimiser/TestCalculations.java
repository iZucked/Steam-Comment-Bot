/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselStartDateCharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.ILoadEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.schedule.VoyagePlanAnnotator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Set of unit tests to check that the expected costs are calculated from an input set of data.
 * 
 */
public class TestCalculations {

	/**
	 * 
	 * 
	 * TODO: Need to test NBO speed (lets increase rate so we get e.g. 13/14 knots) *** Case 1, but with higher NBO speed/rates Need to trigger Base_Supplemental calculations Need to test
	 * load/discharge volumes + cost/revenue calcs
	 */

	/**
	 * This tests the fuel costs + consumptions when travelling at slowest speed with no idle time.
	 */
	@SuppressWarnings({ "unused" })
	@Test
	public void testCalculations1() {

		final Injector injector = createTestInjector();
		final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

		final IPort port1 = builder.createPort("port-1", false, null);
		final IPort port2 = builder.createPort("port-2", false, null);
		final IPort port3 = builder.createPort("port-3", false, null);
		final IPort port4 = builder.createPort("port-4", false, null);

		final int minSpeed = 12000;
		final int maxSpeed = 20000;
		final int capacity = 150000000;
		final int baseFuelUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(400);
		final int baseFuelEquivalence = OptimiserUnitConvertor.convertToInternalConversionFactor(0.5);
		final IVesselClass vesselClass1 = builder.createVesselClass("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuelUnitPrice, baseFuelEquivalence, 0, Integer.MAX_VALUE, 0, 0);

		final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<Integer, Long>();
		ladenKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.6));
		ladenKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.4));
		final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ladenKeypoints);
		final int laden_nboRateInM3PerDay = OptimiserUnitConvertor.convertToInternalDailyRate(1.2);
		final int laden_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
		final int laden_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.5);
		builder.setVesselClassStateParameters(vesselClass1, VesselState.Laden, laden_nboRateInM3PerDay, laden_idleNBORateInM3PerHour, laden_idleConsumptionRateInMTPerHour, ladenConsumptionCalculator,
				0);
		final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<Integer, Long>();
		ballastKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.5));
		ballastKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.3));
		final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ballastKeypoints);

		final int ballast_nboRateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
		final int ballast_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.8);
		final int ballast_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.4);
		builder.setVesselClassStateParameters(vesselClass1, VesselState.Ballast, ballast_nboRateInM3PerHour, ballast_idleNBORateInM3PerHour, ballast_idleConsumptionRateInMTPerHour,
				ballastConsumptionCalculator, 0);
		final IStartEndRequirement startRequirement = builder.createStartEndRequirement(port1, builder.createTimeWindow(0, 0));
		final IStartEndRequirement endRequirement = builder.createStartEndRequirement(port4, builder.createTimeWindow(75, 75));

		final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, new ConstantValueCurve(0), startRequirement, endRequirement, 0, 0, 0, capacity);

		final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
		final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(2.0);
		final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2, loadWindow, 0, 150000000, new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), cargoCVValue, 1, false,
				false, IPortSlot.NO_PRICING_DATE, false);

		final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
		final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-1", port3, dischargeWindow, 0, 150000000, 0, Long.MAX_VALUE,
				new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), 1, IPortSlot.NO_PRICING_DATE, false);

		final ICargo cargo1 = builder.createCargo(Lists.newArrayList(loadSlot, dischargeSlot), false);

		builder.setPortToPortDistance(port1, port2, IMultiMatrixProvider.Default_Key, 12 * 24);
		builder.setPortToPortDistance(port2, port3, IMultiMatrixProvider.Default_Key, 12 * 24);
		builder.setPortToPortDistance(port3, port4, IMultiMatrixProvider.Default_Key, 12 * 24);

		final IOptimisationData data = builder.getOptimisationData();

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndProvider = injector.getInstance(IStartEndRequirementProvider.class);

		final ScheduleCalculator scheduler = injector.getInstance(ScheduleCalculator.class);

		final IResource resource = vesselProvider.getResource(vessel1);

		final ISequenceElement startElement = startEndProvider.getStartElement(resource);
		final ISequenceElement endElement = startEndProvider.getEndElement(resource);

		final ISequenceElement dischargeElement = portSlotProvider.getElement(dischargeSlot);
		final ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
		final List<ISequenceElement> sequenceList = CollectionsUtil.makeArrayList(startElement, loadElement, dischargeElement, endElement);

		final ISequence sequence = new ListSequence(sequenceList);

		// Schedule sequence
		final int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
		final AnnotatedSolution annotatedSolution = new AnnotatedSolution();
		final ISequences sequences = new Sequences(Collections.singletonList(resource), CollectionsUtil.<IResource, ISequence> makeHashMap(resource, sequence));
		final ScheduledSequences scheduledSequence = scheduler.schedule(sequences, new int[][] { expectedArrivalTimes }, annotatedSolution);
		Assert.assertNotNull(scheduledSequence);

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

			Assert.assertEquals((500l * baseFuelUnitPrice) / Calculator.HighScaleFactor, journeyEvent.getFuelCost(FuelComponent.Base));
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
			Assert.assertEquals((1200l * 500000) / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
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

			Assert.assertEquals(24 * ballast_nboRateInM3PerHour / 24l, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((24l * ballast_nboRateInM3PerHour * baseFuelEquivalence) / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(24l * ballast_nboRateInM3PerHour * cargoCVValue / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			// Assert.assertEquals(25 * 1000 * 2,
			// journeyEvent.getFuelConsumption(FuelComponent.NBO,
			// FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(24 * ballast_nboRateInM3PerHour * 2 * 5 / 24l, journeyEvent.getFuelCost(FuelComponent.NBO));
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

	/**
	 * Like case 1, but force a higher travelling speed to get idle time + FBO
	 */
	@SuppressWarnings("unused")
	@Test
	public void testCalculations2() {

		final Injector injector = createTestInjector();
		final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

		final IPort port1 = builder.createPort("port-1", false, null);
		final IPort port2 = builder.createPort("port-2", false, null);
		final IPort port3 = builder.createPort("port-3", false, null);
		final IPort port4 = builder.createPort("port-4", false, null);

		final int minSpeed = 16000;
		final int maxSpeed = 20000;
		final int capacity = 150000000;
		final int baseFuelUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(400);
		final int baseFuelUnitEquivalence = OptimiserUnitConvertor.convertToInternalConversionFactor(0.5);
		final IVesselClass vesselClass1 = builder.createVesselClass("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuelUnitPrice, baseFuelUnitEquivalence, 0, Integer.MAX_VALUE, 0, 0);

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

		final IStartEndRequirement startRequirement = builder.createStartEndRequirement(port1, builder.createTimeWindow(0, 0));
		final IStartEndRequirement endRequirement = builder.createStartEndRequirement(port4, builder.createTimeWindow(75, 75));

		final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, new ConstantValueCurve(0), startRequirement, endRequirement, 0, 0, 0, capacity);

		final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
		final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(2.0);
		final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2, loadWindow, 0, 150000000, new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), cargoCVValue, 1, false,
				false, IPortSlot.NO_PRICING_DATE, false);

		final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
		final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-1", port3, dischargeWindow, 0, 150000000, 0, Long.MAX_VALUE,
				new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), 1, IPortSlot.NO_PRICING_DATE, false);

		final ICargo cargo1 = builder.createCargo(Lists.newArrayList(loadSlot, dischargeSlot), false);

		builder.setPortToPortDistance(port1, port2, IMultiMatrixProvider.Default_Key, 12 * 25);
		builder.setPortToPortDistance(port2, port3, IMultiMatrixProvider.Default_Key, 12 * 25);
		builder.setPortToPortDistance(port3, port4, IMultiMatrixProvider.Default_Key, 12 * 25);

		final IOptimisationData data = builder.getOptimisationData();

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndProvider = injector.getInstance(IStartEndRequirementProvider.class);
		final VoyagePlanAnnotator annotator = injector.getInstance(VoyagePlanAnnotator.class);

		final ScheduleCalculator scheduler = injector.getInstance(ScheduleCalculator.class);

		final IResource resource = vesselProvider.getResource(vessel1);

		final ISequenceElement startElement = startEndProvider.getStartElement(resource);
		final ISequenceElement endElement = startEndProvider.getEndElement(resource);

		final ISequenceElement dischargeElement = portSlotProvider.getElement(dischargeSlot);
		final ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
		final List<ISequenceElement> sequenceList = CollectionsUtil.makeArrayList(startElement, loadElement, dischargeElement, endElement);

		final ISequence sequence = new ListSequence(sequenceList);
		final ISequences sequences = new Sequences(Collections.singletonList(resource), CollectionsUtil.<IResource, ISequence> makeHashMap(resource, sequence));

		// Schedule sequence
		final int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
		final AnnotatedSolution annotatedSolution = new AnnotatedSolution();
		final ScheduledSequences scheduledSequence = scheduler.schedule(sequences, new int[][] { expectedArrivalTimes }, annotatedSolution);
		Assert.assertNotNull(scheduledSequence);

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

			Assert.assertEquals((900l * 18l / 24 * baseFuelUnitPrice) / Calculator.HighScaleFactor, journeyEvent.getFuelCost(FuelComponent.Base));
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
			Assert.assertEquals(6 * 400 / 24l, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals((6l * 400 * baseFuelUnitPrice) / 24l / Calculator.HighScaleFactor, idleEvent.getFuelCost(FuelComponent.IdleBase));

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

			Assert.assertEquals(18 * laden_nboRateInM3PerHour / 24l, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((18l * laden_nboRateInM3PerHour * baseFuelUnitEquivalence) / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(18l * laden_nboRateInM3PerHour * cargoCVValue / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			// Assert.assertEquals(25 * 1200 * 2,
			// journeyEvent.getFuelConsumption(FuelComponent.NBO,
			// FuelUnit.MMBTu));

			Assert.assertEquals(18 * 800 / 24l, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals((18l * 800 * baseFuelUnitEquivalence) / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(18l * 800 * cargoCVValue / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(18l * 1200 * 2 * 5 / 24l, journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(18 * 800 * 2 * 5 / 24l, journeyEvent.getFuelCost(FuelComponent.FBO));
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

			Assert.assertEquals(6 * 1000 / 24l, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals((6l * 1000 * 500) / 24l / Calculator.ScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(6l * 1000 * cargoCVValue / 24l / Calculator.HighScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(6 * 1000 * 2 * 5 / 24l, idleEvent.getFuelCost(FuelComponent.IdleNBO));

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

			Assert.assertEquals(18 * 1000 / 24l, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((18l * 1000l * 500) / 24l / Calculator.ScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(18l * 1000 * cargoCVValue / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			// Assert.assertEquals(25 * 1000 * 2,
			// journeyEvent.getFuelConsumption(FuelComponent.NBO,
			// FuelUnit.MMBTu));
			//
			Assert.assertEquals(18 * 800 / 24l, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals((18l * 800l * 500) / 24l / Calculator.ScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(18l * 800 * cargoCVValue / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(18 * 1000 * 2 * 5 / 24l, journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(18 * 800 * 2 * 5 / 24l, journeyEvent.getFuelCost(FuelComponent.FBO));
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

			Assert.assertEquals(6 * 800 / 24l, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals((6 * 800 * 500) / 24l / Calculator.ScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(6l * 800 * cargoCVValue / 24l / Calculator.HighScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(6 * 800 * 2 * 5 / 24l, idleEvent.getFuelCost(FuelComponent.IdleNBO));

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

	/**
	 * Like case 1, but force a higher travelling speed to get idle time + Base_Supplemental (make NBO more costly than Base)
	 */
	@SuppressWarnings("unused")
	@Test
	public void testCalculations3() {

		final Injector injector = createTestInjector();
		final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

		final IPort port1 = builder.createPort("port-1", false, null);
		final IPort port2 = builder.createPort("port-2", false, null);
		final IPort port3 = builder.createPort("port-3", false, null);
		final IPort port4 = builder.createPort("port-4", false, null);

		final int minSpeed = 16000;
		final int maxSpeed = 20000;
		final int capacity = 150000000;
		final int baseFuelUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(1);
		final int baseFuelEquivalance = OptimiserUnitConvertor.convertToInternalConversionFactor(0.5);
		final IVesselClass vesselClass1 = builder.createVesselClass("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuelUnitPrice, baseFuelEquivalance, 0, Integer.MAX_VALUE, 0, 0);

		final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<Integer, Long>();
		ladenKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.6));
		ladenKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.4));
		final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ladenKeypoints);

		final int laden_nboRateInM3PerDay = OptimiserUnitConvertor.convertToInternalDailyRate(1.2);
		final int laden_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
		final int laden_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.5);

		builder.setVesselClassStateParameters(vesselClass1, VesselState.Laden, laden_nboRateInM3PerDay, laden_idleNBORateInM3PerHour, laden_idleConsumptionRateInMTPerHour, ladenConsumptionCalculator,
				0);

		final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<Integer, Long>();
		ballastKeypoints.put(12000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(0.5));
		ballastKeypoints.put(20000, (long) OptimiserUnitConvertor.convertToInternalDailyRate(1.3));
		final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(ballastKeypoints);

		final int ballast_nboRateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(1.0);
		final int ballast_idleNBORateInM3PerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.8);
		final int ballast_idleConsumptionRateInMTPerHour = OptimiserUnitConvertor.convertToInternalDailyRate(0.4);
		builder.setVesselClassStateParameters(vesselClass1, VesselState.Ballast, ballast_nboRateInM3PerHour, ballast_idleNBORateInM3PerHour, ballast_idleConsumptionRateInMTPerHour,
				ballastConsumptionCalculator, 0);

		final IStartEndRequirement startRequirement = builder.createStartEndRequirement(port1, builder.createTimeWindow(0, 0));
		final IStartEndRequirement endRequirement = builder.createStartEndRequirement(port4, builder.createTimeWindow(75, 75));

		final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, new ConstantValueCurve(0), startRequirement, endRequirement, 0, 0, 0, capacity);

		final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
		final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(2);
		final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2, loadWindow, 0, 150000000, new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(5)), cargoCVValue, 1, false,
				false, IPortSlot.NO_PRICING_DATE, false);

		final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
		final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-1", port3, dischargeWindow, 0, 150000000, 0, Long.MAX_VALUE,
				new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(200)), 1, IPortSlot.NO_PRICING_DATE, false);

		final ICargo cargo1 = builder.createCargo(Lists.newArrayList(loadSlot, dischargeSlot), false);

		builder.setPortToPortDistance(port1, port2, IMultiMatrixProvider.Default_Key, 12 * 25);
		builder.setPortToPortDistance(port2, port3, IMultiMatrixProvider.Default_Key, 12 * 25);
		builder.setPortToPortDistance(port3, port4, IMultiMatrixProvider.Default_Key, 12 * 25);

		final IOptimisationData data = builder.getOptimisationData();

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndProvider = injector.getInstance(IStartEndRequirementProvider.class);

		final ScheduleCalculator scheduler = injector.getInstance(ScheduleCalculator.class);

		final IResource resource = vesselProvider.getResource(vessel1);

		final ISequenceElement startElement = startEndProvider.getStartElement(resource);
		final ISequenceElement endElement = startEndProvider.getEndElement(resource);

		final ISequenceElement dischargeElement = portSlotProvider.getElement(dischargeSlot);
		final ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
		final List<ISequenceElement> sequenceList = CollectionsUtil.makeArrayList(startElement, loadElement, dischargeElement, endElement);

		final ISequence sequence = new ListSequence(sequenceList);

		// Schedule sequence
		final int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
		final AnnotatedSolution annotatedSolution = new AnnotatedSolution();
		final ISequences sequences = new Sequences(Collections.singletonList(resource), CollectionsUtil.<IResource, ISequence> makeHashMap(resource, sequence));
		final ScheduledSequences scheduledSequence = scheduler.schedule(sequences, new int[][] { expectedArrivalTimes }, annotatedSolution);
		Assert.assertNotNull(scheduledSequence);
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

			Assert.assertEquals((900l * 18l / 24 * baseFuelUnitPrice) / Calculator.HighScaleFactor, journeyEvent.getFuelCost(FuelComponent.Base));
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
			Assert.assertEquals(6 * 400 / 24l, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals((6l * 400 * baseFuelUnitPrice) / 24l / Calculator.HighScaleFactor, idleEvent.getFuelCost(FuelComponent.IdleBase));

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

			Assert.assertEquals(18 * 1200 / 24l, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((18l * 1200l * 500) / 24l / Calculator.ScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(18l * laden_nboRateInM3PerDay * cargoCVValue / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			// Assert.assertEquals(25 * 1200 * 2,
			// journeyEvent.getFuelConsumption(FuelComponent.NBO,
			// FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(18 * 1200 * 2 * 200 / 24l, journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(18 * 400 / 24l, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals((18l * 400 * baseFuelUnitPrice) / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
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

			Assert.assertEquals(6 * 1000 / 24l, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals((6l * 1000 * 500) / 24l / Calculator.ScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(6l * 1000 * cargoCVValue / 24l / Calculator.HighScaleFactor, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(6 * 1000 * 2 * 200 / 24l, idleEvent.getFuelCost(FuelComponent.IdleNBO));

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
			Assert.assertEquals(18 * 900 / 24l, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals((18l * 900 * baseFuelUnitPrice) / 24l / Calculator.HighScaleFactor, journeyEvent.getFuelCost(FuelComponent.Base));
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
			Assert.assertEquals(6 * 400 / 24l, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals((6l * 400 * baseFuelUnitPrice) / 24l / Calculator.HighScaleFactor, idleEvent.getFuelCost(FuelComponent.IdleBase));

			final IPortVisitEvent event = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(75, event.getStartTime());
			Assert.assertEquals(75, event.getEndTime());
			Assert.assertEquals(endElement, event.getSequenceElement());
		}
	}

	private Injector createTestInjector() {

		final Injector injector = Guice.createInjector(new DataComponentProviderModule(), new AbstractModule() {
			@Override
			protected void configure() {
				bind(VoyagePlanIterator.class);
				bind(VoyagePlanAnnotator.class);
				bind(VoyagePlanner.class);
				bind(ScheduleCalculator.class);
				bind(ICharterRateCalculator.class).to(VesselStartDateCharterRateCalculator.class);
				bind(IVolumeAllocator.class).toInstance(new IVolumeAllocator() {

					@Override
					public AllocationRecord createAllocationRecord(IVessel vessel, int vesselStartTime, VoyagePlan plan, List<Integer> arrivalTimes) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public IAllocationAnnotation allocate(AllocationRecord allocationRecord) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public IAllocationAnnotation allocate(IVessel vessel, int vesselStartTime, VoyagePlan plan, List<Integer> arrivalTimes) {
						// TODO Auto-generated method stub
						return null;
					}
				});
				;
				bind(SchedulerBuilder.class);
				bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
				bind(IVoyagePlanOptimiser.class).to(VoyagePlanOptimiser.class);
			}
		});
		return injector;
	}
}
