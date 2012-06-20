/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * @author Tom Hinton
 * 
 */
@RunWith(JMock.class)
public class TestProfitSharingContract {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testProfitSharingPrice() {
		final ICurve market = new ConstantValueCurve(1234);
		final ICurve refMarket = new ConstantValueCurve(4321);

		final Set<IPort> baseMarketPorts = new HashSet<IPort>();

		final ProfitSharingContract psc = new ProfitSharingContract(market, refMarket, 100, 300, baseMarketPorts);

		// market purchase price - alpha - beta * (actual sales price) - gamma * (actual
		// * sales price - reference sales price)
		final IDischargeSlot option = context.mock(IDischargeSlot.class);
		final IPort portA = context.mock(IPort.class, "portA");
		final IPort portB = context.mock(IPort.class, "portB");

		context.checking(new Expectations() {
			{
				allowing(option).getPort();
				will(returnValue(portB));
			}
		});

		baseMarketPorts.add(portA);

		final Random random = new Random();
		for (int i = 0; i < 100; i++) {
			final int p = random.nextInt(10000);
			final int price = psc.calculateLoadUnitPrice(null, option, 0, 0, p, 0, null, null);
			double asp = p / 1000.0;
			Assert.assertEquals(1.234 - 0.1 - asp - (0.3 * (asp - 4.321)), price / 1000.0, 1);
		}
	}

	@Test
	public void testProfitSharingPrice2() {
		final ICurve market = new ConstantValueCurve(1234);
		final ICurve refMarket = new ConstantValueCurve(4321);

		final Set<IPort> baseMarketPorts = new HashSet<IPort>();

		final ProfitSharingContract psc = new ProfitSharingContract(market, refMarket, 100, 300, baseMarketPorts);

		// market purchase price - alpha - beta * (actual sales price) - gamma * (actual
		// * sales price - reference sales price)
		final IDischargeSlot option = context.mock(IDischargeSlot.class);
		final IPort portA = context.mock(IPort.class, "portA");
		final IPort portB = context.mock(IPort.class, "portB");

		context.checking(new Expectations() {
			{
				allowing(option).getPort();
				will(returnValue(portA));
			}
		});

		baseMarketPorts.add(portA);

		final Random random = new Random();
		for (int i = 0; i < 100; i++) {
			final int p = random.nextInt();
			final int price = psc.calculateLoadUnitPrice(null, option, 0, 0, p, 0, null, null);
			Assert.assertEquals(1.234 - 0.1 - ((p / 1000.0)), price / 1000.0, 1);
		}
	}
}
