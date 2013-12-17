/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

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
	
	@Test
	public void testCalculateLoadUnitPrice() {
		// create a PriceExpressionContract with a mocked ICurve object
		final ICurve curve = mock(ICurve.class);
		PriceExpressionContract contract = new PriceExpressionContract(curve);
		
		// create named test constants
		final int priceAtLoadTime = (int) OptimiserUnitConvertor.convertToInternalDailyCost(40);
		final int oriceAtPricingDate = (int) OptimiserUnitConvertor.convertToInternalDailyCost(70);
		final int loadTime = 120;
		final int loadPricingDate = 90;
				
		// tell the ICurve mock to return specified values at given points
		when(curve.getValueAtPoint(loadTime)).thenReturn(priceAtLoadTime);
		when(curve.getValueAtPoint(loadPricingDate)).thenReturn(oriceAtPricingDate);
		
		final ILoadSlot loadSlotWithPricingDate = mock(ILoadSlot.class);
		when(loadSlotWithPricingDate.getPricingDate()).thenReturn(loadPricingDate);
		
		final ILoadSlot loadSlotNoPricingDate = mock(ILoadSlot.class);
		when(loadSlotNoPricingDate.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		
		final IDischargeSlot dischargeSlot = mock(IDischargeSlot.class);

		final int dischargeTime = 170;
		final int dischargePricePerMMBTu = 40;		
		final long dischargeVolumeInM3 = 100;
		final long loadVolumeInM3 = 200;
		final IVessel vessel = mock(IVessel.class);
		final VoyagePlan plan = new VoyagePlan();
		final IDetailTree annotations = mock(IDetailTree.class);
		
		final int loadPriceWithPricingDate = contract.calculateFOBPricePerMMBTu(loadSlotWithPricingDate, dischargeSlot, loadTime, dischargeTime, dischargePricePerMMBTu,
				loadVolumeInM3, dischargeVolumeInM3, vessel, plan, annotations);

		final int loadPriceNoPricingDate = contract.calculateFOBPricePerMMBTu(loadSlotNoPricingDate, dischargeSlot, loadTime, dischargeTime, dischargePricePerMMBTu,
				loadVolumeInM3, dischargeVolumeInM3, vessel, plan, annotations);

		verify(curve).getValueAtPoint(loadPricingDate);
		verify(curve).getValueAtPoint(loadTime);
		verifyNoMoreInteractions(curve);
		
		verify(loadSlotWithPricingDate).getPricingDate();
		verifyNoMoreInteractions(loadSlotWithPricingDate);
		
		verify(loadSlotNoPricingDate).getPricingDate();
		verifyNoMoreInteractions(loadSlotNoPricingDate);
		
		// check that the returned results are correct
		Assert.assertEquals(priceAtLoadTime, loadPriceNoPricingDate);
		Assert.assertEquals(oriceAtPricingDate, loadPriceWithPricingDate);
		
		
	}
	
	@Test
	public void testCalculateDischargeUnitPrice() {
		// create a PriceExpressionContract with a mocked ICurve object
		final ICurve curve = mock(ICurve.class);
		PriceExpressionContract contract = new PriceExpressionContract(curve);
		
		// create named test constants
		final int priceAtDischargeTime = (int) OptimiserUnitConvertor.convertToInternalDailyCost(40);
		final int priceAtPricingDate = (int) OptimiserUnitConvertor.convertToInternalDailyCost(70);
		final int dischargeTime = 120;
		final int pricingDate = 90;
				
		// tell the ICurve mock to return specified values at given points
		when(curve.getValueAtPoint(dischargeTime)).thenReturn(priceAtDischargeTime);
		when(curve.getValueAtPoint(pricingDate)).thenReturn(priceAtPricingDate);
				
		final IDischargeSlot dischargeSlotWithPricingDate = mock(IDischargeSlot.class);
		when(dischargeSlotWithPricingDate.getPricingDate()).thenReturn(pricingDate);
		
		final IDischargeSlot dischargeSlotNoPricingDate = mock(IDischargeSlot.class);
		when(dischargeSlotNoPricingDate.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		
		final int salesPriceWithPricingDate = contract.calculateSalesUnitPrice(dischargeSlotWithPricingDate, dischargeTime);

		final int salesPriceNoPricingDate = contract.calculateSalesUnitPrice(dischargeSlotNoPricingDate, dischargeTime);

		verify(curve).getValueAtPoint(pricingDate);
		verify(curve).getValueAtPoint(dischargeTime);
		verifyNoMoreInteractions(curve);
		
		// check that the returned results are correct
		Assert.assertEquals(priceAtDischargeTime, salesPriceNoPricingDate);
		Assert.assertEquals(priceAtPricingDate, salesPriceWithPricingDate);
		
		
	}
}
