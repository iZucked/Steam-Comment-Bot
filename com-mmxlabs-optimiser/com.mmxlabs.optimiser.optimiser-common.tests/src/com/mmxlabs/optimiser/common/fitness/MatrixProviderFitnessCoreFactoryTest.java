/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.fitness;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

public class MatrixProviderFitnessCoreFactoryTest {

	@Test
	public void testMatrixProviderFitnessCoreFactory() {

		final String coreName = "coreName";
		final String componentName = "componentName";

		final MatrixProviderFitnessCoreFactory factory = new MatrixProviderFitnessCoreFactory(coreName, componentName);

		Assert.assertSame(coreName, factory.getFitnessCoreName());
		Assert.assertSame(componentName, factory.getFitnessComponentNames().iterator().next());
	}

	@Test
	public void testInstantiate() {
		final String coreName = "coreName";
		final String componentName = "componentName";

		final MatrixProviderFitnessCoreFactory factory = new MatrixProviderFitnessCoreFactory(coreName, componentName);

		final IFitnessCore core = factory.instantiate();
		Assert.assertNotNull(core);
		Assert.assertTrue(core instanceof MatrixProviderFitnessCore);
	}

}
