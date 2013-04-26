/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;

public class CachingConsumptionRateCalculatorTest {

	@Test
	public void testCachingConsumptionRateCalculator() {

		final IConsumptionRateCalculator calc = Mockito.mock(IConsumptionRateCalculator.class);

		final CachingConsumptionRateCalculator cachingCalc = new CachingConsumptionRateCalculator(calc);

		final int speed = 5;
		Mockito.when(calc.getRate(speed)).thenReturn(100l);

		Assert.assertEquals(100l, cachingCalc.getRate(speed));
		Assert.assertEquals(100l, cachingCalc.getRate(speed));

		final int speed2 = 7;
		Mockito.when(calc.getRate(speed2)).thenReturn(200l);
		Assert.assertEquals(200l, cachingCalc.getRate(speed2));
		Assert.assertEquals(200l, cachingCalc.getRate(speed2));

		Assert.assertEquals(100l, cachingCalc.getRate(speed));
	}
}
