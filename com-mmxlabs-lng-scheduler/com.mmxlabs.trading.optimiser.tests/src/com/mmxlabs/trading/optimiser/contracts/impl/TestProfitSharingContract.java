/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * @author Tom Hinton
 * 
 */
public class TestProfitSharingContract {

	@Test
	public void testProfitSharingPrice() {
		final ICurve market = new ConstantValueCurve(OptimiserUnitConvertor.convertToInternalPrice(1.234));
		final ICurve refMarket = new ConstantValueCurve(OptimiserUnitConvertor.convertToInternalPrice(4.321));

		final Set<IPort> baseMarketPorts = new HashSet<IPort>();

		final ProfitSharingContract psc = new ProfitSharingContract(market, refMarket, OptimiserUnitConvertor.convertToInternalConversionFactor(.1),
				OptimiserUnitConvertor.convertToInternalConversionFactor(.3), baseMarketPorts, OptimiserUnitConvertor.convertToInternalConversionFactor(1));

		// market purchase price - alpha - beta * (actual sales price) - gamma * (actual
		// * sales price - reference sales price)
		final IDischargeSlot option = mock(IDischargeSlot.class);
		final IPort portA = mock(IPort.class, "portA");
		final IPort portB = mock(IPort.class, "portB");

		when(option.getPort()).thenReturn(portB);

		baseMarketPorts.add(portA);

		final Random random = new Random();
		for (int i = 0; i < 100; i++) {
			final int p = random.nextInt(10000);
			final int price = psc.calculateLoadUnitPrice(null, option, 0, 0, p, 0, null, null, null);
			double asp = p / Calculator.HighScaleFactor;
			Assert.assertEquals(1.234 - 0.1 - asp - (0.3 * (asp - 4.321)), price / Calculator.HighScaleFactor, 1);
		}
	}

	@Test
	public void testProfitSharingPrice2() {
		final ICurve market = new ConstantValueCurve(OptimiserUnitConvertor.convertToInternalPrice(1.234));
		final ICurve refMarket = new ConstantValueCurve(OptimiserUnitConvertor.convertToInternalPrice(4.321));

		final Set<IPort> baseMarketPorts = new HashSet<IPort>();

		final ProfitSharingContract psc = new ProfitSharingContract(market, refMarket, OptimiserUnitConvertor.convertToInternalConversionFactor(.1),
				OptimiserUnitConvertor.convertToInternalConversionFactor(.3), baseMarketPorts, OptimiserUnitConvertor.convertToInternalConversionFactor(1));

		// market purchase price - alpha - beta * (actual sales price) - gamma * (actual
		// * sales price - reference sales price)
		final IDischargeSlot option = mock(IDischargeSlot.class);
		final IPort portA = mock(IPort.class, "portA");
		final IPort portB = mock(IPort.class, "portB");

		when(option.getPort()).thenReturn(portA);

		baseMarketPorts.add(portA);

		final Random random = new Random();
		for (int i = 0; i < 100; i++) {
			final int p = random.nextInt();
			final int price = psc.calculateLoadUnitPrice(null, option, 0, 0, p, 0, null, null, null);
			Assert.assertEquals(1.234 - 0.1 - ((p / Calculator.HighScaleFactor)), price / Calculator.HighScaleFactor, 1);
		}
	}
}
