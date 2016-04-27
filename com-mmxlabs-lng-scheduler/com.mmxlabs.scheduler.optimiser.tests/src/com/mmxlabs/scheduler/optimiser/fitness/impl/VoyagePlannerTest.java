/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Triple;
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
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DefaultVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselStartDateCharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@SuppressWarnings({ "null", "unused" })
public final class VoyagePlannerTest {

	/**
	 * Test high level inputs for a a two {@link VoyagePlan} sequence. Outputs cannot easily be tested with e JMock'd {@link IVoyagePlanOptimiser} so we just expect something to come out.
	 * 
	 * @throws CloneNotSupportedException
	 * 
	 */
	@Ignore("Does not work after JMock - Mockito Change. Verify happens after execution where as jmock expectations happen during execution")
	@Test
	public void testSchedule_1() throws CloneNotSupportedException {
		final IIndexingContext index = new SimpleIndexingContext();

		final Port port1 = new Port(index, "port1");
		final Port port2 = new Port(index, "port2");
		final Port port3 = new Port(index, "port3");
		final Port port4 = new Port(index, "port4");

		final ITimeWindow timeWindow1 = new TimeWindow(5, 6);
		final ITimeWindow timeWindow2 = new TimeWindow(10, 11);
		final ITimeWindow timeWindow3 = new TimeWindow(15, 16);
		final ITimeWindow timeWindow4 = new TimeWindow(20, 21);

		final LoadSlot loadSlot1 = new LoadSlot("load-slot-1", port1, timeWindow1, 0L, Long.MAX_VALUE, Mockito.mock(ILoadPriceCalculator.class), 0, false, false);

		final DischargeSlot dischargeSlot1 = new DischargeSlot("discharge-slot-1", port2, timeWindow2, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		final LoadSlot loadSlot2 = new LoadSlot("load-slot-2", port3, timeWindow3, 0L, Long.MAX_VALUE, Mockito.mock(ILoadPriceCalculator.class), 0, true, true);
		final DischargeSlot dischargeSlot2 = new DischargeSlot("discharge-slot-2", port4, timeWindow4, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		loadSlot2.setCooldownForbidden(true);
		loadSlot2.setCooldownSet(true);

		final ISequenceElement element1 = new SequenceElement(index, "element1");
		final ISequenceElement element2 = new SequenceElement(index, "element2");
		final ISequenceElement element3 = new SequenceElement(index, "element3");
		final ISequenceElement element4 = new SequenceElement(index, "element4");

		final ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider();

		timeWindowProvider.setTimeWindows(element1, Collections.singletonList(timeWindow1));
		timeWindowProvider.setTimeWindows(element2, Collections.singletonList(timeWindow2));
		timeWindowProvider.setTimeWindows(element3, Collections.singletonList(timeWindow3));
		timeWindowProvider.setTimeWindows(element4, Collections.singletonList(timeWindow4));

		final HashMapMatrixProvider<IPort, Integer> defaultDistanceProvider = new HashMapMatrixProvider<IPort, Integer>();

		final HashMapMultiMatrixProvider<IPort, Integer> distanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>();
		distanceProvider.set(ERouteOption.DIRECT.name(), defaultDistanceProvider);

		// Only need sparse matrix for testing
		defaultDistanceProvider.set(port1, port2, 400);
		defaultDistanceProvider.set(port2, port3, 400);
		defaultDistanceProvider.set(port3, port4, 400);

		final int duration = 1;
		final IElementDurationProviderEditor durationsProvider = new HashMapElementDurationEditor();
		durationsProvider.setDefaultValue(duration);

		final IPortProviderEditor portProvider = new HashMapPortEditor();
		portProvider.setPortForElement(port1, element1);
		portProvider.setPortForElement(port2, element2);
		portProvider.setPortForElement(port3, element3);
		portProvider.setPortForElement(port4, element4);

		final IPortSlotProviderEditor portSlotProvider = new HashMapPortSlotEditor();
		portSlotProvider.setPortSlot(element1, loadSlot1);
		portSlotProvider.setPortSlot(element2, dischargeSlot1);
		portSlotProvider.setPortSlot(element3, loadSlot2);
		portSlotProvider.setPortSlot(element4, dischargeSlot2);

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		portTypeProvider.setPortType(element1, PortType.Load);
		portTypeProvider.setPortType(element2, PortType.Discharge);
		portTypeProvider.setPortType(element3, PortType.Load);
		portTypeProvider.setPortType(element4, PortType.Discharge);

		final VesselClass vesselClass = new VesselClass();
		vesselClass.setCargoCapacity(1000000);
		final Vessel vessel = new Vessel("Schedule1Vessel", vesselClass, 1000000);

		final IResource resource = new Resource(index, vessel.getName());

		final IConsumptionRateCalculator consumptionRateCalculator = Mockito.mock(IConsumptionRateCalculator.class);
		Mockito.when(consumptionRateCalculator.getSpeed(Matchers.anyInt())).thenReturn(15000);

		vesselClass.setConsumptionRate(VesselState.Laden, consumptionRateCalculator);
		vesselClass.setConsumptionRate(VesselState.Ballast, consumptionRateCalculator);

		final DefaultVesselAvailability vesselAvailability = new DefaultVesselAvailability(vessel, VesselInstanceType.FLEET);

		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		vesselProvider.setVesselAvailabilityResource(resource, vesselAvailability);

		final List<ISequenceElement> elements = CollectionsUtil.makeArrayList(element1, element2, element3, element4);
		final ISequence sequence = new ListSequence(elements);

		final IVoyagePlanOptimiser voyagePlanOptimiser = Mockito.mock(IVoyagePlanOptimiser.class);
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);

		// Set data providers
		final Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			public void configure() {
				bind(new TypeLiteral<IMultiMatrixProvider<IPort, Integer>>() {
				}).toInstance(distanceProvider);
				bind(IElementDurationProvider.class).toInstance(durationsProvider);
				bind(IPortProvider.class).toInstance(portProvider);
				bind(ITimeWindowDataComponentProvider.class).toInstance(timeWindowProvider);
				bind(IPortSlotProvider.class).toInstance(portSlotProvider);
				bind(IPortTypeProvider.class).toInstance(portTypeProvider);
				bind(IVesselProviderEditor.class).toInstance(vesselProvider);
				bind(IVoyagePlanOptimiser.class).toInstance(voyagePlanOptimiser);
				bind(IRouteCostProvider.class).toInstance(routeCostProvider);
				bind(VoyagePlanner.class);
				bind(ICharterRateCalculator.class).to(VesselStartDateCharterRateCalculator.class);
			}
		});

