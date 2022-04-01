/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GeometricThreholderTest {
	@Test
	public void testAcceptanceCases() {
		// can't mock classes, sadly. Perhaps we should use a random interface instead.
		final double p = 0.76;
		@SuppressWarnings("serial")
		final Random mockRandom = new Random() {
			@Override
			public double nextDouble() {
				return p;
			}
		};
		final double mlnHalf = -Math.log(p);
		final double initialTemperature = 100;
		final double alpha = 0.95;
		final GeometricThresholder t = new GeometricThresholder(mockRandom, 2, initialTemperature, alpha);
		t.init();

		Assertions.assertEquals(initialTemperature, t.getTemperature(), 0.0001);

		Assertions.assertFalse(t.accept((long) (mlnHalf * initialTemperature) + 1));
		t.step();
		Assertions.assertTrue(t.accept((long) (mlnHalf * initialTemperature) - 1));
		t.step();
		Assertions.assertEquals(initialTemperature * alpha, t.getTemperature(), 0.0001);
		t.step();
		t.step();
		Assertions.assertEquals(initialTemperature * alpha * alpha, t.getTemperature(), 0.0001);
		Assertions.assertTrue(t.accept((long) (mlnHalf * initialTemperature * alpha * alpha) - 1));
		Assertions.assertFalse(t.accept((long) (mlnHalf * initialTemperature * alpha * alpha) + 1));
	}
}
