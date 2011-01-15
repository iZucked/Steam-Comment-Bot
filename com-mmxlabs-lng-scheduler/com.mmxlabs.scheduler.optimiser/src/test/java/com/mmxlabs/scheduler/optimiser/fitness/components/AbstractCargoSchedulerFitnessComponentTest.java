/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@RunWith(JMock.class)
public class AbstractCargoSchedulerFitnessComponentTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testAbstractCargoSchedulerFitnessComponent() {

		final String name = "name";

		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();

		final TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(
				name, core);

		Assert.assertSame(name, component.getName());
		Assert.assertSame(core, component.getFitnessCore());
	}

	@Test
	public void testGetFitness() {

		final String name = "name";

		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();

		final TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(
				name, core);

		component.setNewFitness(100);
		component.setOldFitness(200);

		Assert.assertEquals(100, component.getFitness());
	}

	@Test
	public void testAccepted() {
		final String name = "name";

		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();

		final TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(
				name, core);

		// / Set some initial state

		final IResource r1 = context.mock(IResource.class, "r1");
		final IResource r2 = context.mock(IResource.class, "r2");

		final long oldFitness = 15;
		final long newFitness = 25;

		final Map<IResource, Long> oldFitnesses = new HashMap<IResource, Long>();
		final Map<IResource, Long> newFitnesses = new HashMap<IResource, Long>();

		oldFitnesses.put(r1, 5l);
		oldFitnesses.put(r2, 10l);

		newFitnesses.put(r1, 15l);
		newFitnesses.put(r2, 20l);

		component.setNewFitness(newFitness);
		component.setNewFitnessByResource(newFitnesses);

		component.setOldFitness(oldFitness);
		component.setOldFitnessByResource(oldFitnesses);

		Assert.assertEquals(25, component.getFitness());

		// / Test the method
		component.accepted(null, CollectionsUtil.makeArrayList(r1, r2));

		Assert.assertEquals(25, component.getFitness());

		Assert.assertEquals(25, component.getOldFitness());
		Assert.assertEquals(25, component.getNewFitness());

		final Map<IResource, Long> oldFitnessByResource = component
				.getOldFitnessByResource();
		Assert.assertEquals(15, oldFitnessByResource.get(r1).longValue());
		Assert.assertEquals(20, oldFitnessByResource.get(r2).longValue());
	}

	@Test
	public void testUpdateFitness() {

		final String name = "name";

		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();

		final TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(
				name, core);

		// / Set some initial state

		final IResource r1 = context.mock(IResource.class, "r1");
		final IResource r2 = context.mock(IResource.class, "r2");

		// / Test the method
		component.prepare();

		Assert.assertEquals(0, component.getFitness());

		Map<IResource, Long> fitnessByResource = component
				.getOldFitnessByResource();
		Assert.assertTrue(fitnessByResource.isEmpty());

		component.updateFitness(r1, 5, false);
		component.updateFitness(r2, 10, false);

		// Check old state is updated as expected

		Assert.assertEquals(15, component.getOldFitness());

		fitnessByResource = component.getOldFitnessByResource();
		Assert.assertEquals(5, fitnessByResource.get(r1).longValue());
		Assert.assertEquals(10, fitnessByResource.get(r2).longValue());

		component.complete();

		// Check new state is updated as expected

		fitnessByResource = component.getNewFitnessByResource();
		Assert.assertEquals(5, fitnessByResource.get(r1).longValue());
		Assert.assertEquals(10, fitnessByResource.get(r2).longValue());

		component.updateFitness(r1, 10, true);

		fitnessByResource = component.getNewFitnessByResource();
		Assert.assertEquals(10, fitnessByResource.get(r1).longValue());
		Assert.assertEquals(10, fitnessByResource.get(r2).longValue());

		Assert.assertEquals(20, component.getNewFitness());
		Assert.assertEquals(20, component.getFitness());

		component.updateFitness(r1, 11, true);

		fitnessByResource = component.getNewFitnessByResource();
		Assert.assertEquals(11, fitnessByResource.get(r1).longValue());
		Assert.assertEquals(10, fitnessByResource.get(r2).longValue());

		Assert.assertEquals(21, component.getNewFitness());
		Assert.assertEquals(21, component.getFitness());

	}

	@Test
	public void testPrepare() {
		final String name = "name";

		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();

		final TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(
				name, core);

		// / Set some initial state

		final IResource r1 = context.mock(IResource.class, "r1");
		final IResource r2 = context.mock(IResource.class, "r2");

		final long oldFitness = 15;
		final long newFitness = 25;

		final Map<IResource, Long> oldFitnesses = new HashMap<IResource, Long>();
		final Map<IResource, Long> newFitnesses = new HashMap<IResource, Long>();

		oldFitnesses.put(r1, 5l);
		oldFitnesses.put(r2, 10l);

		newFitnesses.put(r1, 15l);
		newFitnesses.put(r2, 20l);

		component.setNewFitness(newFitness);
		component.setNewFitnessByResource(newFitnesses);

		component.setOldFitness(oldFitness);
		component.setOldFitnessByResource(oldFitnesses);

		Assert.assertEquals(25, component.getFitness());

		// / Test the method
		component.prepare();

		Assert.assertEquals(0, component.getOldFitness());
		// New fitness doesn't change
		Assert.assertEquals(25, component.getFitness());

		final Map<IResource, Long> fitnessByResource = component
				.getOldFitnessByResource();
		Assert.assertTrue(fitnessByResource.isEmpty());
	}

	@Test
	public void testComplete() {

		final String name = "name";

		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();

		final TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(
				name, core);

		// / Set some initial state

		final IResource r1 = context.mock(IResource.class, "r1");
		final IResource r2 = context.mock(IResource.class, "r2");

		// / Test the method
		component.prepare();

		Assert.assertEquals(0, component.getFitness());

		Map<IResource, Long> fitnessByResource = component
				.getOldFitnessByResource();
		Assert.assertTrue(fitnessByResource.isEmpty());

		component.updateFitness(r1, 5, false);
		component.updateFitness(r2, 10, false);

		// Check old state is updated as expected

		Assert.assertEquals(15, component.getOldFitness());

		fitnessByResource = component.getOldFitnessByResource();
		Assert.assertEquals(5, fitnessByResource.get(r1).longValue());
		Assert.assertEquals(10, fitnessByResource.get(r2).longValue());

		component.complete();

		// Check new state is updated as expected

		fitnessByResource = component.getNewFitnessByResource();
		Assert.assertEquals(5, fitnessByResource.get(r1).longValue());
		Assert.assertEquals(10, fitnessByResource.get(r2).longValue());
	}

	@Test
	public void testDispose() {
		final String name = "name";

		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();

		final TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(
				name, core);

		// / Set some initial state

		final IResource r1 = context.mock(IResource.class, "r1");
		final IResource r2 = context.mock(IResource.class, "r2");

		final long oldFitness = 15;
		final long newFitness = 25;

		final Map<IResource, Long> oldFitnesses = new HashMap<IResource, Long>();
		final Map<IResource, Long> newFitnesses = new HashMap<IResource, Long>();

		oldFitnesses.put(r1, 5l);
		oldFitnesses.put(r2, 10l);

		newFitnesses.put(r1, 15l);
		newFitnesses.put(r2, 20l);

		component.setNewFitness(newFitness);
		component.setNewFitnessByResource(newFitnesses);

		component.setOldFitness(oldFitness);
		component.setOldFitnessByResource(oldFitnesses);

		component.dispose();

		Assert.assertTrue(component.getNewFitnessByResource().isEmpty());
		Assert.assertTrue(component.getOldFitnessByResource().isEmpty());

	}

	private class TestCargoSchedulerFitnessComponent<T> extends
			AbstractCargoSchedulerFitnessComponent<T> {

		protected TestCargoSchedulerFitnessComponent(final String name,
				final CargoSchedulerFitnessCore<T> core) {
			super(name, core);
		}

		@Override
		public void init(final IOptimisationData<T> data) {

			fail("This method is not part of the test");
		}

		public long getOldFitness() {
			return oldFitness;
		}

		public void setOldFitness(final long oldFitness) {
			this.oldFitness = oldFitness;
		}

		public long getNewFitness() {
			return newFitness;
		}

		public void setNewFitness(final long newFitness) {
			this.newFitness = newFitness;
		}

		public Map<IResource, Long> getOldFitnessByResource() {
			return oldFitnessByResource;
		}

		public void setOldFitnessByResource(
				final Map<IResource, Long> oldFitnessByResource) {
			this.oldFitnessByResource = oldFitnessByResource;
		}

		public Map<IResource, Long> getNewFitnessByResource() {
			return newFitnessByResource;
		}

		public void setNewFitnessByResource(
				final Map<IResource, Long> newFitnessByResource) {
			this.newFitnessByResource = newFitnessByResource;
		}

		@Override
		public long rawEvaluateSequence(final IResource resource,
				final ISequence<T> sequence, final List<VoyagePlan> plans,
				final int startTime) {
			fail("This method is not part of the test");
			return 0;
		}

		@Override
		public boolean shouldIterate() {
			fail("This method is not part of the test");
			return false;
		}

		@Override
		public void beginIterating(final IResource resource) {
			fail("This method is not part of the test");
		}

		@Override
		public void evaluateNextObject(final Object object, final int startTime) {
			fail("This method is not part of the test");
		}

		@Override
		public void endIterating() {
			fail("This method is not part of the test");
		}
	}
}
