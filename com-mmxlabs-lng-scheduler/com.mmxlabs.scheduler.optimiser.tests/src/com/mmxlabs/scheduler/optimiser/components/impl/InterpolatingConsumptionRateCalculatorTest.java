/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

public class InterpolatingConsumptionRateCalculatorTest {

	@Test
	public void testInterpolatingConsumptionRateCalculator() {

		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(5, 500l);
		keypoints.put(10, 1000l);

		final InterpolatingConsumptionRateCalculator calc = new InterpolatingConsumptionRateCalculator(keypoints);

		// Test exact values
		Assert.assertEquals(500l, calc.getRate(5));
		Assert.assertEquals(1000l, calc.getRate(10));

		// Test interpolation
		Assert.assertEquals(600l, calc.getRate(6));
		Assert.assertEquals(700l, calc.getRate(7));
		Assert.assertEquals(800l, calc.getRate(8));
		Assert.assertEquals(900l, calc.getRate(9));
	}

	@Test(expected = RuntimeException.class)
	public void testInterpolatingConsumptionRateCalculator2() {

		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(5, 500l);
		keypoints.put(10, 1000l);

		final InterpolatingConsumptionRateCalculator calc = new InterpolatingConsumptionRateCalculator(keypoints);

		// Lower bound check
		calc.getRate(4);
	}

	@Test(expected = RuntimeException.class)
	public void testInterpolatingConsumptionRateCalculator3() {

		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(5, 500l);
		keypoints.put(10, 1000l);

		final InterpolatingConsumptionRateCalculator calc = new InterpolatingConsumptionRateCalculator(keypoints);

		// Upper bound check
		calc.getRate(11);
	}
}
