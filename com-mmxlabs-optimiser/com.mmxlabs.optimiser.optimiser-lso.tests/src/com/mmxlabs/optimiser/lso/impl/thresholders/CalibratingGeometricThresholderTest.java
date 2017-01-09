/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class CalibratingGeometricThresholderTest {
	@Test
	public void testCalibration() {
		// check that the calibration is OK
		final Random random = new Random();
		final CalibratingGeometricThresholder cgt = new CalibratingGeometricThresholder(random, 20, 0.75, 0.95);
		cgt.init();

		final int base = 2200;

		for (int i = 0; i < 19; i++) {
			cgt.accept(i + base);
			Assert.assertFalse("Calibrated at step " + i, cgt.isCalibrated());
			cgt.step();
		}
		cgt.accept(119);
		cgt.step();
		Assert.assertTrue("Is calibrated after 20 steps", cgt.isCalibrated());

		// test the calibrated values

		final double roughTemperature = (-(base + 10)) / Math.log(0.75);

		// ten percent error because it's not a massively accurate solution procedure.
		Assert.assertEquals("Calibrated temperature should be around " + (int) roughTemperature, roughTemperature, cgt.getCalibratedTemperature(), roughTemperature / 10);
		// test the acceptance rate

		int accepts = 0;
		final int tests = 1000000;
		for (int i = 0; i < tests; i++) {
			final long delta = base + (i % 20);
			if (cgt.accept(delta)) {
				accepts++;
			}
		}

		Assert.assertEquals("Acceptance rate should be around 75%", 0.75, accepts / (double) tests, 0.15);
	}
}
