package com.mmxlabs.jobcontroller.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.components.impl.TimeWindow;
import com.mmxlabs.optimiser.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.dcproviders.ITimeWindowDataComponentProviderEditor;
import com.mmxlabs.optimiser.dcproviders.impl.HashMapElementDurationEditor;
import com.mmxlabs.optimiser.dcproviders.impl.TimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.impl.ListSequence;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.Resource;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.optimiser.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.fitness.IAnnotatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AnnotatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SimpleSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider.PortType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlanAnnotator;

public class DummyDataCreator {

//	IOptimisationData<ISequenceElement> createProblem() {
//
//		final SchedulerBuilder builder = new SchedulerBuilder();
//
//		// Build XY ports so distance is automatically populated`
//		// TODO: Add API to determine which distance provider to use
//		final IPort port1 = builder.createPort("port-1", 0, 0);
//		final IPort port2 = builder.createPort("port-2", 0, 5);
//		final IPort port3 = builder.createPort("port-3", 5, 0);
//		final IPort port4 = builder.createPort("port-4", 5, 5);
//		final IPort port5 = builder.createPort("port-5", 0, 10);
//		final IPort port6 = builder.createPort("port-6", 5, 10);
//
//		TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
//		keypoints.put(12000, 12000l);
//		keypoints.put(13000, 13000l);
//		keypoints.put(14000, 14000l);
//		keypoints.put(15000, 15000l);
//		keypoints.put(16000, 16000l);
//		keypoints.put(17000, 17000l);
//		keypoints.put(18000, 18000l);
//		keypoints.put(19000, 19000l);
//		keypoints.put(20000, 20000l);
//		InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(
//				keypoints);
//
//		IVesselClass vesselClass1 = builder.createVesselClass("vesselClass-1",
//				12000, 20000, 150000000, 0);
//
//		builder.setVesselClassStateParamaters(vesselClass1, VesselState.Laden,
//				15000, 10000, 10000, consumptionCalculator, 15000);
//		builder.setVesselClassStateParamaters(vesselClass1,
//				VesselState.Ballast, 15000, 10000, 10000,
//				consumptionCalculator, 15000);
//
//		builder.createVessel("vessel-1", vesselClass1);
//		builder.createVessel("vessel-2", vesselClass1);
//		builder.createVessel("vessel-3", vesselClass1);
//
//		final ITimeWindow tw1 = builder.createTimeWindow(5, 6);
//		final ITimeWindow tw2 = builder.createTimeWindow(10, 11);
//
//		final ITimeWindow tw3 = builder.createTimeWindow(15, 16);
//		final ITimeWindow tw4 = builder.createTimeWindow(20, 21);
//
//		final ITimeWindow tw5 = builder.createTimeWindow(25, 26);
//		final ITimeWindow tw6 = builder.createTimeWindow(30, 31);
//
//		final ITimeWindow tw7 = builder.createTimeWindow(35, 36);
//
//		ILoadSlot load1 = builder.createLoadSlot("load1", port1, tw1, 0,
//				1000000, 5);
//		ILoadSlot load2 = builder.createLoadSlot("load2", port1, tw3, 0,
//				1000000, 5);
//		ILoadSlot load3 = builder.createLoadSlot("load3", port1, tw5, 0,
//				1000000, 5);
//		ILoadSlot load4 = builder.createLoadSlot("load4", port1, tw4, 0,
//				1000000, 5);
//		ILoadSlot load5 = builder.createLoadSlot("load5", port3, tw2, 0,
//				1000000, 5);
//		ILoadSlot load6 = builder.createLoadSlot("load6", port3, tw4, 0,
//				1000000, 5);
//		ILoadSlot load7 = builder.createLoadSlot("load7", port5, tw6, 0,
//				1000000, 5);
//
//		IDischargeSlot discharge1 = builder.createDischargeSlot("discharge1",
//				port2, tw2, 0, 1000000, 6);
//		IDischargeSlot discharge2 = builder.createDischargeSlot("discharge2",
//				port2, tw4, 0, 1000000, 6);
//		IDischargeSlot discharge3 = builder.createDischargeSlot("discharge3",
//				port2, tw6, 0, 1000000, 6);
//		IDischargeSlot discharge4 = builder.createDischargeSlot("discharge4",
//				port6, tw6, 0, 1000000, 6);
//		IDischargeSlot discharge5 = builder.createDischargeSlot("discharge5",
//				port4, tw3, 0, 1000000, 6);
//		IDischargeSlot discharge6 = builder.createDischargeSlot("discharge6",
//				port4, tw5, 0, 1000000, 6);
//		IDischargeSlot discharge7 = builder.createDischargeSlot("discharge7",
//				port6, tw7, 0, 1000000, 6);
//
//		builder.createCargo("cargo1", load1, discharge1);
//		builder.createCargo("cargo2", load2, discharge2);
//		builder.createCargo("cargo3", load3, discharge3);
//		builder.createCargo("cargo4", load4, discharge4);
//		builder.createCargo("cargo5", load5, discharge5);
//		builder.createCargo("cargo6", load6, discharge6);
//		builder.createCargo("cargo7", load7, discharge7);
//
//		// TODO: Set port durations
//
//		// ....
//
//		// Generate the optimisation data
//		final IOptimisationData<ISequenceElement> data = builder
//				.getOptimisationData();
//
//		builder.dispose();
//
//		return data;
//	}
//
//	/**
//	 * Create a randomly generated {@link ISequences} object using the given
//	 * seed
//	 * 
//	 * @param <T>
//	 * @param data
//	 * @param seed
//	 * @return
//	 */
//	<T> ISequences<T> createInitialSequences(final IOptimisationData<T> data,
//			final long seed) {
//		final Random random = new Random(seed);
//		final Collection<T> sequenceElements = data.getSequenceElements();
//		final List<T> shuffledElements = new ArrayList<T>(sequenceElements);
//		Collections.shuffle(shuffledElements, random);
//
//		final List<IResource> resources = data.getResources();
//		final IModifiableSequences<T> sequences = new ModifiableSequences<T>(
//				resources);
//		for (final T e : shuffledElements) {
//			final int nextInt = random.nextInt(resources.size());
//			sequences.getModifiableSequence(nextInt).add(e);
//		}
//		return sequences;
//	}
//
//	public void testLSO() {
//
//		final long seed = 1;
//
//		// Build opt data
//		final IOptimisationData<ISequenceElement> data = createProblem();
//		// Generate initial state
//		final ISequences<ISequenceElement> initialSequences = createInitialSequences(
//				data, seed);
//
//		
//		SimpleSequenceScheduler<ISequenceElement> scheduler = new SimpleSequenceScheduler<ISequenceElement>();
//		
//
//	}
//	
//	public static IAnnotatedSequence<ISequenceElement> createData2() {
//
//		SimpleSequenceScheduler<ISequenceElement> scheduler = new SimpleSequenceScheduler<ISequenceElement>();
//
//		Port port1 = new Port("port1");
//		Port port2 = new Port("port2");
//		Port port3 = new Port("port3");
//		Port port4 = new Port("port4");
//
//		ITimeWindow timeWindow1 = new TimeWindow(5, 6);
//		ITimeWindow timeWindow2 = new TimeWindow(10, 11);
//		ITimeWindow timeWindow3 = new TimeWindow(15, 16);
//		ITimeWindow timeWindow4 = new TimeWindow(20, 21);
//
//		LoadSlot loadSlot1 = new LoadSlot();
//		loadSlot1.setPort(port1);
//		loadSlot1.setTimeWindow(timeWindow1);
//		DischargeSlot dischargeSlot1 = new DischargeSlot();
//		dischargeSlot1.setPort(port2);
//		dischargeSlot1.setTimeWindow(timeWindow2);
//
//		Cargo cargo1 = new Cargo();
//		cargo1.setId("cargo1");
//		cargo1.setLoadSlot(loadSlot1);
//		cargo1.setDischargeSlot(dischargeSlot1);
//
//		LoadSlot loadSlot2 = new LoadSlot();
//		loadSlot2.setPort(port3);
//		loadSlot2.setTimeWindow(timeWindow3);
//		DischargeSlot dischargeSlot2 = new DischargeSlot();
//		dischargeSlot2.setPort(port4);
//		dischargeSlot2.setTimeWindow(timeWindow4);
//
//		Cargo cargo2 = new Cargo();
//		cargo2.setId("cargo2");
//		cargo2.setLoadSlot(loadSlot2);
//		cargo2.setDischargeSlot(dischargeSlot2);
//
//		
//		loadSlot1.setMaxLoadVolume(Long.MAX_VALUE);
//		loadSlot2.setMaxLoadVolume(Long.MAX_VALUE);
//		dischargeSlot1.setMaxDischargeVolume(Long.MAX_VALUE);
//		dischargeSlot2.setMaxDischargeVolume(Long.MAX_VALUE);
//		
//		ISequenceElement element1 = new SequenceElement("element1", loadSlot1);
//		ISequenceElement element2 = new SequenceElement("element2",
//				dischargeSlot1);
//		ISequenceElement element3 = new SequenceElement("element3", loadSlot2);
//		ISequenceElement element4 = new SequenceElement("element4",
//				dischargeSlot2);
//
//		ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(
//				SchedulerConstants.DCP_timeWindowProvider);
//
//		timeWindowProvider.setTimeWindows(element1,
//				Collections.singletonList(timeWindow1));
//		timeWindowProvider.setTimeWindows(element2,
//				Collections.singletonList(timeWindow2));
//		timeWindowProvider.setTimeWindows(element3,
//				Collections.singletonList(timeWindow3));
//		timeWindowProvider.setTimeWindows(element4,
//				Collections.singletonList(timeWindow4));
//
//		HashMapMatrixProvider<IPort, Integer> distanceProvider = new HashMapMatrixProvider<IPort, Integer>(
//				SchedulerConstants.DCP_portDistanceProvider);
//
//		// Only need sparse matrix for testing
//		distanceProvider.set(port1, port2, 400);
//		distanceProvider.set(port2, port3, 400);
//		distanceProvider.set(port3, port4, 400);
//
//		final int duration = 1;
//		IElementDurationProviderEditor<ISequenceElement> durationsProvider = new HashMapElementDurationEditor<ISequenceElement>(
//				SchedulerConstants.DCP_elementDurationsProvider);
//		durationsProvider.setDefaultValue(duration);
//
//		IPortProviderEditor portProvider = new HashMapPortEditor(
//				SchedulerConstants.DCP_portProvider);
//		portProvider.setPortForElement(port1, element1);
//		portProvider.setPortForElement(port2, element2);
//		portProvider.setPortForElement(port3, element3);
//		portProvider.setPortForElement(port4, element4);
//
//		IPortSlotProviderEditor<ISequenceElement> portSlotProvider = new HashMapPortSlotEditor<ISequenceElement>(
//				SchedulerConstants.DCP_portSlotsProvider);
//		portSlotProvider.setPortSlot(element1, loadSlot1);
//		portSlotProvider.setPortSlot(element2, dischargeSlot1);
//		portSlotProvider.setPortSlot(element3, loadSlot2);
//		portSlotProvider.setPortSlot(element4, dischargeSlot2);
//
//		IPortTypeProviderEditor<ISequenceElement> portTypeProvider = new HashMapPortTypeEditor<ISequenceElement>(
//				SchedulerConstants.DCP_portTypeProvider);
//		portTypeProvider.setPortType(element1, PortType.Load);
//		portTypeProvider.setPortType(element2, PortType.Discharge);
//		portTypeProvider.setPortType(element3, PortType.Load);
//		portTypeProvider.setPortType(element4, PortType.Discharge);
//
//		// Set data providers
//		scheduler.setDistanceProvider(distanceProvider);
//		scheduler.setDurationsProvider(durationsProvider);
//		scheduler.setPortProvider(portProvider);
//		scheduler.setTimeWindowProvider(timeWindowProvider);
//		scheduler.setPortSlotProvider(portSlotProvider);
//		scheduler.setPortTypeProvider(portTypeProvider);
//
//		IResource resource = new Resource();
//		
//		
//		TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
//		keypoints.put(12000, 12000l);
//		keypoints.put(13000, 13000l);
//		keypoints.put(14000, 14000l);
//		keypoints.put(15000, 15000l);
//		keypoints.put(16000, 16000l);
//		keypoints.put(17000, 17000l);
//		keypoints.put(18000, 18000l);
//		keypoints.put(19000, 19000l);
//		keypoints.put(20000, 20000l);
//		InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(
//				keypoints);
//
//		VesselClass vesselClass1 = new VesselClass();
//		vesselClass1.setName("vesselClass-1");
//		vesselClass1.setMinSpeed(12000);
//		vesselClass1.setMaxSpeed(20000);
//		vesselClass1.setMinHeel(0);
//		vesselClass1.setCargoCapacity(150000000);
//
//		
//		for (VesselState state : VesselState.values()) {
//			vesselClass1.setConsumptionRate(state, consumptionCalculator);
//			vesselClass1.setMinNBOSpeed(state, 150000);
//			vesselClass1.setNBORate(state, 10000);
//			vesselClass1.setIdleConsumptionRate(state, 10000);
//			vesselClass1.setIdleNBORate(state, 10000);
//		}
//		
//		final Vessel vessel = new Vessel();
//		vessel.setName("vessel-1");
//		vessel.setVesselClass(vesselClass1);
//
//		IVesselProviderEditor vesselProvider = new HashMapVesselEditor(
//				SchedulerConstants.DCP_vesselProvider);
//		vesselProvider.setVesselResource(resource, vessel);
//		scheduler.setVesselProvider(vesselProvider);
//
//		List<ISequenceElement> elements = CollectionsUtil.makeArrayList(
//				element1, element2, element3, element4);
//		ISequence<ISequenceElement> sequence = new ListSequence<ISequenceElement>(
//				elements);
//
//		final LNGVoyageCalculator<ISequenceElement> voyageCalculator = new LNGVoyageCalculator<ISequenceElement>();
//		scheduler.setVoyageCalculator(voyageCalculator);
//
//		// This may throw IllegalStateException if not all the elements are set.
//		// TODO: Expand this into it's own series of test cases
//		scheduler.init();
//		
//		// Schedule sequence
//		List<IVoyagePlan> plans = scheduler.schedule(resource, sequence);
//
//		VoyagePlanAnnotator<ISequenceElement> annotator = new VoyagePlanAnnotator<ISequenceElement>();
//		annotator.setPortSlotProvider(portSlotProvider);
//		
//		AnnotatedSequence<ISequenceElement> annotatedSequence = new AnnotatedSequence<ISequenceElement>();
//		annotator.annonateFromVoyagePlan(resource, plans, annotatedSequence);
////		annotatedSequence.setSequenceElements(elements);
////		annotatedSequence.setVessel(vessel);
//		
//
//		return annotatedSequence;
//	}
}
