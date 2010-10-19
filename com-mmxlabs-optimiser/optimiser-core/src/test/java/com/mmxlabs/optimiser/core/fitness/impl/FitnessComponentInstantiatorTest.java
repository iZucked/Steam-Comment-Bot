/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;

public class FitnessComponentInstantiatorTest {

	@Test
	public void testInstantiateFitnessesIFitnessFunctionRegistry() {

		final IFitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		final FitnessComponentInstantiator inst = new FitnessComponentInstantiator();
		final List<IFitnessComponent<Object>> components = inst
				.instantiateFitnesses(registry);

		Assert.assertTrue(components.isEmpty());
	}

	@Test
	public void testInstantiateFitnessesIFitnessFunctionRegistry2() {

		final IFitnessFunctionRegistry registry = new FitnessFunctionRegistry();

		registry.registerFitnessCoreFactory(new MockFitnessCoreFactory("core",
				Collections.singletonList("component")));

		final FitnessComponentInstantiator inst = new FitnessComponentInstantiator();
		final List<IFitnessComponent<Object>> components = inst
				.instantiateFitnesses(registry);

		Assert.assertEquals(1, components.size());

		Assert
				.assertTrue(components.get(0) instanceof MockFitnessComponent<?>);
	}

	@Test
	public void testInstantiateFitnessesIFitnessFunctionRegistryListOfString() {

		final IFitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		registry.registerFitnessCoreFactory(new MockFitnessCoreFactory("core",
				Collections.singletonList("component")));
		final FitnessComponentInstantiator inst = new FitnessComponentInstantiator();
		final List<String> names = Collections.emptyList();
		final List<IFitnessComponent<Object>> components = inst
				.instantiateFitnesses(registry, names);

		Assert.assertTrue(components.isEmpty());
	}

	@Test
	public void testInstantiateFitnessesIFitnessFunctionRegistryListOfString2() {
		final IFitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		registry.registerFitnessCoreFactory(new MockFitnessCoreFactory("core",
				Collections.singletonList("component")));

		final FitnessComponentInstantiator inst = new FitnessComponentInstantiator();
		final List<IFitnessComponent<Object>> components = inst
				.instantiateFitnesses(registry, Collections
						.singletonList("Unknown"));

		Assert.assertEquals(1, components.size());

		Assert.assertNull(components.get(0));

	}

	@Test
	public void testInstantiateFitnessesIFitnessFunctionRegistryListOfString3() {
		final IFitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		registry.registerFitnessCoreFactory(new MockFitnessCoreFactory("core",
				Collections.singletonList("component")));

		final FitnessComponentInstantiator inst = new FitnessComponentInstantiator();
		final List<IFitnessComponent<Object>> components = inst
				.instantiateFitnesses(registry, Collections
						.singletonList("component"));

		Assert.assertEquals(1, components.size());

		Assert.assertTrue(components.get(0) instanceof MockFitnessComponent<?>);
	}
}