		// Init scheduler and ensure all required components are in place
		final VoyagePlanner planner = injector.getInstance(VoyagePlanner.class);

		final VoyageOptions expectedVoyageOptions1 = new VoyageOptions(loadSlot1, dischargeSlot1);
		expectedVoyageOptions1.setAvailableTime(4);
		expectedVoyageOptions1.setUseNBOForTravel(true);
		expectedVoyageOptions1.setUseNBOForIdle(false);
		expectedVoyageOptions1.setUseFBOForSupplement(false);
		expectedVoyageOptions1.setVessel(vessel);
		expectedVoyageOptions1.setVesselState(VesselState.Laden);
		expectedVoyageOptions1.setNBOSpeed(15000);

		// The NBO travel options will have completed the setup of previous
		// options (options1) filling in distance info.
		final VoyageOptions expectedVoyageOptions1a = expectedVoyageOptions1.clone();
		expectedVoyageOptions1a.setRoute(ERouteOption.DIRECT, 400, 0L);

		final VoyageOptions expectedVoyageOptions2 = new VoyageOptions(dischargeSlot1, loadSlot2);
		expectedVoyageOptions2.setAvailableTime(4);
		expectedVoyageOptions2.setUseFBOForSupplement(false);
		expectedVoyageOptions2.setUseNBOForIdle(false);
		expectedVoyageOptions2.setUseNBOForTravel(true);
		expectedVoyageOptions2.setVessel(vessel);
		expectedVoyageOptions2.setVesselState(VesselState.Ballast);
		expectedVoyageOptions2.setNBOSpeed(15000);
		expectedVoyageOptions2.setShouldBeCold(true);
		final VoyageOptions expectedVoyageOptions2a = expectedVoyageOptions2.clone();
		expectedVoyageOptions2a.setRoute(ERouteOption.DIRECT, 400, 0L);

