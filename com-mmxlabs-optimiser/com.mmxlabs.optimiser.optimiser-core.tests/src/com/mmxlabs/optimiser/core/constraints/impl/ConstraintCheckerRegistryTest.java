/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints.impl;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

public class ConstraintCheckerRegistryTest {

	@Test
	public void testRegisterConstraintCheckerFactory() {

		final ConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		final IConstraintCheckerFactory factory1 = new MockConstraintCheckerFactory("factory1");

		Assert.assertFalse(registry.getConstraintCheckerNames().contains("factory1"));

		registry.registerConstraintCheckerFactory(factory1);

		Assert.assertTrue(registry.getConstraintCheckerNames().contains("factory1"));
	}

	@Test
	public void testDeregisterConstraintCheckerFactory() {

		final ConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		final IConstraintCheckerFactory factory1 = new MockConstraintCheckerFactory("factory1");

		Assert.assertFalse(registry.getConstraintCheckerNames().contains("factory1"));

		registry.registerConstraintCheckerFactory(factory1);

		Assert.assertTrue(registry.getConstraintCheckerNames().contains("factory1"));

		registry.deregisterConstraintCheckerFactory(factory1);

		Assert.assertFalse(registry.getConstraintCheckerNames().contains("factory1"));
	}

	@Test(expected = RuntimeException.class)
	public void testRegisterConstraintCheckerFactory2() {

		final ConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		final IConstraintCheckerFactory factory1 = new MockConstraintCheckerFactory("factory1");
		final IConstraintCheckerFactory factory2 = new MockConstraintCheckerFactory("factory1");

		Assert.assertFalse(registry.getConstraintCheckerNames().contains("factory1"));

		registry.registerConstraintCheckerFactory(factory1);

		Assert.assertTrue(registry.getConstraintCheckerNames().contains("factory1"));

		registry.registerConstraintCheckerFactory(factory2);
	}

	@Test
	public void testGetFitnessFunctionFactories() {

		final ConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();

		final IConstraintCheckerFactory factory1 = new MockConstraintCheckerFactory("factory1");
		final IConstraintCheckerFactory factory2 = new MockConstraintCheckerFactory("factory2");
		final IConstraintCheckerFactory factory3 = new MockConstraintCheckerFactory("factory3");

		registry.registerConstraintCheckerFactory(factory1);
		registry.registerConstraintCheckerFactory(factory2);
		registry.registerConstraintCheckerFactory(factory3);

		final Collection<IConstraintCheckerFactory> constraintCheckerFactories = registry.getConstraintCheckerFactories();

		Assert.assertEquals(3, constraintCheckerFactories.size());

		Assert.assertTrue(constraintCheckerFactories.contains(factory1));
		Assert.assertTrue(constraintCheckerFactories.contains(factory2));
		Assert.assertTrue(constraintCheckerFactories.contains(factory3));
	}

	@Test
	public void testGetFitnessFunctionFactories_Str() {

		final ConstraintCheckerRegistry registery = new ConstraintCheckerRegistry();

		final IConstraintCheckerFactory factory1 = new MockConstraintCheckerFactory("factory1");
		final IConstraintCheckerFactory factory2 = new MockConstraintCheckerFactory("factory2");
		final IConstraintCheckerFactory factory3 = new MockConstraintCheckerFactory("factory3");

		registery.registerConstraintCheckerFactory(factory1);
		registery.registerConstraintCheckerFactory(factory2);
		registery.registerConstraintCheckerFactory(factory3);

		final List<String> names = CollectionsUtil.makeArrayList("factory3", "factory2");

		final Collection<IConstraintCheckerFactory> constraintCheckersFactories = registery.getConstraintCheckerFactories(names);
		Assert.assertEquals(2, constraintCheckersFactories.size());

		Assert.assertTrue(constraintCheckersFactories.contains(factory3));
		Assert.assertTrue(constraintCheckersFactories.contains(factory2));
	}

	@Test
	public void testGetFitnessFunctionFactories_Str2() {

		final ConstraintCheckerRegistry registery = new ConstraintCheckerRegistry();

		final IConstraintCheckerFactory factory1 = new MockConstraintCheckerFactory("factory1");
		final IConstraintCheckerFactory factory2 = new MockConstraintCheckerFactory("factory2");
		final IConstraintCheckerFactory factory3 = new MockConstraintCheckerFactory("factory3");

		registery.registerConstraintCheckerFactory(factory1);
		registery.registerConstraintCheckerFactory(factory2);
		registery.registerConstraintCheckerFactory(factory3);

		final List<String> names = CollectionsUtil.makeArrayList("factory3", "factory3");

		final Collection<IConstraintCheckerFactory> constraintCheckerFactories = registery.getConstraintCheckerFactories(names);
		Assert.assertEquals(1, constraintCheckerFactories.size());

		Assert.assertTrue(constraintCheckerFactories.contains(factory3));
	}

	@Test
	public void testGetFitnessFunctionFactories_Str3() {

		final ConstraintCheckerRegistry registery = new ConstraintCheckerRegistry();

		final IConstraintCheckerFactory factory1 = new MockConstraintCheckerFactory("factory1");
		final IConstraintCheckerFactory factory2 = new MockConstraintCheckerFactory("factory2");
		final IConstraintCheckerFactory factory3 = new MockConstraintCheckerFactory("factory3");

		registery.registerConstraintCheckerFactory(factory1);
		registery.registerConstraintCheckerFactory(factory2);
		registery.registerConstraintCheckerFactory(factory3);

		final List<String> names = CollectionsUtil.makeArrayList("factory3", "unknown", "factory1");

		final Collection<IConstraintCheckerFactory> constraintFactories = registery.getConstraintCheckerFactories(names);

		Assert.assertEquals(2, constraintFactories.size());

		Assert.assertTrue(constraintFactories.contains(factory1));
		Assert.assertTrue(constraintFactories.contains(factory3));
	}

	@Test
	public void testSetConstraintCheckerFactories() {

		final ConstraintCheckerRegistry registery = new ConstraintCheckerRegistry();

		final IConstraintCheckerFactory factory1 = new MockConstraintCheckerFactory("factory1");
		final IConstraintCheckerFactory factory2 = new MockConstraintCheckerFactory("factory2");
		final IConstraintCheckerFactory factory3 = new MockConstraintCheckerFactory("factory3");

		final List<IConstraintCheckerFactory> factoryList = CollectionsUtil.makeArrayList(factory2, factory3);

		registery.registerConstraintCheckerFactory(factory1);
		registery.setConstraintCheckerFactories(factoryList);

		final Collection<IConstraintCheckerFactory> constraintCheckerFactories = registery.getConstraintCheckerFactories();

		Assert.assertEquals(3, constraintCheckerFactories.size());

		Assert.assertTrue(constraintCheckerFactories.contains(factory1));
		Assert.assertTrue(constraintCheckerFactories.contains(factory2));
		Assert.assertTrue(constraintCheckerFactories.contains(factory3));
	}

}
