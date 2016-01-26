/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints.impl;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

@SuppressWarnings("null")
public class ConstraintCheckerInstantiatorTest {

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistry() {

		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final IOptimisationData data = Mockito.mock(IOptimisationData.class);
		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, data);

		Assert.assertTrue(checkers.isEmpty());
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistry2() {

		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final IOptimisationData data = Mockito.mock(IOptimisationData.class);

		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, data);

		Assert.assertEquals(1, checkers.size());

		Assert.assertTrue(checkers.get(0) instanceof MockConstraintChecker);
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<String> names = Collections.emptyList();
		final IOptimisationData data = Mockito.mock(IOptimisationData.class);

		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, names, data);

		Assert.assertTrue(checkers.isEmpty());
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString2() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final IOptimisationData data = Mockito.mock(IOptimisationData.class);

		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, Collections.singletonList("Unknown"), data);

		Assert.assertEquals(1, checkers.size());

		Assert.assertNull(checkers.get(0));

	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString3() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final IOptimisationData data = Mockito.mock(IOptimisationData.class);

		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, Collections.singletonList("Checker"), data);

		Assert.assertEquals(1, checkers.size());

		Assert.assertTrue(checkers.get(0) instanceof MockConstraintChecker);
	}
}
