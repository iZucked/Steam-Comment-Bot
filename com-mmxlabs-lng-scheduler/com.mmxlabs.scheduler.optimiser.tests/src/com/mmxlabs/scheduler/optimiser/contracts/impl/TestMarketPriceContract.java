/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;

/**
 * @author Tom Hinton
 * 
 */
public class TestMarketPriceContract {
	@Test
	public void testComputeMarketPrice() {
		final Random random = new Random();
		final StepwiseIntegerCurve market = new StepwiseIntegerCurve();
		market.setDefaultValue(random.nextInt());
		for (int i = 0; i < 1000; i++) {
			market.setValueAfter(random.nextInt(), random.nextInt());
		}
		final MarketPriceContract mpc = new MarketPriceContract(market, 0, OptimiserUnitConvertor.convertToInternalConversionFactor(1));
		for (int i = 0; i < 1000; i++) {
			final int date = random.nextInt();
			Assert.assertEquals(mpc.calculateLoadUnitPrice(null, null, date, date, 0, 0, 0, null, null, null), market.getValueAtPoint(date));
		}
	}
}
