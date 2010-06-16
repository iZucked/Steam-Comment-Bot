package com.mmxlabs.scheduler.optimiser.fitness.components;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.IAnnotatedSequence;


@RunWith(JMock.class)
public class AbstractCargoSchedulerFitnessComponentTest {

	Mockery context = new JUnit4Mockery();
	
	@Test
	public void testAbstractCargoSchedulerFitnessComponent() {
		
		String name = "name";

		CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();
		
		TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(name, core);

		Assert.assertSame(name, component.getName());
		Assert.assertSame(core, component.getFitnessCore());
	}

	@Test
	public void testGetFitness() {
		
		String name = "name";

		CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();
		
		TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(name, core);

		component.setNewFitness(100);
		component.setOldFitness(200);
		
		Assert.assertEquals(100, component.getFitness());
	}

	@Test
	public void testAccepted() {
		String name = "name";

		CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();
		
		TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(name, core);

		
		/// Set some initial state
		
		IResource r1 = context.mock(IResource.class, "r1");
		IResource r2 = context.mock(IResource.class, "r2");
		
		long oldFitness = 15;
		long newFitness = 25;
		
		Map<IResource, Long> oldFitnesses = new HashMap<IResource, Long>();
		Map<IResource, Long> newFitnesses = new HashMap<IResource, Long>();
		
		oldFitnesses.put(r1, 5l);
		oldFitnesses.put(r2, 10l);
		
		newFitnesses.put(r1, 15l);
		newFitnesses.put(r2, 20l);
		
		component.setNewFitness(newFitness);
		component.setNewFitnessByResource(newFitnesses);
		
		component.setOldFitness(oldFitness);
		component.setOldFitnessByResource(oldFitnesses);
	
		Assert.assertEquals(25, component.getFitness());
		
		/// Test the method
		component.accepted(null, CollectionsUtil.makeArrayList(r1, r2));
		
		Assert.assertEquals(25, component.getFitness());
		
		Assert.assertEquals(25, component.getOldFitness());
		Assert.assertEquals(25, component.getNewFitness());
		
		Map<IResource, Long> oldFitnessByResource = component.getOldFitnessByResource();
		Assert.assertEquals(15, oldFitnessByResource.get(r1).longValue());
		Assert.assertEquals(20, oldFitnessByResource.get(r2).longValue());
	}

	@Test
	public void testUpdateFitness() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrepare() {
		String name = "name";

		CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();
		
		TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(name, core);

		
		/// Set some initial state
		
		IResource r1 = context.mock(IResource.class, "r1");
		IResource r2 = context.mock(IResource.class, "r2");
		
		long oldFitness = 15;
		long newFitness = 25;
		
		Map<IResource, Long> oldFitnesses = new HashMap<IResource, Long>();
		Map<IResource, Long> newFitnesses = new HashMap<IResource, Long>();
		
		oldFitnesses.put(r1, 5l);
		oldFitnesses.put(r2, 10l);
		
		newFitnesses.put(r1, 15l);
		newFitnesses.put(r2, 20l);
		
		component.setNewFitness(newFitness);
		component.setNewFitnessByResource(newFitnesses);
		
		component.setOldFitness(oldFitness);
		component.setOldFitnessByResource(oldFitnesses);
	
		Assert.assertEquals(25, component.getFitness());
		
		/// Test the method
		component.prepare();
		
		Assert.assertEquals(0, component.getFitness());
		
		Map<IResource, Long> fitnessByResource = component.getOldFitnessByResource();
		Assert.assertTrue(fitnessByResource.isEmpty());
	}

	@Test
	public void testComplete() {

		String name = "name";
		
		CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();
		
		TestCargoSchedulerFitnessComponent<Object> component = new TestCargoSchedulerFitnessComponent<Object>(name, core);

		
		/// Set some initial state
		
		IResource r1 = context.mock(IResource.class, "r1");
		IResource r2 = context.mock(IResource.class, "r2");
		
		/// Test the method
		component.prepare();
		
		Assert.assertEquals(0, component.getFitness());
		
		Map<IResource, Long> fitnessByResource = component.getOldFitnessByResource();
		Assert.assertTrue(fitnessByResource.isEmpty());

		component.updateFitness(r1, 5, false);
		component.updateFitness(r2, 10, false);

		// Check old state is updated as expected
		
		Assert.assertEquals(15, component.getFitness());

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
		fail("Not yet implemented");
	}

	private class TestCargoSchedulerFitnessComponent<T> extends
			AbstractCargoSchedulerFitnessComponent<T> {

		protected TestCargoSchedulerFitnessComponent(String name,
				CargoSchedulerFitnessCore<T> core) {
			super(name, core);
		}

		@Override
		public void evaluateSequence(IResource resource, ISequence<T> sequence,
				IAnnotatedSequence<T> annotatedSequence, boolean newSequence) {
			fail("This method is not part of the test");

		}

		@Override
		public void init(IOptimisationData<T> data) {
			
			
			fail("This method is not part of the test");
		}
		

		public long getOldFitness() {
			return oldFitness;
		}

		public void setOldFitness(long oldFitness) {
			this.oldFitness = oldFitness;
		}

		public long getNewFitness() {
			return newFitness;
		}

		public void setNewFitness(long newFitness) {
			this.newFitness = newFitness;
		}

		public Map<IResource, Long> getOldFitnessByResource() {
			return oldFitnessByResource;
		}

		public void setOldFitnessByResource(Map<IResource, Long> oldFitnessByResource) {
			this.oldFitnessByResource = oldFitnessByResource;
		}

		public Map<IResource, Long> getNewFitnessByResource() {
			return newFitnessByResource;
		}

		public void setNewFitnessByResource(Map<IResource, Long> newFitnessByResource) {
			this.newFitnessByResource = newFitnessByResource;
		}
		
	}
}
