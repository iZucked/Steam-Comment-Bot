/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;

public class CooldownLumpSumCalculatorTest {

	@Test
	public void testUTC() {
		testCalculatorLumpSum("UTC");
	}

	@Test
	public void testEtc12() {
		testCalculatorLumpSum("Etc/GMT+12");
	}

	public void testCalculatorLumpSum(String timeZone) {
		ILongCurve curve = Mockito.mock(ILongCurve.class);
		IVesselClass mockVC = Mockito.mock(IVesselClass.class);
		IPort mockPort = Mockito.mock(IPort.class);
		// create a cooldown calculator with injection
		CooldownLumpSumCalculator lsc = createCooldownLumpSumCalculator(curve);

		// create prices for different times (to test UTC)
		long priceA = OptimiserUnitConvertor.convertToInternalFixedCost(100000);
		long priceAMinus = OptimiserUnitConvertor.convertToInternalFixedCost(50000);
		long priceB = OptimiserUnitConvertor.convertToInternalFixedCost(200000);
		long priceBMinus = OptimiserUnitConvertor.convertToInternalFixedCost(100000);
		int timeA = 100;
		int timeB = 200;

		// mock some return values
		Mockito.when(curve.getValueAtPoint(timeA)).thenReturn(priceA);
		Mockito.when(curve.getValueAtPoint(timeA - 12)).thenReturn(priceAMinus);
		Mockito.when(curve.getValueAtPoint(timeB)).thenReturn(priceB);
		Mockito.when(curve.getValueAtPoint(timeB - 12)).thenReturn(priceBMinus);

		// mock passed in timezone
		Mockito.when(mockPort.getTimeZoneId()).thenReturn(timeZone);

		long expectedA, expectedB;
		if (timeZone.equals("UTC")) {
			expectedA = OptimiserUnitConvertor.convertToInternalFixedCost(100000);
			expectedB = OptimiserUnitConvertor.convertToInternalFixedCost(200000);
		} else {
			expectedA = OptimiserUnitConvertor.convertToInternalFixedCost(50000);
			expectedB = OptimiserUnitConvertor.convertToInternalFixedCost(100000);
		}

		long testCalculationA = lsc.calculateCooldownCost(mockVC, mockPort, 0, timeA);
		long testCalculationB = lsc.calculateCooldownCost(mockVC, mockPort, 0, timeB);

		Assert.assertEquals(String.format("CooldownLumpSumCalculator returns %d but should be %d.", testCalculationA, expectedA), testCalculationA, expectedA);
		Assert.assertEquals(String.format("CooldownLumpSumCalculator returns %d but should be %d.", testCalculationB, expectedB), testCalculationB, expectedB);

	}

	private CooldownLumpSumCalculator createCooldownLumpSumCalculator(final ILongCurve curve) {
		CooldownLumpSumCalculator contract = new CooldownLumpSumCalculator(curve);

		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).to(TimeZoneToUtcOffsetProvider.class);
			}
		});

		injector.injectMembers(contract);
		return contract;
	}

}
