package com.mmxlabs.optimiser.fitness;

import org.junit.Assert;
import org.junit.Test;

public class MatrixProviderFitnessCoreFactoryTest {

	@Test
	public void testMatrixProviderFitnessCoreFactory() {

		final String coreName = "coreName";
		final String componentName = "componentName";
		final String keyName = "keyName";

		final MatrixProviderFitnessCoreFactory factory = new MatrixProviderFitnessCoreFactory(
				coreName, componentName, keyName);

		Assert.assertSame(coreName, factory.getFitnessCoreName());
		Assert.assertSame(componentName, factory.getFitnessComponentNames()
				.iterator().next());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInstantiate() {
		final String coreName = "coreName";
		final String componentName = "componentName";
		final String keyName = "keyName";

		final MatrixProviderFitnessCoreFactory factory = new MatrixProviderFitnessCoreFactory(
				coreName, componentName, keyName);

		final IFitnessCore<Object> core = factory.instantiate();
		Assert.assertNotNull(core);
		Assert.assertTrue(core instanceof MatrixProviderFitnessCore);
	}

}
