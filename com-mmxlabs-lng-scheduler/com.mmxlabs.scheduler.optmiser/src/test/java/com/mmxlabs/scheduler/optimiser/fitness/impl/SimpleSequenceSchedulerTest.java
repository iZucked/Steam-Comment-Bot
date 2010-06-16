package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.components.impl.TimeWindow;
import com.mmxlabs.optimiser.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.dcproviders.ITimeWindowDataComponentProviderEditor;
import com.mmxlabs.optimiser.dcproviders.impl.HashMapElementDurationEditor;
import com.mmxlabs.optimiser.dcproviders.impl.TimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.impl.ListSequence;
import com.mmxlabs.optimiser.impl.Resource;
import com.mmxlabs.optimiser.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.IAnnotatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;

public final class SimpleSequenceSchedulerTest {

	/**
	 * Simple case, everything runs exactly on time. Only one idle time required
	 * at start.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSchedule_1() {

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
		DischargeSlot dischargeSlot1 = new  DischargeSlot();
		dischargeSlot1.setPort(port2);
		dischargeSlot1.setTimeWindow(timeWindow2);
		
		Cargo cargo1 = new Cargo();
		cargo1.setId("cargo1");
		cargo1.setLoadSlot(loadSlot1);
		cargo1.setDischargeSlot(dischargeSlot1);

		LoadSlot loadSlot2 = new LoadSlot();
		loadSlot2.setPort(port3);
		loadSlot2.setTimeWindow(timeWindow3);
		DischargeSlot dischargeSlot2 = new  DischargeSlot();
		dischargeSlot2.setPort(port4);
		dischargeSlot2.setTimeWindow(timeWindow4);
		
		Cargo cargo2 = new Cargo();
		cargo2.setId("cargo2");
		cargo2.setLoadSlot(loadSlot2);
		cargo2.setDischargeSlot(dischargeSlot2);

		ISequenceElement element1 = new SequenceElement("element1", loadSlot1);
		ISequenceElement element2 = new SequenceElement("element2", dischargeSlot1);
		ISequenceElement element3 = new SequenceElement("element3", loadSlot2);
		ISequenceElement element4 = new SequenceElement("element4", dischargeSlot2);

		ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider);

		timeWindowProvider.setTimeWindows(element1, Collections
				.singletonList(timeWindow1));
		timeWindowProvider.setTimeWindows(element2, Collections
				.singletonList(timeWindow2));
		timeWindowProvider.setTimeWindows(element3, Collections
				.singletonList(timeWindow3));
		timeWindowProvider.setTimeWindows(element4, Collections
				.singletonList(timeWindow4));

		HashMapMatrixProvider<IPort, Integer> distanceProvider = new HashMapMatrixProvider<IPort, Integer>(
				SchedulerConstants.DCP_portDistanceProvider);

		// Only need sparse matrix for testing
		distanceProvider.set(port1, port2, 4);
		distanceProvider.set(port2, port3, 4);
		distanceProvider.set(port3, port4, 4);

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

		// Set data providers
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);

		IResource resource = new Resource();

		List<ISequenceElement> elements = CollectionsUtil.makeArrayList(
				element1, element2, element3, element4);
		ISequence<ISequenceElement> sequence = new ListSequence<ISequenceElement>(
				elements);

		IAnnotatedSequence<ISequenceElement> annotatedSequence = new AnnotatedSequence<ISequenceElement>();
		// Schedule sequence
		scheduler.schedule(resource, sequence, annotatedSequence);

		// TODO: look through additional info objects and validate expected
		// output
		{
			IPortVisitEvent<ISequenceElement> visitElement = annotatedSequence
					.getAnnotation(element1, SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(visitElement);
			Assert.assertSame(port1, visitElement.getPort());
			Assert.assertEquals(duration, visitElement.getDuration());
			Assert.assertEquals(timeWindow1.getStart(), visitElement
					.getStartTime());
			Assert.assertEquals(timeWindow1.getStart() + duration, visitElement
					.getEndTime());
			Assert.assertSame(element1, visitElement.getSequenceElement());

			IIdleEvent<ISequenceElement> idleElement = annotatedSequence
					.getAnnotation(element1, SchedulerConstants.AI_idleInfo,
							IIdleEvent.class);
			Assert.assertNotNull(idleElement);
			Assert.assertSame(port1, idleElement.getPort());
			Assert.assertSame(element1, idleElement.getSequenceElement());
			Assert.assertEquals(0, idleElement.getStartTime());
			Assert.assertEquals(5, idleElement.getEndTime());
			Assert.assertEquals(5, idleElement.getDuration());

			IJourneyEvent<ISequenceElement> journeyElement = annotatedSequence
					.getAnnotation(element1, SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNull(journeyElement);
		}

		{
			IPortVisitEvent<ISequenceElement> visitElement = annotatedSequence
					.getAnnotation(element2, SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(visitElement);
			Assert.assertSame(port2, visitElement.getPort());
			Assert.assertEquals(duration, visitElement.getDuration());
			Assert.assertEquals(timeWindow2.getStart(), visitElement
					.getStartTime());
			Assert.assertEquals(timeWindow2.getStart() + duration, visitElement
					.getEndTime());
			Assert.assertSame(element2, visitElement.getSequenceElement());

			IIdleEvent idleElement = annotatedSequence.getAnnotation(element2,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleElement);
			Assert.assertSame(port2, idleElement.getPort());
			Assert.assertSame(element2, idleElement.getSequenceElement());
			Assert.assertEquals(10, idleElement.getStartTime());
			Assert.assertEquals(10, idleElement.getEndTime());
			Assert.assertEquals(0, idleElement.getDuration());

			IJourneyEvent journeyElement = annotatedSequence.getAnnotation(
					element2, SchedulerConstants.AI_journeyInfo,
					IJourneyEvent.class);
			Assert.assertNotNull(journeyElement);
			Assert.assertSame(port1, journeyElement.getFromPort());
			Assert.assertSame(port2, journeyElement.getToPort());
			Assert.assertSame(element2, journeyElement.getSequenceElement());
			Assert.assertEquals(6, journeyElement.getStartTime());
			Assert.assertEquals(10, journeyElement.getEndTime());
			Assert.assertEquals(4, journeyElement.getDuration());
			Assert.assertEquals(4, journeyElement.getDistance());
		}

		{
			IPortVisitEvent visitElement = annotatedSequence.getAnnotation(
					element3, SchedulerConstants.AI_visitInfo,
					IPortVisitEvent.class);
			Assert.assertNotNull(visitElement);
			Assert.assertSame(port3, visitElement.getPort());
			Assert.assertEquals(duration, visitElement.getDuration());
			Assert.assertEquals(timeWindow3.getStart(), visitElement
					.getStartTime());
			Assert.assertEquals(timeWindow3.getStart() + duration, visitElement
					.getEndTime());
			Assert.assertSame(element3, visitElement.getSequenceElement());

			IIdleEvent idleElement = annotatedSequence.getAnnotation(element3,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleElement);
			Assert.assertSame(port3, idleElement.getPort());
			Assert.assertSame(element3, idleElement.getSequenceElement());
			Assert.assertEquals(15, idleElement.getStartTime());
			Assert.assertEquals(15, idleElement.getEndTime());
			Assert.assertEquals(0, idleElement.getDuration());

			IJourneyEvent journeyElement = annotatedSequence.getAnnotation(
					element3, SchedulerConstants.AI_journeyInfo,
					IJourneyEvent.class);
			Assert.assertNotNull(journeyElement);
			Assert.assertSame(port2, journeyElement.getFromPort());
			Assert.assertSame(port3, journeyElement.getToPort());
			Assert.assertSame(element3, journeyElement.getSequenceElement());
			Assert.assertEquals(11, journeyElement.getStartTime());
			Assert.assertEquals(15, journeyElement.getEndTime());
			Assert.assertEquals(4, journeyElement.getDuration());
			Assert.assertEquals(4, journeyElement.getDistance());
		}

		{
			IPortVisitEvent visitElement = annotatedSequence.getAnnotation(
					element4, SchedulerConstants.AI_visitInfo,
					IPortVisitEvent.class);
			Assert.assertNotNull(visitElement);
			Assert.assertSame(port4, visitElement.getPort());
			Assert.assertEquals(duration, visitElement.getDuration());
			Assert.assertEquals(timeWindow4.getStart(), visitElement
					.getStartTime());
			Assert.assertEquals(timeWindow4.getStart() + duration, visitElement
					.getEndTime());
			Assert.assertSame(element4, visitElement.getSequenceElement());

			IIdleEvent idleElement = annotatedSequence.getAnnotation(element4,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleElement);
			Assert.assertSame(port4, idleElement.getPort());
			Assert.assertSame(element4, idleElement.getSequenceElement());
			Assert.assertEquals(20, idleElement.getStartTime());
			Assert.assertEquals(20, idleElement.getEndTime());
			Assert.assertEquals(0, idleElement.getDuration());

			IJourneyEvent journeyElement = annotatedSequence.getAnnotation(
					element4, SchedulerConstants.AI_journeyInfo,
					IJourneyEvent.class);
			Assert.assertNotNull(journeyElement);
			Assert.assertSame(port3, journeyElement.getFromPort());
			Assert.assertSame(port4, journeyElement.getToPort());
			Assert.assertSame(element4, journeyElement.getSequenceElement());
			Assert.assertEquals(16, journeyElement.getStartTime());
			Assert.assertEquals(20, journeyElement.getEndTime());
			Assert.assertEquals(4, journeyElement.getDuration());
			Assert.assertEquals(4, journeyElement.getDistance());
		}
	}

	/**
	 * Slightly more complex case - introduce a lateness
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSchedule_2() {

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
		DischargeSlot dischargeSlot1 = new  DischargeSlot();
		dischargeSlot1.setPort(port2);
		dischargeSlot1.setTimeWindow(timeWindow2);
		
		Cargo cargo1 = new Cargo();
		cargo1.setId("cargo1");
		cargo1.setLoadSlot(loadSlot1);
		cargo1.setDischargeSlot(dischargeSlot1);

		LoadSlot loadSlot2 = new LoadSlot();
		loadSlot2.setPort(port3);
		loadSlot2.setTimeWindow(timeWindow3);
		DischargeSlot dischargeSlot2 = new  DischargeSlot();
		dischargeSlot2.setPort(port4);
		dischargeSlot2.setTimeWindow(timeWindow4);
		
		Cargo cargo2 = new Cargo();
		cargo2.setId("cargo2");
		cargo2.setLoadSlot(loadSlot2);
		cargo2.setDischargeSlot(dischargeSlot2);

		ISequenceElement element1 = new SequenceElement("element1", loadSlot1);
		ISequenceElement element2 = new SequenceElement("element2", dischargeSlot1);
		ISequenceElement element3 = new SequenceElement("element3", loadSlot2);
		ISequenceElement element4 = new SequenceElement("element4", dischargeSlot2);

		ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider);

		timeWindowProvider.setTimeWindows(element1, Collections
				.singletonList(timeWindow1));
		timeWindowProvider.setTimeWindows(element2, Collections
				.singletonList(timeWindow2));
		timeWindowProvider.setTimeWindows(element3, Collections
				.singletonList(timeWindow3));
		timeWindowProvider.setTimeWindows(element4, Collections
				.singletonList(timeWindow4));

		HashMapMatrixProvider<IPort, Integer> distanceProvider = new HashMapMatrixProvider<IPort, Integer>(
				SchedulerConstants.DCP_portDistanceProvider);

		// Only need sparse matrix for testing
		distanceProvider.set(port1, port2, 4);
		distanceProvider.set(port2, port3, 5);
		distanceProvider.set(port3, port4, 4);

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

		// Set data providers
		scheduler.setDistanceProvider(distanceProvider);
		scheduler.setDurationsProvider(durationsProvider);
		scheduler.setPortProvider(portProvider);
		scheduler.setTimeWindowProvider(timeWindowProvider);

		IResource resource = new Resource();

		List<ISequenceElement> elements = CollectionsUtil.makeArrayList(
				element1, element2, element3, element4);
		ISequence<ISequenceElement> sequence = new ListSequence<ISequenceElement>(
				elements);

		IAnnotatedSequence<ISequenceElement> annotatedSequence = new AnnotatedSequence<ISequenceElement>();
		// Schedule sequence
		scheduler.schedule(resource, sequence, annotatedSequence);

		// TODO: look through additional info objects and validate expected
		// output
		{
			IPortVisitEvent visitElement = annotatedSequence.getAnnotation(
					element1, SchedulerConstants.AI_visitInfo,
					IPortVisitEvent.class);
			Assert.assertNotNull(visitElement);
			Assert.assertSame(port1, visitElement.getPort());
			Assert.assertEquals(duration, visitElement.getDuration());
			Assert.assertEquals(timeWindow1.getStart(), visitElement
					.getStartTime());
			Assert.assertEquals(timeWindow1.getStart() + duration, visitElement
					.getEndTime());
			Assert.assertSame(element1, visitElement.getSequenceElement());

			IIdleEvent idleElement = annotatedSequence.getAnnotation(element1,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleElement);
			Assert.assertSame(port1, idleElement.getPort());
			Assert.assertSame(element1, idleElement.getSequenceElement());
			Assert.assertEquals(0, idleElement.getStartTime());
			Assert.assertEquals(5, idleElement.getEndTime());
			Assert.assertEquals(5, idleElement.getDuration());

			IJourneyEvent journeyElement = annotatedSequence.getAnnotation(
					element1, SchedulerConstants.AI_journeyInfo,
					IJourneyEvent.class);
			Assert.assertNull(journeyElement);
		}

		{
			IPortVisitEvent visitElement = annotatedSequence.getAnnotation(
					element2, SchedulerConstants.AI_visitInfo,
					IPortVisitEvent.class);
			Assert.assertNotNull(visitElement);
			Assert.assertSame(port2, visitElement.getPort());
			Assert.assertEquals(duration, visitElement.getDuration());
			Assert.assertEquals(timeWindow2.getStart(), visitElement
					.getStartTime());
			Assert.assertEquals(timeWindow2.getStart() + duration, visitElement
					.getEndTime());
			Assert.assertSame(element2, visitElement.getSequenceElement());

			IIdleEvent idleElement = annotatedSequence.getAnnotation(element2,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleElement);
			Assert.assertSame(port2, idleElement.getPort());
			Assert.assertSame(element2, idleElement.getSequenceElement());
			Assert.assertEquals(10, idleElement.getStartTime());
			Assert.assertEquals(10, idleElement.getEndTime());
			Assert.assertEquals(0, idleElement.getDuration());

			IJourneyEvent journeyElement = annotatedSequence.getAnnotation(
					element2, SchedulerConstants.AI_journeyInfo,
					IJourneyEvent.class);
			Assert.assertNotNull(journeyElement);
			Assert.assertSame(port1, journeyElement.getFromPort());
			Assert.assertSame(port2, journeyElement.getToPort());
			Assert.assertSame(element2, journeyElement.getSequenceElement());
			Assert.assertEquals(6, journeyElement.getStartTime());
			Assert.assertEquals(10, journeyElement.getEndTime());
			Assert.assertEquals(4, journeyElement.getDuration());
			Assert.assertEquals(4, journeyElement.getDistance());
		}

		{
			IPortVisitEvent visitElement = annotatedSequence.getAnnotation(
					element3, SchedulerConstants.AI_visitInfo,
					IPortVisitEvent.class);
			Assert.assertNotNull(visitElement);
			Assert.assertSame(port3, visitElement.getPort());
			Assert.assertEquals(duration, visitElement.getDuration());
			Assert.assertEquals(timeWindow3.getStart() + 1, visitElement
					.getStartTime());
			Assert.assertEquals(timeWindow3.getStart() + duration + 1,
					visitElement.getEndTime());
			Assert.assertSame(element3, visitElement.getSequenceElement());

			IIdleEvent idleElement = annotatedSequence.getAnnotation(element3,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleElement);
			Assert.assertSame(port3, idleElement.getPort());
			Assert.assertSame(element3, idleElement.getSequenceElement());
			Assert.assertEquals(16, idleElement.getStartTime());
			Assert.assertEquals(16, idleElement.getEndTime());
			Assert.assertEquals(0, idleElement.getDuration());

			IJourneyEvent journeyElement = annotatedSequence.getAnnotation(
					element3, SchedulerConstants.AI_journeyInfo,
					IJourneyEvent.class);
			Assert.assertNotNull(journeyElement);
			Assert.assertSame(port2, journeyElement.getFromPort());
			Assert.assertSame(port3, journeyElement.getToPort());
			Assert.assertSame(element3, journeyElement.getSequenceElement());
			Assert.assertEquals(11, journeyElement.getStartTime());
			Assert.assertEquals(16, journeyElement.getEndTime());
			Assert.assertEquals(5, journeyElement.getDuration());
			Assert.assertEquals(5, journeyElement.getDistance());
		}

		{
			IPortVisitEvent visitElement = annotatedSequence.getAnnotation(
					element4, SchedulerConstants.AI_visitInfo,
					IPortVisitEvent.class);
			Assert.assertNotNull(visitElement);
			Assert.assertSame(port4, visitElement.getPort());
			Assert.assertEquals(duration, visitElement.getDuration());
			Assert.assertEquals(1 + timeWindow4.getStart(), visitElement
					.getStartTime());
			Assert.assertEquals(1 + timeWindow4.getStart() + duration,
					visitElement.getEndTime());
			Assert.assertSame(element4, visitElement.getSequenceElement());

			IIdleEvent idleElement = annotatedSequence.getAnnotation(element4,
					SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idleElement);
			Assert.assertSame(port4, idleElement.getPort());
			Assert.assertSame(element4, idleElement.getSequenceElement());
			Assert.assertEquals(21, idleElement.getStartTime());
			Assert.assertEquals(21, idleElement.getEndTime());
			Assert.assertEquals(0, idleElement.getDuration());

			IJourneyEvent journeyElement = annotatedSequence.getAnnotation(
					element4, SchedulerConstants.AI_journeyInfo,
					IJourneyEvent.class);
			Assert.assertNotNull(journeyElement);
			Assert.assertSame(port3, journeyElement.getFromPort());
			Assert.assertSame(port4, journeyElement.getToPort());
			Assert.assertSame(element4, journeyElement.getSequenceElement());
			Assert.assertEquals(17, journeyElement.getStartTime());
			Assert.assertEquals(21, journeyElement.getEndTime());
			Assert.assertEquals(4, journeyElement.getDuration());
			Assert.assertEquals(4, journeyElement.getDistance());
		}
	}
}
