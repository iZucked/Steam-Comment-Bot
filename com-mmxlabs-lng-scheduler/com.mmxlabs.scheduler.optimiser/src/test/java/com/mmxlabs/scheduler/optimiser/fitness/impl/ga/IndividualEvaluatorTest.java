/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Ignore;
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
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMultiMatrixProvider;
import com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;

@RunWith(JMock.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IndividualEvaluatorTest {

	/**
	 * Mock implementation of {@link AbstractSequenceScheduler} to allow use of abstract class in tests
	 * 
	 * @author Simon Goodall
	 * 
	 * @param <T>
	 */
	private static class MockSequenceScheduler<T> extends AbstractSequenceScheduler<T> {

		@Override
		public ScheduledSequences schedule(ISequences<T> sequences, boolean b) {
			throw new UnsupportedOperationException("Method invocation is not part of the tests!");

		}

		@Override
		public ScheduledSequences schedule(ISequences<T> sequences, Collection<IResource> affectedResources, boolean forExport) {
			throw new UnsupportedOperationException("Method invocation is not part of the tests!");
		}

		@Override
		public void acceptLastSchedule() {
			throw new UnsupportedOperationException("Method invocation is not part of the tests!");
		}
	}

	private final Mockery context = new JUnit4Mockery();

	@Test
	public void testInit1() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);
		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit2() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setDistanceProvider(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit3() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setDurationsProvider(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit4() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setFitnessComponents(null);

		evaluator.init();
	}

	// @Test(expected = IllegalStateException.class)
	// public void testInit5() {
	// Assert.fail("Not implemented");
	//
	// final IndividualEvaluator<Object> evaluator = new
	// IndividualEvaluator<Object>();
	// createFullyInitialisedEvaluator(evaluator);
	//
	// evaluator.setFitnessComponentWeights(null);
	//
	// evaluator.init();
	// }

	@Test(expected = IllegalStateException.class)
	public void testInit6() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setPortProvider(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit7() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setSequenceScheduler(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit8() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setTimeWindowProvider(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit9() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setVesselProvider(null);

		evaluator.init();
	}

	/**
	 * For use in initXXX() tests. Add in all required elements for init().
	 * initXXX() tests should null one entry to test failure.
	 * 
	 * @param evaluator
	 */
	private void createFullyInitialisedEvaluator(
			final IndividualEvaluator<Object> evaluator) {

		final AbstractSequenceScheduler<Object> scheduler = new MockSequenceScheduler();
		evaluator.setSequenceScheduler(scheduler);

		final Collection<ICargoSchedulerFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final Map<String, Double> fitnessComponentWeights = Collections
				.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessComponentWeights);

		final IMultiMatrixProvider<IPort, Integer> distanceProvider = context
				.mock(IMultiMatrixProvider.class);
		evaluator.setDistanceProvider(distanceProvider);

		final IElementDurationProvider<Object> durationsProvider = context
				.mock(IElementDurationProvider.class);
		evaluator.setDurationsProvider(durationsProvider);

		final IPortProvider portProvider = context.mock(IPortProvider.class);
		evaluator.setPortProvider(portProvider);

		final ITimeWindowDataComponentProvider timeWindowProvider = context
				.mock(ITimeWindowDataComponentProvider.class);
		evaluator.setTimeWindowProvider(timeWindowProvider);

		final IVesselProvider vesselProvider = context
				.mock(IVesselProvider.class);
		evaluator.setVesselProvider(vesselProvider);
	}

	@Test
	public void testGetSetSequenceScheduler() {

		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final AbstractSequenceScheduler<Object> value = new MockSequenceScheduler();

		Assert.assertNull(evaluator.getSequenceScheduler());
		evaluator.setSequenceScheduler(value);
		Assert.assertSame(value, evaluator.getSequenceScheduler());

	}

	@Test
	public void testGetSetTimeWindowProvider() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final ITimeWindowDataComponentProvider value = context
				.mock(ITimeWindowDataComponentProvider.class);
		Assert.assertNull(evaluator.getTimeWindowProvider());
		evaluator.setTimeWindowProvider(value);
		Assert.assertSame(value, evaluator.getTimeWindowProvider());

		evaluator.dispose();
		Assert.assertNull(evaluator.getTimeWindowProvider());
	}

	@Test
	public void testGetSetFitnessComponents() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final Collection<ICargoSchedulerFitnessComponent<Object>> value = Collections
				.emptyList();
		Assert.assertNull(evaluator.getFitnessComponents());
		evaluator.setFitnessComponents(value);
		Assert.assertSame(value, evaluator.getFitnessComponents());

		evaluator.dispose();
		Assert.assertNull(evaluator.getFitnessComponents());

	}

	@Test()
	public void testGetSetFitnessComponentWeights() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessComponentWeights());
		final Map<String, Double> fitnessComponentWeights = Collections
				.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessComponentWeights);
		Assert.assertSame(fitnessComponentWeights,
				evaluator.getFitnessComponentWeights());
		evaluator.dispose();
		Assert.assertNull(evaluator.getFitnessComponentWeights());

	}

	@Test
	public void testGetSetVesselProvider() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final IVesselProvider value = context.mock(IVesselProvider.class);
		Assert.assertNull(evaluator.getVesselProvider());
		evaluator.setVesselProvider(value);
		Assert.assertSame(value, evaluator.getVesselProvider());

		evaluator.dispose();
		Assert.assertNull(evaluator.getVesselProvider());
	}

	@Test
	public void testGetSetPortProvider() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final IPortProvider value = context.mock(IPortProvider.class);
		Assert.assertNull(evaluator.getPortProvider());
		evaluator.setPortProvider(value);
		Assert.assertSame(value, evaluator.getPortProvider());

		evaluator.dispose();
		Assert.assertNull(evaluator.getPortProvider());
	}

	@Test
	public void testGetSetDistanceProvider() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final IMultiMatrixProvider<IPort, Integer> value = context
				.mock(IMultiMatrixProvider.class);
		Assert.assertNull(evaluator.getDistanceProvider());
		evaluator.setDistanceProvider(value);
		Assert.assertSame(value, evaluator.getDistanceProvider());

		evaluator.dispose();
		Assert.assertNull(evaluator.getDistanceProvider());
	}

	@Test
	public void testGetSetDurationsProvider() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final IElementDurationProvider<Object> value = context
				.mock(IElementDurationProvider.class);
		Assert.assertNull(evaluator.getDurationsProvider());
		evaluator.setDurationsProvider(value);
		Assert.assertSame(value, evaluator.getDurationsProvider());

		evaluator.dispose();
		Assert.assertNull(evaluator.getDurationsProvider());
	}

	/**
	 * Test setup with no time windows
	 */
	@Test
	public void testSetup1() {

		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();

		final IMatrixEditor<IPort, Integer> matrix = new HashMapMatrixProvider<IPort, Integer>(
				"default");
		final IMultiMatrixEditor<IPort, Integer> distanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>(
				"distanceProvider");
		distanceProvider.set(IMultiMatrixEditor.Default_Key, matrix);
		evaluator.setDistanceProvider(distanceProvider);

		final IResource resource = context.mock(IResource.class);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setMaxSpeed(20000);
		context.setDefaultResultForType(IVesselClass.class, vesselClass);

		final IVesselProvider vesselProvider = context
				.mock(IVesselProvider.class);
		evaluator.setVesselProvider(vesselProvider);

		final IPortProviderEditor portProvider = new HashMapPortEditor(
				"portProvider");
		evaluator.setPortProvider(portProvider);

		final IPort port1 = context.mock(IPort.class, "port-1");
		final IPort port2 = context.mock(IPort.class, "port-2");
		final IPort port3 = context.mock(IPort.class, "port-3");
		final IPort port4 = context.mock(IPort.class, "port-4");

		final ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(
				"timeWindowProvider");
		evaluator.setTimeWindowProvider(timeWindowProvider);
		final IElementDurationProviderEditor<Object> durationsProvider = new HashMapElementDurationEditor<Object>(
				"durationsProvider");
		evaluator.setDurationsProvider(durationsProvider);

		final Object element1 = new Object();
		final Object element2 = new Object();
		final Object element3 = new Object();
		final Object element4 = new Object();

		portProvider.setPortForElement(port1, element1);
		portProvider.setPortForElement(port2, element2);
		portProvider.setPortForElement(port3, element3);
		portProvider.setPortForElement(port4, element4);

		durationsProvider.setElementDuration(element1, 1);
		durationsProvider.setElementDuration(element2, 2);
		durationsProvider.setElementDuration(element3, 3);
		durationsProvider.setElementDuration(element4, 4);

		matrix.set(port1, port2, 100);
		matrix.set(port2, port3, 200);
		matrix.set(port3, port4, 400);

		evaluator.setSequenceScheduler(new MockSequenceScheduler());
		final Collection<ICargoSchedulerFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final Map<String, Double> fitnessComponentWeights = Collections
				.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessComponentWeights);

		evaluator.init();

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(element1, element2, element3,
						element4));

		context.checking(new Expectations() {
			{
				one(vesselProvider).getVessel(resource);
			}
		});

		evaluator.setup(resource, sequence);

		Assert.assertSame(resource, evaluator.getResource());
		Assert.assertSame(sequence, evaluator.getSequence());

		final int[] multiplier = evaluator.getMultiplier();
		Assert.assertEquals(4, multiplier.length);
		for (int i = 0; i < multiplier.length; ++i) {
			Assert.assertEquals(1, multiplier[i]);
		}

		final int[] ranges = evaluator.getRanges();
		Assert.assertEquals(4, ranges.length);
		for (int i = 0; i < ranges.length; ++i) {
			Assert.assertEquals(0, ranges[i]);
		}

		final int[] travelTimes = evaluator.getTravelTimes();
		final int[] expectedTravelTimes = new int[] { 0, 6, 12, 23 };
		Assert.assertEquals(4, travelTimes.length);
		Assert.assertArrayEquals(expectedTravelTimes, travelTimes);

		final int[] windowStarts = evaluator.getWindowStarts();
		Assert.assertEquals(4, windowStarts.length);
		for (int i = 0; i < windowStarts.length; ++i) {
			Assert.assertEquals(-1, windowStarts[i]);
		}
	}

	@Test
	public void testSetup2() {

		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();

		final IMatrixEditor<IPort, Integer> matrix = new HashMapMatrixProvider<IPort, Integer>(
				"default");
		final IMultiMatrixEditor<IPort, Integer> distanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>(
				"distanceProvider");
		distanceProvider.set(IMultiMatrixEditor.Default_Key, matrix);
		evaluator.setDistanceProvider(distanceProvider);

		final IResource resource = context.mock(IResource.class);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setMaxSpeed(20000);
		context.setDefaultResultForType(IVesselClass.class, vesselClass);

		final IVesselProvider vesselProvider = context
				.mock(IVesselProvider.class);
		evaluator.setVesselProvider(vesselProvider);

		final IPortProviderEditor portProvider = new HashMapPortEditor(
				"portProvider");
		evaluator.setPortProvider(portProvider);

		final IPort port1 = context.mock(IPort.class, "port-1");
		final IPort port2 = context.mock(IPort.class, "port-2");
		final IPort port3 = context.mock(IPort.class, "port-3");
		final IPort port4 = context.mock(IPort.class, "port-4");

		final ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(
				"timeWindowProvider");
		evaluator.setTimeWindowProvider(timeWindowProvider);
		final IElementDurationProviderEditor<Object> durationsProvider = new HashMapElementDurationEditor<Object>(
				"durationsProvider");
		evaluator.setDurationsProvider(durationsProvider);

		final Object element1 = new Object();
		final Object element2 = new Object();
		final Object element3 = new Object();
		final Object element4 = new Object();

		portProvider.setPortForElement(port1, element1);
		portProvider.setPortForElement(port2, element2);
		portProvider.setPortForElement(port3, element3);
		portProvider.setPortForElement(port4, element4);

		durationsProvider.setElementDuration(element1, 1);
		durationsProvider.setElementDuration(element2, 1);
		durationsProvider.setElementDuration(element3, 1);
		durationsProvider.setElementDuration(element4, 1);

		matrix.set(port1, port2, 100);
		matrix.set(port2, port3, 100);
		matrix.set(port3, port4, 100);

		final ITimeWindow tw1 = new TimeWindow(0, 0);
		final ITimeWindow tw2 = new TimeWindow(5, 6);
		final ITimeWindow tw3 = new TimeWindow(10, 15);
		final ITimeWindow tw4 = new TimeWindow(20, 25);

		timeWindowProvider.setTimeWindows(element1,
				Collections.singletonList(tw1));
		timeWindowProvider.setTimeWindows(element2,
				Collections.singletonList(tw2));
		timeWindowProvider.setTimeWindows(element3,
				Collections.singletonList(tw3));
		timeWindowProvider.setTimeWindows(element4,
				Collections.singletonList(tw4));

		evaluator.setSequenceScheduler(new MockSequenceScheduler());
		final Collection<ICargoSchedulerFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final Map<String, Double> fitnessComponentWeights = Collections
				.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessComponentWeights);

		evaluator.init();

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(element1, element2, element3,
						element4));

		context.checking(new Expectations() {
			{
				one(vesselProvider).getVessel(resource);
			}
		});

		evaluator.setup(resource, sequence);

		Assert.assertSame(resource, evaluator.getResource());
		Assert.assertSame(sequence, evaluator.getSequence());

		final int[] multiplier = evaluator.getMultiplier();
		Assert.assertEquals(4, multiplier.length);
		for (int i = 0; i < multiplier.length; ++i) {
			Assert.assertEquals(1, multiplier[i]);
		}

		final int[] ranges = evaluator.getRanges();
		final int[] expectedRanges = new int[] { 0, 0, 3, 5 };
		Assert.assertEquals(4, ranges.length);
		Assert.assertArrayEquals(expectedRanges, ranges);

		final int[] travelTimes = evaluator.getTravelTimes();
		final int[] expectedTravelTimes = new int[] { 0, 6, 6, 6 };
		Assert.assertEquals(4, travelTimes.length);
		Assert.assertArrayEquals(expectedTravelTimes, travelTimes);

		final int[] windowStarts = evaluator.getWindowStarts();
		final int[] expectedWindowStarts = new int[] { 0, 6, 12, 20 };
		Assert.assertEquals(4, windowStarts.length);
		Assert.assertArrayEquals(expectedWindowStarts, windowStarts);
	}

	@Test
	public void testSetup3() {

		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();

		final IMatrixEditor<IPort, Integer> matrix = new HashMapMatrixProvider<IPort, Integer>(
				"default");
		final IMultiMatrixEditor<IPort, Integer> distanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>(
				"distanceProvider");
		distanceProvider.set(IMultiMatrixEditor.Default_Key, matrix);
		evaluator.setDistanceProvider(distanceProvider);

		final IResource resource = context.mock(IResource.class);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setMaxSpeed(20000);
		context.setDefaultResultForType(IVesselClass.class, vesselClass);

		final IVesselProvider vesselProvider = context
				.mock(IVesselProvider.class);
		evaluator.setVesselProvider(vesselProvider);

		final IPortProviderEditor portProvider = new HashMapPortEditor(
				"portProvider");
		evaluator.setPortProvider(portProvider);

		final IPort port1 = context.mock(IPort.class, "port-1");
		final IPort port2 = context.mock(IPort.class, "port-2");
		final IPort port3 = context.mock(IPort.class, "port-3");
		final IPort port4 = context.mock(IPort.class, "port-4");

		final ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(
				"timeWindowProvider");
		evaluator.setTimeWindowProvider(timeWindowProvider);
		final IElementDurationProviderEditor<Object> durationsProvider = new HashMapElementDurationEditor<Object>(
				"durationsProvider");
		evaluator.setDurationsProvider(durationsProvider);

		final Object element1 = new Object();
		final Object element2 = new Object();
		final Object element3 = new Object();
		final Object element4 = new Object();

		portProvider.setPortForElement(port1, element1);
		portProvider.setPortForElement(port2, element2);
		portProvider.setPortForElement(port3, element3);
		portProvider.setPortForElement(port4, element4);

		durationsProvider.setElementDuration(element1, 1);
		durationsProvider.setElementDuration(element2, 2);
		durationsProvider.setElementDuration(element3, 3);
		durationsProvider.setElementDuration(element4, 4);

		matrix.set(port1, port2, 100);
		matrix.set(port2, port3, 200);
		matrix.set(port3, port4, 400);

		final ITimeWindow tw1 = new TimeWindow(0, 0);
		final ITimeWindow tw2 = new TimeWindow(5, 6);
		final ITimeWindow tw3 = new TimeWindow(10, 15);
		final ITimeWindow tw4 = new TimeWindow(20, 25);

		timeWindowProvider.setTimeWindows(element1,
				Collections.singletonList(tw1));
		timeWindowProvider.setTimeWindows(element2,
				Collections.singletonList(tw2));
		timeWindowProvider.setTimeWindows(element3,
				Collections.singletonList(tw3));
		timeWindowProvider.setTimeWindows(element4,
				Collections.singletonList(tw4));

		evaluator.setSequenceScheduler(new MockSequenceScheduler());
		final Collection<ICargoSchedulerFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final Map<String, Double> fitnessComponentWeights = Collections
				.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessComponentWeights);

		evaluator.init();

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(element1, element2, element3,
						element4));

		context.checking(new Expectations() {
			{
				one(vesselProvider).getVessel(resource);
			}
		});

		evaluator.setup(resource, sequence);

		Assert.assertSame(resource, evaluator.getResource());
		Assert.assertSame(sequence, evaluator.getSequence());

		final int[] multiplier = evaluator.getMultiplier();
		Assert.assertEquals(4, multiplier.length);
		for (int i = 0; i < multiplier.length; ++i) {
			Assert.assertEquals(1, multiplier[i]);
		}

		final int[] ranges = evaluator.getRanges();
		Assert.assertEquals(4, ranges.length);
		for (int i = 0; i < ranges.length; ++i) {
			Assert.assertEquals(0, ranges[i]);
		}

		final int[] travelTimes = evaluator.getTravelTimes();
		final int[] expectedTravelTimes = new int[] { 0, 6, 12, 23 };
		Assert.assertEquals(4, travelTimes.length);
		Assert.assertArrayEquals(expectedTravelTimes, travelTimes);

		final int[] windowStarts = evaluator.getWindowStarts();
		final int[] expectedWindowStarts = new int[] { 0, 6, 18, 41 };
		Assert.assertEquals(4, windowStarts.length);
		Assert.assertArrayEquals(expectedWindowStarts, windowStarts);
	}

	/**
	 * Another setup test to trigger the backwards pass code
	 */
	@Test
	public void testSetup4() {

		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();

		final IMatrixEditor<IPort, Integer> matrix = new HashMapMatrixProvider<IPort, Integer>(
				"default");
		final IMultiMatrixEditor<IPort, Integer> distanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>(
				"distanceProvider");
		distanceProvider.set(IMultiMatrixEditor.Default_Key, matrix);
		evaluator.setDistanceProvider(distanceProvider);

		final IResource resource = context.mock(IResource.class);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setMaxSpeed(20000);
		context.setDefaultResultForType(IVesselClass.class, vesselClass);

		final IVesselProvider vesselProvider = context
				.mock(IVesselProvider.class);
		evaluator.setVesselProvider(vesselProvider);

		final IPortProviderEditor portProvider = new HashMapPortEditor(
				"portProvider");
		evaluator.setPortProvider(portProvider);

		final IPort port1 = context.mock(IPort.class, "port-1");
		final IPort port2 = context.mock(IPort.class, "port-2");
		final IPort port3 = context.mock(IPort.class, "port-3");
		final IPort port4 = context.mock(IPort.class, "port-4");

		final ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(
				"timeWindowProvider");
		evaluator.setTimeWindowProvider(timeWindowProvider);
		final IElementDurationProviderEditor<Object> durationsProvider = new HashMapElementDurationEditor<Object>(
				"durationsProvider");
		evaluator.setDurationsProvider(durationsProvider);

		final Object element1 = new Object();
		final Object element2 = new Object();
		final Object element3 = new Object();
		final Object element4 = new Object();

		portProvider.setPortForElement(port1, element1);
		portProvider.setPortForElement(port2, element2);
		portProvider.setPortForElement(port3, element3);
		portProvider.setPortForElement(port4, element4);

		durationsProvider.setElementDuration(element1, 1);
		durationsProvider.setElementDuration(element2, 1);
		durationsProvider.setElementDuration(element3, 1);
		durationsProvider.setElementDuration(element4, 1);

		matrix.set(port1, port2, 80);
		matrix.set(port2, port3, 80);
		matrix.set(port3, port4, 80);

		final ITimeWindow tw1 = new TimeWindow(0, 0);
		final ITimeWindow tw2 = new TimeWindow(5, 6);
		final ITimeWindow tw3 = new TimeWindow(10, 15);
		final ITimeWindow tw4 = new TimeWindow(18, 19);

		timeWindowProvider.setTimeWindows(element1,
				Collections.singletonList(tw1));
		timeWindowProvider.setTimeWindows(element2,
				Collections.singletonList(tw2));
		timeWindowProvider.setTimeWindows(element3,
				Collections.singletonList(tw3));
		timeWindowProvider.setTimeWindows(element4,
				Collections.singletonList(tw4));

		evaluator.setSequenceScheduler(new MockSequenceScheduler());
		final Collection<ICargoSchedulerFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final Map<String, Double> fitnessComponentWeights = Collections
				.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessComponentWeights);

		evaluator.init();

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(element1, element2, element3,
						element4));

		context.checking(new Expectations() {
			{
				one(vesselProvider).getVessel(resource);
			}
		});

		evaluator.setup(resource, sequence);

		Assert.assertSame(resource, evaluator.getResource());
		Assert.assertSame(sequence, evaluator.getSequence());

		final int[] multiplier = evaluator.getMultiplier();
		Assert.assertEquals(4, multiplier.length);
		for (int i = 0; i < multiplier.length; ++i) {
			Assert.assertEquals(1, multiplier[i]);
		}

		final int[] ranges = evaluator.getRanges();
		final int[] expectedRanges = new int[] { 0, 1, 4, 1 };
		Assert.assertEquals(4, ranges.length);
		Assert.assertArrayEquals(expectedRanges, ranges);

		final int[] travelTimes = evaluator.getTravelTimes();
		final int[] expectedTravelTimes = new int[] { 0, 5, 5, 5 };
		Assert.assertEquals(4, travelTimes.length);
		Assert.assertArrayEquals(expectedTravelTimes, travelTimes);

		final int[] windowStarts = evaluator.getWindowStarts();
		final int[] expectedWindowStarts = new int[] { 0, 5, 10, 18 };
		Assert.assertEquals(4, windowStarts.length);
		Assert.assertArrayEquals(expectedWindowStarts, windowStarts);
	}

	@Test
	public void testDecode() {

		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();

		final IMatrixEditor<IPort, Integer> matrix = new HashMapMatrixProvider<IPort, Integer>(
				"default");
		final IMultiMatrixEditor<IPort, Integer> distanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>(
				"distanceProvider");
		distanceProvider.set(IMultiMatrixEditor.Default_Key, matrix);
		evaluator.setDistanceProvider(distanceProvider);

		final IResource resource = context.mock(IResource.class);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setMaxSpeed(20000);
		context.setDefaultResultForType(IVesselClass.class, vesselClass);

		final IVesselProvider vesselProvider = context
				.mock(IVesselProvider.class);
		evaluator.setVesselProvider(vesselProvider);

		final IPortProviderEditor portProvider = new HashMapPortEditor(
				"portProvider");
		evaluator.setPortProvider(portProvider);

		final IPort port1 = context.mock(IPort.class, "port-1");
		final IPort port2 = context.mock(IPort.class, "port-2");
		final IPort port3 = context.mock(IPort.class, "port-3");
		final IPort port4 = context.mock(IPort.class, "port-4");

		final ITimeWindowDataComponentProviderEditor timeWindowProvider = new TimeWindowDataComponentProvider(
				"timeWindowProvider");
		evaluator.setTimeWindowProvider(timeWindowProvider);
		final IElementDurationProviderEditor<Object> durationsProvider = new HashMapElementDurationEditor<Object>(
				"durationsProvider");
		evaluator.setDurationsProvider(durationsProvider);

		final Object element1 = new Object();
		final Object element2 = new Object();
		final Object element3 = new Object();
		final Object element4 = new Object();

		portProvider.setPortForElement(port1, element1);
		portProvider.setPortForElement(port2, element2);
		portProvider.setPortForElement(port3, element3);
		portProvider.setPortForElement(port4, element4);

		durationsProvider.setElementDuration(element1, 1);
		durationsProvider.setElementDuration(element2, 1);
		durationsProvider.setElementDuration(element3, 1);
		durationsProvider.setElementDuration(element4, 1);

		matrix.set(port1, port2, 100);
		matrix.set(port2, port3, 100);
		matrix.set(port3, port4, 100);

		final ITimeWindow tw1 = new TimeWindow(0, 0);
		final ITimeWindow tw2 = new TimeWindow(5, 6);
		final ITimeWindow tw3 = new TimeWindow(10, 15);
		final ITimeWindow tw4 = new TimeWindow(20, 25);

		timeWindowProvider.setTimeWindows(element1,
				Collections.singletonList(tw1));
		timeWindowProvider.setTimeWindows(element2,
				Collections.singletonList(tw2));
		timeWindowProvider.setTimeWindows(element3,
				Collections.singletonList(tw3));
		timeWindowProvider.setTimeWindows(element4,
				Collections.singletonList(tw4));

		evaluator.setSequenceScheduler(new MockSequenceScheduler());
		final Collection<ICargoSchedulerFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final Map<String, Double> fitnessComponentWeights = Collections
				.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessComponentWeights);

		evaluator.init();

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(element1, element2, element3,
						element4));

		context.checking(new Expectations() {
			{
				one(vesselProvider).getVessel(resource);
			}
		});

		int numBytes = evaluator.setup(resource, sequence);

		Assert.assertSame(resource, evaluator.getResource());
		Assert.assertSame(sequence, evaluator.getSequence());

		final int[] multiplier = evaluator.getMultiplier();
		Assert.assertEquals(4, multiplier.length);
		for (int i = 0; i < multiplier.length; ++i) {
			Assert.assertEquals(1, multiplier[i]);
		}

		final int[] ranges = evaluator.getRanges();
		final int[] expectedRanges = new int[] { 0, 0, 3, 5 };
		Assert.assertEquals(4, ranges.length);
		Assert.assertArrayEquals(expectedRanges, ranges);

		final int[] travelTimes = evaluator.getTravelTimes();
		final int[] expectedTravelTimes = new int[] { 0, 6, 6, 6 };
		Assert.assertEquals(4, travelTimes.length);
		Assert.assertArrayEquals(expectedTravelTimes, travelTimes);

		final int[] windowStarts = evaluator.getWindowStarts();
		final int[] expectedWindowStarts = new int[] { 0, 6, 12, 20 };
		Assert.assertEquals(4, windowStarts.length);
		Assert.assertArrayEquals(expectedWindowStarts, windowStarts);

		int[] arrivalTimes = new int[4];
		byte[] bytes = new byte[numBytes];
		ByteArrayIndividual individual = new ByteArrayIndividual(bytes);
		{
			Arrays.fill(arrivalTimes, 0);
			Arrays.fill(bytes, (byte) 0);
			evaluator.decode(individual, arrivalTimes);
			int[] expectedArrivalTimes = new int[] { 0, 6, 12, 20 };
			Assert.assertArrayEquals(expectedArrivalTimes, arrivalTimes);
		}
		{
			Arrays.fill(arrivalTimes, 0);
			Arrays.fill(bytes, (byte) 0);
			bytes[0] = 0x30;
			evaluator.decode(individual, arrivalTimes);
			int[] expectedArrivalTimes = new int[] { 0, 6, 13, 20 };
			Assert.assertArrayEquals(expectedArrivalTimes, arrivalTimes);
		}
		{
			Arrays.fill(arrivalTimes, 0);
			Arrays.fill(bytes, (byte) 0);
			bytes[0] = 0x30;
			bytes[1] = 0x30;
			evaluator.decode(individual, arrivalTimes);
			int[] expectedArrivalTimes = new int[] { 0, 6, 13, 21 };
			Assert.assertArrayEquals(expectedArrivalTimes, arrivalTimes);
		}

	}

	@Ignore
	@Test
	public void testEvaluate() {
		fail("Not yet implemented");
	}

}
