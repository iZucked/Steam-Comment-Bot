/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;

public class CooldownPriceIndexedCalculatorTest {

	@Test
	public void testUTC() {
		testCalculatorPriceIndex("UTC");
	}

	@Test
	public void testEtc12() {
		testCalculatorPriceIndex("Etc/GMT+12");
	}

	public void testCalculatorPriceIndex(final String timeZone) {
		final ICurve curve = Mockito.mock(ICurve.class);
		final IVessel mockVessel = Mockito.mock(IVessel.class);
		final IPort mockPort = Mockito.mock(IPort.class);
		// create a cooldown calculator and avoid using injection
		// TODO Use injection
		final CooldownPriceIndexedCalculator lsc = createCooldownPriceIndexedCalculator(curve);

		// create prices for different times (to test UTC)
		final int priceA = OptimiserUnitConvertor.convertToInternalConversionFactor(10);
		final int priceAMinus = OptimiserUnitConvertor.convertToInternalConversionFactor(5);
		final int priceB = OptimiserUnitConvertor.convertToInternalConversionFactor(20);
		final int priceBMinus = OptimiserUnitConvertor.convertToInternalConversionFactor(10);
		final int timeA = 100;
		final int timeB = 200;

		// create different cv values (to test correct calculation)
		final int cvA = OptimiserUnitConvertor.convertToInternalConversionFactor(20);
		final int cvB = OptimiserUnitConvertor.convertToInternalConversionFactor(22);

		// mock some return values
		Mockito.when(curve.getValueAtPoint(timeA)).thenReturn(priceA);
		Mockito.when(curve.getValueAtPoint(timeA - 12)).thenReturn(priceAMinus);
		Mockito.when(curve.getValueAtPoint(timeB)).thenReturn(priceB);
		Mockito.when(curve.getValueAtPoint(timeB - 12)).thenReturn(priceBMinus);
		Mockito.when(mockVessel.getCooldownVolume()).thenReturn(OptimiserUnitConvertor.convertToInternalVolume(1000));

		// mock passed in timezone
		Mockito.when(mockPort.getTimeZoneId()).thenReturn(timeZone);

		long expectedA, expectedB;
		if (timeZone.equals("UTC")) {
			expectedA = OptimiserUnitConvertor.convertToInternalFixedCost(200000);
			expectedB = OptimiserUnitConvertor.convertToInternalFixedCost(440000);
		} else {
			expectedA = OptimiserUnitConvertor.convertToInternalFixedCost(100000);
			expectedB = OptimiserUnitConvertor.convertToInternalFixedCost(220000);
		}

		final long testCalculationA = lsc.calculateCooldownCost(mockVessel, mockPort, cvA, timeA);
		final long testCalculationB = lsc.calculateCooldownCost(mockVessel, mockPort, cvB, timeB);

		Assertions.assertEquals(testCalculationA, expectedA, String.format("CooldownPriceIndexedCalculator returns %d but should be %d.", testCalculationA, expectedA));
		Assertions.assertEquals(testCalculationB, expectedB, String.format("CooldownPriceIndexedCalculator returns %d but should be %d.", testCalculationB, expectedB));

	}

	private CooldownPriceIndexedCalculator createCooldownPriceIndexedCalculator(final ICurve curve) {
		final CooldownPriceIndexedCalculator contract = new CooldownPriceIndexedCalculator(curve);

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).to(TimeZoneToUtcOffsetProvider.class);
			}
		});

		injector.injectMembers(contract);
		return contract;
	}

}
