/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;

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
		createInjector().injectMembers(mpc);
		for (int i = 0; i < 1000; i++) {
			final int date = random.nextInt();
			Assert.assertEquals(mpc.calculateFOBPricePerMMBTu(null, null, date, date, 0, 0, 0, null, 0, null, null), market.getValueAtPoint(date));
		}
	}

	private Injector createInjector() {
		return Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).to(TimeZoneToUtcOffsetProvider.class);

			}
		});
	}
}
