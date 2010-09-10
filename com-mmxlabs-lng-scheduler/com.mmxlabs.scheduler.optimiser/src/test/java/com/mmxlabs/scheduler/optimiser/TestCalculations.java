package com.mmxlabs.scheduler.optimiser;

import java.util.List;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.impl.AnnotatedSequence;
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
import com.mmxlabs.scheduler.optimiser.events.IDischargeEvent;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.ILoadEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SimpleSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;
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
	 * TODO: Complete tests, fix up abstract sequence scheduler and rebase simple on that. Retest.
	 *       Then only use abstract sequence scheduler here, passing in the expected arrival times.
	 *  
	 * 
	 * TODO: Need to test NBO speed (lets increase rate so we get e.g. 13/14 knots)
	 *       *** Case 1, but with higher NBO speed/rates
	 *       Need to trigger Base_Supplemental calculations
	 *       Need to test load/discharge volumes + cost/revenue calcs
	 */
	
	
	/**
	 * This tests the fuel costs + consumptions when travelling at slowest speed with no idle time.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Test
	public void testCalculations1() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port1 = builder.createPort("port-1");
		final IPort port2 = builder.createPort("port-2");
		final IPort port3 = builder.createPort("port-2");
		final IPort port4 = builder.createPort("port-2");

		final int minSpeed = 12000;
		final int maxSpeed = 20000;
		final int capacity = 150000000;
		final int baseFuelUnitPrice = 400000;
		final IVesselClass vesselClass1 = builder.createVesselClass(
				"vessel-class-1", minSpeed, maxSpeed, capacity, 0,
				baseFuelUnitPrice, 500, 0);

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
				.createStartEndRequirement(port1, 0);
		final IStartEndRequirement endRequirement = builder
				.createStartEndRequirement(port4, 75);

		final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1,
				startRequirement, endRequirement);

		final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
		final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2,
				loadWindow, 0, 150000000, 5000, 2000);

		final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
		final IDischargeSlot dischargeSlot = builder.createDischargeSlot(
				"discharge-1", port3, dischargeWindow, 0, 150000000, 5000);

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
		scheduler.setDurationsProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_elementDurationsProvider,
				IElementDurationProvider.class));
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
		final VoyagePlanOptimiser<ISequenceElement> voyagePlanOptimiser = new VoyagePlanOptimiser<ISequenceElement>();
		voyagePlanOptimiser.setVoyageCalculator(voyageCalculator);

		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		// This may throw IllegalStateException if not all
		// the elements are set.
		scheduler.init();

		final IResource resource = vesselProvider.getResource(vessel1);

		final ISequenceElement startElement = startEndProvider
				.getStartElement(resource);
		final ISequenceElement endElement = startEndProvider
				.getEndElement(resource);

		ISequenceElement dischargeElement = portSlotProvider
				.getElement(dischargeSlot);
		ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
		final List<ISequenceElement> sequenceList = CollectionsUtil
				.makeArrayList(startElement, loadElement, dischargeElement,
						endElement);

		final ISequence<ISequenceElement> sequence = new ListSequence<ISequenceElement>(
				sequenceList);

		final VoyagePlanAnnotator<ISequenceElement> annotator = new VoyagePlanAnnotator<ISequenceElement>();
		annotator.setPortSlotProvider(portSlotProvider);

		// Schedule sequence
		final List<IVoyagePlan> plans = scheduler.schedule(resource, sequence);

		final AnnotatedSequence<ISequenceElement> annotatedSequence = new AnnotatedSequence<ISequenceElement>();
		annotator.annotateFromVoyagePlan(resource, plans, annotatedSequence);

		// TODO: Start checking results
		{
			Assert.assertNull(annotatedSequence.getAnnotation(startElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
			Assert.assertNull(annotatedSequence.getAnnotation(startElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class));
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(startElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(0, event.getStartTime());
			Assert.assertEquals(0, event.getEndTime());
			Assert.assertEquals(startElement, event.getSequenceElement());
		}

		{
			
			IJourneyEvent<ISequenceElement> journeyEvent = annotatedSequence.getAnnotation(loadElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			
			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(25, journeyEvent.getDuration());
			
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
			Assert.assertEquals(500 * 25, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));
			
			Assert.assertEquals((500l * 25l * baseFuelUnitPrice) / 1000, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));
			
			
			IIdleEvent<ISequenceElement> idleEvent = annotatedSequence.getAnnotation(loadElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
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

			
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(loadElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(25, event.getStartTime());
			Assert.assertEquals(25, event.getEndTime());
			Assert.assertEquals(loadElement, event.getSequenceElement());
		}

		
		{
			
			IJourneyEvent<ISequenceElement> journeyEvent = annotatedSequence.getAnnotation(dischargeElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			
			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(25, journeyEvent.getDuration());
			
			// Expect only base fuel use on this ballast leg
			
			Assert.assertSame(VesselState.Laden, journeyEvent.getVesselState());
			
			Assert.assertEquals(12000, journeyEvent.getSpeed());
			
			Assert.assertEquals(25 * 1200, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((25l * 1200l * 500) / 1000, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
//			Assert.assertEquals(25 * 1200 * 2, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(25 * 1200 * 2 * 5, journeyEvent.getFuelCost(FuelComponent.NBO));
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
			
			
			IIdleEvent<ISequenceElement> idleEvent = annotatedSequence.getAnnotation(dischargeElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
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

			
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(50, event.getStartTime());
			Assert.assertEquals(50, event.getEndTime());
			Assert.assertEquals(dischargeElement, event.getSequenceElement());
		}

{
			
			IJourneyEvent<ISequenceElement> journeyEvent = annotatedSequence.getAnnotation(endElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			
			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(25, journeyEvent.getDuration());
			
			// Expect only base fuel use on this ballast leg
			
			Assert.assertSame(VesselState.Ballast, journeyEvent.getVesselState());
			
			Assert.assertEquals(12000, journeyEvent.getSpeed());
			
			Assert.assertEquals(25 * 1000, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((25l * 1000l * 500) / 1000, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
//			Assert.assertEquals(25 * 1000 * 2, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(25 * 1000 * 2 * 5, journeyEvent.getFuelCost(FuelComponent.NBO));
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
			
			
			IIdleEvent<ISequenceElement> idleEvent = annotatedSequence.getAnnotation(endElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
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

			
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(endElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(75, event.getStartTime());
			Assert.assertEquals(75, event.getEndTime());
			Assert.assertEquals(endElement, event.getSequenceElement());
		}
	}
	

	/**
	 * Like case 1,  but force a higher travelling speed to get idle time + FBO 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Test
	public void testCalculations2() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port1 = builder.createPort("port-1");
		final IPort port2 = builder.createPort("port-2");
		final IPort port3 = builder.createPort("port-2");
		final IPort port4 = builder.createPort("port-2");

		final int minSpeed = 16000;
		final int maxSpeed = 20000;
		final int capacity = 150000000;
		final int baseFuelUnitPrice = 400000;
		final IVesselClass vesselClass1 = builder.createVesselClass(
				"vessel-class-1", minSpeed, maxSpeed, capacity, 0,
				baseFuelUnitPrice, 500, 0);

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
				.createStartEndRequirement(port1, 0);
		final IStartEndRequirement endRequirement = builder
				.createStartEndRequirement(port4, 75);

		final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1,
				startRequirement, endRequirement);

		final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
		final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2,
				loadWindow, 0, 150000000, 5000, 2000);

		final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
		final IDischargeSlot dischargeSlot = builder.createDischargeSlot(
				"discharge-1", port3, dischargeWindow, 0, 150000000, 5000);

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
		scheduler.setDurationsProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_elementDurationsProvider,
				IElementDurationProvider.class));
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
		final VoyagePlanOptimiser<ISequenceElement> voyagePlanOptimiser = new VoyagePlanOptimiser<ISequenceElement>();
		voyagePlanOptimiser.setVoyageCalculator(voyageCalculator);

		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		// This may throw IllegalStateException if not all
		// the elements are set.
		scheduler.init();

		final IResource resource = vesselProvider.getResource(vessel1);

		final ISequenceElement startElement = startEndProvider
				.getStartElement(resource);
		final ISequenceElement endElement = startEndProvider
				.getEndElement(resource);

		ISequenceElement dischargeElement = portSlotProvider
				.getElement(dischargeSlot);
		ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
		final List<ISequenceElement> sequenceList = CollectionsUtil
				.makeArrayList(startElement, loadElement, dischargeElement,
						endElement);

		final ISequence<ISequenceElement> sequence = new ListSequence<ISequenceElement>(
				sequenceList);

		final VoyagePlanAnnotator<ISequenceElement> annotator = new VoyagePlanAnnotator<ISequenceElement>();
		annotator.setPortSlotProvider(portSlotProvider);

		// Schedule sequence
		final List<IVoyagePlan> plans = scheduler.schedule(resource, sequence);

		final AnnotatedSequence<ISequenceElement> annotatedSequence = new AnnotatedSequence<ISequenceElement>();
		annotator.annotateFromVoyagePlan(resource, plans, annotatedSequence);

		// TODO: Start checking results
		{
			Assert.assertNull(annotatedSequence.getAnnotation(startElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
			Assert.assertNull(annotatedSequence.getAnnotation(startElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class));
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(startElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(0, event.getStartTime());
			Assert.assertEquals(0, event.getEndTime());
			Assert.assertEquals(startElement, event.getSequenceElement());
		}

		{
			
			IJourneyEvent<ISequenceElement> journeyEvent = annotatedSequence.getAnnotation(loadElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			
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
			Assert.assertEquals(900 * 18, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));
			
			Assert.assertEquals((900l * 18l * baseFuelUnitPrice) / 1000, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));
			
			
			IIdleEvent<ISequenceElement> idleEvent = annotatedSequence.getAnnotation(loadElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(loadElement, idleEvent.getSequenceElement());
			Assert.assertSame(port2, idleEvent.getPort());
			Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
			Assert.assertEquals(7, idleEvent.getDuration());
			Assert.assertEquals(18, idleEvent.getStartTime());
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
			Assert.assertEquals(7 * 400, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals((7l * 400 * baseFuelUnitPrice) / 1000, idleEvent.getFuelCost(FuelComponent.IdleBase));

			
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(loadElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(25, event.getStartTime());
			Assert.assertEquals(25, event.getEndTime());
			Assert.assertEquals(loadElement, event.getSequenceElement());
		}

		
		{
			
			IJourneyEvent<ISequenceElement> journeyEvent = annotatedSequence.getAnnotation(dischargeElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			
			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(18, journeyEvent.getDuration());
			
			// Expect only base fuel use on this ballast leg
			
			Assert.assertSame(VesselState.Laden, journeyEvent.getVesselState());
			
			Assert.assertEquals(16000, journeyEvent.getSpeed());
			
			Assert.assertEquals(18 * 1200, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((18l * 1200l * 500) / 1000, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
//			Assert.assertEquals(25 * 1200 * 2, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(18 * 800, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals((18l * 800 * 500) / 1000, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(18 * 1200 * 2 * 5, journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(18 * 800 * 2 * 5, journeyEvent.getFuelCost(FuelComponent.FBO));
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
			
			
			IIdleEvent<ISequenceElement> idleEvent = annotatedSequence.getAnnotation(dischargeElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(dischargeElement, idleEvent.getSequenceElement());
			Assert.assertSame(port3, idleEvent.getPort());
			Assert.assertSame(VesselState.Laden, idleEvent.getVesselState());
			Assert.assertEquals(7, idleEvent.getDuration());
			Assert.assertEquals(43, idleEvent.getStartTime());
			Assert.assertEquals(50, idleEvent.getEndTime());
			
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(7 * 1000, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals((7l * 1000 * 500) / 1000, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(7 * 1000 * 2 * 5, idleEvent.getFuelCost(FuelComponent.IdleNBO));
			
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

			
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(50, event.getStartTime());
			Assert.assertEquals(50, event.getEndTime());
			Assert.assertEquals(dischargeElement, event.getSequenceElement());
		}

{
			
			IJourneyEvent<ISequenceElement> journeyEvent = annotatedSequence.getAnnotation(endElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			
			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(18, journeyEvent.getDuration());
			
			// Expect only base fuel use on this ballast leg
			
			Assert.assertSame(VesselState.Ballast, journeyEvent.getVesselState());
			
			Assert.assertEquals(16000, journeyEvent.getSpeed());
			
			Assert.assertEquals(18 * 1000, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((18l * 1000l * 500) / 1000, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
//			Assert.assertEquals(25 * 1000 * 2, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
//			
			Assert.assertEquals(18 * 800, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals((18l * 800l * 500) / 1000, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(18 * 1000 * 2 * 5, journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(18 * 800 * 2 * 5, journeyEvent.getFuelCost(FuelComponent.FBO));
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
			
			
			IIdleEvent<ISequenceElement> idleEvent = annotatedSequence.getAnnotation(endElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(endElement, idleEvent.getSequenceElement());
			Assert.assertSame(port4, idleEvent.getPort());
			Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
			Assert.assertEquals(7, idleEvent.getDuration());
			Assert.assertEquals(68, idleEvent.getStartTime());
			Assert.assertEquals(75, idleEvent.getEndTime());
			
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(7 * 800, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals((7 * 800 * 500) / 1000, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(7 * 800 * 2 * 5, idleEvent.getFuelCost(FuelComponent.IdleNBO));
			
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

			
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(endElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(75, event.getStartTime());
			Assert.assertEquals(75, event.getEndTime());
			Assert.assertEquals(endElement, event.getSequenceElement());
		}
	}

	

	/**
	 * Like case 1,  but force a higher travelling speed to get idle time + Base_Supplemental (make NBO more costly than Base) 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Test
	public void testCalculations3() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port1 = builder.createPort("port-1");
		final IPort port2 = builder.createPort("port-2");
		final IPort port3 = builder.createPort("port-2");
		final IPort port4 = builder.createPort("port-2");

		final int minSpeed = 16000;
		final int maxSpeed = 20000;
		final int capacity = 150000000;
		final int baseFuelUnitPrice = 1000;
		final IVesselClass vesselClass1 = builder.createVesselClass(
				"vessel-class-1", minSpeed, maxSpeed, capacity, 0,
				baseFuelUnitPrice, 500, 0);

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
				.createStartEndRequirement(port1, 0);
		final IStartEndRequirement endRequirement = builder
				.createStartEndRequirement(port4, 75);

		final IVessel vessel1 = builder.createVessel("vessel-1", vesselClass1,
				startRequirement, endRequirement);

		final ITimeWindow loadWindow = builder.createTimeWindow(25, 25);
		final ILoadSlot loadSlot = builder.createLoadSlot("load-1", port2,
				loadWindow, 0, 150000000, 5000, 2000);

		final ITimeWindow dischargeWindow = builder.createTimeWindow(50, 50);
		final IDischargeSlot dischargeSlot = builder.createDischargeSlot(
				"discharge-1", port3, dischargeWindow, 0, 150000000, 200000);

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
		scheduler.setDurationsProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_elementDurationsProvider,
				IElementDurationProvider.class));
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
		final VoyagePlanOptimiser<ISequenceElement> voyagePlanOptimiser = new VoyagePlanOptimiser<ISequenceElement>();
		voyagePlanOptimiser.setVoyageCalculator(voyageCalculator);

		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		// This may throw IllegalStateException if not all
		// the elements are set.
		scheduler.init();

		final IResource resource = vesselProvider.getResource(vessel1);

		final ISequenceElement startElement = startEndProvider
				.getStartElement(resource);
		final ISequenceElement endElement = startEndProvider
				.getEndElement(resource);

		ISequenceElement dischargeElement = portSlotProvider
				.getElement(dischargeSlot);
		ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
		final List<ISequenceElement> sequenceList = CollectionsUtil
				.makeArrayList(startElement, loadElement, dischargeElement,
						endElement);

		final ISequence<ISequenceElement> sequence = new ListSequence<ISequenceElement>(
				sequenceList);

		final VoyagePlanAnnotator<ISequenceElement> annotator = new VoyagePlanAnnotator<ISequenceElement>();
		annotator.setPortSlotProvider(portSlotProvider);

		// Schedule sequence
		final List<IVoyagePlan> plans = scheduler.schedule(resource, sequence);

		final AnnotatedSequence<ISequenceElement> annotatedSequence = new AnnotatedSequence<ISequenceElement>();
		annotator.annotateFromVoyagePlan(resource, plans, annotatedSequence);

		// TODO: Start checking results
		{
			Assert.assertNull(annotatedSequence.getAnnotation(startElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
			Assert.assertNull(annotatedSequence.getAnnotation(startElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class));
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(startElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(0, event.getStartTime());
			Assert.assertEquals(0, event.getEndTime());
			Assert.assertEquals(startElement, event.getSequenceElement());
		}

		{
			
			IJourneyEvent<ISequenceElement> journeyEvent = annotatedSequence.getAnnotation(loadElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			
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
			Assert.assertEquals(900 * 18, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));
			
			Assert.assertEquals((900l * 18l * baseFuelUnitPrice) / 1000, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));
			
			
			IIdleEvent<ISequenceElement> idleEvent = annotatedSequence.getAnnotation(loadElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(loadElement, idleEvent.getSequenceElement());
			Assert.assertSame(port2, idleEvent.getPort());
			Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
			Assert.assertEquals(7, idleEvent.getDuration());
			Assert.assertEquals(18, idleEvent.getStartTime());
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
			Assert.assertEquals(7 * 400, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals((7l * 400 * baseFuelUnitPrice) / 1000, idleEvent.getFuelCost(FuelComponent.IdleBase));

			
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(loadElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(25, event.getStartTime());
			Assert.assertEquals(25, event.getEndTime());
			Assert.assertEquals(loadElement, event.getSequenceElement());
			
			Assert.assertTrue(event instanceof ILoadEvent);
		}

		
		{
			
			IJourneyEvent<ISequenceElement> journeyEvent = annotatedSequence.getAnnotation(dischargeElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			
			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(18, journeyEvent.getDuration());
			
			// Expect only base fuel use on this ballast leg
			
			Assert.assertSame(VesselState.Laden, journeyEvent.getVesselState());
			
			Assert.assertEquals(16000, journeyEvent.getSpeed());
			
			Assert.assertEquals(18 * 1200, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals((18l * 1200l * 500) / 1000, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
//			Assert.assertEquals(25 * 1200 * 2, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(18 * 1200 * 2 * 200, journeyEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleNBO));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(18 * 400, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals((18 * 400 * baseFuelUnitPrice) / 1000, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));
			
			
			IIdleEvent<ISequenceElement> idleEvent = annotatedSequence.getAnnotation(dischargeElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(dischargeElement, idleEvent.getSequenceElement());
			Assert.assertSame(port3, idleEvent.getPort());
			Assert.assertSame(VesselState.Laden, idleEvent.getVesselState());
			Assert.assertEquals(7, idleEvent.getDuration());
			Assert.assertEquals(43, idleEvent.getStartTime());
			Assert.assertEquals(50, idleEvent.getEndTime());
			
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(7 * 1000, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3));
			Assert.assertEquals((7l * 1000 * 500) / 1000, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.FBO));
			Assert.assertEquals(7 * 1000 * 2 * 200, idleEvent.getFuelCost(FuelComponent.IdleNBO));
			
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

			
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(dischargeElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(50, event.getStartTime());
			Assert.assertEquals(50, event.getEndTime());
			Assert.assertEquals(dischargeElement, event.getSequenceElement());
			
			Assert.assertTrue(event instanceof IDischargeEvent);
			
			IDischargeEvent<ISequenceElement> discharge = (IDischargeEvent<ISequenceElement>)event;
			Assert.assertEquals(loadSlot.getMaxLoadVolume()
					- (18 * 1200 + 7 * 1000), discharge.getDischargeVolume());
			
			Assert.assertEquals((loadSlot.getMaxLoadVolume()
					- (18 * 1200 + 7 * 1000)) * 2 * 200, discharge.getSalesPrice());
		}

		{

			IJourneyEvent<ISequenceElement> journeyEvent = annotatedSequence.getAnnotation(endElement,
					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journeyEvent);
			
			Assert.assertEquals(25 * 12, journeyEvent.getDistance());
			Assert.assertEquals(18, journeyEvent.getDuration());
			
			// Expect only base fuel use on this ballast leg
			
			Assert.assertSame(VesselState.Ballast, journeyEvent.getVesselState());
			
			Assert.assertEquals(16000, journeyEvent.getSpeed());
			
			// Why still nbo? NEED TO CHECK vpo COST CALS. -- Basefuel should be the cheaper option!
			// -- is base fuel being evaluated or is the choice not being allowed?
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MT));
			// Not yet set
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
//			Assert.assertEquals(25 * 1000 * 2, journeyEvent.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu));
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
			Assert.assertEquals(18 * 900, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.M3));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, journeyEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));
			
			Assert.assertEquals((18l * 900 * baseFuelUnitPrice) / 1000, journeyEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, journeyEvent.getFuelCost(FuelComponent.IdleBase));
			
			
			IIdleEvent<ISequenceElement> idleEvent = annotatedSequence.getAnnotation(endElement,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleEvent);
			Assert.assertSame(endElement, idleEvent.getSequenceElement());
			Assert.assertSame(port4, idleEvent.getPort());
			Assert.assertSame(VesselState.Ballast, idleEvent.getVesselState());
			Assert.assertEquals(7, idleEvent.getDuration());
			Assert.assertEquals(68, idleEvent.getStartTime());
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
			Assert.assertEquals(7 * 400, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT));
			Assert.assertEquals(0, idleEvent.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MMBTu));
			
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, idleEvent.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals((7 * 400 * baseFuelUnitPrice) / 1000, idleEvent.getFuelCost(FuelComponent.IdleBase));

			
			IPortVisitEvent<ISequenceElement> event = annotatedSequence
					.getAnnotation(endElement,
							SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(event);
			Assert.assertEquals(0, event.getDuration());
			Assert.assertEquals(75, event.getStartTime());
			Assert.assertEquals(75, event.getEndTime());
			Assert.assertEquals(endElement, event.getSequenceElement());
		}
	}

}
