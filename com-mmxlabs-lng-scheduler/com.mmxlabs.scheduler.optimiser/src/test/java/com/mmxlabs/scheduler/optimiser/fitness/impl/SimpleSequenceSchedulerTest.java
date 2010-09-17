package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.HashMapElementDurationEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.TimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@RunWith(JMock.class)
public final class SimpleSequenceSchedulerTest {

	Mockery context = new JUnit4Mockery();

	/**
	 * Simple case, everything runs exactly on time. Only one idle time required
	 * at start.
	 * @throws CloneNotSupportedException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSchedule_1() throws CloneNotSupportedException {

		SimpleSequenceScheduler<ISequenceElement> scheduler = new SimpleSequenceScheduler<ISequenceElement>();

		Port port1 = new Port("port1");
		Port port2 = new Port("port2");
		Port port3 = new Port("port3");
		Port port4 = new Port("port4");

		ITimeWindow timeWindow1 = new TimeWindow(5, 6);
		ITimeWindow timeWindow2 = new TimeWindow(10, 11);
		ITimeWindow timeWindow3 = new TimeWindow(15, 16);
		ITimeWindow timeWindow4 = new TimeWindow(20, 21);

		LoadSlot loadSlot1 = new LoadSlot();
		loadSlot1.setPort(port1);
		loadSlot1.setTimeWindow(timeWindow1);
		DischargeSlot dischargeSlot1 = new DischargeSlot();
		dischargeSlot1.setPort(port2);
		dischargeSlot1.setTimeWindow(timeWindow2);

		Cargo cargo1 = new Cargo();
		cargo1.setId("cargo1");
		cargo1.setLoadSlot(loadSlot1);
		cargo1.setDischargeSlot(dischargeSlot1);

		LoadSlot loadSlot2 = new LoadSlot();
		loadSlot2.setPort(port3);
		loadSlot2.setTimeWindow(timeWindow3);
		DischargeSlot dischargeSlot2 = new DischargeSlot();
		dischargeSlot2.setPort(port4);
		dischargeSlot2.setTimeWindow(timeWindow4);

		Cargo cargo2 = new Cargo();
		cargo2.setId("cargo2");
		cargo2.setLoadSlot(loadSlot2);
		cargo2.setDischargeSlot(dischargeSlot2);

		ISequenceElement element1 = new SequenceElement("element1", loadSlot1);
		ISequenceElement element2 = new SequenceElement("element2",
				dischargeSlot1);
		ISequenceElement element3 = new SequenceElement("element3", loadSlot2);
		ISequenceElement element4 = new SequenceElement("element4",
				dischargeSlot2);

		ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider);

		timeWindowProvider.setTimeWindows(element1,
				Collections.singletonList(timeWindow1));
		timeWindowProvider.setTimeWindows(element2,
				Collections.singletonList(timeWindow2));
		timeWindowProvider.setTimeWindows(element3,
				Collections.singletonList(timeWindow3));
		timeWindowProvider.setTimeWindows(element4,
				Collections.singletonList(timeWindow4));

		HashMapMatrixProvider<IPort, Integer> defaultDistanceProvider = new HashMapMatrixProvider<IPort, Integer>(
				SchedulerConstants.DCP_portDistanceProvider);
		
		final HashMapMultiMatrixProvider<IPort, Integer> distanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>(
				SchedulerConstants.DCP_portDistanceProvider);
		distanceProvider.set(IMultiMatrixProvider.Default_Key, defaultDistanceProvider);

		// Only need sparse matrix for testing
		defaultDistanceProvider.set(port1, port2, 400);
		defaultDistanceProvider.set(port2, port3, 400);
		defaultDistanceProvider.set(port3, port4, 400);

		final int duration = 1;
		IElementDurationProviderEditor<ISequenceElement> durationsProvider = new HashMapElementDurationEditor<ISequenceElement>(
				SchedulerConstants.DCP_elementDurationsProvider);
		durationsProvider.setDefaultValue(duration);

		IPortProviderEditor portProvider = new HashMapPortEditor(
				SchedulerConstants.DCP_portProvider);
		portProvider.setPortForElement(port1, element1);
		portProvider.setPortForElement(port2, element2);
		portProvider.setPortForElement(port3, element3);
		portProvider.setPortForElement(port4, element4);

		IPortSlotProviderEditor<ISequenceElement> portSlotProvider = new HashMapPortSlotEditor<ISequenceElement>(
				SchedulerConstants.DCP_portSlotsProvider);
		portSlotProvider.setPortSlot(element1, loadSlot1);
		portSlotProvider.setPortSlot(element2, dischargeSlot1);
		portSlotProvider.setPortSlot(element3, loadSlot2);
		portSlotProvider.setPortSlot(element4, dischargeSlot2);

		IPortTypeProviderEditor<ISequenceElement> portTypeProvider = new HashMapPortTypeEditor<ISequenceElement>(
				SchedulerConstants.DCP_portTypeProvider);
		portTypeProvider.setPortType(element1, PortType.Load);
		portTypeProvider.setPortType(element2, PortType.Discharge);
		portTypeProvider.setPortType(element3, PortType.Load);
		portTypeProvider.setPortType(element4, PortType.Discharge);

		// Set data providers
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);

		IResource resource = new Resource();
		final Vessel vessel = new Vessel();
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setMinNBOSpeed(VesselState.Laden, 15000);
		vesselClass.setMinNBOSpeed(VesselState.Ballast, 15000);
		vessel.setVesselClass(vesselClass);

		IVesselProviderEditor vesselProvider = new HashMapVesselEditor(
				SchedulerConstants.DCP_vesselProvider);
		vesselProvider.setVesselResource(resource, vessel);
		scheduler.setVesselProvider(vesselProvider);

		List<ISequenceElement> elements = CollectionsUtil.makeArrayList(
				element1, element2, element3, element4);
		ISequence<ISequenceElement> sequence = new ListSequence<ISequenceElement>(
				elements);

		final IVoyagePlanOptimiser<ISequenceElement> voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
//		
//		final ILNGVoyageCalculator<ISequenceElement> voyageCalculator = context
//				.mock(ILNGVoyageCalculator.class);
//		voyagePlanOptimiser.setVoyageCalculator(voyageCalculator);

		// This may throw IllegalStateException if not all the elements are set.
		// TODO: Expand this into it's own series of test cases
		scheduler.init();

//		VPO now plays into this, we can expect a range of choices to be evaluated during VPO calcualtions - and therefore potentially multiple invocations of each evaluation
//		
//		 -- Only one distance so ignore that
//		 -- NBO + FBO + IDLE
//		
//		
		// First element has longer than expected avail time as the first
		// timewindow is ignored as there is no voyage to start port.
		// TODO: Fix the vessel start conditions
		final VoyageOptions expectedOptions1_a = new VoyageOptions();
		expectedOptions1_a.setRoute(IMultiMatrixProvider.Default_Key);
		expectedOptions1_a.setAvailableTime(9);
		expectedOptions1_a.setDistance(400);
		expectedOptions1_a.setFromPortSlot(loadSlot1);
		expectedOptions1_a.setToPortSlot(dischargeSlot1);
		expectedOptions1_a.setUseNBOForTravel(true);
		expectedOptions1_a.setUseNBOForIdle(false);
		expectedOptions1_a.setUseFBOForSupplement(false);
		expectedOptions1_a.setVessel(vessel);
		expectedOptions1_a.setVesselState(VesselState.Laden);
		expectedOptions1_a.setNBOSpeed(15000);

		final VoyageOptions expectedOptions2_a = new VoyageOptions();
		expectedOptions2_a.setRoute("default");
		expectedOptions2_a.setAvailableTime(4);
		expectedOptions2_a.setDistance(400);
		expectedOptions2_a.setFromPortSlot(dischargeSlot1);
		expectedOptions2_a.setToPortSlot(loadSlot2);
		expectedOptions2_a.setUseFBOForSupplement(false);
		expectedOptions2_a.setUseNBOForIdle(false);
		expectedOptions2_a.setUseNBOForTravel(true);
		expectedOptions2_a.setVessel(vessel);
		expectedOptions2_a.setVesselState(VesselState.Ballast);
		expectedOptions2_a.setNBOSpeed(15000);
		
		final VoyageOptions expectedOptions3_a = new VoyageOptions();
		expectedOptions3_a.setRoute("default");
		expectedOptions3_a.setAvailableTime(4);
		expectedOptions3_a.setDistance(400);
		expectedOptions3_a.setFromPortSlot(loadSlot2);
		expectedOptions3_a.setToPortSlot(dischargeSlot2);
		expectedOptions3_a.setUseFBOForSupplement(false);
		expectedOptions3_a.setUseNBOForIdle(false);
		expectedOptions3_a.setUseNBOForTravel(true);
		expectedOptions3_a.setVessel(vessel);
		expectedOptions3_a.setVesselState(VesselState.Laden);
		expectedOptions3_a.setNBOSpeed(15000);
		
		
		final PortDetails expectedPortDetails1 = new PortDetails();
		expectedPortDetails1.setPortSlot(loadSlot1);
		expectedPortDetails1.setVisitDuration(1);

		final PortDetails expectedPortDetails2 = new PortDetails();
		expectedPortDetails2.setPortSlot(dischargeSlot1);
		expectedPortDetails2.setVisitDuration(1);

		final PortDetails expectedPortDetails3 = new PortDetails();
		expectedPortDetails3.setPortSlot(loadSlot2);
		expectedPortDetails3.setVisitDuration(1);

		final PortDetails expectedPortDetails4 = new PortDetails();
		expectedPortDetails4.setPortSlot(dischargeSlot2);
		expectedPortDetails4.setVisitDuration(1);

		final VoyageDetails<ISequenceElement> expectedVoyageDetails1 = new VoyageDetails<ISequenceElement>();
		expectedVoyageDetails1.setOptions(expectedOptions1_a);
		expectedVoyageDetails1.setStartTime(1);

		final VoyageDetails<ISequenceElement> expectedVoyageDetails2 = new VoyageDetails<ISequenceElement>();
		expectedVoyageDetails2.setOptions(expectedOptions2_a);
		expectedVoyageDetails2.setStartTime(11);

		final VoyageDetails<ISequenceElement> expectedVoyageDetails3 = new VoyageDetails<ISequenceElement>();
		expectedVoyageDetails3.setOptions(expectedOptions3_a);
		expectedVoyageDetails3.setStartTime(16);
		
		final List<Object> expectedBasicSequence1 = new LinkedList<Object>();
		expectedBasicSequence1.add(expectedPortDetails1);
		expectedBasicSequence1.add(expectedOptions1_a);
		expectedBasicSequence1.add(expectedPortDetails2);
		expectedBasicSequence1.add(expectedOptions2_a);
		expectedBasicSequence1.add(expectedPortDetails3);
		
		final List<Object> expectedBasicSequence2 = new LinkedList<Object>();
		expectedBasicSequence2.add(expectedPortDetails3);
		expectedBasicSequence2.add(expectedOptions3_a);
		expectedBasicSequence2.add(expectedPortDetails4);
		
		
		// TODO: Implement
//		Object[] testSequence = new Object[] { ... , ... , ... };
//		IVoyagePlan testVoyagePlan = new VoyagePlan();
//		testVoyagePlan.setDischargeVolume(1000l);
//		testVoyagePlan.setLoadVolume(1000l);
//		testVoyagePlan.setPurchaseCost(1000l);
//		testVoyagePlan.setSalesRevenue(1000l);
//		
//		testVoyagePlan.setSequence(testSequence);
//		
//		testVoyagePlan.setTotalFuelCost(FuelComponent.Base, 100);
//		testVoyagePlan.setTotalFuelCost(FuelComponent.Base_Supplemental, 100);
//		testVoyagePlan.setTotalFuelCost(FuelComponent.NBO, 100);
//		testVoyagePlan.setTotalFuelCost(FuelComponent.FBO, 100);
//		testVoyagePlan.setTotalFuelCost(FuelComponent.IdleBase, 100);
//		testVoyagePlan.setTotalFuelCost(FuelComponent.IdleNBO, 100);
//		
//		context.setDefaultResultForType(IVoyagePlan.class, testVoyagePlan);
//		
		// Rely upon objects equals() methods to aid JMock equal(..) case
		context.checking(new Expectations() {
			{
				one(voyagePlanOptimiser).setVessel(vessel);

				// Set expected list of VPO choices
				one(voyagePlanOptimiser).addChoice(with(equal(new FBOVoyagePlanChoice(expectedOptions1_a, new boolean[] { true, false}))));
				one(voyagePlanOptimiser).addChoice(with(equal(new IdleNBOVoyagePlanChoice(expectedOptions1_a, new boolean[] { true, false}))));
				one(voyagePlanOptimiser).addChoice(with(equal(new RouteVoyagePlanChoice(expectedOptions1_a, new String[] { IMultiMatrixProvider.Default_Key }, distanceProvider))));
				
				one(voyagePlanOptimiser).addChoice(with(equal(new NBOTravelVoyagePlanChoice(expectedOptions1_a, expectedOptions2_a, new boolean[] { true, false}))));
				one(voyagePlanOptimiser).addChoice(with(equal(new FBOVoyagePlanChoice(expectedOptions2_a, new boolean[] { true, false}))));
				one(voyagePlanOptimiser).addChoice(with(equal(new IdleNBOVoyagePlanChoice(expectedOptions2_a, new boolean[] { true, false}))));
				one(voyagePlanOptimiser).addChoice(with(equal(new RouteVoyagePlanChoice(expectedOptions2_a, new String[] { IMultiMatrixProvider.Default_Key }, distanceProvider))));

				one(voyagePlanOptimiser).addChoice(with(equal(new FBOVoyagePlanChoice(expectedOptions3_a, new boolean[] { true, false}))));
				one(voyagePlanOptimiser).addChoice(with(equal(new IdleNBOVoyagePlanChoice(expectedOptions3_a, new boolean[] { true, false}))));
				one(voyagePlanOptimiser).addChoice(with(equal(new RouteVoyagePlanChoice(expectedOptions3_a, new String[] { IMultiMatrixProvider.Default_Key }, distanceProvider))));
				
				// Expect two runs of the VPO
				one(voyagePlanOptimiser).setBasicSequence(with(equal(expectedBasicSequence1)));
				one(voyagePlanOptimiser).setBasicSequence(with(equal(expectedBasicSequence2)));

				one(voyagePlanOptimiser).init();
				one(voyagePlanOptimiser).optimise();
				one(voyagePlanOptimiser).reset();
				one(voyagePlanOptimiser).init();
				one(voyagePlanOptimiser).optimise();
				one(voyagePlanOptimiser).reset();
			}
		});

		// Schedule sequence
		List<VoyagePlan> plans = scheduler.schedule(resource, sequence);

		Assert.assertNotNull(plans);
		Assert.assertEquals(2, plans.size());

		// TODO: Check plan details are as expected
		// TODO: Return a default plan from VPO and check expected output is populated correctly.
		// TODO: Can we return different objects for each invocation?
//		
//		// Check plan 1
//		IVoyagePlan plan1 = plans.get(0);
//		Assert.assertNotNull(plan1);
//		Assert.assertEquals(0, plan1.getLoadVolume());
//		Assert.assertEquals(0, plan1.getDischargeVolume());
//		Assert.assertEquals(0, plan1.getPurchaseCost());
//		Assert.assertEquals(0, plan1.getSalesRevenue());
//		Object[] sequence1 = plan1.getSequence();
//		Assert.assertNotNull(sequence1);
//		Assert.assertEquals(5, sequence1.length);
//		
//		// Check plan 2
//		IVoyagePlan plan2 = plans.get(0);
//		Assert.assertNotNull(plan2);
//		Assert.assertEquals(0, plan2.getLoadVolume());
//		Assert.assertEquals(0, plan2.getDischargeVolume());
//		Assert.assertEquals(0, plan2.getPurchaseCost());
//		Assert.assertEquals(0, plan2.getSalesRevenue());
//		Object[] sequence2 = plan2.getSequence();
//		Assert.assertNotNull(sequence2);
//		Assert.assertEquals(3, sequence2.length);		
		
		context.assertIsSatisfied();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testDispose() {
		
		// Mock objects
		IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		IPortProvider portProvider = context.mock(IPortProvider.class);
		IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		
		// Set on SSS
		SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();
		
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
		scheduler.init();
		
		// sss.dispose();
		scheduler.dispose();
		
		// assert null method returns
		Assert.assertNull(scheduler.getDistanceProvider());
		Assert.assertNull(scheduler.getDurationsProvider());
		Assert.assertNull(scheduler.getPortProvider());
		Assert.assertNull(scheduler.getPortSlotProvider());
		Assert.assertNull(scheduler.getPortTypeProvider());
		Assert.assertNull(scheduler.getTimeWindowProvider());
		Assert.assertNull(scheduler.getVesselProvider());
		Assert.assertNull(scheduler.getVoyagePlanOptimiser());
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testInit1() {
		
		// Mock objects
		IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		IPortProvider portProvider = context.mock(IPortProvider.class);
		IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		
		// Set on SSS
		SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();
		
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
		scheduler.init();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalStateException.class)
	public void testInit2() {
		
		// Mock objects
		IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		IPortProvider portProvider = context.mock(IPortProvider.class);
		IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
//		IMatrixProvider distanceProvider = context.mock(IMatrixProvider.class);
		ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		
		// Set on SSS
		SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();
		
//		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
		scheduler.init();
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalStateException.class)
	public void testInit3() {
		
		// Mock objects
		IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		IPortProvider portProvider = context.mock(IPortProvider.class);
		IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
//		IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		
		// Set on SSS
		SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();
		
		scheduler.setDistanceProvider(distanceProvider);
//		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalStateException.class)
	public void testInit4() {
		
		// Mock objects
		IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
//		IPortProvider portProvider = context.mock(IPortProvider.class);
		IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		
		// Set on SSS
		SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();
		
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
//		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalStateException.class)
	public void testInit5() {
		
		// Mock objects
		IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		IPortProvider portProvider = context.mock(IPortProvider.class);
//		IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		
		// Set on SSS
		SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();
		
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
//		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalStateException.class)
	public void testInit7() {
		
		// Mock objects
		IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		IPortProvider portProvider = context.mock(IPortProvider.class);
		IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
//		IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		
		// Set on SSS
		SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();
		
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
//		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalStateException.class)
	public void testInit8() {
		
		// Mock objects
		IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		IPortProvider portProvider = context.mock(IPortProvider.class);
		IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
//		ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		
		// Set on SSS
		SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();
		
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
//		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalStateException.class)
	public void testInit9() {
		
		// Mock objects
//		IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		IPortProvider portProvider = context.mock(IPortProvider.class);
		IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		
		// Set on SSS
		SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();
		
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
//		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalStateException.class)
	public void testInit10() {
		
		// Mock objects
		IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		IPortProvider portProvider = context.mock(IPortProvider.class);
		IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
//		IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		
		// Set on SSS
		SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();
		
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
//		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		
		scheduler.init();
	}
}
