/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;

public class LookupTableConsumptionRateCalculatorTest {

	@Test
	public void testLookupTableConsumptionRateCalculator() {
		final IConsumptionRateCalculator calc = Mockito.mock(IConsumptionRateCalculator.class);

		Mockito.when(calc.getRate(Matchers.anyInt())).thenReturn(100L);

		final LookupTableConsumptionRateCalculator lookupCalc = new LookupTableConsumptionRateCalculator(5, 10, calc);

		Assert.assertEquals(100L, lookupCalc.getRate(5));
		Assert.assertEquals(100L, lookupCalc.getRate(6));
		Assert.assertEquals(100L, lookupCalc.getRate(7));
		Assert.assertEquals(100L, lookupCalc.getRate(8));
		Assert.assertEquals(100L, lookupCalc.getRate(9));
		Assert.assertEquals(100L, lookupCalc.getRate(10));

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testLookupTableConsumptionRateCalculator2() {
		final IConsumptionRateCalculator calc = Mockito.mock(IConsumptionRateCalculator.class);

		Mockito.when(calc.getRate(Matchers.anyInt())).thenReturn(100L);

		final LookupTableConsumptionRateCalculator lookupCalc = new LookupTableConsumptionRateCalculator(5, 10, calc);

		lookupCalc.getRate(4);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testLookupTableConsumptionRateCalculator3() {
		final IConsumptionRateCalculator calc = Mockito.mock(IConsumptionRateCalculator.class);
		Mockito.when(calc.getRate(Matchers.anyInt())).thenReturn(100L);

		final LookupTableConsumptionRateCalculator lookupCalc = new LookupTableConsumptionRateCalculator(5, 10, calc);

		lookupCalc.getRate(11);

	}
}
