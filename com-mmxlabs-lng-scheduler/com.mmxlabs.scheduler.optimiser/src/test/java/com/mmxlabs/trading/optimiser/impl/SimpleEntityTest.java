package com.mmxlabs.trading.optimiser.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.curves.ICurve;

@RunWith(JMock.class)
public class SimpleEntityTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetDownstreamTransferPrice() {

		final SimpleEntity entity = new SimpleEntity("name", 500, null, 2000);
		// 1234 - (scaled)500 * 2000
		Assert.assertEquals(1234 - 10000, entity.getDownstreamTransferPrice(1234, 5000));
	}

	@Test
	public void testGetUpstreamTransferPrice() {

		final SimpleEntity entity = new SimpleEntity("name", 500, null, 2000);
		// 1234 + (scaled)500 * 2000
		Assert.assertEquals(1234 + 10000, entity.getUpstreamTransferPrice(1234, 5000));
	}

	@Test
	public void testGetTaxedProfit() {
		final ICurve curve = context.mock(ICurve.class);

		final SimpleEntity entity = new SimpleEntity("name", 500, curve, 2000);

		final int time = 12345;

		context.checking(new Expectations() {
			{
				one(curve).getValueAtPoint(time);
				// 50 %
				will(returnValue(500.0));
			}
		});

		// 50% * 50% * 10000
		Assert.assertEquals(2500, entity.getTaxedProfit(10000, time));

		context.assertIsSatisfied();
	}

}