		final VoyageOptions expectedVoyageOptions3 = new VoyageOptions(loadSlot2, dischargeSlot2);
		expectedVoyageOptions3.setAvailableTime(4);
		expectedVoyageOptions3.setUseFBOForSupplement(false);
		expectedVoyageOptions3.setUseNBOForIdle(false);
		expectedVoyageOptions3.setUseNBOForTravel(true);
		expectedVoyageOptions3.setVessel(vessel);
		expectedVoyageOptions3.setVesselState(VesselState.Laden);
		expectedVoyageOptions3.setNBOSpeed(15000);

		final VoyageOptions expectedVoyageOptions3a = expectedVoyageOptions3.clone();
		expectedVoyageOptions3a.setRoute(ERouteOption.DIRECT, 400, 0L);

		final PortOptions expectedPortOptions1 = new PortOptions(1, vessel, loadSlot1, VesselState.Ballast);
		final PortDetails expectedPortDetails1 = new PortDetails(expectedPortOptions1);

		final PortOptions expectedPortOptions2 = new PortOptions(1, vessel, dischargeSlot1, VesselState.Laden);
		final PortDetails expectedPortDetails2 = new PortDetails(expectedPortOptions2);

		final PortOptions expectedPortOptions3 = new PortOptions(1, vessel, loadSlot2, VesselState.Ballast);
		final PortDetails expectedPortDetails3 = new PortDetails(expectedPortOptions3);

		final PortOptions expectedPortOptions4 = new PortOptions(1, vessel, dischargeSlot2, VesselState.Laden);
		final PortDetails expectedPortDetails4 = new PortDetails(expectedPortOptions4);

		final VoyageDetails expectedVoyageDetails1 = new VoyageDetails(expectedVoyageOptions1);
		// expectedVoyageDetails1.setStartTime(6);
		expectedVoyageDetails1.setTravelTime(4);

		final VoyageDetails expectedVoyageDetails2 = new VoyageDetails(expectedVoyageOptions2);
		// expectedVoyageDetails2.setStartTime(11);
		expectedVoyageDetails2.setTravelTime(4);

		final VoyageDetails expectedVoyageDetails3 = new VoyageDetails(expectedVoyageOptions3);
		// expectedVoyageDetails3.setStartTime(16);
		expectedVoyageDetails3.setTravelTime(4);

		final List<IOptionsSequenceElement> expectedBasicSequence1 = new LinkedList<IOptionsSequenceElement>();
		expectedBasicSequence1.add(expectedPortOptions1);
		expectedBasicSequence1.add(expectedVoyageOptions1a);
		expectedBasicSequence1.add(expectedPortOptions2);
		expectedBasicSequence1.add(expectedVoyageOptions2a);
		expectedBasicSequence1.add(expectedPortOptions3);

		final List<IOptionsSequenceElement> expectedBasicSequence2 = new LinkedList<IOptionsSequenceElement>();
		expectedBasicSequence2.add(expectedPortOptions3);
		expectedBasicSequence2.add(expectedVoyageOptions3a);
		expectedBasicSequence2.add(expectedPortOptions4);

		// Return "empty" plan from optimiser
		final VoyagePlan testVoyagePlan = new VoyagePlan();
		final IDetailsSequenceElement[] testSequence = new IDetailsSequenceElement[] {};
		testVoyagePlan.setSequence(testSequence);

		// Expected arrival times per plan
		final PortTimesRecord portTimesRecord1 = new PortTimesRecord();
		portTimesRecord1.setSlotTime(loadSlot1, 5);
		portTimesRecord1.setSlotTime(dischargeSlot1, 10);
		portTimesRecord1.setSlotTime(loadSlot2, 15);

		final PortTimesRecord portTimesRecord2 = new PortTimesRecord();
		portTimesRecord1.setSlotTime(loadSlot2, 15);
		portTimesRecord1.setSlotTime(dischargeSlot2, 20);

		List<@NonNull IPortTimesRecord> portTimesRecords = Lists.<@NonNull IPortTimesRecord> newArrayList(portTimesRecord1, portTimesRecord2);

		// Set expected list of VPO choices
		List<IVoyagePlanChoice> vpoChoices1 = new LinkedList<>();
		List<IVoyagePlanChoice> vpoChoices2 = new LinkedList<>();

