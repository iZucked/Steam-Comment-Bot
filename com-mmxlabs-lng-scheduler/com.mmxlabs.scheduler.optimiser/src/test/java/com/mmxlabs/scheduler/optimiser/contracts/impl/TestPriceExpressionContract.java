package com.mmxlabs.scheduler.optimiser.contracts.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;

/**
 * 
 * @author Simon McGregor
 *
 * Simple JUnit test for the optimiser internal PriceExpressionContract class.
 */
public class TestPriceExpressionContract {
	/**
	 * Test for the PriceExpressionContract.calculateSimpleUnitPrice method.
	 * 
	 * Tests whether the ICurve object supplied as a constructor argument is called with appropriate arguments when 
	 * calculateSimpleUnitPrice is called.
	 * Also tests that the appropriate values are returned.
	 * 
	 */
	@Test
	public void testCalculateSimpleUnitPrice() {
		// create a PriceExpressionContract with a mocked ICurve object
		final ICurve curve = mock(ICurve.class);
		PriceExpressionContract contract = new PriceExpressionContract(curve);
		
		// create named test constants
		final int p1 = (int) OptimiserUnitConvertor.convertToInternalDailyCost(40);
		final int p2 = (int) OptimiserUnitConvertor.convertToInternalDailyCost(70);
		final int t1 = 35;
		final int t2 = 55;
		
		// tell the ICurve mock to return specified values at given points
		when(curve.getValueAtPoint(t1)).thenReturn(p1);
		when(curve.getValueAtPoint(t2)).thenReturn(p2);
		
		// calculate the unit price at times t1 and t2
		final int price1 = contract.calculateSimpleUnitPrice(t1);
		final int price2 = contract.calculateSimpleUnitPrice(t2);
		
		// make sure the mock was evaluated at the right points and nowhere else
		verify(curve).getValueAtPoint(t1);
		verify(curve).getValueAtPoint(t2);
		verifyNoMoreInteractions(curve);
		
		// check that the returned results are correct
		Assert.assertEquals(p1, price1);
		Assert.assertEquals(p2, price2);
	}
}
