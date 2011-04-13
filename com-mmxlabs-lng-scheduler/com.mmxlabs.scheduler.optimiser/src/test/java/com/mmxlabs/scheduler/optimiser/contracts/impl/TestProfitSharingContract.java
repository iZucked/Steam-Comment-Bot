/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;

/**
 * @author Tom Hinton
 * 
 */
public class TestProfitSharingContract {
	@Test
	public void testProfitSharingPrice() {
		final ICurve market = new ConstantValueCurve(1234);
		final ICurve refMarket = new ConstantValueCurve(4321);

		final ProfitSharingContract psc = new ProfitSharingContract(market,
				refMarket, 100, 200, 300);
		
		//market purchase price - alpha - beta * (actual sales price) - gamma * (actual
		//		 * sales price - reference sales price)
		
		final Random random = new Random();
		for (int i = 0; i<100; i++) {
			final int p = random.nextInt();
			final int price = psc.calculateLoadUnitPrice(0, 0, 0, p, 0, null, null, null);
			Assert.assertEquals(
					1.234 - 0.1 - 0.2 * (p/1000.0) - 0.3 * (p/ 1000.0 - 3.421),
					price / 1000.0, 1);
		}
	}
}