		vpoChoices1.add(new FBOVoyagePlanChoice(expectedVoyageOptions1));
		vpoChoices1.add(new IdleNBOVoyagePlanChoice(expectedVoyageOptions1));
		vpoChoices1.add(new NBOTravelVoyagePlanChoice(expectedVoyageOptions1a, expectedVoyageOptions2));
		vpoChoices1.add(new FBOVoyagePlanChoice(expectedVoyageOptions2));
		vpoChoices1.add(new IdleNBOVoyagePlanChoice(expectedVoyageOptions2));

		vpoChoices2.add(new FBOVoyagePlanChoice(expectedVoyageOptions3));
		vpoChoices2.add(new IdleNBOVoyagePlanChoice(expectedVoyageOptions3));

		// Matchers.eq!!
		Mockito.when(voyagePlanOptimiser.optimise(resource, vessel, 0, 0, 0, portTimesRecord1, expectedBasicSequence1, vpoChoices1)).thenReturn(testVoyagePlan);
		Mockito.when(voyagePlanOptimiser.optimise(resource, vessel, 0, 0, 0, portTimesRecord2, expectedBasicSequence2, vpoChoices2)).thenReturn(testVoyagePlan);

		// Schedule sequence
		final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> plans = planner.makeVoyagePlans(resource, sequence, portTimesRecords);

