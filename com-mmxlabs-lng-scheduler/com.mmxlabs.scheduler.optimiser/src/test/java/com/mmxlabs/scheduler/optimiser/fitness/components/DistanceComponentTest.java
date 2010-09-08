package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.OptimiserTestUtil;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.impl.JourneyEventImpl;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;

@RunWith(JMock.class)
public class DistanceComponentTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testDistanceComponent() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();
		final DistanceComponent<Object> c = new DistanceComponent<Object>(name,
				core);

		Assert.assertSame(name, c.getName());
		Assert.assertSame(core, c.getFitnessCore());
	}

	@Test
	public void testInit() {

		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		final DistanceComponent<Object> c = new DistanceComponent<Object>(name,
				core);

		@SuppressWarnings("unchecked")
		final IOptimisationData<Object> data = context
				.mock(IOptimisationData.class);
		c.init(data);

		context.assertIsSatisfied();

		// If we get here, all is good
	}

	@Test
	public void testEvaluateSequence() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		final DistanceComponent<Object> c = new DistanceComponent<Object>(name,
				core);

		@SuppressWarnings("unchecked")
		final IOptimisationData<Object> data = context
				.mock(IOptimisationData.class);
		c.init(data);

		@SuppressWarnings("unchecked")
		final IAnnotatedSequence<Object> scheduler = context
				.mock(IAnnotatedSequence.class);

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final IResource resource = context.mock(IResource.class);
		final IModifiableSequence<Object> sequence = OptimiserTestUtil
				.makeSequence(obj1, obj2);

		final JourneyEventImpl<Object> journeyEvent = new JourneyEventImpl<Object>();
		journeyEvent.setDistance(10);

		context.setDefaultResultForType(Object.class, journeyEvent);
		context.checking(new Expectations() {
			{
				one(scheduler).getAnnotation(obj1,
						SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
				one(scheduler).getAnnotation(obj2,
						SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			}
		});

		c.prepare();

		c.evaluateSequence(resource, sequence, scheduler, false);

		c.complete();

		Assert.assertEquals(20, c.getFitness());

		context.assertIsSatisfied();
	}
}
