/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;

public class FitnessFunctionRegistryTest {

	@Test
	public void testRegisterFitnessFunctionFactory() {

		final FitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		final IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1", CollectionsUtil.makeArrayList("component1"));

		Assert.assertFalse(registry.getFitnessComponentNames().contains("component1"));
		Assert.assertFalse(registry.getFitnessCoreFactoryNames().contains("factory1"));

		registry.registerFitnessCoreFactory(factory1);

		Assert.assertTrue(registry.getFitnessComponentNames().contains("component1"));
		Assert.assertTrue(registry.getFitnessCoreFactoryNames().contains("factory1"));
	}

	@Test(expected = RuntimeException.class)
	public void testRegisterFitnessFunctionFactory2() {

		final FitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		final IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1", CollectionsUtil.makeArrayList("component1"));

		final IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory1", CollectionsUtil.makeArrayList("component2"));

		Assert.assertFalse(registry.getFitnessComponentNames().contains("component1"));
		Assert.assertFalse(registry.getFitnessCoreFactoryNames().contains("factory1"));

		registry.registerFitnessCoreFactory(factory1);

		Assert.assertTrue(registry.getFitnessComponentNames().contains("component1"));
		Assert.assertTrue(registry.getFitnessCoreFactoryNames().contains("factory1"));

		registry.registerFitnessCoreFactory(factory2);
	}

	@Test(expected = RuntimeException.class)
	public void testRegisterFitnessFunctionFactory3() {

		final FitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		final IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1", CollectionsUtil.makeArrayList("component1"));

		final IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory2", CollectionsUtil.makeArrayList("component1"));

		Assert.assertFalse(registry.getFitnessComponentNames().contains("component1"));
		Assert.assertFalse(registry.getFitnessCoreFactoryNames().contains("factory1"));

		registry.registerFitnessCoreFactory(factory1);

		Assert.assertTrue(registry.getFitnessComponentNames().contains("component1"));
		Assert.assertTrue(registry.getFitnessCoreFactoryNames().contains("factory1"));

		registry.registerFitnessCoreFactory(factory2);
	}

	@Test
	public void testGetFitnessFunctionFactories() {

		final FitnessFunctionRegistry registry = new FitnessFunctionRegistry();

		final IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1", CollectionsUtil.makeArrayList("component1"));
		final IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory2", CollectionsUtil.makeArrayList("component2"));
		final IFitnessCoreFactory factory3 = new MockFitnessCoreFactory("factory3", CollectionsUtil.makeArrayList("component3"));

		registry.registerFitnessCoreFactory(factory1);
		registry.registerFitnessCoreFactory(factory2);
		registry.registerFitnessCoreFactory(factory3);

		final Collection<IFitnessCoreFactory> fitnessFunctionFactories = registry.getFitnessCoreFactories();

		Assert.assertEquals(3, fitnessFunctionFactories.size());

		Assert.assertTrue(fitnessFunctionFactories.contains(factory1));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory2));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory3));
	}

	@Test
	public void testGetFitnessFunctionFactories_Str() {

		final FitnessFunctionRegistry registery = new FitnessFunctionRegistry();

		final IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1", CollectionsUtil.makeArrayList("component1"));
		final IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory2", CollectionsUtil.makeArrayList("component2"));
		final IFitnessCoreFactory factory3 = new MockFitnessCoreFactory("factory3", CollectionsUtil.makeArrayList("component3"));

		registery.registerFitnessCoreFactory(factory1);
		registery.registerFitnessCoreFactory(factory2);
		registery.registerFitnessCoreFactory(factory3);

		final List<String> names = CollectionsUtil.makeArrayList("component3", "component2");

		final Collection<IFitnessCoreFactory> fitnessFunctionFactories = registery.getFitnessCoreFactories(names);
		Assert.assertEquals(2, fitnessFunctionFactories.size());

		Assert.assertTrue(fitnessFunctionFactories.contains(factory3));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory2));
	}

	@Test
	public void testGetFitnessFunctionFactories_Str2() {

		final FitnessFunctionRegistry registery = new FitnessFunctionRegistry();

		final IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1", CollectionsUtil.makeArrayList("component1"));
		final IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory2", CollectionsUtil.makeArrayList("component2"));
		final IFitnessCoreFactory factory3 = new MockFitnessCoreFactory("factory3", CollectionsUtil.makeArrayList("component3"));

		registery.registerFitnessCoreFactory(factory1);
		registery.registerFitnessCoreFactory(factory2);
		registery.registerFitnessCoreFactory(factory3);

		final List<String> names = CollectionsUtil.makeArrayList("component3", "component3");

		final Collection<IFitnessCoreFactory> fitnessFunctionFactories = registery.getFitnessCoreFactories(names);
		Assert.assertEquals(1, fitnessFunctionFactories.size());

		Assert.assertTrue(fitnessFunctionFactories.contains(factory3));
	}

	@Test
	public void testGetFitnessFunctionFactories_Str3() {

		final FitnessFunctionRegistry registery = new FitnessFunctionRegistry();

		final IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1", CollectionsUtil.makeArrayList("component1"));
		final IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory2", CollectionsUtil.makeArrayList("component2"));
		final IFitnessCoreFactory factory3 = new MockFitnessCoreFactory("factory3", CollectionsUtil.makeArrayList("component3"));

		registery.registerFitnessCoreFactory(factory1);
		registery.registerFitnessCoreFactory(factory2);
		registery.registerFitnessCoreFactory(factory3);

		final List<String> names = CollectionsUtil.makeArrayList("component3", "unknown", "component1");

		final Collection<IFitnessCoreFactory> fitnessFunctionFactories = registery.getFitnessCoreFactories(names);

		Assert.assertEquals(2, fitnessFunctionFactories.size());

		Assert.assertTrue(fitnessFunctionFactories.contains(factory1));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory3));
	}

	@Test
	public void testSetFitnessCoreFactories() {

		final FitnessFunctionRegistry registery = new FitnessFunctionRegistry();

		final IFitnessCoreFactory factory1 = new MockFitnessCoreFactory("factory1", CollectionsUtil.makeArrayList("component1"));
		final IFitnessCoreFactory factory2 = new MockFitnessCoreFactory("factory2", CollectionsUtil.makeArrayList("component2"));
		final IFitnessCoreFactory factory3 = new MockFitnessCoreFactory("factory3", CollectionsUtil.makeArrayList("component3"));

		final List<IFitnessCoreFactory> factoryList = CollectionsUtil.makeArrayList(factory2, factory3);

		registery.registerFitnessCoreFactory(factory1);
		registery.setFitnessCoreFactories(factoryList);

		final Collection<IFitnessCoreFactory> fitnessFunctionFactories = registery.getFitnessCoreFactories();

		Assert.assertEquals(3, fitnessFunctionFactories.size());

		Assert.assertTrue(fitnessFunctionFactories.contains(factory1));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory2));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory3));
	}
}
