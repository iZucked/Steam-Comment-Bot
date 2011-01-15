/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.fitness;


import org.junit.Assert;
import org.junit.Test;

public class MatrixProviderFitnessComponentTest {

	@Test
	public void testMatrixProviderFitnessComponent() {

		final String name = "name";
		final String key = "key";
		final MatrixProviderFitnessCore<Object> core = new MatrixProviderFitnessCore<Object>(
				name, key);
		final MatrixProviderFitnessComponent<Object> component = new MatrixProviderFitnessComponent<Object>(
				name, core);

		Assert.assertSame(name, component.getName());
		Assert.assertSame(core, component.getFitnessCore());
	}

	//@Test
	public void testGetFitness() {
		 // TODO: Can't easily test this.
	}
}
