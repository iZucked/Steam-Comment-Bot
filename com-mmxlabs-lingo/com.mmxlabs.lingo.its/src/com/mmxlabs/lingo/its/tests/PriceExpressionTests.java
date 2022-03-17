/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.time.LocalDateTime;
import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class PriceExpressionTests {
	@NonNull
	private static final CommodityCurve[] indicies = { //
			makeBrent(), //
			makeJKM_H1(), //
			makeJKM_H2(), //
			makeWeekly_W1(), //
			makeWeekly_W2(), //
			makeWeekly_W3(), //
			makeWeekly_W4() //
	};

	private static CommodityCurve makeBrent() {
		return makeIndex("Brent", "$", "mmBtu", YearMonth.of(2017, 3), /* 2017 */ 54.89, 55.47, 55.76, 56.01, 56.16, 56.26, 56.23, 56.28, 56.23, 56.17, //
				/* 2018 */ 56.2, 56.14, 56.04, 55.95, 55.86, 55.76, 55.67, 55.56, 55.46, 55.39, 55.34, 55.3 //
		);
	}

	private static CommodityCurve makeJKM_H1() {
		return makeIndex("JKM_H1", "$", "mmBtu", YearMonth.of(2017, 3), /* 2017 */ 54.89, 55.47, 55.76, 56.01, 56.16, 56.26, 56.23, 56.28, 56.23, 56.17, //
				/* 2018 */ 56.2, 56.14, 56.04, 55.95, 55.86, 55.76, 55.67, 55.56, 55.46, 55.39, 55.34, 55.3 //
		);
	}

	private static CommodityCurve makeJKM_H2() {
		return makeIndex("JKM_H2", "$", "mmBtu", YearMonth.of(2017, 3), /* 2017 */ 10, 20.47, 30.76, 40.01, 50.16, 60.26, 70.23, 80.28, 90.23, 100.17, //
				/* 2018 */ 110.2, 120.14, 130.04, 140.95, 150.86, 160.76, 170.67, 180.56, 190.46, 200.39, 210.34, 220.3 //
		);
	}

	private static CommodityCurve makeWeekly_W1() {
		return makeIndex("WEEK_W1", "$", "mmBtu", YearMonth.of(2017, 3), /* 2017 */ 10);
	}

	private static CommodityCurve makeWeekly_W2() {
		return makeIndex("WEEK_W2", "$", "mmBtu", YearMonth.of(2017, 3), /* 2017 */ 20);
	}

	private static CommodityCurve makeWeekly_W3() {
		return makeIndex("WEEK_W3", "$", "mmBtu", YearMonth.of(2017, 3), /* 2017 */ 30);
	}

	private static CommodityCurve makeWeekly_W4() {
		return makeIndex("WEEK_W4", "$", "mmBtu", YearMonth.of(2017, 3), /* 2017 */ 40);
	}

	@Test
	public void test_datedavg_3_0_1() {
		Assertions.assertEquals(55.373, parseExpression("DATEDAVG(Brent,3,0,1)", LocalDateTime.of(2017, 6, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(55.7467, parseExpression("DATEDAVG(Brent,3,0,1)", LocalDateTime.of(2017, 7, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2467, parseExpression("DATEDAVG(Brent,3,0,1)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2267, parseExpression("DATEDAVG(Brent,3,0,1)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);

	}

	@Test
	public void test_datedavg_6_0_1() {
		Assertions.assertEquals(55.7583, parseExpression("DATEDAVG(Brent,6,0,1)", LocalDateTime.of(2017, 9, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.1950, parseExpression("DATEDAVG(Brent,6,0,1)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2217, parseExpression("DATEDAVG(Brent,6,0,1)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);
	}

	@Test
	public void test_datedavg_3_0_3() {
		Assertions.assertEquals(55.7467, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2017, 7, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(55.7467, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2017, 8, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(55.7467, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2017, 9, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2167, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2017, 10, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2167, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2267, parseExpression("DATEDAVG(Brent,3,0,3)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);
	}

	@Test
	public void test_datedavg_3_1_3() {
		Assertions.assertEquals(55.3733, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2017, 7, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(55.3733, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2017, 8, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(55.3733, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2017, 9, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.1433, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2017, 10, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.1433, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2467, parseExpression("DATEDAVG(Brent,3,1,3)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);
	}

	@Test
	public void test_datedavg_6_1_3() {
		Assertions.assertEquals(55.7583, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2017, 10, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(55.7583, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2017, 11, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(55.7583, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.1950, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.1950, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2018, 2, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.1950, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2018, 3, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2083, parseExpression("DATEDAVG(Brent,6,1,3)", LocalDateTime.of(2018, 4, 1, 0, 0, 0)), 0.001);
	}

	@Test
	public void test_datedavg_6_0_3() {
		Assertions.assertEquals(55.9817, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2017, 10, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(55.9817, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2017, 11, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(55.9817, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2017, 12, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2217, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2018, 1, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2217, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2018, 2, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2217, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2018, 3, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.1767, parseExpression("DATEDAVG(Brent,6,0,3)", LocalDateTime.of(2018, 4, 1, 0, 0, 0)), 0.001);
	}

	@Test
	public void test_splitmonth() {
		Assertions.assertEquals(40.01, parseExpression("SPLITMONTH(JKM_H1,JKM_H2,15)", LocalDateTime.of(2017, 6, 16, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.01, parseExpression("SPLITMONTH(JKM_H1,JKM_H2,15)", LocalDateTime.of(2017, 6, 13, 0, 0, 0)), 0.001);

		Assertions.assertEquals(40.01, parseExpression("SPLITMONTH(JKM_H1,JKM_H2,15)", LocalDateTime.of(2017, 6, 24, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.01, parseExpression("SPLITMONTH(JKM_H1,JKM_H2,15)", LocalDateTime.of(2017, 6, 14, 23, 59, 0)), 0.001);
	}

	@Test
	public void test_nestedsplitmonth() {
		String expression = "SPLITMONTH(SPLITMONTH(WEEK_W1,WEEK_W2,7),SPLITMONTH(WEEK_W3,WEEK_W4,21),14)";
		Assertions.assertEquals(10.0, parseExpression(expression, LocalDateTime.of(2017, 3, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(20.0, parseExpression(expression, LocalDateTime.of(2017, 3, 7, 0, 0, 0)), 0.001);
		Assertions.assertEquals(30.0, parseExpression(expression, LocalDateTime.of(2017, 3, 14, 0, 0, 0)), 0.001);
		Assertions.assertEquals(40.0, parseExpression(expression, LocalDateTime.of(2017, 3, 21, 0, 0, 0)), 0.001);
	}

	private double parseExpression(final String expression, LocalDateTime time) {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();

		for (final CommodityCurve idx : indicies) {
			pricingModel.getCommodityCurves().add(idx);
		}

		final SeriesParser p = PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.COMMODITY);
		final IExpression<ISeries> series = p.parse(expression);
		final Number evaluate = series.evaluate().evaluate(Hours.between(PriceIndexUtils.dateTimeZero, time));
		double unitPrice = evaluate.doubleValue();

		return unitPrice;
	}

	private static CommodityCurve makeIndex(final String name, final String currencyUnit, final String volumeUnit, final YearMonth startDate, final double... values) {

		final CommodityCurve index = PricingFactory.eINSTANCE.createCommodityCurve();
		index.setName(name);

		index.setCurrencyUnit(currencyUnit);
		index.setVolumeUnit(volumeUnit);
		YearMonth date = startDate;
		for (final double v : values) {
			final YearMonthPoint pt = PricingFactory.eINSTANCE.createYearMonthPoint();
			pt.setDate(date);
			pt.setValue(v);

			index.getPoints().add(pt);
			date = date.plusMonths(1);
		}
		return index;
	}

}