		Assert.assertNotNull(plans);
		Assert.assertEquals(2, plans.size());
	}

	// /**
	// * Generate a single {@link VoyagePlan} sequence and check expected outputs
	// *
	// * @throws CloneNotSupportedException
	// */
	// @Ignore("Does not work after JMock - Mockito Change. Verify happens after execution where as jmock expectations happen during execution")
	// @SuppressWarnings({ "unchecked", "rawtypes", "null" })
	// @Test
	// public void testSchedule_2() throws CloneNotSupportedException {
	// final IIndexingContext index = new SimpleIndexingContext();
	//
	// final Port port1 = new Port(index, "port1");
	// final Port port2 = new Port(index, "port2");
	// final Port port3 = new Port(index, "port3");
	//
	// final ITimeWindow timeWindow1 = new TimeWindow(5, 6);
	// final ITimeWindow timeWindow2 = new TimeWindow(10, 11);
	// final ITimeWindow timeWindow3 = new TimeWindow(15, 16);
	//
	// final LoadSlot loadSlot1 = new LoadSlot();
	// loadSlot1.setPort(port1);
	// loadSlot1.setTimeWindow(timeWindow1);
	//
	// loadSlot1.setCooldownForbidden(true);
	// loadSlot1.setCooldownSet(true);
	//
	// final DischargeSlot dischargeSlot1 = new DischargeSlot();
	// dischargeSlot1.setPort(port2);
	// dischargeSlot1.setTimeWindow(timeWindow2);
	//
	// final LoadSlot loadSlot2 = new LoadSlot();
	// loadSlot2.setPort(port3);
	// loadSlot2.setTimeWindow(timeWindow3);
	//
	// loadSlot2.setCooldownForbidden(true);
	// loadSlot2.setCooldownSet(true);
	//
	// final ISequenceElement element1 = new SequenceElement(index, "element1");
	// final ISequenceElement element2 = new SequenceElement(index, "element2");
	// final ISequenceElement element3 = new SequenceElement(index, "element3");
	//
	// final ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider();
	//
	// timeWindowProvider.setTimeWindows(element1, Collections.singletonList(timeWindow1));
	// timeWindowProvider.setTimeWindows(element2, Collections.singletonList(timeWindow2));
	// timeWindowProvider.setTimeWindows(element3, Collections.singletonList(timeWindow3));
	//
	// final HashMapMatrixProvider<IPort, Integer> defaultDistanceProvider = new HashMapMatrixProvider<IPort, Integer>();
	//
	// final HashMapMultiMatrixProvider<IPort, Integer> distanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>();
	// distanceProvider.set(ERouteOption.DIRECT.name(), defaultDistanceProvider);
	//
	// // Only need sparse matrix for testing
	// defaultDistanceProvider.set(port1, port2, 400);
	// defaultDistanceProvider.set(port2, port3, 400);
	//
	// final int duration = 1;
	// final IElementDurationProviderEditor durationsProvider = new HashMapElementDurationEditor();
	// durationsProvider.setDefaultValue(duration);
	//
	// final IPortProviderEditor portProvider = new HashMapPortEditor();
	// portProvider.setPortForElement(port1, element1);
	// portProvider.setPortForElement(port2, element2);
	// portProvider.setPortForElement(port3, element3);
	//
	// final IPortSlotProviderEditor portSlotProvider = new HashMapPortSlotEditor();
	// portSlotProvider.setPortSlot(element1, loadSlot1);
	// portSlotProvider.setPortSlot(element2, dischargeSlot1);
	// portSlotProvider.setPortSlot(element3, loadSlot2);
	//
	// final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
	// portTypeProvider.setPortType(element1, PortType.Load);
	// portTypeProvider.setPortType(element2, PortType.Discharge);
	// portTypeProvider.setPortType(element3, PortType.Load);
	//
	// final VesselClass vesselClass = new VesselClass();
	// final Vessel vessel = new Vessel("Schedule2Vessel", vesselClass, vesselClass.getCargoCapacity());
	// final IResource resource = new Resource(index, vessel.getName());
	//
	// final IConsumptionRateCalculator consumptionRateCalculator = Mockito.mock(IConsumptionRateCalculator.class);
	// Mockito.when(consumptionRateCalculator.getSpeed(Matchers.anyInt())).thenReturn(15000);
	//
	// vesselClass.setConsumptionRate(VesselState.Laden, consumptionRateCalculator);
	// vesselClass.setConsumptionRate(VesselState.Ballast, consumptionRateCalculator);
	//
	// final DefaultVesselAvailability vesselAvailability = new DefaultVesselAvailability(vessel, VesselInstanceType.FLEET);
	//
	// final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
	// vesselProvider.setVesselAvailabilityResource(resource, vesselAvailability);
	//
	// final List elements = CollectionsUtil.makeArrayList(element1, element2, element3);
	// final ISequence sequence = new ListSequence(elements);
	//
	// final IVoyagePlanOptimiser voyagePlanOptimiser = Mockito.mock(IVoyagePlanOptimiser.class);
	//
	// final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);
	//
	// // Set data providers
	// final Injector injector = Guice.createInjector(new AbstractModule() {
	// @Override
	// public void configure() {
	// bind(new TypeLiteral<IMultiMatrixProvider<IPort, Integer>>() {
	// }).toInstance(distanceProvider);
	// bind(IElementDurationProvider.class).toInstance(durationsProvider);
	// bind(IPortProvider.class).toInstance(portProvider);
	// bind(ITimeWindowDataComponentProvider.class).toInstance(timeWindowProvider);
	// bind(IPortSlotProvider.class).toInstance(portSlotProvider);
	// bind(IPortTypeProvider.class).toInstance(portTypeProvider);
	// bind(IVesselProviderEditor.class).toInstance(vesselProvider);
	// bind(IVoyagePlanOptimiser.class).toInstance(voyagePlanOptimiser);
	// bind(IRouteCostProvider.class).toInstance(routeCostProvider);
	// bind(VoyagePlanner.class);
	// bind(ICharterRateCalculator.class).to(VesselStartDateCharterRateCalculator.class);
	//
	// }
	// });
	//
	// // Init scheduler and ensure all required components are in place
	// final VoyagePlanner planner = injector.getInstance(VoyagePlanner.class);
	//
	// final VoyageOptions expectedVoyageOptions1 = new VoyageOptions();
	// expectedVoyageOptions1.setAvailableTime(4);
	// expectedVoyageOptions1.setFromPortSlot(loadSlot1);
	// expectedVoyageOptions1.setToPortSlot(dischargeSlot1);
	// expectedVoyageOptions1.setUseNBOForTravel(true);
	// expectedVoyageOptions1.setUseNBOForIdle(false);
	// expectedVoyageOptions1.setUseFBOForSupplement(false);
	// expectedVoyageOptions1.setVessel(vessel);
	// expectedVoyageOptions1.setVesselState(VesselState.Laden);
	// expectedVoyageOptions1.setNBOSpeed(15000);
	//
	// // The NBO travel options will have completed the setup of previous
	// // options (options1) filling in distance info.
	// final VoyageOptions expectedVoyageOptions1a = expectedVoyageOptions1.clone();
	// expectedVoyageOptions1a.setRoute(ERouteOption.DIRECT, 400, 0L);
	//
	// final VoyageOptions expectedVoyageOptions2 = new VoyageOptions();
	// expectedVoyageOptions2.setAvailableTime(4);
	// expectedVoyageOptions2.setFromPortSlot(dischargeSlot1);
	// expectedVoyageOptions2.setToPortSlot(loadSlot2);
	// expectedVoyageOptions2.setUseFBOForSupplement(false);
	// expectedVoyageOptions2.setUseNBOForIdle(false);
	// expectedVoyageOptions2.setUseNBOForTravel(true);
	// expectedVoyageOptions2.setVessel(vessel);
	// expectedVoyageOptions2.setVesselState(VesselState.Ballast);
	// expectedVoyageOptions2.setNBOSpeed(15000);
	// expectedVoyageOptions2.setShouldBeCold(true);
	// final VoyageOptions expectedVoyageOptions2a = expectedVoyageOptions2.clone();
	// expectedVoyageOptions2a.setRoute(ERouteOption.DIRECT, 400, 0L);
	//
	// final PortDetails expectedPortDetails1 = new PortDetails();
	// final PortOptions expectedPortOptions1 = new PortOptions(1, vessel, loadSlot1, VesselState.Ballast);
	// expectedPortDetails1.setOptions(expectedPortOptions1);
	//
	// final PortDetails expectedPortDetails2 = new PortDetails();
	// final PortOptions expectedPortOptions2 = new PortOptions(1, vessel, dischargeSlot1, VesselState.Laden);
	// expectedPortDetails2.setOptions(expectedPortOptions2);
	//
	// final PortDetails expectedPortDetails3 = new PortDetails();
	// final PortOptions expectedPortOptions3 = new PortOptions(1, vessel, loadSlot2, VesselState.Ballast);
	// expectedPortDetails3.setOptions(expectedPortOptions3);
	//
	// final VoyageDetails expectedVoyageDetails1 = new VoyageDetails();
	// expectedVoyageDetails1.setOptions(expectedVoyageOptions1);
	// // expectedVoyageDetails1.setStartTime(6);
	// expectedVoyageDetails1.setTravelTime(4);
	//
	// final VoyageDetails expectedVoyageDetails2 = new VoyageDetails();
	// expectedVoyageDetails2.setOptions(expectedVoyageOptions2);
	// // expectedVoyageDetails2.setStartTime(11);
	// expectedVoyageDetails2.setTravelTime(4);
	//
	// final List<IOptionsSequenceElement> expectedBasicSequence1 = new LinkedList<IOptionsSequenceElement>();
	// expectedBasicSequence1.add(expectedPortOptions1);
	// expectedBasicSequence1.add(expectedVoyageOptions1a);
	// expectedBasicSequence1.add(expectedPortOptions2);
	// expectedBasicSequence1.add(expectedVoyageOptions2a);
	// expectedBasicSequence1.add(expectedPortOptions3);
	//
	// final VoyagePlan testVoyagePlan = new VoyagePlan();
	// final IDetailsSequenceElement[] testSequence = new IDetailsSequenceElement[] { expectedPortDetails1, expectedVoyageDetails1, expectedPortDetails2, expectedVoyageDetails2,
	// expectedPortDetails3 };
	// testVoyagePlan.setSequence(testSequence);
	//
	// testVoyagePlan.setTotalFuelCost(FuelComponent.Base, 100);
	// testVoyagePlan.setTotalFuelCost(FuelComponent.Base_Supplemental, 100);
	// testVoyagePlan.setTotalFuelCost(FuelComponent.NBO, 100);
	// testVoyagePlan.setTotalFuelCost(FuelComponent.FBO, 100);
	// testVoyagePlan.setTotalFuelCost(FuelComponent.IdleBase, 100);
	// testVoyagePlan.setTotalFuelCost(FuelComponent.IdleNBO, 100);
	//
	// List<IVoyagePlanChoice> vpoChoices = new LinkedList<>();
	// // Set expected list of VPO choices
	// vpoChoices.add(new FBOVoyagePlanChoice(expectedVoyageOptions1));
	//
	// vpoChoices.add(new IdleNBOVoyagePlanChoice(expectedVoyageOptions1));
	//
	// vpoChoices.add(new NBOTravelVoyagePlanChoice(expectedVoyageOptions1a, expectedVoyageOptions2));
	//
	// vpoChoices.add(new FBOVoyagePlanChoice(expectedVoyageOptions2));
	//
	// vpoChoices.add(new IdleNBOVoyagePlanChoice(expectedVoyageOptions2));
	//
	// final PortTimesRecord portTimesRecord = new PortTimesRecord();
	// portTimesRecord.setSlotTime(loadSlot1, 5);
	// portTimesRecord.setSlotTime(dischargeSlot1, 10);
	// portTimesRecord.setSlotTime(loadSlot2, 15);
	//
	// List<@NonNull IPortTimesRecord> portTimesRecords = Lists.<@NonNull IPortTimesRecord> newArrayList(portTimesRecord);
	//
	// Mockito.when(voyagePlanOptimiser.optimise(resource, vessel, 0, 0, 0, portTimesRecord, expectedBasicSequence1, vpoChoices)).thenReturn(testVoyagePlan);
	//
	// // Schedule sequence
	// final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans = planner.makeVoyagePlans(resource, sequence, portTimesRecords);
	//
	// // // Rely upon objects equals() methods to aid JMock equal(..) case
	// // Mockito.verify(voyagePlanOptimiser).setVessel(vessel, resource, 0);
	// //
	// // // Set expected list of VPO choices
	// // Mockito.verify(voyagePlanOptimiser).addChoice(Matchers.eq(new FBOVoyagePlanChoice(expectedVoyageOptions1)));
	// //
	// // Mockito.verify(voyagePlanOptimiser).addChoice(Matchers.eq(new IdleNBOVoyagePlanChoice(expectedVoyageOptions1)));
	// //
	// // Mockito.verify(voyagePlanOptimiser).addChoice(Matchers.eq(new NBOTravelVoyagePlanChoice(expectedVoyageOptions1a, expectedVoyageOptions2)));
	// //
	// // Mockito.verify(voyagePlanOptimiser).addChoice(Matchers.eq(new FBOVoyagePlanChoice(expectedVoyageOptions2)));
	// //
	// // Mockito.verify(voyagePlanOptimiser).addChoice(Matchers.eq(new IdleNBOVoyagePlanChoice(expectedVoyageOptions2)));
	// //
	// // // Expect two runs of the VPO
	// // Mockito.verify(voyagePlanOptimiser).setBasicSequence(Matchers.eq(expectedBasicSequence1));
	// //
	// // Mockito.verify(voyagePlanOptimiser).init();
	// // Mockito.verify(voyagePlanOptimiser).optimise();
	//
	// Mockito.verify(voyagePlanOptimiser).optimise(resource, vessel, 0, 0, 0, portTimesRecord, expectedBasicSequence1, vpoChoices);
	//
	// // Mockito.verify(voyagePlanOptimiser).reset();
	// Mockito.verifyNoMoreInteractions(voyagePlanOptimiser);
	//
	// // Expected arrival times per plan
	//
	// // Mockito.verify(voyagePlanOptimiser).setPortTimesRecord(Matchers.eq(portTimesRecord));
	//
	// Assert.assertNotNull(voyagePlans);
	// Assert.assertEquals(1, voyagePlans.size());
	//
	// // TODO: Check plan details are as expected
	// // TODO: Return a default plan from VPO and check expected output is
	// // populated correctly.
	// // TODO: Can we return different objects for each invocation?
	// //
	// // // Check plan 1
	// final VoyagePlan plan1 = voyagePlans.get(0).getFirst();
	// Assert.assertSame(testVoyagePlan, plan1);
	//
	// final Object[] outputSequence = testVoyagePlan.getSequence();
	//
	// // Assert.assertEquals(5,
	// // ((PortDetails) outputSequence[0]).getStartTime());
	// Assert.assertEquals(1, ((PortDetails) outputSequence[0]).getOptions().getVisitDuration());
	// // Assert.assertEquals(6,
	// // ((VoyageDetails) outputSequence[1]).getStartTime());
	// // Assert.assertEquals(10,
	// // ((PortDetails) outputSequence[2]).getStartTime());
	// Assert.assertEquals(1, ((PortDetails) outputSequence[2]).getOptions().getVisitDuration());
	// // Assert.assertEquals(11,
	// // ((VoyageDetails) outputSequence[3]).getStartTime());
	// // Assert.assertEquals(15,
	// // ((PortDetails) outputSequence[4]).getStartTime());
	// Assert.assertEquals(1, ((PortDetails) outputSequence[4]).getOptions().getVisitDuration());
	// }
}
