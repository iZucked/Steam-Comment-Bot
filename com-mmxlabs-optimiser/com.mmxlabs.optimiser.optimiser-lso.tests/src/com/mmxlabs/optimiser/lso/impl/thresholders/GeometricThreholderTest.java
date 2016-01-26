/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

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

		Assert.assertEquals(initialTemperature, t.getTemperature(), 0.0);

		Assert.assertFalse(t.accept((long) (mlnHalf * initialTemperature) + 1));
		t.step();
		Assert.assertTrue(t.accept((long) (mlnHalf * initialTemperature) - 1));
		t.step();
		Assert.assertEquals(initialTemperature * alpha, t.getTemperature(), 0.0);
		t.step();
		t.step();
		Assert.assertEquals(initialTemperature * alpha * alpha, t.getTemperature(), 0.0);
		Assert.assertTrue(t.accept((long) (mlnHalf * initialTemperature * alpha * alpha) - 1));
		Assert.assertFalse(t.accept((long) (mlnHalf * initialTemperature * alpha * alpha) + 1));
	}
}
