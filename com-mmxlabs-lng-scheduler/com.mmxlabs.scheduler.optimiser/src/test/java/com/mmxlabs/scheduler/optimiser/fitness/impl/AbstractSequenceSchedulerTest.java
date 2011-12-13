/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collection;
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
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
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
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@RunWith(JMock.class)
public final class AbstractSequenceSchedulerTest {

	Mockery context = new JUnit4Mockery();

	/**
	 * Test high level inputs for a a two {@link VoyagePlan} sequence. Outputs cannot easily be tested with e JMock'd {@link IVoyagePlanOptimiser} so we just expect something to come out.
	 * 
	 * @throws CloneNotSupportedException
	 * 
	 */
	@Test
	public void testSchedule_1() throws CloneNotSupportedException {
		final IIndexingContext index = new SimpleIndexingContext();
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		final Port port1 = new Port(index, "port1");
		final Port port2 = new Port(index, "port2");
		final Port port3 = new Port(index, "port3");
		final Port port4 = new Port(index, "port4");

		final ITimeWindow timeWindow1 = new TimeWindow(5, 6);
		final ITimeWindow timeWindow2 = new TimeWindow(10, 11);
		final ITimeWindow timeWindow3 = new TimeWindow(15, 16);
		final ITimeWindow timeWindow4 = new TimeWindow(20, 21);

		final LoadSlot loadSlot1 = new LoadSlot();
		loadSlot1.setPort(port1);
		loadSlot1.setTimeWindow(timeWindow1);
		final DischargeSlot dischargeSlot1 = new DischargeSlot();
		dischargeSlot1.setPort(port2);
		dischargeSlot1.setTimeWindow(timeWindow2);

		loadSlot1.setId("load-slot-1");

		final Cargo cargo1 = new Cargo();
		cargo1.setId("cargo1");
		cargo1.setLoadOption(loadSlot1);
		cargo1.setDischargeOption(dischargeSlot1);

		dischargeSlot1.setId("discharge-slot-1");

		final LoadSlot loadSlot2 = new LoadSlot();
		loadSlot2.setPort(port3);
		loadSlot2.setTimeWindow(timeWindow3);
		loadSlot2.setId("load-slot-2");
		final DischargeSlot dischargeSlot2 = new DischargeSlot();
		dischargeSlot2.setPort(port4);
		dischargeSlot2.setTimeWindow(timeWindow4);

		dischargeSlot2.setId("discharge-slot-2");

		loadSlot2.setCooldownForbidden(true);
		loadSlot2.setCooldownSet(true);

		final Cargo cargo2 = new Cargo();
		cargo2.setId("cargo2");
		cargo2.setLoadOption(loadSlot2);
		cargo2.setDischargeOption(dischargeSlot2);

		final ISequenceElement element1 = new SequenceElement(index, "element1");
		final ISequenceElement element2 = new SequenceElement(index, "element2");
		final ISequenceElement element3 = new SequenceElement(index, "element3");
		final ISequenceElement element4 = new SequenceElement(index, "element4");

		final ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(SchedulerConstants.DCP_timeWindowProvider);

		timeWindowProvider.setTimeWindows(element1, Collections.singletonList(timeWindow1));
		timeWindowProvider.setTimeWindows(element2, Collections.singletonList(timeWindow2));
		timeWindowProvider.setTimeWindows(element3, Collections.singletonList(timeWindow3));
		timeWindowProvider.setTimeWindows(element4, Collections.singletonList(timeWindow4));

		final HashMapMatrixProvider<IPort, Integer> defaultDistanceProvider = new HashMapMatrixProvider<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider);

