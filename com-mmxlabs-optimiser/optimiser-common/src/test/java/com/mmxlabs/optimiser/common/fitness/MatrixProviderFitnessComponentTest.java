package com.mmxlabs.optimiser.common.fitness;


import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.common.fitness.MatrixProviderFitnessComponent;
import com.mmxlabs.optimiser.common.fitness.MatrixProviderFitnessCore;

class MatrixProviderFitnessComponentTest {

	@Test
	public void testMatrixProviderFitnessComponent() {

		String name = "name";
		String key = "key";
		MatrixProviderFitnessCore<Object> core = new MatrixProviderFitnessCore<Object>(
				name, key);
		MatrixProviderFitnessComponent<Object> component = new MatrixProviderFitnessComponent<Object>(
				name, core);

		Assert.assertSame(name, component.getName());
		Assert.assertSame(core, component.getFitnessCore());
	}

	@Test
	public void testGetFitness() {
		fail("Not yet implemented");
		// TODO: Ideally jmock would be used to ensure we call the right method on the core, but it cannot instantiate our class
	}
}
