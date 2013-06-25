/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.Index;

/**
 * JUnit tests for the exposures calculations.
 * 
 * @author Simon McGregor
 * 
 */
public class ExposuresTest {
	public void testPriceExpressionExposureCoefficient(String expression, String indexName, double expected) {
		@SuppressWarnings("rawtypes")
		CommodityIndex index = mock(CommodityIndex.class);
		when(index.getName()).thenReturn(indexName);

		double actual = Exposures.getExposureCoefficient(expression, index);
		Assert.assertEquals(expected, actual, 0.000000001);
	}

	public void testSlotExposureCoefficient(String slotPriceExpression, LNGPriceCalculatorParameters priceInfo, CommodityIndex index, double expected) {
		Slot slot = mock(Slot.class);
		Contract contract = mock(Contract.class);
		when(slot.getContract()).thenReturn(contract);
		when(contract.getPriceInfo()).thenReturn(priceInfo);

		if (slotPriceExpression != null) {
			when(slot.isSetPriceExpression()).thenReturn(true);
			when(slot.getPriceExpression()).thenReturn(slotPriceExpression);
		} else {
			when(slot.isSetPriceExpression()).thenReturn(false);
		}

		double actual = Exposures.getExposureCoefficient(slot, index);
		Assert.assertEquals(expected, actual, 0.000000001);
	}

	@Test
	public void testPriceExpressionExposureCoefficients() {
		testPriceExpressionExposureCoefficient("HH", "HH", 1);
		testPriceExpressionExposureCoefficient("A + 1", "HH", 0);
		testPriceExpressionExposureCoefficient("HH * 3 - 2", "HH", 3);
		testPriceExpressionExposureCoefficient("2.5 * HH + 2 * A", "HH", 2.5);
		testPriceExpressionExposureCoefficient("A - HH * 4", "HH", -4);
	}

	@Test
	public void testSlotExposureCoefficients() {
		String indexName = "X";
		String redHerringName = "Y";
		double pecCoefficient = 4.2;
		double slotCoefficient = 2;
		double ipcCoefficient = 1.8;
		String contractPriceExpression = String.format("%f * %s + 2 * %s - 2", pecCoefficient, indexName, redHerringName);
		String slotPriceExpression = String.format("%f * %s + 2 * %s - 2", slotCoefficient, indexName, redHerringName);

		// set up mock indices
		CommodityIndex index = mock(CommodityIndex.class);
		when(index.getName()).thenReturn(indexName);
		CommodityIndex otherIndex = mock(CommodityIndex.class);
		when(otherIndex.getName()).thenReturn(redHerringName);

		// set up mock contracts
		ExpressionPriceParameters priceExpressionContract = mock(ExpressionPriceParameters.class);
		when(priceExpressionContract.getPriceExpression()).thenReturn(contractPriceExpression);

		// a slot with a price expression contract uses the price expression
		testSlotExposureCoefficient(null, priceExpressionContract, index, pecCoefficient);

		// a slot with a price expression overrides any contract
		testSlotExposureCoefficient(slotPriceExpression, priceExpressionContract, index, slotCoefficient);
	}

}
