/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints.impl;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;

public class ConstraintCheckerInstantiatorTest {

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistry() {

		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, null);

		Assert.assertTrue(checkers.isEmpty());
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistry2() {

		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, null);

		Assert.assertEquals(1, checkers.size());

		Assert.assertTrue(checkers.get(0) instanceof MockConstraintChecker);
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<String> names = Collections.emptyList();
		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, names, null);

		Assert.assertTrue(checkers.isEmpty());
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString2() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, Collections.singletonList("Unknown"), null);

		Assert.assertEquals(1, checkers.size());

		Assert.assertNull(checkers.get(0));

	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString3() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, Collections.singletonList("Checker"), null);

		Assert.assertEquals(1, checkers.size());

		Assert.assertTrue(checkers.get(0) instanceof MockConstraintChecker);
	}
}
