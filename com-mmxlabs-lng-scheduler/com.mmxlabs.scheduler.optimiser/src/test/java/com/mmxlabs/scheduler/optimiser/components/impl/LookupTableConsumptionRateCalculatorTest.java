/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;

@RunWith(JMock.class)
public class LookupTableConsumptionRateCalculatorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testLookupTableConsumptionRateCalculator() {
		final IConsumptionRateCalculator calc = context.mock(IConsumptionRateCalculator.class);

		context.setDefaultResultForType(long.class, 100l);

		context.checking(new Expectations() {
			{
				one(calc).getRate(5);
				one(calc).getRate(6);
				one(calc).getRate(7);
				one(calc).getRate(8);
				one(calc).getRate(9);
				one(calc).getRate(10);
			}
		});

		final LookupTableConsumptionRateCalculator lookupCalc = new LookupTableConsumptionRateCalculator(5, 10, calc);

		Assert.assertEquals(100l, lookupCalc.getRate(5));
		Assert.assertEquals(100l, lookupCalc.getRate(6));
		Assert.assertEquals(100l, lookupCalc.getRate(7));
		Assert.assertEquals(100l, lookupCalc.getRate(8));
		Assert.assertEquals(100l, lookupCalc.getRate(9));
		Assert.assertEquals(100l, lookupCalc.getRate(10));

		context.assertIsSatisfied();

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testLookupTableConsumptionRateCalculator2() {
		final IConsumptionRateCalculator calc = context.mock(IConsumptionRateCalculator.class);

		context.setDefaultResultForType(long.class, 100l);

		context.checking(new Expectations() {
			{
				one(calc).getRate(5);
				one(calc).getRate(6);
				one(calc).getRate(7);
				one(calc).getRate(8);
				one(calc).getRate(9);
				one(calc).getRate(10);
			}
		});

		final LookupTableConsumptionRateCalculator lookupCalc = new LookupTableConsumptionRateCalculator(5, 10, calc);

		context.assertIsSatisfied();
		lookupCalc.getRate(4);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testLookupTableConsumptionRateCalculator3() {
		final IConsumptionRateCalculator calc = context.mock(IConsumptionRateCalculator.class);

		context.setDefaultResultForType(long.class, 100l);

		context.checking(new Expectations() {
			{
				one(calc).getRate(5);
				one(calc).getRate(6);
				one(calc).getRate(7);
				one(calc).getRate(8);
				one(calc).getRate(9);
				one(calc).getRate(10);
			}
		});

		final LookupTableConsumptionRateCalculator lookupCalc = new LookupTableConsumptionRateCalculator(5, 10, calc);

		context.assertIsSatisfied();
		lookupCalc.getRate(11);

	}
}
