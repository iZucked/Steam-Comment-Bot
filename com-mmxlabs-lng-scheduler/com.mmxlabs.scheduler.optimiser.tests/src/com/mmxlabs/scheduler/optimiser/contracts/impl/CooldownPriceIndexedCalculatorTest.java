package com.mmxlabs.scheduler.optimiser.contracts.impl;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public class CooldownPriceIndexedCalculatorTest {
	
	@Test
	public void testCalculatorPriceIndex() {
		ICurve curve = Mockito.mock(ICurve.class);
		IVesselClass mockVC = Mockito.mock(IVesselClass.class);
		IPort mockPort = Mockito.mock(IPort.class); 
		CooldownPriceIndexedCalculator lsc = new CooldownPriceIndexedCalculator(curve);
		
		int priceA = OptimiserUnitConvertor.convertToInternalConversionFactor(10);
		int priceB = OptimiserUnitConvertor.convertToInternalConversionFactor(20);
		int timeA = 100;
		int timeB = 200;
		int cvA = OptimiserUnitConvertor.convertToInternalConversionFactor(20);
		int cvB = OptimiserUnitConvertor.convertToInternalConversionFactor(22);
		
		Mockito.when(curve.getValueAtPoint(timeA)).thenReturn(priceA);
		Mockito.when(curve.getValueAtPoint(timeB)).thenReturn(priceB);
		Mockito.when(mockVC.getCooldownVolume()).thenReturn(OptimiserUnitConvertor.convertToInternalVolume(1000));
		
		long expectedA = OptimiserUnitConvertor.convertToInternalFixedCost(200000);
		long expectedB = OptimiserUnitConvertor.convertToInternalFixedCost(440000);
		long testCalculationA = lsc.calculateCooldownCost(mockVC, mockPort, cvA, timeA);
		long testCalculationB = lsc.calculateCooldownCost(mockVC, mockPort, cvB, timeB);
		
		
		Assert.assertEquals(String.format("CooldownPriceIndexedCalculator returns %d but should be %d", testCalculationA, expectedA), testCalculationA, expectedA);
		Assert.assertEquals(String.format("CooldownPriceIndexedCalculator returns %d but should be %d", testCalculationB, expectedB), testCalculationB, expectedB);

	}

}
