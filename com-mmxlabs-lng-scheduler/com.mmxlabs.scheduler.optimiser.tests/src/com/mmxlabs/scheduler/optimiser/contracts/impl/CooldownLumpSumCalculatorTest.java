package com.mmxlabs.scheduler.optimiser.contracts.impl;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public class CooldownLumpSumCalculatorTest {
	
	@Test
	public void testCalculatorLumpSum() {
		ICurve curve = Mockito.mock(ICurve.class);
		IVesselClass mockVC = Mockito.mock(IVesselClass.class);
		IPort mockPort = Mockito.mock(IPort.class); 
		CooldownLumpSumCalculator lsc = new CooldownLumpSumCalculator(curve);
		
		int expected = 100000;
		Mockito.when(curve.getValueAtPoint(Mockito.anyInt())).thenReturn(expected);
		long actual = lsc.calculateCooldownCost(mockVC, mockPort, 10, 10);
		Assert.assertEquals(String.format("CooldownLumpSumCalculator returns %d but should be %d", actual, (long) expected), (long) expected, actual);
	}

}
