/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalibratingGeometricThresholderTest {
	@Test
	public void testCalibration() {
		// check that the calibration is OK
		final Random random = new Random();
		final CalibratingGeometricThresholder cgt = new CalibratingGeometricThresholder(random, 20, 0.75, 0.95);
		cgt.init();

		final long base = 2200;

		for (int i = 0; i < 19; i++) {
			cgt.accept(i + base);
			Assertions.assertFalse(cgt.isCalibrated(), "Calibrated at step " + i);
			cgt.step();
		}
		cgt.accept(119);
		cgt.step();
		Assertions.assertTrue(cgt.isCalibrated(), "Is calibrated after 20 steps");

		// test the calibrated values

		final double roughTemperature = (-(base + 10)) / Math.log(0.75);

		// ten percent error because it's not a massively accurate solution procedure.
		Assertions.assertEquals(roughTemperature, cgt.getCalibratedTemperature(), roughTemperature / 10, "Calibrated temperature should be around " + (int) roughTemperature);
		// test the acceptance rate

		int accepts = 0;
		final int tests = 1000000;
		for (int i = 0; i < tests; i++) {
			final long delta = base + (i % 20);
			if (cgt.accept(delta)) {
				accepts++;
			}
		}

		Assertions.assertEquals(0.75, accepts / (double) tests, 0.15, "Acceptance rate should be around 75%");
	}
}
