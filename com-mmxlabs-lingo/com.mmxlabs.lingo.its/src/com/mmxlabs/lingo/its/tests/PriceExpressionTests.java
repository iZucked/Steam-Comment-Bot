/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.time.LocalDateTime;
import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class PriceExpressionTests {
	@NonNull
	final static CommodityIndex[] indicies = new CommodityIndex[] { makeBrent() };

	private static CommodityIndex makeBrent() {
		return makeIndex("Brent", "$", "mmBtu", YearMonth.of(2017, 3), /* 2017 */ 54.89, 55.47, 55.76, 56.01, 56.16, 56.26, 56.23, 56.28, 56.23, 56.17, //
				/* 2018 */ 56.2, 56.14, 56.04, 55.95, 55.86, 55.76, 55.67, 55.56, 55.46, 55.39, 55.34, 55.3 //
		);
	}

	@Test
	public void test_3_0_1() {
		Assert.assertEquals(55.373, parseExpression("DATEDAVG(Brent,3,0,1)", LocalDateTime.of(2017, 6, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(55.7467, parseExpression("DATEDAVG(Brent,3,0,1)", LocalDateTime.of(2017, 7, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2467, parseExpression("DATEDAVG(Brent,3,0,1)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2267, parseExpression("DATEDAVG(Brent,3,0,1)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);

	}

	@Test
	public void test_6_0_1() {
		Assert.assertEquals(55.7583, parseExpression("DATEDAVG(Brent,6,0,1)", LocalDateTime.of(2017, 9, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.1950, parseExpression("DATEDAVG(Brent,6,0,1)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2217, parseExpression("DATEDAVG(Brent,6,0,1)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);
	}

	@Test
	public void test_3_0_3() {
		Assert.assertEquals(55.7467, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2017, 7, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(55.7467, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2017, 8, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(55.7467, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2017, 9, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2167, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2017, 10, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2167, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2267, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);
	}

	@Test
	public void test_3_1_3() {
		Assert.assertEquals(55.3733, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2017, 7, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(55.3733, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2017, 8, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(55.3733, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2017, 9, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.1433, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2017, 10, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.1433, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2467, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);
	}

	@Test
	public void test_6_1_3() {
		Assert.assertEquals(55.7583, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2017, 10, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(55.7583, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2017, 11, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(55.7583, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.1950, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.1950, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2018, 2, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.1950, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2018, 3, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2083, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2018, 4, 1, 0, 0, 0)), 0.001);
	}

	@Test
	public void test_6_0_3() {
		Assert.assertEquals(55.9817, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2017, 10, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(55.9817, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2017, 11, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(55.9817, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2217, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2217, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2018, 2, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.2217, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2018, 3, 1, 0, 0, 0)), 0.001);
		Assert.assertEquals(56.1767, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2018, 4, 1, 0, 0, 0)), 0.001);
	}

	private double parseExpression(final String expression, LocalDateTime time) {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();

		for (final CommodityIndex idx : indicies) {
			pricingModel.getCommodityIndices().add(idx);
		}

		final SeriesParser p = PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.COMMODITY);
		final IExpression<ISeries> series = p.parse(expression);
		final Number evaluate = series.evaluate().evaluate(Hours.between(PriceIndexUtils.dateTimeZero, time));
		double unitPrice = evaluate.doubleValue();

		return unitPrice;
	}

	private static CommodityIndex makeIndex(final String name, final String currencyUnit, final String volumeUnit, final YearMonth startDate, final double... values) {

		final DataIndex<Double> data = PricingFactory.eINSTANCE.createDataIndex();

		YearMonth date = startDate;
		for (final double v : values) {
			final IndexPoint<Double> pt = PricingFactory.eINSTANCE.createIndexPoint();
			pt.setDate(date);
			pt.setValue(v);

			data.getPoints().add(pt);
			date = date.plusMonths(1);
		}

		final CommodityIndex index = PricingFactory.eINSTANCE.createCommodityIndex();
		index.setName(name);
		index.setData(data);

		index.setCurrencyUnit(currencyUnit);
		index.setVolumeUnit(volumeUnit);

		return index;
	}

}
