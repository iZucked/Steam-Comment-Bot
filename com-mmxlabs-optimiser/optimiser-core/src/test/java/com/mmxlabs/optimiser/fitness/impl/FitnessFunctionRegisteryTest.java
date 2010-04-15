package com.mmxlabs.optimiser.fitness.impl;

import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.fitness.IFitnessCoreFactory;

public class FitnessFunctionRegisteryTest {

	@Test
	public void testGetFitnessFunctionFactories() {

		FitnessFunctionRegistry registery = new FitnessFunctionRegistry();

		IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1",
				CollectionsUtil.makeArrayList("component1"));
		IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory2",
				CollectionsUtil.makeArrayList("component2"));
		IFitnessCoreFactory factory3 = new MockFitnessCoreFactory("factory3",
				CollectionsUtil.makeArrayList("component3"));

		registery.registerFitnessCoreFactory(factory1);
		registery.registerFitnessCoreFactory(factory2);
		registery.registerFitnessCoreFactory(factory3);

		Collection<IFitnessCoreFactory> fitnessFunctionFactories = registery
				.getFitnessCoreFactories();

		Assert.assertEquals(3, fitnessFunctionFactories.size());

		Assert.assertTrue(fitnessFunctionFactories.contains(factory1));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory2));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory3));
	}

	@Test
	public void testGetFitnessFunctionFactories_Str() {

		FitnessFunctionRegistry registery = new FitnessFunctionRegistry();

		IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1",
				CollectionsUtil.makeArrayList("component1"));
		IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory2",
				CollectionsUtil.makeArrayList("component2"));
		IFitnessCoreFactory factory3 = new MockFitnessCoreFactory("factory3",
				CollectionsUtil.makeArrayList("component3"));

		registery.registerFitnessCoreFactory(factory1);
		registery.registerFitnessCoreFactory(factory2);
		registery.registerFitnessCoreFactory(factory3);

		List<String> names = CollectionsUtil.makeArrayList("component3",
				"component2");

		Collection<IFitnessCoreFactory> fitnessFunctionFactories = registery
				.getFitnessCoreFactories(names);
		Assert.assertEquals(2, fitnessFunctionFactories.size());

		Assert.assertTrue(fitnessFunctionFactories.contains(factory3));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory2));
	}

	@Test
	public void testGetFitnessFunctionFactories_Str2() {

		FitnessFunctionRegistry registery = new FitnessFunctionRegistry();

		IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1",
				CollectionsUtil.makeArrayList("component1"));
		IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory2",
				CollectionsUtil.makeArrayList("component2"));
		IFitnessCoreFactory factory3 = new MockFitnessCoreFactory("factory3",
				CollectionsUtil.makeArrayList("component3"));

		registery.registerFitnessCoreFactory(factory1);
		registery.registerFitnessCoreFactory(factory2);
		registery.registerFitnessCoreFactory(factory3);

		List<String> names = CollectionsUtil.makeArrayList("component3",
				"component3");

		Collection<IFitnessCoreFactory> fitnessFunctionFactories = registery
				.getFitnessCoreFactories(names);
		Assert.assertEquals(1, fitnessFunctionFactories.size());

		Assert.assertTrue(fitnessFunctionFactories.contains(factory3));
	}

	@Test
	public void testGetFitnessFunctionFactories_Str3() {

		FitnessFunctionRegistry registery = new FitnessFunctionRegistry();

		IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1",
				CollectionsUtil.makeArrayList("component1"));
		IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory2",
				CollectionsUtil.makeArrayList("component2"));
		IFitnessCoreFactory factory3 = new MockFitnessCoreFactory("factory3",
				CollectionsUtil.makeArrayList("component3"));

		registery.registerFitnessCoreFactory(factory1);
		registery.registerFitnessCoreFactory(factory2);
		registery.registerFitnessCoreFactory(factory3);

		List<String> names = CollectionsUtil.makeArrayList("component3",
				"unknown", "component1");

		Collection<IFitnessCoreFactory> fitnessFunctionFactories = registery
				.getFitnessCoreFactories(names);

		Assert.assertEquals(2, fitnessFunctionFactories.size());

		Assert.assertTrue(fitnessFunctionFactories.contains(factory1));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory3));
	}

	/**
	 * Mock implementation of an {@link IFitnessCoreFactory} which just return
	 * the core name and a {@link Collection} of component names.
	 * 
	 * @author Simon Goodall
	 * 
	 */
	private static class MockFitnessCoreFactory implements IFitnessCoreFactory {

		private final String coreName;
		private final Collection<String> componentNames;

		public MockFitnessCoreFactory(String coreName,
				Collection<String> componentNames) {
			this.coreName = coreName;
			this.componentNames = componentNames;
		}

		@Override
		public Collection<String> getFitnessComponentNames() {
			return componentNames;
		}

		@Override
		public String getFitnessCoreName() {
			return coreName;
		}

		@Override
		public <T> IFitnessCore<T> instantiate() {
			throw new UnsupportedOperationException("Not implemented");
		}
	}
}
