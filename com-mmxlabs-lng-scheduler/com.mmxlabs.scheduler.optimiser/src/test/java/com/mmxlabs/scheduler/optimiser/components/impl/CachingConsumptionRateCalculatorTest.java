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
public class CachingConsumptionRateCalculatorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testCachingConsumptionRateCalculator() {

		final IConsumptionRateCalculator calc = context.mock(IConsumptionRateCalculator.class);

		final CachingConsumptionRateCalculator cachingCalc = new CachingConsumptionRateCalculator(calc);

		context.setDefaultResultForType(long.class, 100l);

		final int speed = 5;
		context.checking(new Expectations() {
			{
				one(calc).getRate(speed);
			}
		});

		Assert.assertEquals(100l, cachingCalc.getRate(speed));

		context.checking(new Expectations() {
			{
				// Nothing!
			}
		});
		Assert.assertEquals(100l, cachingCalc.getRate(speed));

		context.setDefaultResultForType(long.class, 200l);

		final int speed2 = 7;
		context.checking(new Expectations() {
			{
				one(calc).getRate(speed2);
			}
		});

		Assert.assertEquals(200l, cachingCalc.getRate(speed2));

		context.checking(new Expectations() {
			{
				// Nothing!
			}
		});
		Assert.assertEquals(200l, cachingCalc.getRate(speed2));

		Assert.assertEquals(100l, cachingCalc.getRate(speed));

		context.assertIsSatisfied();
	}
}