		final HashMapMultiMatrixProvider<IPort, Integer> distanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider);
		distanceProvider.set(IMultiMatrixProvider.Default_Key, defaultDistanceProvider);

		// Only need sparse matrix for testing
		defaultDistanceProvider.set(port1, port2, 400);
		defaultDistanceProvider.set(port2, port3, 400);
		defaultDistanceProvider.set(port3, port4, 400);

		final int duration = 1;
		final IElementDurationProviderEditor durationsProvider = new HashMapElementDurationEditor(SchedulerConstants.DCP_elementDurationsProvider);
		durationsProvider.setDefaultValue(duration);

		final IPortProviderEditor portProvider = new HashMapPortEditor(SchedulerConstants.DCP_portProvider);
		portProvider.setPortForElement(port1, element1);
		portProvider.setPortForElement(port2, element2);
		portProvider.setPortForElement(port3, element3);
		portProvider.setPortForElement(port4, element4);

		final IPortSlotProviderEditor portSlotProvider = new HashMapPortSlotEditor(SchedulerConstants.DCP_portSlotsProvider);
		portSlotProvider.setPortSlot(element1, loadSlot1);
		portSlotProvider.setPortSlot(element2, dischargeSlot1);
		portSlotProvider.setPortSlot(element3, loadSlot2);
		portSlotProvider.setPortSlot(element4, dischargeSlot2);

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor(SchedulerConstants.DCP_portTypeProvider);
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

		final IResource resource = new Resource(index);
		final Vessel vessel = new Vessel(index);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setMinNBOSpeed(VesselState.Laden, 15000);
		vesselClass.setMinNBOSpeed(VesselState.Ballast, 15000);
		vesselClass.setCargoCapacity(1000000);

		vessel.setVesselClass(vesselClass);

		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor(SchedulerConstants.DCP_vesselProvider);
		vesselProvider.setVesselResource(resource, vessel);
		scheduler.setVesselProvider(vesselProvider);

		final List elements = CollectionsUtil.makeArrayList(element1, element2, element3, element4);
		final ISequence sequence = new ListSequence(elements);

		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		final IRouteCostProvider routeCostProvider = context.mock(IRouteCostProvider.class);
		scheduler.setRouteCostProvider(routeCostProvider);

		// Init scheduler and ensure all required components are in place
		scheduler.init();

		final VoyageOptions expectedOptions1 = new VoyageOptions();
		expectedOptions1.setAvailableTime(4);
		expectedOptions1.setFromPortSlot(loadSlot1);
		expectedOptions1.setToPortSlot(dischargeSlot1);
		expectedOptions1.setUseNBOForTravel(true);
		expectedOptions1.setUseNBOForIdle(false);
		expectedOptions1.setUseFBOForSupplement(false);
		expectedOptions1.setVessel(vessel);
		expectedOptions1.setVesselState(VesselState.Laden);
		expectedOptions1.setNBOSpeed(15000);
		expectedOptions1.setAvailableLNG(vesselClass.getCargoCapacity());

		// The NBO travel options will have completed the setup of previous
		// options (options1) filling in distance info.
		final VoyageOptions expectedOptions1a = expectedOptions1.clone();
		expectedOptions1a.setRoute(IMultiMatrixProvider.Default_Key);
		expectedOptions1a.setDistance(400);

		final VoyageOptions expectedOptions2 = new VoyageOptions();
		expectedOptions2.setAvailableTime(4);
		expectedOptions2.setFromPortSlot(dischargeSlot1);
		expectedOptions2.setToPortSlot(loadSlot2);
		expectedOptions2.setUseFBOForSupplement(false);
		expectedOptions2.setUseNBOForIdle(false);
		expectedOptions2.setUseNBOForTravel(true);
		expectedOptions2.setVessel(vessel);
		expectedOptions2.setVesselState(VesselState.Ballast);
		expectedOptions2.setNBOSpeed(15000);
		expectedOptions2.setAvailableLNG(vesselClass.getCargoCapacity());
		expectedOptions2.setShouldBeCold(true);
		final VoyageOptions expectedOptions2a = expectedOptions2.clone();
		expectedOptions2a.setRoute(IMultiMatrixProvider.Default_Key);
		expectedOptions2a.setDistance(400);

		final VoyageOptions expectedOptions3 = new VoyageOptions();
		expectedOptions3.setAvailableTime(4);
		expectedOptions3.setFromPortSlot(loadSlot2);
		expectedOptions3.setToPortSlot(dischargeSlot2);
		expectedOptions3.setUseFBOForSupplement(false);
		expectedOptions3.setUseNBOForIdle(false);
		expectedOptions3.setUseNBOForTravel(true);
		expectedOptions3.setVessel(vessel);
		expectedOptions3.setVesselState(VesselState.Laden);
		expectedOptions3.setNBOSpeed(15000);
		expectedOptions3.setAvailableLNG(vesselClass.getCargoCapacity());

		final VoyageOptions expectedOptions3a = expectedOptions3.clone();
		expectedOptions3a.setRoute(IMultiMatrixProvider.Default_Key);
		expectedOptions3a.setDistance(400);

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

		final VoyageDetails expectedVoyageDetails1 = new VoyageDetails();
		expectedVoyageDetails1.setOptions(expectedOptions1);
		// expectedVoyageDetails1.setStartTime(6);
		expectedVoyageDetails1.setTravelTime(4);

		final VoyageDetails expectedVoyageDetails2 = new VoyageDetails();
		expectedVoyageDetails2.setOptions(expectedOptions2);
		// expectedVoyageDetails2.setStartTime(11);
		expectedVoyageDetails2.setTravelTime(4);

		final VoyageDetails expectedVoyageDetails3 = new VoyageDetails();
		expectedVoyageDetails3.setOptions(expectedOptions3);
		// expectedVoyageDetails3.setStartTime(16);
		expectedVoyageDetails3.setTravelTime(4);

		final List<Object> expectedBasicSequence1 = new LinkedList<Object>();
		expectedBasicSequence1.add(expectedPortDetails1);
		expectedBasicSequence1.add(expectedOptions1a);
		expectedBasicSequence1.add(expectedPortDetails2);
		expectedBasicSequence1.add(expectedOptions2a);
		expectedBasicSequence1.add(expectedPortDetails3);

		final List<Object> expectedBasicSequence2 = new LinkedList<Object>();
		expectedBasicSequence2.add(expectedPortDetails3);
		expectedBasicSequence2.add(expectedOptions3a);
		expectedBasicSequence2.add(expectedPortDetails4);

		// Return "empty" plan from optimiser
		final VoyagePlan testVoyagePlan = new VoyagePlan();
		final Object[] testSequence = new Object[] {};
		testVoyagePlan.setSequence(testSequence);

		context.setDefaultResultForType(VoyagePlan.class, testVoyagePlan);

		// Final set of arrival times
		final int[] arrivalTimes = new int[] { 5, 10, 15, 20 };

		// Expected arrival times per plan
		final int[] arrivalTimes1 = new int[] { 5, 10, 15 };
		final int[] arrivalTimes2 = new int[] { 15, 20 };

		//
		// Rely upon objects equals() methods to aid JMock equal(..) case
		context.checking(new Expectations() {
			{
				one(voyagePlanOptimiser).setVessel(vessel);

				// Set expected list of VPO choices
				one(voyagePlanOptimiser).addChoice(with(equal(new FBOVoyagePlanChoice(expectedOptions1))));

				one(voyagePlanOptimiser).addChoice(with(equal(new IdleNBOVoyagePlanChoice(expectedOptions1))));

				one(voyagePlanOptimiser).addChoice(with(equal(new NBOTravelVoyagePlanChoice(expectedOptions1a, expectedOptions2))));

				one(voyagePlanOptimiser).addChoice(with(equal(new FBOVoyagePlanChoice(expectedOptions2))));

				one(voyagePlanOptimiser).addChoice(with(equal(new IdleNBOVoyagePlanChoice(expectedOptions2))));

				one(voyagePlanOptimiser).addChoice(with(equal(new FBOVoyagePlanChoice(expectedOptions3))));
				one(voyagePlanOptimiser).addChoice(with(equal(new IdleNBOVoyagePlanChoice(expectedOptions3))));

				// Expect two runs of the VPO
				one(voyagePlanOptimiser).setBasicSequence(with(equal(expectedBasicSequence1)));

				one(voyagePlanOptimiser).setBasicSequence(with(equal(expectedBasicSequence2)));

				one(voyagePlanOptimiser).init();
				one(voyagePlanOptimiser).optimise();
				one(voyagePlanOptimiser).reset();
				one(voyagePlanOptimiser).init();
				one(voyagePlanOptimiser).optimise();
				one(voyagePlanOptimiser).reset();

				one(voyagePlanOptimiser).setArrivalTimes(with(equal(CollectionsUtil.toArrayList(arrivalTimes1))));
				one(voyagePlanOptimiser).setArrivalTimes(with(equal(CollectionsUtil.toArrayList(arrivalTimes2))));
			}
		});

		// Schedule sequence
		final ScheduledSequence plans = scheduler.schedule(resource, sequence, arrivalTimes);

		Assert.assertNotNull(plans);
		Assert.assertEquals(2, plans.getVoyagePlans().size());

		context.assertIsSatisfied();
	}

	/**
	 * Generate a single {@link VoyagePlan} sequence and check expected outputs
	 * 
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testSchedule_2() throws CloneNotSupportedException {
		final IIndexingContext index = new SimpleIndexingContext();
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		final Port port1 = new Port(index, "port1");
		final Port port2 = new Port(index, "port2");
		final Port port3 = new Port(index, "port3");

		final ITimeWindow timeWindow1 = new TimeWindow(5, 6);
		final ITimeWindow timeWindow2 = new TimeWindow(10, 11);
		final ITimeWindow timeWindow3 = new TimeWindow(15, 16);

		final LoadSlot loadSlot1 = new LoadSlot();
		loadSlot1.setPort(port1);
		loadSlot1.setTimeWindow(timeWindow1);

		loadSlot1.setCooldownForbidden(true);
		loadSlot1.setCooldownSet(true);

		final DischargeSlot dischargeSlot1 = new DischargeSlot();
		dischargeSlot1.setPort(port2);
		dischargeSlot1.setTimeWindow(timeWindow2);

		final Cargo cargo1 = new Cargo();
		cargo1.setId("cargo1");
		cargo1.setLoadOption(loadSlot1);
		cargo1.setDischargeOption(dischargeSlot1);

		final LoadSlot loadSlot2 = new LoadSlot();
		loadSlot2.setPort(port3);
		loadSlot2.setTimeWindow(timeWindow3);

		loadSlot2.setCooldownForbidden(true);
		loadSlot2.setCooldownSet(true);

		final ISequenceElement element1 = new SequenceElement(index, "element1");
		final ISequenceElement element2 = new SequenceElement(index, "element2");
		final ISequenceElement element3 = new SequenceElement(index, "element3");

		final ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider);

		timeWindowProvider.setTimeWindows(element1,
				Collections.singletonList(timeWindow1));
		timeWindowProvider.setTimeWindows(element2,
				Collections.singletonList(timeWindow2));
		timeWindowProvider.setTimeWindows(element3,
				Collections.singletonList(timeWindow3));

		final HashMapMatrixProvider<IPort, Integer> defaultDistanceProvider = new HashMapMatrixProvider<IPort, Integer>(
				SchedulerConstants.DCP_portDistanceProvider);

		final HashMapMultiMatrixProvider<IPort, Integer> distanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>(
				SchedulerConstants.DCP_portDistanceProvider);
		distanceProvider.set(IMultiMatrixProvider.Default_Key,
				defaultDistanceProvider);

		// Only need sparse matrix for testing
		defaultDistanceProvider.set(port1, port2, 400);
		defaultDistanceProvider.set(port2, port3, 400);

		final int duration = 1;
		final IElementDurationProviderEditor durationsProvider = new HashMapElementDurationEditor(
				SchedulerConstants.DCP_elementDurationsProvider);
		durationsProvider.setDefaultValue(duration);

		final IPortProviderEditor portProvider = new HashMapPortEditor(
				SchedulerConstants.DCP_portProvider);
		portProvider.setPortForElement(port1, element1);
		portProvider.setPortForElement(port2, element2);
		portProvider.setPortForElement(port3, element3);

		final IPortSlotProviderEditor portSlotProvider = new HashMapPortSlotEditor(
				SchedulerConstants.DCP_portSlotsProvider);
		portSlotProvider.setPortSlot(element1, loadSlot1);
		portSlotProvider.setPortSlot(element2, dischargeSlot1);
		portSlotProvider.setPortSlot(element3, loadSlot2);

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor(
				SchedulerConstants.DCP_portTypeProvider);
		portTypeProvider.setPortType(element1, PortType.Load);
		portTypeProvider.setPortType(element2, PortType.Discharge);
		portTypeProvider.setPortType(element3, PortType.Load);

		// Set data providers
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);

		final IResource resource = new Resource(index);
		final Vessel vessel = new Vessel(index);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setMinNBOSpeed(VesselState.Laden, 15000);
		vesselClass.setMinNBOSpeed(VesselState.Ballast, 15000);
		vesselClass.setCargoCapacity(100000);
		vessel.setVesselClass(vesselClass);

		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor(SchedulerConstants.DCP_vesselProvider);
		vesselProvider.setVesselResource(resource, vessel);
		scheduler.setVesselProvider(vesselProvider);

		final List elements = CollectionsUtil.makeArrayList(element1, element2, element3);
		final ISequence sequence = new ListSequence(elements);

		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		final IRouteCostProvider routeCostProvider = context.mock(IRouteCostProvider.class);
		scheduler.setRouteCostProvider(routeCostProvider);

		// Init scheduler and ensure all required components are in place
		scheduler.init();

		final VoyageOptions expectedOptions1 = new VoyageOptions();
		expectedOptions1.setAvailableTime(4);
		expectedOptions1.setFromPortSlot(loadSlot1);
		expectedOptions1.setToPortSlot(dischargeSlot1);
		expectedOptions1.setUseNBOForTravel(true);
		expectedOptions1.setUseNBOForIdle(false);
		expectedOptions1.setUseFBOForSupplement(false);
		expectedOptions1.setVessel(vessel);
		expectedOptions1.setVesselState(VesselState.Laden);
		expectedOptions1.setNBOSpeed(15000);
		expectedOptions1.setAvailableLNG(vesselClass.getCargoCapacity());

		// The NBO travel options will have completed the setup of previous
		// options (options1) filling in distance info.
		final VoyageOptions expectedOptions1a = expectedOptions1.clone();
		expectedOptions1a.setRoute(IMultiMatrixProvider.Default_Key);
		expectedOptions1a.setDistance(400);

		final VoyageOptions expectedOptions2 = new VoyageOptions();
		expectedOptions2.setAvailableTime(4);
		expectedOptions2.setFromPortSlot(dischargeSlot1);
		expectedOptions2.setToPortSlot(loadSlot2);
		expectedOptions2.setUseFBOForSupplement(false);
		expectedOptions2.setUseNBOForIdle(false);
		expectedOptions2.setUseNBOForTravel(true);
		expectedOptions2.setVessel(vessel);
		expectedOptions2.setVesselState(VesselState.Ballast);
		expectedOptions2.setNBOSpeed(15000);
		expectedOptions2.setAvailableLNG(vesselClass.getCargoCapacity());
		expectedOptions2.setShouldBeCold(true);
		final VoyageOptions expectedOptions2a = expectedOptions2.clone();
		expectedOptions2a.setRoute(IMultiMatrixProvider.Default_Key);
		expectedOptions2a.setDistance(400);

		final PortDetails expectedPortDetails1 = new PortDetails();
		expectedPortDetails1.setPortSlot(loadSlot1);
		expectedPortDetails1.setVisitDuration(1);

		final PortDetails expectedPortDetails2 = new PortDetails();
		expectedPortDetails2.setPortSlot(dischargeSlot1);
		expectedPortDetails2.setVisitDuration(1);

		final PortDetails expectedPortDetails3 = new PortDetails();
		expectedPortDetails3.setPortSlot(loadSlot2);
		expectedPortDetails3.setVisitDuration(1);

		final VoyageDetails expectedVoyageDetails1 = new VoyageDetails();
		expectedVoyageDetails1.setOptions(expectedOptions1);
		// expectedVoyageDetails1.setStartTime(6);
		expectedVoyageDetails1.setTravelTime(4);

		final VoyageDetails expectedVoyageDetails2 = new VoyageDetails();
		expectedVoyageDetails2.setOptions(expectedOptions2);
		// expectedVoyageDetails2.setStartTime(11);
		expectedVoyageDetails2.setTravelTime(4);

		final List<Object> expectedBasicSequence1 = new LinkedList<Object>();
		expectedBasicSequence1.add(expectedPortDetails1);
		expectedBasicSequence1.add(expectedOptions1a);
		expectedBasicSequence1.add(expectedPortDetails2);
		expectedBasicSequence1.add(expectedOptions2a);
		expectedBasicSequence1.add(expectedPortDetails3);

		final VoyagePlan testVoyagePlan = new VoyagePlan();
		final Object[] testSequence = new Object[] { expectedPortDetails1, expectedVoyageDetails1, expectedPortDetails2, expectedVoyageDetails2, expectedPortDetails3 };
		testVoyagePlan.setSequence(testSequence);

		testVoyagePlan.setTotalFuelCost(FuelComponent.Base, 100);
		testVoyagePlan.setTotalFuelCost(FuelComponent.Base_Supplemental, 100);
		testVoyagePlan.setTotalFuelCost(FuelComponent.NBO, 100);
		testVoyagePlan.setTotalFuelCost(FuelComponent.FBO, 100);
		testVoyagePlan.setTotalFuelCost(FuelComponent.IdleBase, 100);
		testVoyagePlan.setTotalFuelCost(FuelComponent.IdleNBO, 100);

		context.setDefaultResultForType(VoyagePlan.class, testVoyagePlan);

		final int[] arrivalTimes = new int[] { 5, 10, 15 };
		//
		// Rely upon objects equals() methods to aid JMock equal(..) case
		context.checking(new Expectations() {
			{
				one(voyagePlanOptimiser).setVessel(vessel);

				// Set expected list of VPO choices
				one(voyagePlanOptimiser).addChoice(with(equal(new FBOVoyagePlanChoice(expectedOptions1))));

				one(voyagePlanOptimiser).addChoice(with(equal(new IdleNBOVoyagePlanChoice(expectedOptions1))));

				one(voyagePlanOptimiser).addChoice(with(equal(new NBOTravelVoyagePlanChoice(expectedOptions1a, expectedOptions2))));

				one(voyagePlanOptimiser).addChoice(with(equal(new FBOVoyagePlanChoice(expectedOptions2))));

				one(voyagePlanOptimiser).addChoice(with(equal(new IdleNBOVoyagePlanChoice(expectedOptions2))));

				// Expect two runs of the VPO
				one(voyagePlanOptimiser).setBasicSequence(with(equal(expectedBasicSequence1)));

				one(voyagePlanOptimiser).init();
				one(voyagePlanOptimiser).optimise();
				one(voyagePlanOptimiser).reset();

				one(voyagePlanOptimiser).setArrivalTimes(with(equal(CollectionsUtil.toArrayList(arrivalTimes))));
			}
		});

		// Schedule sequence
		final ScheduledSequence plansAndTime = scheduler.schedule(resource, sequence, arrivalTimes);

		Assert.assertNotNull(plansAndTime);
		final List<VoyagePlan> plans = plansAndTime.getVoyagePlans();
		Assert.assertEquals(1, plans.size());

		// TODO: Check plan details are as expected
		// TODO: Return a default plan from VPO and check expected output is
		// populated correctly.
		// TODO: Can we return different objects for each invocation?
		//
		// // Check plan 1
		final VoyagePlan plan1 = plans.get(0);
		Assert.assertSame(testVoyagePlan, plan1);

		final Object[] outputSequence = testVoyagePlan.getSequence();

		// Assert.assertEquals(5,
		// ((PortDetails) outputSequence[0]).getStartTime());
		Assert.assertEquals(1, ((PortDetails) outputSequence[0]).getVisitDuration());
		// Assert.assertEquals(6,
		// ((VoyageDetails) outputSequence[1]).getStartTime());
		// Assert.assertEquals(10,
		// ((PortDetails) outputSequence[2]).getStartTime());
		Assert.assertEquals(1, ((PortDetails) outputSequence[2]).getVisitDuration());
		// Assert.assertEquals(11,
		// ((VoyageDetails) outputSequence[3]).getStartTime());
		// Assert.assertEquals(15,
		// ((PortDetails) outputSequence[4]).getStartTime());
		Assert.assertEquals(1, ((PortDetails) outputSequence[4]).getVisitDuration());

		context.assertIsSatisfied();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testDispose() {

		// Mock objects
		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		final IPortProvider portProvider = context.mock(IPortProvider.class);
		final IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		final IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		final IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		final IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		final ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		final IRouteCostProvider routeCostProvider = context.mock(IRouteCostProvider.class);

		// Set on SSS
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		scheduler.setRouteCostProvider(routeCostProvider);

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
		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		final IPortProvider portProvider = context.mock(IPortProvider.class);
		final IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		final IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		final IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		final IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		final ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);
		final IRouteCostProvider routeCostProvider = context.mock(IRouteCostProvider.class);

		// Set on SSS
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);
		scheduler.setRouteCostProvider(routeCostProvider);

		scheduler.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit2() {

		// Mock objects
		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		final IPortProvider portProvider = context.mock(IPortProvider.class);
		final IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		final IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		final IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		// IMatrixProvider distanceProvider =
		// context.mock(IMatrixProvider.class);
		final ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);

		// Set on SSS
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		// scheduler.setDistanceProvider(distanceProvider);
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
	@Test(expected = IllegalStateException.class)
	public void testInit3() {

		// Mock objects
		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		final IPortProvider portProvider = context.mock(IPortProvider.class);
		final IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		final IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		// IElementDurationProvider durationsProvider =
		// context.mock(IElementDurationProvider.class);
		final IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		final ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);

		// Set on SSS
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		scheduler.setDistanceProvider(distanceProvider);
		// scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected = IllegalStateException.class)
	public void testInit4() {

		// Mock objects
		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		// IPortProvider portProvider = context.mock(IPortProvider.class);
		final IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		final IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		final IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		final IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		final ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);

		// Set on SSS
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		// scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected = IllegalStateException.class)
	public void testInit5() {

		// Mock objects
		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		final IPortProvider portProvider = context.mock(IPortProvider.class);
		// IPortSlotProvider portSlotProvider =
		// context.mock(IPortSlotProvider.class);
		final IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		final IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		final IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		final ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);

		// Set on SSS
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		// scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected = IllegalStateException.class)
	public void testInit7() {

		// Mock objects
		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		final IPortProvider portProvider = context.mock(IPortProvider.class);
		final IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		// IPortTypeProvider portTypeProvider =
		// context.mock(IPortTypeProvider.class);
		final IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		final IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		final ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);

		// Set on SSS
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		// scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected = IllegalStateException.class)
	public void testInit8() {

		// Mock objects
		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		final IPortProvider portProvider = context.mock(IPortProvider.class);
		final IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		final IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		final IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		final IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		// ITimeWindowDataComponentProvider timeWindowProvider =
		// context.mock(ITimeWindowDataComponentProvider.class);
		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);

		// Set on SSS
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		// scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected = IllegalStateException.class)
	public void testInit9() {

		// Mock objects
		// IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		final IPortProvider portProvider = context.mock(IPortProvider.class);
		final IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		final IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		final IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		final IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		final ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		final IVoyagePlanOptimiser voyagePlanOptimiser = context.mock(IVoyagePlanOptimiser.class);

		// Set on SSS
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		// scheduler.setVesselProvider(vesselProvider);
		scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		scheduler.init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected = IllegalStateException.class)
	public void testInit10() {

		// Mock objects
		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		final IPortProvider portProvider = context.mock(IPortProvider.class);
		final IPortSlotProvider portSlotProvider = context.mock(IPortSlotProvider.class);
		final IPortTypeProvider portTypeProvider = context.mock(IPortTypeProvider.class);
		final IElementDurationProvider durationsProvider = context.mock(IElementDurationProvider.class);
		final IMultiMatrixProvider distanceProvider = context.mock(IMultiMatrixProvider.class);
		final ITimeWindowDataComponentProvider timeWindowProvider = context.mock(ITimeWindowDataComponentProvider.class);
		// IVoyagePlanOptimiser voyagePlanOptimiser =
		// context.mock(IVoyagePlanOptimiser.class);

		// Set on SSS
		final MockSequenceScheduler scheduler = new MockSequenceScheduler();

		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setPortSlotProvider(portSlotProvider);
		scheduler.setPortTypeProvider(portTypeProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		scheduler.setVesselProvider(vesselProvider);
		// scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

		scheduler.init();
	}

	/**
	 * Mock implementation of {@link AbstractSequenceScheduler} to allow use of abstract class in tests
	 * 
	 * @author Simon Goodall
	 * 
	 */
	private static class MockSequenceScheduler extends AbstractSequenceScheduler {

		@Override
		public ScheduledSequences schedule(ISequences sequences, boolean b) {
			throw new UnsupportedOperationException("Method invocation is not part of the tests!");

		}

		@Override
		public ScheduledSequences schedule(ISequences sequences, Collection<IResource> affectedResources, boolean forExport) {
			throw new UnsupportedOperationException("Method invocation is not part of the tests!");
		}

		@Override
		public void acceptLastSchedule() {
			throw new UnsupportedOperationException("Method invocation is not part of the tests!");
		}
	}
}
