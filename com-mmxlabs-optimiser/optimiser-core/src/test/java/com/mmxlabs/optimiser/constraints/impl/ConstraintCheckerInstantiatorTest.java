package com.mmxlabs.optimiser.constraints.impl;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;

public class ConstraintCheckerInstantiatorTest {

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistry() {

		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker<Object>> checkers = inst
				.instantiateConstraintCheckers(registry);

		Assert.assertTrue(checkers.isEmpty());
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistry2() {

		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry
				.registerConstraintCheckerFactory(new MockConstraintCheckerFactory(
						"Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker<Object>> checkers = inst
				.instantiateConstraintCheckers(registry);

		Assert.assertEquals(1, checkers.size());

		Assert.assertTrue(checkers.get(0) instanceof MockConstraintChecker<?>);
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry
				.registerConstraintCheckerFactory(new MockConstraintCheckerFactory(
						"Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<String> names = Collections.emptyList();
		final List<IConstraintChecker<Object>> checkers = inst
				.instantiateConstraintCheckers(registry, names);

		Assert.assertTrue(checkers.isEmpty());
	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString2() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry
				.registerConstraintCheckerFactory(new MockConstraintCheckerFactory(
						"Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker<Object>> checkers = inst
				.instantiateConstraintCheckers(registry, Collections
						.singletonList("Unknown"));

		Assert.assertEquals(1, checkers.size());

		Assert.assertNull(checkers.get(0));

	}

	@Test
	public void testInstantiateConstraintCheckersIConstraintCheckerRegistryListOfString3() {
		final IConstraintCheckerRegistry registry = new ConstraintCheckerRegistry();
		registry
				.registerConstraintCheckerFactory(new MockConstraintCheckerFactory(
						"Checker"));

		final ConstraintCheckerInstantiator inst = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker<Object>> checkers = inst
				.instantiateConstraintCheckers(registry, Collections
						.singletonList("Checker"));

		Assert.assertEquals(1, checkers.size());

		Assert.assertTrue(checkers.get(0) instanceof MockConstraintChecker<?>);
	}
}
