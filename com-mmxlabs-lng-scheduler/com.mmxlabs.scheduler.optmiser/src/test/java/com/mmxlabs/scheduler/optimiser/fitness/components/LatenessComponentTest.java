package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.lang.ref.WeakReference;
import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.components.impl.TimeWindow;
import com.mmxlabs.optimiser.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.dcproviders.impl.TimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.lso.impl.OptimiserTestUtil;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.optimiser.scenario.impl.OptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.events.impl.PortVisitEventImpl;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.IAnnotatedSequence;

public class LatenessComponentTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testLatenessComponent() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();
		final LatenessComponent<Object> c = new LatenessComponent<Object>(name,
				core);

		Assert.assertSame(name, c.getName());
		Assert.assertSame(core, c.getFitnessCore());
	}

	@Test
	public void testInit() {

		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		final LatenessComponent<Object> c = new LatenessComponent<Object>(name,
				core);

		@SuppressWarnings("unchecked")
		final IOptimisationData<Object> data = context
				.mock(IOptimisationData.class);

		context.setDefaultResultForType(IDataComponentProvider.class, context
				.mock(ITimeWindowDataComponentProvider.class));

		context.checking(new Expectations() {
			{
				one(data).getDataComponentProvider(
						SchedulerConstants.DCP_timeWindowProvider,
						ITimeWindowDataComponentProvider.class);
			}
		});

		c.init(data);

		context.assertIsSatisfied();
	}

	@Test
	public void testEvaluateSequence() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		final LatenessComponent<Object> c = new LatenessComponent<Object>(name,
				core);

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final TimeWindow window1 = new TimeWindow(10, 11);
		final TimeWindow window2 = new TimeWindow(20, 21);

		final TimeWindowDataComponentProvider provider = new TimeWindowDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider);
		provider.setTimeWindows(obj1, Collections
				.singletonList((ITimeWindow) window1));
		provider.setTimeWindows(obj2, Collections
				.singletonList((ITimeWindow) window2));

		context.setDefaultResultForType(IDataComponentProvider.class, provider);

		@SuppressWarnings("unchecked")
		final IOptimisationData<Object> data = context
				.mock(IOptimisationData.class);

		context.checking(new Expectations() {
			{
				one(data).getDataComponentProvider(
						SchedulerConstants.DCP_timeWindowProvider,
						ITimeWindowDataComponentProvider.class);
			}
		});

		c.init(data);

		@SuppressWarnings("unchecked")
		final IAnnotatedSequence<Object> scheduler = context
				.mock(IAnnotatedSequence.class);

		final IResource resource = context.mock(IResource.class);
		final IModifiableSequence<Object> sequence = OptimiserTestUtil
				.makeSequence(obj1, obj2);

		final PortVisitEventImpl<Object> portEvent = new PortVisitEventImpl<Object>();
		portEvent.setStartTime(10);
		portEvent.setEndTime(11);

		context.setDefaultResultForType(Object.class, portEvent);
		context.checking(new Expectations() {
			{
				one(scheduler).getAnnotation(obj1,
						SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				one(scheduler).getAnnotation(obj2,
						SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
			}
		});

		c.prepare();

		c.evaluateSequence(resource, sequence, scheduler, false);

		c.complete();

		// Ontime or early -- no penalty
		Assert.assertEquals(0, c.getFitness());

		context.assertIsSatisfied();
	}

	@Test
	public void testEvaluateSequence2() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		final LatenessComponent<Object> c = new LatenessComponent<Object>(name,
				core);

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final TimeWindow window1 = new TimeWindow(10, 11);
		final TimeWindow window2 = new TimeWindow(20, 21);

		final TimeWindowDataComponentProvider provider = new TimeWindowDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider);
		provider.setTimeWindows(obj1, Collections
				.singletonList((ITimeWindow) window1));
		provider.setTimeWindows(obj2, Collections
				.singletonList((ITimeWindow) window2));

		context.setDefaultResultForType(IDataComponentProvider.class, provider);

		@SuppressWarnings("unchecked")
		final IOptimisationData<Object> data = context
				.mock(IOptimisationData.class);

		context.checking(new Expectations() {
			{
				one(data).getDataComponentProvider(
						SchedulerConstants.DCP_timeWindowProvider,
						ITimeWindowDataComponentProvider.class);
			}
		});

		c.init(data);

		@SuppressWarnings("unchecked")
		final IAnnotatedSequence<Object> scheduler = context
				.mock(IAnnotatedSequence.class);

		final IResource resource = context.mock(IResource.class);
		final IModifiableSequence<Object> sequence = OptimiserTestUtil
				.makeSequence(obj1, obj2);

		final PortVisitEventImpl<Object> portEvent = new PortVisitEventImpl<Object>();
		portEvent.setStartTime(20);
		portEvent.setEndTime(21);

		context.setDefaultResultForType(Object.class, portEvent);
		context.checking(new Expectations() {
			{
				one(scheduler).getAnnotation(obj1,
						SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
				one(scheduler).getAnnotation(obj2,
						SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
			}
		});

		c.prepare();

		c.evaluateSequence(resource, sequence, scheduler, false);

		c.complete();

		// Late!
		Assert.assertEquals(9, c.getFitness());

		context.assertIsSatisfied();
	}

	@Test
	public void testDispose() {

		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		final LatenessComponent<Object> c = new LatenessComponent<Object>(name,
				core);

		// Avoid using jmock here in case it caches object refs.
		OptimisationData<Object> data = new OptimisationData<Object>();
		TimeWindowDataComponentProvider provider = new TimeWindowDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider);
		data.addDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider, provider);

		c.init(data);

		final WeakReference<TimeWindowDataComponentProvider> ref = new WeakReference<TimeWindowDataComponentProvider>(
				provider);

		// Call dispose to clean up internal refs
		data.dispose();
		c.dispose();
		// Remove local refs
		data = null;
		provider = null;

		// Run GC. WeakRef will *hopefully* be cleaned up.
		// We may wish to run this a few times to be sure(r)
		System.gc();

		// This should return null if the object ref has been properly cleared.
		Assert.assertNull(ref.get());
	}
}
