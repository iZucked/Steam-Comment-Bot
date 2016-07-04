/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.YearMonth;

import org.eclipse.emf.common.util.BasicEList;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures.CoEff;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.ui.properties.factory.DetailPropertyFactoryUtil;

/**
 * JUnit tests for the exposures calculations.
 * 
 * @author Simon McGregor
 * 
 */
public class ExposuresTest {

	private CommodityIndex makeIndex(final String name, final String expression) {

		final DerivedIndex<Double> data = PricingFactory.eINSTANCE.createDerivedIndex();

		data.setExpression(expression);

		final CommodityIndex index = PricingFactory.eINSTANCE.createCommodityIndex();
		index.setName(name);
		index.setData(data);

		return index;
	}

	private CommodityIndex makeIndex(final String name, final YearMonth date, final double value) {

		final DataIndex<Double> data = PricingFactory.eINSTANCE.createDataIndex();

		final IndexPoint<Double> pt = PricingFactory.eINSTANCE.createIndexPoint();
		pt.setDate(date);
		pt.setValue(value);

		data.getPoints().add(pt);

		final CommodityIndex index = PricingFactory.eINSTANCE.createCommodityIndex();
		index.setName(name);
		index.setData(data);

		return index;
	}

	public void testPriceExpressionExposureCoefficient(final String expression, final String indexName, final double expected, final CommodityIndex... extraIndicies) {
		@SuppressWarnings("rawtypes")
		final CommodityIndex index = mock(CommodityIndex.class);
		when(index.getName()).thenReturn(indexName);

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		for (final CommodityIndex idx : extraIndicies) {
			pricingModel.getCommodityIndices().add(idx);
		}

		final Exposures.CoEff actual = Exposures.getExposureCoefficient(expression, index, pricingModel, YearMonth.of(2010, 4));
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected, actual.getCoeff(), 0.000000001);
	}

	public void testSlotExposureCoefficient(final String slotPriceExpression, final LNGPriceCalculatorParameters priceInfo, final CommodityIndex index, final double expected) {
		final Slot slot = mock(Slot.class);
		final Contract contract = mock(Contract.class);
		when(slot.getContract()).thenReturn(contract);
		when(contract.getPriceInfo()).thenReturn(priceInfo);

		if (slotPriceExpression != null) {
			when(slot.isSetPriceExpression()).thenReturn(true);
			when(slot.getPriceExpression()).thenReturn(slotPriceExpression);
		} else {
			when(slot.isSetPriceExpression()).thenReturn(false);
		}

		final PricingModel pricingModel = mock(PricingModel.class);
		when(pricingModel.getCommodityIndices()).thenReturn(new BasicEList<>());

		final CoEff actual = Exposures.getExposureCoefficient(slot, index, pricingModel, YearMonth.of(2010, 4));
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected, actual.getCoeff(), 0.000000001);
	}

	@Test
	public void testPriceExpressionExposureCoefficients() {
		testPriceExpressionExposureCoefficient("HH", "HH", 1);
		testPriceExpressionExposureCoefficient("A + 1", "HH", 0);
		testPriceExpressionExposureCoefficient("HH * 3 - 2", "HH", 3);
		testPriceExpressionExposureCoefficient("2.5 * HH + 2 * A", "HH", 2.5);
		testPriceExpressionExposureCoefficient("A - HH * 4", "HH", -4);
		testPriceExpressionExposureCoefficient("10%HH", "HH", 0.1);
		testPriceExpressionExposureCoefficient("5*HH+6*HH", "HH", 11);
		testPriceExpressionExposureCoefficient("5*HH+6*HH", "HH", 11);
		testPriceExpressionExposureCoefficient("(5+6)*HH", "HH", 11);
		testPriceExpressionExposureCoefficient("10%100*HH", "HH", 10);
		testPriceExpressionExposureCoefficient("A*HH", "HH", 10, makeIndex("A", "10"));
		testPriceExpressionExposureCoefficient("A*HH", "HH", 10, makeIndex("A", YearMonth.of(2000, 1), 10));
		testPriceExpressionExposureCoefficient("A*HH", "HH", 10, makeIndex("A", "B/2"), makeIndex("B", "20"));
	}

	@Test
	public void testSlotExposureCoefficients() {
		final String indexName = "X";
		final String redHerringName = "Y";
		final double pecCoefficient = 4.2;
		final double slotCoefficient = 2;
		final double ipcCoefficient = 1.8;
		final String contractPriceExpression = String.format("%f * %s + 2 * %s - 2", pecCoefficient, indexName, redHerringName);
		final String slotPriceExpression = String.format("%f * %s + 2 * %s - 2", slotCoefficient, indexName, redHerringName);

		// set up mock indices
		final CommodityIndex index = mock(CommodityIndex.class);
		when(index.getName()).thenReturn(indexName);
		final CommodityIndex otherIndex = mock(CommodityIndex.class);
		when(otherIndex.getName()).thenReturn(redHerringName);

		// set up mock contracts
		final ExpressionPriceParameters priceExpressionContract = mock(ExpressionPriceParameters.class);
		when(priceExpressionContract.getPriceExpression()).thenReturn(contractPriceExpression);

		// a slot with a price expression contract uses the price expression
		testSlotExposureCoefficient(null, priceExpressionContract, index, pecCoefficient);

		// a slot with a price expression overrides any contract
		testSlotExposureCoefficient(slotPriceExpression, priceExpressionContract, index, slotCoefficient);
	}

}
