package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.OptimiserTestUtil;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

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

		c.init(null);

		VoyageOptions options = new VoyageOptions();
		options.setDistance(20);

		VoyageDetails<Object> voyageDetails = new VoyageDetails<Object>();
		voyageDetails.setOptions(options);

		final Object[] routeSequence = new Object[] { null, voyageDetails, null };

		final VoyagePlan voyagePlan = new VoyagePlan();
		voyagePlan.setSequence(routeSequence);

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final IResource resource = context.mock(IResource.class);
		final IModifiableSequence<Object> sequence = OptimiserTestUtil
				.makeSequence(obj1, obj2);

		c.prepare();

		c.evaluateSequence(resource, sequence,
				CollectionsUtil.makeArrayList(voyagePlan), false);

		c.complete();

		Assert.fail("Component always returns zero!");
		Assert.assertEquals(20, c.getFitness());

		context.assertIsSatisfied();
	}
}
