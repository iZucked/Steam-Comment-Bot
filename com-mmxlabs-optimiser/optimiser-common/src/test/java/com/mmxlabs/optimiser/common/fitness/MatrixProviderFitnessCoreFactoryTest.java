/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.fitness;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.common.fitness.MatrixProviderFitnessCore;
import com.mmxlabs.optimiser.common.fitness.MatrixProviderFitnessCoreFactory;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

public class MatrixProviderFitnessCoreFactoryTest {

	@Test
	public void testMatrixProviderFitnessCoreFactory() {

		final String coreName = "coreName";
		final String componentName = "componentName";
		final String keyName = "keyName";

		final MatrixProviderFitnessCoreFactory factory = new MatrixProviderFitnessCoreFactory(coreName, componentName, keyName);

		Assert.assertSame(coreName, factory.getFitnessCoreName());
		Assert.assertSame(componentName, factory.getFitnessComponentNames().iterator().next());
	}

	@Test
	public void testInstantiate() {
		final String coreName = "coreName";
		final String componentName = "componentName";
		final String keyName = "keyName";

		final MatrixProviderFitnessCoreFactory factory = new MatrixProviderFitnessCoreFactory(coreName, componentName, keyName);

		final IFitnessCore core = factory.instantiate();
		Assert.assertNotNull(core);
		Assert.assertTrue(core instanceof MatrixProviderFitnessCore);
	}

}
