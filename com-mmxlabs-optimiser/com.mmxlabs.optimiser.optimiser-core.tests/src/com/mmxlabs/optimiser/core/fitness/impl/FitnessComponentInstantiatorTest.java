/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;

public class FitnessComponentInstantiatorTest {

	@Test
	public void testInstantiateFitnessesIFitnessFunctionRegistry() {

		final IFitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		final FitnessComponentInstantiator inst = new FitnessComponentInstantiator();
		final List<IFitnessComponent> components = inst.instantiateFitnesses(registry);

		Assertions.assertTrue(components.isEmpty());
	}

	@Test
	public void testInstantiateFitnessesIFitnessFunctionRegistry2() {

		final IFitnessFunctionRegistry registry = new FitnessFunctionRegistry();

		registry.registerFitnessCoreFactory(new MockFitnessCoreFactory("core", Collections.singletonList("component")));

		final FitnessComponentInstantiator inst = new FitnessComponentInstantiator();
		final List<IFitnessComponent> components = inst.instantiateFitnesses(registry);

		Assertions.assertEquals(1, components.size());

		Assertions.assertTrue(components.get(0) instanceof MockFitnessComponent);
	}

	@Test
	public void testInstantiateFitnessesIFitnessFunctionRegistryListOfString() {

		final IFitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		registry.registerFitnessCoreFactory(new MockFitnessCoreFactory("core", Collections.singletonList("component")));
		final FitnessComponentInstantiator inst = new FitnessComponentInstantiator();
		final List<String> names = Collections.emptyList();
		final List<IFitnessComponent> components = inst.instantiateFitnesses(registry, names);

		Assertions.assertTrue(components.isEmpty());
	}

	@Test
	public void testInstantiateFitnessesIFitnessFunctionRegistryListOfString2() {
		final IFitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		registry.registerFitnessCoreFactory(new MockFitnessCoreFactory("core", Collections.singletonList("component")));

		final FitnessComponentInstantiator inst = new FitnessComponentInstantiator();
		final List<IFitnessComponent> components = inst.instantiateFitnesses(registry, Collections.singletonList("Unknown"));

		Assertions.assertEquals(0, components.size());
	}

	@Test
	public void testInstantiateFitnessesIFitnessFunctionRegistryListOfString3() {
		final IFitnessFunctionRegistry registry = new FitnessFunctionRegistry();
		registry.registerFitnessCoreFactory(new MockFitnessCoreFactory("core", Collections.singletonList("component")));

		final FitnessComponentInstantiator inst = new FitnessComponentInstantiator();
		final List<IFitnessComponent> components = inst.instantiateFitnesses(registry, Collections.singletonList("component"));

		Assertions.assertEquals(1, components.size());

		Assertions.assertTrue(components.get(0) instanceof MockFitnessComponent);
	}
}
