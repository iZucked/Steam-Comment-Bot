/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InterpolatingConsumptionRateCalculatorTest {

	@Test
	public void testInterpolatingConsumptionRateCalculator() {

		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(5, 500L);
		keypoints.put(10, 1000L);

		final InterpolatingConsumptionRateCalculator calc = new InterpolatingConsumptionRateCalculator(keypoints);

		// Test exact values
		Assertions.assertEquals(500L, calc.getRate(5));
		Assertions.assertEquals(1000L, calc.getRate(10));

		// Test interpolation
		Assertions.assertEquals(600L, calc.getRate(6));
		Assertions.assertEquals(700L, calc.getRate(7));
		Assertions.assertEquals(800L, calc.getRate(8));
		Assertions.assertEquals(900L, calc.getRate(9));
	}

	@Test
	public void testInterpolatingConsumptionRateCalculator2() {

		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(5, 500L);
		keypoints.put(10, 1000L);

		final InterpolatingConsumptionRateCalculator calc = new InterpolatingConsumptionRateCalculator(keypoints);

		// Lower bound check
		Assertions.assertThrows(RuntimeException.class, () -> calc.getRate(4));
	}

	@Test
	public void testInterpolatingConsumptionRateCalculator3() {

		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(5, 500L);
		keypoints.put(10, 1000L);

		final InterpolatingConsumptionRateCalculator calc = new InterpolatingConsumptionRateCalculator(keypoints);

		// Upper bound check
		Assertions.assertThrows(RuntimeException.class, () -> calc.getRate(11));
	}
}
