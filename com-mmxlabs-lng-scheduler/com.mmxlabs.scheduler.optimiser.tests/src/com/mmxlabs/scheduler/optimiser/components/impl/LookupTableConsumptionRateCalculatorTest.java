/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;

public class LookupTableConsumptionRateCalculatorTest {

	@Test
	public void testLookupTableConsumptionRateCalculator() {
		final IConsumptionRateCalculator calc = Mockito.mock(IConsumptionRateCalculator.class);

		Mockito.when(calc.getRate(ArgumentMatchers.anyInt())).thenReturn(100L);

		final LookupTableConsumptionRateCalculator lookupCalc = new LookupTableConsumptionRateCalculator(5, 10, calc);

		Assertions.assertEquals(100L, lookupCalc.getRate(5));
		Assertions.assertEquals(100L, lookupCalc.getRate(6));
		Assertions.assertEquals(100L, lookupCalc.getRate(7));
		Assertions.assertEquals(100L, lookupCalc.getRate(8));
		Assertions.assertEquals(100L, lookupCalc.getRate(9));
		Assertions.assertEquals(100L, lookupCalc.getRate(10));

	}

	@Test
	public void testLookupTableConsumptionRateCalculator2() {
		final IConsumptionRateCalculator calc = Mockito.mock(IConsumptionRateCalculator.class);

		Mockito.when(calc.getRate(ArgumentMatchers.anyInt())).thenReturn(100L);

		final LookupTableConsumptionRateCalculator lookupCalc = new LookupTableConsumptionRateCalculator(5, 10, calc);

		Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> lookupCalc.getRate(4));

	}

	@Test
	public void testLookupTableConsumptionRateCalculator3() {
		final IConsumptionRateCalculator calc = Mockito.mock(IConsumptionRateCalculator.class);
		Mockito.when(calc.getRate(ArgumentMatchers.anyInt())).thenReturn(100L);

		final LookupTableConsumptionRateCalculator lookupCalc = new LookupTableConsumptionRateCalculator(5, 10, calc);

		Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> lookupCalc.getRate(11));
	}
}
