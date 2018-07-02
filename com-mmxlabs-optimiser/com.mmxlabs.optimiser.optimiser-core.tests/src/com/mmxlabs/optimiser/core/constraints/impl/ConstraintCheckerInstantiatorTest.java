/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints.impl;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

public class ConstraintCheckerInstantiatorTest {

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistry() {

		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final IPhaseOptimisationData data = Mockito.mock(IPhaseOptimisationData.class);
		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, data);

		Assertions.assertTrue(checkers.isEmpty());
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistry2() {

		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final IPhaseOptimisationData data = Mockito.mock(IPhaseOptimisationData.class);

		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, data);

		Assertions.assertEquals(1, checkers.size());

		Assertions.assertTrue(checkers.get(0) instanceof MockConstraintChecker);
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<String> names = Collections.emptyList();
		final IPhaseOptimisationData data = Mockito.mock(IPhaseOptimisationData.class);

		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, names, data);

		Assertions.assertTrue(checkers.isEmpty());
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString2() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final IPhaseOptimisationData data = Mockito.mock(IPhaseOptimisationData.class);

		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, Collections.singletonList("Unknown"), data);

		Assertions.assertEquals(1, checkers.size());

		Assertions.assertNull(checkers.get(0));

	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString3() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new MockConstraintCheckerFactory("Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final IPhaseOptimisationData data = Mockito.mock(IPhaseOptimisationData.class);

		final List<IConstraintChecker> checkers = inst.instantiateConstraintCheckers(registry, Collections.singletonList("Checker"), data);

		Assertions.assertEquals(1, checkers.size());

		Assertions.assertTrue(checkers.get(0) instanceof MockConstraintChecker);
	}
}
