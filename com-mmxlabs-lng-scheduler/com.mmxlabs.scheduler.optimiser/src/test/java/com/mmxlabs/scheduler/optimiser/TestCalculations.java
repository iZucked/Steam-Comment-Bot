/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;


import java.util.List;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.events.IDischargeEvent;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.ILoadEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SimpleSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlanAnnotator;

/**
 * Set of unit tests to check that the expected costs are calculated from an
 * input set of data.
 * 
 */
public class TestCalculations {

	/**
	 * 
	 * 
	 * TODO: Need to test NBO speed (lets increase rate so we get e.g. 13/14
	 * knots) *** Case 1, but with higher NBO speed/rates Need to trigger
	 * Base_Supplemental calculations Need to test load/discharge volumes +
	 * cost/revenue calcs
	 */

	/**
	 * This tests the fuel costs + consumptions when travelling at slowest speed
	 * with no idle time.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Test
	public void testCalculations1() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port1 = builder.createPort("port-1", false, null);
		final IPort port2 = builder.createPort("port-2", false, null);
		final IPort port3 = builder.createPort("port-3", false, null);
		final IPort port4 = builder.createPort("port-4", false, null);

		final int minSpeed = 12000;
		final int maxSpeed = 20000;
		final int capacity = 150000000;
		final int baseFuelUnitPrice = 400000;
		final IVesselClass vesselClass1 = builder.createVesselClass("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuelUnitPrice, 500, 0, 0, Integer.MAX_VALUE, 0, 0);

		final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<Integer, Long>();
		ladenKeypoints.put(12000, 600l);
		ladenKeypoints.put(20000, 1400l);
		final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(
				ladenKeypoints);

		builder.setVesselClassStateParamaters(vesselClass1, VesselState.Laden,
				1200, 1000, 500, ladenConsumptionCalculator);

		final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<Integer, Long>();
		ballastKeypoints.put(12000, 500l);
		ballastKeypoints.put(20000, 1300l);
		final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(
				ballastKeypoints);

		builder.setVesselClassStateParamaters(vesselClass1,
				VesselState.Ballast, 1000, 800, 400,
				ballastConsumptionCalculator);

		final IStartEndRequirement startRequirement = builder
				.createStartEndRequirement(port1, builder.createTimeWindow(0, 0));
		final IStartEndRequirement endRequirement = builder
				.createStartEndRequirement(port4, builder.createTimeWindow(75, 75));

		final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, 0,
 startRequirement, endRequirement, 0, 0, 0);

		final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
		final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2,
				loadWindow, 0, 150000000, new FixedPriceContract(5000), 2000, 1, false, false);

		final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
		final IDischargeSlot dischargeSlot = builder.createDischargeSlot(
				"discharge-1", port3, dischargeWindow, 0, 150000000, new ConstantValueCurve(5000), 1);

		final ICargo cargo1 = builder.createCargo("cargo-1", loadSlot,
				dischargeSlot);

		builder.setPortToPortDistance(port1, port2,
				IMultiMatrixProvider.Default_Key, 12 * 24);
		builder.setPortToPortDistance(port2, port3,
				IMultiMatrixProvider.Default_Key, 12 * 24);
		builder.setPortToPortDistance(port3, port4,
				IMultiMatrixProvider.Default_Key, 12 * 24);

		final IOptimisationData<ISequenceElement> data = builder
				.getOptimisationData();

		final MockSequenceScheduler<ISequenceElement> scheduler = new MockSequenceScheduler<ISequenceElement>();

		scheduler.setDistanceProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portDistanceProvider,
				IMultiMatrixProvider.class));

		final IElementDurationProviderEditor<ISequenceElement> durationsProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_elementDurationsProvider,
						IElementDurationProviderEditor.class);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portProvider, IPortProvider.class));
		scheduler.setTimeWindowProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider,
				ITimeWindowDataComponentProvider.class));
		final IPortSlotProvider<ISequenceElement> portSlotProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProvider.class);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portTypeProvider,
				IPortTypeProvider.class));
		final IVesselProvider vesselProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		scheduler.setVesselProvider(vesselProvider);

		final IStartEndRequirementProvider<ISequenceElement> startEndProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_startEndRequirementProvider,
						IStartEndRequirementProvider.class);

		
		final LNGVoyageCalculator<ISequenceElement> voyageCalculator = new LNGVoyageCalculator<ISequenceElement>();
		final IRouteCostProvider routeCostProvider = data.getDataComponentProvider(SchedulerConstants.DCP_routePriceProvider, IRouteCostProvider.class);
		scheduler.setRouteCostProvider(routeCostProvider);
		voyageCalculator.setRouteCostDataComponentProvider(routeCostProvider);
		voyageCalculator.init();
		
		final VoyagePlanOptimiser<ISequenceElement> voyagePlanOptimiser = new VoyagePlanOptimiser<ISequenceElement>(
				voyageCalculator);
		
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		// This may throw IllegalStateException if not all
		// the elements are set.
		scheduler.init();

		final IResource resource = vesselProvider.getResource(vessel1);

		final ISequenceElement startElement = startEndProvider
				.getStartElement(resource);
		final ISequenceElement endElement = startEndProvider
				.getEndElement(resource);

		final ISequenceElement dischargeElement = portSlotProvider
				.getElement(dischargeSlot);
		final ISequenceElement loadElement = portSlotProvider
				.getElement(loadSlot);
		final List<ISequenceElement> sequenceList = CollectionsUtil
				.makeArrayList(startElement, loadElement, dischargeElement,
						endElement);

		final ISequence<ISequenceElement> sequence = new ListSequence<ISequenceElement>(
				sequenceList);

		final VoyagePlanAnnotator<ISequenceElement> annotator = new VoyagePlanAnnotator<ISequenceElement>();
		annotator.setPortSlotProvider(portSlotProvider);

		// Schedule sequence
		int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
		final ScheduledSequence plansAndStartTime = scheduler
				.schedule(resource, sequence, expectedArrivalTimes);

		Assert.assertNotNull(plansAndStartTime);

		final AnnotatedSolution<ISequenceElement> annotatedSolution = new AnnotatedSolution<ISequenceElement>();
		annotator.annotateFromVoyagePlan(plansAndStartTime.getResource(), plansAndStartTime.getVoyagePlans(), plansAndStartTime.getStartTime(), annotatedSolution); 

		// TODO: Start checking results
		{
			Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
			Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class));
			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(startElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(1, event.getStartTime());
			Assert.assertEquals(1, event.getEndTime());
			Assert.assertEquals(startElement, event.getSequenceElement());
		}

		{

			final IJourneyEvent<ISequenceElement> journeyEvent = annotatedSolution.getElementAnnotations().
					getAnnotation(loadElement,
							SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			Assert.assertEquals(1, journeyEvent.getStartTime());

			Assert.assertEquals(24 * 12, journeyEvent.getDistance());
			Assert.assertEquals(24, journeyEvent.getDuration());

			Assert.assertEquals(12000, journeyEvent.getSpeed());

			// Expect only base fuel use on this ballast leg

			Assert.assertSame(VesselState.Ballast,
					journeyEvent.getVesselState());

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(500 * 24, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));	
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals((500l * 24l * baseFuelUnitPrice) / 1000,
					journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleBase));

			final IIdleEvent<ISequenceElement> idleEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(loadElement, SchedulerConstants.AI_idleInfo,
							IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(loadElement, idleEvent.getSequenceElement());
			Assert.assertSame(port2, idleEvent.getPort());
			Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
			Assert.assertEquals(0, idleEvent.getDuration());
			Assert.assertEquals(25, idleEvent.getStartTime());
			Assert.assertEquals(25, idleEvent.getEndTime());

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.IdleBase));

			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(loadElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(1, event.getDuration());
			Assert.assertEquals(25, event.getStartTime());
			Assert.assertEquals(26, event.getEndTime());
			Assert.assertEquals(loadElement, event.getSequenceElement());
		}

		{

			final IJourneyEvent<ISequenceElement> journeyEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			Assert.assertEquals(26, journeyEvent.getStartTime());
			Assert.assertEquals(24 * 12, journeyEvent.getDistance());
			Assert.assertEquals(24, journeyEvent.getDuration());

			// Expect only base fuel use on this ballast leg

			Assert.assertSame(VesselState.Laden, journeyEvent.getVesselState());

			Assert.assertEquals(12000, journeyEvent.getSpeed());

			Assert.assertEquals(24 * 1200, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((24l * 1200l * 500) / 1000, journeyEvent
					.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));
			// Assert.assertEquals(25 * 1200 * 2,
			// journeyEvent.getFuelConsumption(FuelComponent.NBO,
			// FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(24 * 1200 * 2 * 5,
					journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleBase));

			final IIdleEvent<ISequenceElement> idleEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(dischargeElement, idleEvent.getSequenceElement());
			Assert.assertSame(port3, idleEvent.getPort());
			Assert.assertSame(VesselState.Laden, idleEvent.getVesselState());
			Assert.assertEquals(0, idleEvent.getDuration());
			Assert.assertEquals(50, idleEvent.getStartTime());
			Assert.assertEquals(50, idleEvent.getEndTime());

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.IdleBase));

			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(1, event.getDuration());
			Assert.assertEquals(50, event.getStartTime());
			Assert.assertEquals(51, event.getEndTime());
			Assert.assertEquals(dischargeElement, event.getSequenceElement());
		}

		{

			final IJourneyEvent<ISequenceElement> journeyEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(endElement,
							SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);

			Assert.assertEquals(24 * 12, journeyEvent.getDistance());
			Assert.assertEquals(24, journeyEvent.getDuration());
			Assert.assertEquals(51, journeyEvent.getStartTime());

			// Expect only base fuel use on this ballast leg

			Assert.assertSame(VesselState.Ballast,
					journeyEvent.getVesselState());

			Assert.assertEquals(12000, journeyEvent.getSpeed());

			Assert.assertEquals(24 * 1000, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((24l * 1000l * 500) / 1000, journeyEvent
					.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));
			// Assert.assertEquals(25 * 1000 * 2,
			// journeyEvent.getFuelConsumption(FuelComponent.NBO,
			// FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(24 * 1000 * 2 * 5,
					journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleBase));

			final IIdleEvent<ISequenceElement> idleEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(endElement, SchedulerConstants.AI_idleInfo,
							IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(endElement, idleEvent.getSequenceElement());
			Assert.assertSame(port4, idleEvent.getPort());
			Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
			Assert.assertEquals(0, idleEvent.getDuration());
			Assert.assertEquals(75, idleEvent.getStartTime());
			Assert.assertEquals(75, idleEvent.getEndTime());

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.IdleBase));

			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(endElement, SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
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
	@SuppressWarnings({ "unchecked", "unused" })
	@Test
	public void testCalculations2() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port1 = builder.createPort("port-1", false, null);
		final IPort port2 = builder.createPort("port-2", false, null);
		final IPort port3 = builder.createPort("port-3", false, null);
		final IPort port4 = builder.createPort("port-4", false, null);

		final int minSpeed = 16000;
		final int maxSpeed = 20000;
		final int capacity = 150000000;
		final int baseFuelUnitPrice = 400000;
		final IVesselClass vesselClass1 = builder.createVesselClass("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuelUnitPrice, 500, 0, 0, Integer.MAX_VALUE, 0, 0);

		final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<Integer, Long>();
		ladenKeypoints.put(12000, 600l);
		ladenKeypoints.put(20000, 1400l);
		final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(
				ladenKeypoints);

		builder.setVesselClassStateParamaters(vesselClass1, VesselState.Laden,
				1200, 1000, 500, ladenConsumptionCalculator);

		final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<Integer, Long>();
		ballastKeypoints.put(12000, 500l);
		ballastKeypoints.put(20000, 1300l);
		final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(
				ballastKeypoints);

		builder.setVesselClassStateParamaters(vesselClass1,
				VesselState.Ballast, 1000, 800, 400,
				ballastConsumptionCalculator);

		final IStartEndRequirement startRequirement = builder
				.createStartEndRequirement(port1, builder.createTimeWindow(0, 0));
		final IStartEndRequirement endRequirement = builder
				.createStartEndRequirement(port4, builder.createTimeWindow(75, 75));

		final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, 0,
 startRequirement, endRequirement, 0, 0, 0);

		final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
		final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2,
				loadWindow, 0, 150000000, new FixedPriceContract(5000), 2000, 1, false, false);

		final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
		final IDischargeSlot dischargeSlot = builder.createDischargeSlot(
				"discharge-1", port3, dischargeWindow, 0, 150000000, new ConstantValueCurve(5000), 1);

		final ICargo cargo1 = builder.createCargo("cargo-1", loadSlot,
				dischargeSlot);

		builder.setPortToPortDistance(port1, port2,
				IMultiMatrixProvider.Default_Key, 12 * 25);
		builder.setPortToPortDistance(port2, port3,
				IMultiMatrixProvider.Default_Key, 12 * 25);
		builder.setPortToPortDistance(port3, port4,
				IMultiMatrixProvider.Default_Key, 12 * 25);

		final IOptimisationData<ISequenceElement> data = builder
				.getOptimisationData();

		final SimpleSequenceScheduler<ISequenceElement> scheduler = new SimpleSequenceScheduler<ISequenceElement>();

		scheduler.setDistanceProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portDistanceProvider,
				IMultiMatrixProvider.class));
		final IElementDurationProviderEditor<ISequenceElement> durationsProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_elementDurationsProvider,
						IElementDurationProviderEditor.class);

		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portProvider, IPortProvider.class));
		scheduler.setTimeWindowProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider,
				ITimeWindowDataComponentProvider.class));
		final IPortSlotProvider<ISequenceElement> portSlotProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProvider.class);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portTypeProvider,
				IPortTypeProvider.class));
		final IVesselProvider vesselProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		scheduler.setVesselProvider(vesselProvider);

		final IStartEndRequirementProvider<ISequenceElement> startEndProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_startEndRequirementProvider,
						IStartEndRequirementProvider.class);

		final LNGVoyageCalculator<ISequenceElement> voyageCalculator = new LNGVoyageCalculator<ISequenceElement>();
		final IRouteCostProvider routeCostProvider = data.getDataComponentProvider(SchedulerConstants.DCP_routePriceProvider, IRouteCostProvider.class);
		scheduler.setRouteCostProvider(routeCostProvider);
		voyageCalculator.setRouteCostDataComponentProvider(routeCostProvider);
		voyageCalculator.init();
		
		final VoyagePlanOptimiser<ISequenceElement> voyagePlanOptimiser = new VoyagePlanOptimiser<ISequenceElement>(
				voyageCalculator);

		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		// This may throw IllegalStateException if not all
		// the elements are set.
		scheduler.init();

		final IResource resource = vesselProvider.getResource(vessel1);

		final ISequenceElement startElement = startEndProvider
				.getStartElement(resource);
		final ISequenceElement endElement = startEndProvider
				.getEndElement(resource);

		final ISequenceElement dischargeElement = portSlotProvider
				.getElement(dischargeSlot);
		final ISequenceElement loadElement = portSlotProvider
				.getElement(loadSlot);
		final List<ISequenceElement> sequenceList = CollectionsUtil
				.makeArrayList(startElement, loadElement, dischargeElement,
						endElement);

		final ISequence<ISequenceElement> sequence = new ListSequence<ISequenceElement>(
				sequenceList);

		final VoyagePlanAnnotator<ISequenceElement> annotator = new VoyagePlanAnnotator<ISequenceElement>();
		annotator.setPortSlotProvider(portSlotProvider);

		// Schedule sequence
		int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
		final ScheduledSequence plansAndStartTime = scheduler.schedule(
				resource, sequence, expectedArrivalTimes);

		Assert.assertNotNull(plansAndStartTime);

		final AnnotatedSolution<ISequenceElement> annotatedSolution = new AnnotatedSolution<ISequenceElement>();
		annotator.annotateFromVoyagePlan(plansAndStartTime.getResource(), plansAndStartTime.getVoyagePlans(), plansAndStartTime.getStartTime(), annotatedSolution);
		
		// TODO: Start checking results
		{
			Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
			Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class));
			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(startElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(1, event.getStartTime());
			Assert.assertEquals(1, event.getEndTime());
			Assert.assertEquals(startElement, event.getSequenceElement());
		}

		{

			final IJourneyEvent<ISequenceElement> journeyEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(loadElement,
							SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);

			Assert.assertEquals(1, journeyEvent.getStartTime());
			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(18, journeyEvent.getDuration());

			Assert.assertEquals(16000, journeyEvent.getSpeed());

			// Expect only base fuel use on this ballast leg

			Assert.assertSame(VesselState.Ballast,
					journeyEvent.getVesselState());

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(900 * 18, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals((900l * 18l * baseFuelUnitPrice) / 1000,
					journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleBase));

			final IIdleEvent<ISequenceElement> idleEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(loadElement, SchedulerConstants.AI_idleInfo,
							IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(loadElement, idleEvent.getSequenceElement());
			Assert.assertSame(port2, idleEvent.getPort());
			Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
			Assert.assertEquals(6, idleEvent.getDuration());
			Assert.assertEquals(19, idleEvent.getStartTime());
			Assert.assertEquals(25, idleEvent.getEndTime());

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(6 * 400, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals((6l * 400 * baseFuelUnitPrice) / 1000,
					idleEvent.getFuelCost(FuelComponent.IdleBase));

			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(loadElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(1, event.getDuration());
			Assert.assertEquals(25, event.getStartTime());
			Assert.assertEquals(26, event.getEndTime());
			Assert.assertEquals(loadElement, event.getSequenceElement());
		}

		{

			final IJourneyEvent<ISequenceElement> journeyEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);

			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(18, journeyEvent.getDuration());
			Assert.assertEquals(26, journeyEvent.getStartTime());

			// Expect only base fuel use on this ballast leg

			Assert.assertSame(VesselState.Laden, journeyEvent.getVesselState());

			Assert.assertEquals(16000, journeyEvent.getSpeed());

			Assert.assertEquals(18 * 1200, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((18l * 1200l * 500) / 1000, journeyEvent
					.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));
			// Assert.assertEquals(25 * 1200 * 2,
			// journeyEvent.getFuelConsumption(FuelComponent.NBO,
			// FuelUnit.MMBTu));

			Assert.assertEquals(18 * 800, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals((18l * 800 * 500) / 1000, journeyEvent
					.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(18 * 1200 * 2 * 5,
					journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(18 * 800 * 2 * 5,
					journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleBase));

			final IIdleEvent<ISequenceElement> idleEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(dischargeElement, idleEvent.getSequenceElement());
			Assert.assertSame(port3, idleEvent.getPort());
			Assert.assertSame(VesselState.Laden, idleEvent.getVesselState());
			Assert.assertEquals(6, idleEvent.getDuration());
			Assert.assertEquals(44, idleEvent.getStartTime());
			Assert.assertEquals(50, idleEvent.getEndTime());

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(6 * 1000, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals((6l * 1000 * 500) / 1000, idleEvent
					.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(6 * 1000 * 2 * 5,
					idleEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.IdleBase));

			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(1, event.getDuration());
			Assert.assertEquals(50, event.getStartTime());
			Assert.assertEquals(51, event.getEndTime());
			Assert.assertEquals(dischargeElement, event.getSequenceElement());
		}

		{

			final IJourneyEvent<ISequenceElement> journeyEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(endElement,
							SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);

			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(18, journeyEvent.getDuration());

			// Expect only base fuel use on this ballast leg

			Assert.assertSame(VesselState.Ballast,
					journeyEvent.getVesselState());

			Assert.assertEquals(16000, journeyEvent.getSpeed());

			Assert.assertEquals(18 * 1000, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((18l * 1000l * 500) / 1000, journeyEvent
					.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));
			// Assert.assertEquals(25 * 1000 * 2,
			// journeyEvent.getFuelConsumption(FuelComponent.NBO,
			// FuelUnit.MMBTu));
			//
			Assert.assertEquals(18 * 800, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals((18l * 800l * 500) / 1000, journeyEvent
					.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(18 * 1000 * 2 * 5,
					journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(18 * 800 * 2 * 5,
					journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleBase));

			final IIdleEvent<ISequenceElement> idleEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(endElement, SchedulerConstants.AI_idleInfo,
							IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(endElement, idleEvent.getSequenceElement());
			Assert.assertSame(port4, idleEvent.getPort());
			Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
			Assert.assertEquals(6, idleEvent.getDuration());
			Assert.assertEquals(69, idleEvent.getStartTime());
			Assert.assertEquals(75, idleEvent.getEndTime());

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(6 * 800, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals((6 * 800 * 500) / 1000, idleEvent
					.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(6 * 800 * 2 * 5,
					idleEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.IdleBase));

			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(endElement, SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(75, event.getStartTime());
			Assert.assertEquals(75, event.getEndTime());
			Assert.assertEquals(endElement, event.getSequenceElement());
		}
	}

	/**
	 * Like case 1, but force a higher travelling speed to get idle time +
	 * Base_Supplemental (make NBO more costly than Base)
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Test
	public void testCalculations3() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port1 = builder.createPort("port-1", false, null);
		final IPort port2 = builder.createPort("port-2", false, null);
		final IPort port3 = builder.createPort("port-3", false, null);
		final IPort port4 = builder.createPort("port-4", false, null);

		final int minSpeed = 16000;
		final int maxSpeed = 20000;
		final int capacity = 150000000;
		final int baseFuelUnitPrice = 1000;
		final IVesselClass vesselClass1 = builder.createVesselClass("vessel-class-1", minSpeed, maxSpeed, capacity, 0, baseFuelUnitPrice, 500, 0, 0, Integer.MAX_VALUE, 0, 0);

		final TreeMap<Integer, Long> ladenKeypoints = new TreeMap<Integer, Long>();
		ladenKeypoints.put(12000, 600l);
		ladenKeypoints.put(20000, 1400l);
		final InterpolatingConsumptionRateCalculator ladenConsumptionCalculator = new InterpolatingConsumptionRateCalculator(
				ladenKeypoints);

		builder.setVesselClassStateParamaters(vesselClass1, VesselState.Laden,
				1200, 1000, 500, ladenConsumptionCalculator);

		final TreeMap<Integer, Long> ballastKeypoints = new TreeMap<Integer, Long>();
		ballastKeypoints.put(12000, 500l);
		ballastKeypoints.put(20000, 1300l);
		final InterpolatingConsumptionRateCalculator ballastConsumptionCalculator = new InterpolatingConsumptionRateCalculator(
				ballastKeypoints);

		builder.setVesselClassStateParamaters(vesselClass1,
				VesselState.Ballast, 1000, 800, 400,
				ballastConsumptionCalculator);

		final IStartEndRequirement startRequirement = builder
				.createStartEndRequirement(port1, builder.createTimeWindow(0, 0));
		final IStartEndRequirement endRequirement = builder
				.createStartEndRequirement(port4, builder.createTimeWindow(75, 75));

		final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1, 0, startRequirement, endRequirement, 0, 0, 0);

		final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
		final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2, loadWindow, 0, 150000000, new FixedPriceContract(5000), 2000, 1, false, false);

		final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
		final IDischargeSlot dischargeSlot = builder.createDischargeSlot(
				"discharge-1", port3, dischargeWindow, 0, 150000000, new ConstantValueCurve(200000), 1);

		final ICargo cargo1 = builder.createCargo("cargo-1", loadSlot,
				dischargeSlot);

		builder.setPortToPortDistance(port1, port2,
				IMultiMatrixProvider.Default_Key, 12 * 25);
		builder.setPortToPortDistance(port2, port3,
				IMultiMatrixProvider.Default_Key, 12 * 25);
		builder.setPortToPortDistance(port3, port4,
				IMultiMatrixProvider.Default_Key, 12 * 25);

		final IOptimisationData<ISequenceElement> data = builder
				.getOptimisationData();

		final SimpleSequenceScheduler<ISequenceElement> scheduler = new SimpleSequenceScheduler<ISequenceElement>();

		scheduler.setDistanceProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portDistanceProvider,
				IMultiMatrixProvider.class));
		final IElementDurationProviderEditor<ISequenceElement> durationsProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_elementDurationsProvider,
						IElementDurationProviderEditor.class);

		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portProvider, IPortProvider.class));
		scheduler.setTimeWindowProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider,
				ITimeWindowDataComponentProvider.class));
		final IPortSlotProvider<ISequenceElement> portSlotProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProvider.class);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portTypeProvider,
				IPortTypeProvider.class));
		final IVesselProvider vesselProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		scheduler.setVesselProvider(vesselProvider);

		final IStartEndRequirementProvider<ISequenceElement> startEndProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_startEndRequirementProvider,
						IStartEndRequirementProvider.class);

		final LNGVoyageCalculator<ISequenceElement> voyageCalculator = new LNGVoyageCalculator<ISequenceElement>();
		final IRouteCostProvider routeCostProvider = data.getDataComponentProvider(SchedulerConstants.DCP_routePriceProvider, IRouteCostProvider.class);
		scheduler.setRouteCostProvider(routeCostProvider);
		voyageCalculator.setRouteCostDataComponentProvider(routeCostProvider);
		voyageCalculator.init();
		
		final VoyagePlanOptimiser<ISequenceElement> voyagePlanOptimiser = new VoyagePlanOptimiser<ISequenceElement>(
				voyageCalculator);

		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		// This may throw IllegalStateException if not all
		// the elements are set.
		scheduler.init();

		final IResource resource = vesselProvider.getResource(vessel1);

		final ISequenceElement startElement = startEndProvider
				.getStartElement(resource);
		final ISequenceElement endElement = startEndProvider
				.getEndElement(resource);

		final ISequenceElement dischargeElement = portSlotProvider
				.getElement(dischargeSlot);
		final ISequenceElement loadElement = portSlotProvider
				.getElement(loadSlot);
		final List<ISequenceElement> sequenceList = CollectionsUtil
				.makeArrayList(startElement, loadElement, dischargeElement,
						endElement);

		final ISequence<ISequenceElement> sequence = new ListSequence<ISequenceElement>(
				sequenceList);

		final VoyagePlanAnnotator<ISequenceElement> annotator = new VoyagePlanAnnotator<ISequenceElement>();
		annotator.setPortSlotProvider(portSlotProvider);

		// Schedule sequence
		int[] expectedArrivalTimes = new int[] { 1, 25, 50, 75 };
		final ScheduledSequence plansAndStartTime = scheduler.schedule(
				resource, sequence, expectedArrivalTimes);


		Assert.assertNotNull(plansAndStartTime);

		final AnnotatedSolution<ISequenceElement> annotatedSolution = new AnnotatedSolution<ISequenceElement>();
		annotator.annotateFromVoyagePlan(plansAndStartTime.getResource(), plansAndStartTime.getVoyagePlans(), plansAndStartTime.getStartTime(), annotatedSolution);


		// TODO: Start checking results
		{
			Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
			Assert.assertNull(annotatedSolution.getElementAnnotations().getAnnotation(startElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class));
			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(startElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(1, event.getStartTime());
			Assert.assertEquals(1, event.getEndTime());
			Assert.assertEquals(startElement, event.getSequenceElement());
		}

		{

			final IJourneyEvent<ISequenceElement> journeyEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(loadElement,
							SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);

			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(18, journeyEvent.getDuration());
			Assert.assertEquals(1, journeyEvent.getStartTime());

			Assert.assertEquals(16000, journeyEvent.getSpeed());

			// Expect only base fuel use on this ballast leg

			Assert.assertSame(VesselState.Ballast,
					journeyEvent.getVesselState());

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(900 * 18, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals((900l * 18l * baseFuelUnitPrice) / 1000,
					journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleBase));

			final IIdleEvent<ISequenceElement> idleEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(loadElement, SchedulerConstants.AI_idleInfo,
							IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(loadElement, idleEvent.getSequenceElement());
			Assert.assertSame(port2, idleEvent.getPort());
			Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
			Assert.assertEquals(6, idleEvent.getDuration());
			Assert.assertEquals(19, idleEvent.getStartTime());
			Assert.assertEquals(25, idleEvent.getEndTime());

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(6 * 400, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals((6l * 400 * baseFuelUnitPrice) / 1000,
					idleEvent.getFuelCost(FuelComponent.IdleBase));

			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(loadElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(1, event.getDuration());
			Assert.assertEquals(25, event.getStartTime());
			Assert.assertEquals(26, event.getEndTime());
			Assert.assertEquals(loadElement, event.getSequenceElement());

			Assert.assertTrue(event instanceof ILoadEvent);
		}

		{

			final IJourneyEvent<ISequenceElement> journeyEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);

			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(18, journeyEvent.getDuration());
			Assert.assertEquals(26, journeyEvent.getStartTime());

			// Expect only base fuel use on this ballast leg

			Assert.assertSame(VesselState.Laden, journeyEvent.getVesselState());

			Assert.assertEquals(16000, journeyEvent.getSpeed());

			Assert.assertEquals(18 * 1200, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((18l * 1200l * 500) / 1000, journeyEvent
					.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));
			// Assert.assertEquals(25 * 1200 * 2,
			// journeyEvent.getFuelConsumption(FuelComponent.NBO,
			// FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(18 * 1200 * 2 * 200,
					journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(18 * 400, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals((18 * 400 * baseFuelUnitPrice) / 1000,
					journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleBase));

			final IIdleEvent<ISequenceElement> idleEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(dischargeElement, idleEvent.getSequenceElement());
			Assert.assertSame(port3, idleEvent.getPort());
			Assert.assertSame(VesselState.Laden, idleEvent.getVesselState());
			Assert.assertEquals(6, idleEvent.getDuration());
			Assert.assertEquals(44, idleEvent.getStartTime());
			Assert.assertEquals(50, idleEvent.getEndTime());

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(6 * 1000, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals((6l * 1000 * 500) / 1000, idleEvent
					.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(6 * 1000 * 2 * 200,
					idleEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.IdleBase));

			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(1, event.getDuration());
			Assert.assertEquals(50, event.getStartTime());
			Assert.assertEquals(51, event.getEndTime());
			Assert.assertEquals(dischargeElement, event.getSequenceElement());

			Assert.assertTrue(event instanceof IDischargeEvent);

			final IDischargeEvent<ISequenceElement> discharge = (IDischargeEvent<ISequenceElement>) event;
			Assert.assertEquals(loadSlot.getMaxLoadVolume()
					- (18 * 1200 + 6 * 1000), discharge.getDischargeVolume());

			Assert.assertEquals(
					(loadSlot.getMaxLoadVolume() - (18 * 1200 + 6 * 1000)) * 2 * 200,
					discharge.getSalesPrice());
		}

		{

			final IJourneyEvent<ISequenceElement> journeyEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(endElement,
							SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);

			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(18, journeyEvent.getDuration());
			Assert.assertEquals(51, journeyEvent.getStartTime());

			// Expect only base fuel use on this ballast leg

			Assert.assertSame(VesselState.Ballast,
					journeyEvent.getVesselState());

			Assert.assertEquals(16000, journeyEvent.getSpeed());

			// Why still nbo? NEED TO CHECK vpo COST CALS. -- Basefuel should be
			// the cheaper option!
			// -- is base fuel being evaluated or is the choice not being
			// allowed?

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));
			// Assert.assertEquals(25 * 1000 * 2,
			// journeyEvent.getFuelConsumption(FuelComponent.NBO,
			// FuelUnit.MMBTu));
			//
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(18 * 900, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals((18l * 900 * baseFuelUnitPrice) / 1000,
					journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0,
					journeyEvent.getFuelCost(FuelComponent.IdleBase));

			final IIdleEvent<ISequenceElement> idleEvent = annotatedSolution.getElementAnnotations()
					.getAnnotation(endElement, SchedulerConstants.AI_idleInfo,
							IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(endElement, idleEvent.getSequenceElement());
			Assert.assertSame(port4, idleEvent.getPort());
			Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
			Assert.assertEquals(6, idleEvent.getDuration());
			Assert.assertEquals(69, idleEvent.getStartTime());
			Assert.assertEquals(75, idleEvent.getEndTime());

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.NBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.FBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleNBO, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.Base_Supplemental, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(6 * 400, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(
					FuelComponent.IdleBase, FuelUnit.MMBTu));

			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals((6 * 400 * baseFuelUnitPrice) / 1000,
					idleEvent.getFuelCost(FuelComponent.IdleBase));

			final IPortVisitEvent<ISequenceElement> event = annotatedSolution.getElementAnnotations()
					.getAnnotation(endElement, SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(75, event.getStartTime());
			Assert.assertEquals(75, event.getEndTime());
			Assert.assertEquals(endElement, event.getSequenceElement());
		}
	}

	private static class MockSequenceScheduler<T> extends
			AbstractSequenceScheduler<T> {

		@Override
		public ScheduledSequences schedule(ISequences<T> sequences, boolean b) {
			throw new UnsupportedOperationException(
					"Method invocation is not part of the tests!");
		}
	}
}
