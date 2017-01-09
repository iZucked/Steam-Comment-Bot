/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import org.junit.Assert;
import org.junit.Test;

public class StepThresholderTest {

	@Test
	public void testStepThresholderIntLong() {

		final StepThresholder thresholder = new StepThresholder(100, 1000);
		Assert.assertEquals(100, thresholder.getStepSize());
		Assert.assertEquals(1000, thresholder.getInitialThreshold());
		Assert.assertEquals(1000, thresholder.getCurrentThreshold());
	}

	@Test
	public void testGetSetStepSize() {
		final StepThresholder thresholder = new StepThresholder(100, 1000);
		Assert.assertEquals(100, thresholder.getStepSize());
		thresholder.setStepSize(50);
		Assert.assertEquals(50, thresholder.getStepSize());
	}

	@Test
	public void testGetSetInitialThreshold() {
		final StepThresholder thresholder = new StepThresholder(100, 1000);
		Assert.assertEquals(1000, thresholder.getInitialThreshold());
		thresholder.setInitialThreshold(50);
		Assert.assertEquals(50, thresholder.getInitialThreshold());

	}

	@Test
	public void testAccept() {
		final StepThresholder thresholder = new StepThresholder(1, 10);

		Assert.assertTrue(thresholder.accept(-1));
		Assert.assertTrue(thresholder.accept(9));
		Assert.assertFalse(thresholder.accept(10));

		thresholder.step();

		Assert.assertTrue(thresholder.accept(-1));
		Assert.assertTrue(thresholder.accept(8));
		Assert.assertFalse(thresholder.accept(9));

		thresholder.step();

		Assert.assertTrue(thresholder.accept(-1));
		Assert.assertTrue(thresholder.accept(7));
		Assert.assertFalse(thresholder.accept(8));

	}

	@Test
	public void testInit() {
		final StepThresholder thresholder = new StepThresholder(1, 10);
		Assert.assertEquals(1, thresholder.getStepSize());
		Assert.assertEquals(10, thresholder.getInitialThreshold());
		Assert.assertEquals(10, thresholder.getCurrentThreshold());

		thresholder.init();

		Assert.assertEquals(1, thresholder.getStepSize());
		Assert.assertEquals(10, thresholder.getInitialThreshold());
		Assert.assertEquals(10, thresholder.getCurrentThreshold());

		thresholder.step();
		thresholder.step();

		Assert.assertEquals(1, thresholder.getStepSize());
		Assert.assertEquals(10, thresholder.getInitialThreshold());
		Assert.assertEquals(8, thresholder.getCurrentThreshold());

		thresholder.init();

		Assert.assertEquals(1, thresholder.getStepSize());
		Assert.assertEquals(10, thresholder.getInitialThreshold());
		Assert.assertEquals(10, thresholder.getCurrentThreshold());

	}

	@Test
	public void testStepAndCurrentThreshold() {
		final StepThresholder thresholder = new StepThresholder(1, 10);
		Assert.assertEquals(1, thresholder.getStepSize());
		Assert.assertEquals(10, thresholder.getInitialThreshold());
		Assert.assertEquals(10, thresholder.getCurrentThreshold());

		thresholder.step();

		Assert.assertEquals(1, thresholder.getStepSize());
		Assert.assertEquals(10, thresholder.getInitialThreshold());
		Assert.assertEquals(9, thresholder.getCurrentThreshold());

		thresholder.step();

		Assert.assertEquals(1, thresholder.getStepSize());
		Assert.assertEquals(10, thresholder.getInitialThreshold());
		Assert.assertEquals(8, thresholder.getCurrentThreshold());

	}

}
