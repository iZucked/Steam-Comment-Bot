/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;

public class CachingConsumptionRateCalculatorTest {

	@Test
	public void testCachingConsumptionRateCalculator() {

		final IConsumptionRateCalculator calc = Mockito.mock(IConsumptionRateCalculator.class);

		final CachingConsumptionRateCalculator cachingCalc = new CachingConsumptionRateCalculator(calc);

		final int speed = 5;
		Mockito.when(calc.getRate(speed)).thenReturn(100L);

		Assertions.assertEquals(100L, cachingCalc.getRate(speed));
		Assertions.assertEquals(100L, cachingCalc.getRate(speed));

		final int speed2 = 7;
		Mockito.when(calc.getRate(speed2)).thenReturn(200L);
		Assertions.assertEquals(200L, cachingCalc.getRate(speed2));
		Assertions.assertEquals(200L, cachingCalc.getRate(speed2));

		Assertions.assertEquals(100L, cachingCalc.getRate(speed));
	}
}
