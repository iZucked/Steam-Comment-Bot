/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;

public class CooldownPriceIndexedCalculatorTest {
	
	@Test
	public void testUTC(){
		testCalculatorPriceIndex("UTC");
	}
	
	@Test
	public void testEtc12(){
		testCalculatorPriceIndex("Etc/GMT+12");
	}
	
	public void testCalculatorPriceIndex(String timeZone) {
		ICurve curve = Mockito.mock(ICurve.class);
		IVesselClass mockVC = Mockito.mock(IVesselClass.class);
		IPort mockPort = Mockito.mock(IPort.class);
		// create a cooldown calculator and avoid using injection
		// TODO Use injection
		CooldownPriceIndexedCalculator lsc = createCooldownPriceIndexedCalculator(curve);
		
		// create prices for different times (to test UTC)
		int priceA = OptimiserUnitConvertor.convertToInternalConversionFactor(10);
		int priceAMinus = OptimiserUnitConvertor.convertToInternalConversionFactor(5);
		int priceB = OptimiserUnitConvertor.convertToInternalConversionFactor(20);
		int priceBMinus = OptimiserUnitConvertor.convertToInternalConversionFactor(10);
		int timeA = 100;
		int timeB = 200;
		
		// create different cv values (to test correct calculation)
		int cvA = OptimiserUnitConvertor.convertToInternalConversionFactor(20);
		int cvB = OptimiserUnitConvertor.convertToInternalConversionFactor(22);
		
		// mock some return values
		Mockito.when(curve.getValueAtPoint(timeA)).thenReturn(priceA);
		Mockito.when(curve.getValueAtPoint(timeA-12)).thenReturn(priceAMinus);
		Mockito.when(curve.getValueAtPoint(timeB)).thenReturn(priceB);
		Mockito.when(curve.getValueAtPoint(timeB-12)).thenReturn(priceBMinus);
		Mockito.when(mockVC.getCooldownVolume()).thenReturn(OptimiserUnitConvertor.convertToInternalVolume(1000));
		
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
		
		long testCalculationA = lsc.calculateCooldownCost(mockVC, mockPort, cvA, timeA);
		long testCalculationB = lsc.calculateCooldownCost(mockVC, mockPort, cvB, timeB);
		
		
		Assert.assertEquals(String.format("CooldownPriceIndexedCalculator returns %d but should be %d.", testCalculationA, expectedA), testCalculationA, expectedA);
		Assert.assertEquals(String.format("CooldownPriceIndexedCalculator returns %d but should be %d.", testCalculationB, expectedB), testCalculationB, expectedB);

	}

	private CooldownPriceIndexedCalculator createCooldownPriceIndexedCalculator(final ICurve curve) {
		CooldownPriceIndexedCalculator contract = new CooldownPriceIndexedCalculator(curve);

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
