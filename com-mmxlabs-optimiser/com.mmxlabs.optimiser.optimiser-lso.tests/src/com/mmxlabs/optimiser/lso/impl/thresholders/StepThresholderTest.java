/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StepThresholderTest {

	@Test
	public void testStepThresholderIntLong() {

		final StepThresholder thresholder = new StepThresholder(100, 1000);
		Assertions.assertEquals(100, thresholder.getStepSize());
		Assertions.assertEquals(1000, thresholder.getInitialThreshold());
		Assertions.assertEquals(1000, thresholder.getCurrentThreshold());
	}

	@Test
	public void testGetSetStepSize() {
		final StepThresholder thresholder = new StepThresholder(100, 1000);
		Assertions.assertEquals(100, thresholder.getStepSize());
		thresholder.setStepSize(50);
		Assertions.assertEquals(50, thresholder.getStepSize());
	}

	@Test
	public void testGetSetInitialThreshold() {
		final StepThresholder thresholder = new StepThresholder(100, 1000);
		Assertions.assertEquals(1000, thresholder.getInitialThreshold());
		thresholder.setInitialThreshold(50);
		Assertions.assertEquals(50, thresholder.getInitialThreshold());

	}

	@Test
	public void testAccept() {
		final StepThresholder thresholder = new StepThresholder(1, 10);

		Assertions.assertTrue(thresholder.accept(-1));
		Assertions.assertTrue(thresholder.accept(9));
		Assertions.assertFalse(thresholder.accept(10));

		thresholder.step();

		Assertions.assertTrue(thresholder.accept(-1));
		Assertions.assertTrue(thresholder.accept(8));
		Assertions.assertFalse(thresholder.accept(9));

		thresholder.step();

		Assertions.assertTrue(thresholder.accept(-1));
		Assertions.assertTrue(thresholder.accept(7));
		Assertions.assertFalse(thresholder.accept(8));

	}

	@Test
	public void testInit() {
		final StepThresholder thresholder = new StepThresholder(1, 10);
		Assertions.assertEquals(1, thresholder.getStepSize());
		Assertions.assertEquals(10, thresholder.getInitialThreshold());
		Assertions.assertEquals(10, thresholder.getCurrentThreshold());

		thresholder.init();

		Assertions.assertEquals(1, thresholder.getStepSize());
		Assertions.assertEquals(10, thresholder.getInitialThreshold());
		Assertions.assertEquals(10, thresholder.getCurrentThreshold());

		thresholder.step();
		thresholder.step();

		Assertions.assertEquals(1, thresholder.getStepSize());
		Assertions.assertEquals(10, thresholder.getInitialThreshold());
		Assertions.assertEquals(8, thresholder.getCurrentThreshold());

		thresholder.init();

		Assertions.assertEquals(1, thresholder.getStepSize());
		Assertions.assertEquals(10, thresholder.getInitialThreshold());
		Assertions.assertEquals(10, thresholder.getCurrentThreshold());

	}

	@Test
	public void testStepAndCurrentThreshold() {
		final StepThresholder thresholder = new StepThresholder(1, 10);
		Assertions.assertEquals(1, thresholder.getStepSize());
		Assertions.assertEquals(10, thresholder.getInitialThreshold());
		Assertions.assertEquals(10, thresholder.getCurrentThreshold());

		thresholder.step();

		Assertions.assertEquals(1, thresholder.getStepSize());
		Assertions.assertEquals(10, thresholder.getInitialThreshold());
		Assertions.assertEquals(9, thresholder.getCurrentThreshold());

		thresholder.step();

		Assertions.assertEquals(1, thresholder.getStepSize());
		Assertions.assertEquals(10, thresholder.getInitialThreshold());
		Assertions.assertEquals(8, thresholder.getCurrentThreshold());

	}

}
