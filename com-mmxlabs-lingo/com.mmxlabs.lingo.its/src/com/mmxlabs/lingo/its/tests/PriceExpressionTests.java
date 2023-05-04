/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.SeriesParserData;
import com.mmxlabs.common.parser.series.SeriesType;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class PriceExpressionTests {
	@NonNull
	private static final CommodityCurve[] indicies = { //
			makeHH(), //
			makeBrent(), //
			makeJKM_H1(), //
			makeJKM_H2(), //
			makeWeekly_W1(), //
			makeWeekly_W2(), //
			makeWeekly_W3(), //
			makeWeekly_W4() //
	};

	private static CommodityCurve makeHH() {
		return makeIndex("HH", "$", "mmBtu", YearMonth.of(2017, 3), /* 2017 */ 5 //
		);
	}

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
		Assertions.assertEquals(56.2083, parseExpression("Brent[6,1,3]", LocalDateTime.of(2018, 4, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(56.2083, parseExpression("Brent[613]", LocalDateTime.of(2018, 4, 1, 0, 0, 0)), 0.001);
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
		final String expression = "SPLITMONTH(SPLITMONTH(WEEK_W1,WEEK_W2,7),SPLITMONTH(WEEK_W3,WEEK_W4,21),14)";
		Assertions.assertEquals(10.0, parseExpression(expression, LocalDateTime.of(2017, 3, 1, 0, 0, 0)), 0.001);
		Assertions.assertEquals(20.0, parseExpression(expression, LocalDateTime.of(2017, 3, 7, 0, 0, 0)), 0.001);
		Assertions.assertEquals(30.0, parseExpression(expression, LocalDateTime.of(2017, 3, 14, 0, 0, 0)), 0.001);
		Assertions.assertEquals(40.0, parseExpression(expression, LocalDateTime.of(2017, 3, 21, 0, 0, 0)), 0.001);
	}

	private double parseExpression(final String expression, final LocalDateTime time) {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();

		for (final CommodityCurve idx : indicies) {
			pricingModel.getCommodityCurves().add(idx);
		}

		final SeriesParser p = PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.COMMODITY);
		final ISeries series = p.asSeries(expression);
		final Number evaluate = series.evaluate(Hours.between(PriceIndexUtils.dateTimeZero, time), Collections.emptyMap());
		final double unitPrice = evaluate.doubleValue();

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

	/**
	 * Tests to make sure asString() returns the same input. There will be some variation due to case (handled by toLowerCase() in assertion) and whitespace (input expression whitespace needs to match
	 * output)
	 * 
	 * There are some expressions we can't handle in this simple test. E.g.
	 * 
	 * 10-10-10 becomes (10-10)-10.
	 * 
	 * HH[m] becomes HH[m0].
	 * 
	 * #v=HH ; #v becomes just HH.
	 *
	 * References to other expression curves are also inlined.
	 * 
	 * @param expr
	 */
	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0}")
	@ValueSource(strings = { //
			"HH", //
			"SHIFT(HH, 1)", //
			"SHIFT(HH, -1)", //
			"HH[m0]", //
			"HH[m-1]", //
			"HH[m1]", //
			"10%HH", //
			"10%(HH-HH[301])", //
			"10%(HH-HH[m-1])", //
			"10%(HH[m-1]-HH[101])", //
			"10%-HH[101]", //
			"HH[301]", //
			"HH[3,0,1]", //
			"DATEDAVG(HH, 3, 0, 1)", //
			"S(HH, 1, 2.0, 3, 4, 5, 6, 7, 8)", //
			"TIER(HH, < 40, 1, 2)|TIER(HH, 1, < 40, 2)", // Old style will be replaced with new style
			"TIER(HH, < 40, 1, <= 90.4, 2, 3)|TIER(HH, 1, < 40, 2, <= 90.4, 3)", // Old style will be replaced with new style
			"TIER(HH, 1, < 40, 2, <= 90.4, 3)", //
			"TIER(HH, 1, < 40, 2)", //
			"5-6", //
			"-6*-7", //
			"-6*(7-6)", //
			"MIN(1.5,2)", //
			"FLOOR(1,2)", //
			"MAX(1,2)", //
			"CAP(1,2)", //
			"SPLITMONTH(HH, HH, 15)", //
	})
	public void testASTNodeAsString(final String baseExpr) {

		final String expr;
		final String exprToCompare;
		if (baseExpr.contains("|")) {
			final String[] s = baseExpr.split("\\|");
			expr = s[0];
			exprToCompare = s[1];
		} else {
			expr = baseExpr;
			exprToCompare = baseExpr;
		}

		final SeriesParserData data = new SeriesParserData();
		final SeriesParser parser = new SeriesParser(data);
		// Add a dummy curve for use in expressions
		parser.addConstant("HH", SeriesType.COMMODITY, 1.0);

		final ASTNode node = parser.parse(expr);

		final String nodeAsStr = node.asString();

		Assertions.assertEquals(exprToCompare.toLowerCase(), nodeAsStr.toLowerCase());

	}

	public static Iterable<Object[]> testExpressionValues() {
		return Arrays.asList(new Object[][] { //

				{ "1", 1.0, null }, //
				{ "-1", -1.0, null }, //
				{ "10-10", 0.0, null }, //
				{ "10-10-10", -10.0, null }, //
				{ "5-6", -1.0, null }, //
				{ "-6*-7", 42.0, null }, //
				{ "-6*(7-6)", -6.0, null }, //
				{ "MIN(1)", 1, null }, //
				{ "MIN(1.5,2,5)", 1.5, null }, //
				{ "FLOOR(1,2,5)", 1, null }, //
				{ "MAX(1)", 1.0, null }, //
				{ "MAX(1,2,3)", 3.0, null }, //
				{ "MAX(2,1)", 2.0, null }, //
				{ "CAP(1,2)", 2.0, null }, //

				{ "Tier(1, 10, <2, 20)", 10.0, null }, //
				{ "Tier(2, 10, <=2, 20)", 10.0, null }, //
				{ "Tier(2, 10, <2, 20)", 20.0, null }, //
				{ "Tier(3, 10, <=2, 20)", 20.0, null }, //

				{ "Tier(1, 10, <2, 20, <= 3, 30)", 10.0, null }, //
				{ "Tier(2, 10, <2, 20, <= 3, 30)", 20.0, null }, //
				{ "Tier(3, 10, <2, 20, <= 3, 30)", 20.0, null }, //
				{ "Tier(3.1, 10, <2, 20, <= 3, 30)", 30.0, null }, //
				// Old style tier
				{ "Tier(1, <2, 10, 20)", 10.0, null }, //
				{ "Tier(2, <=2, 10, 20)", 10.0, null }, //
				{ "Tier(2, <2, 10, 20)", 20.0, null }, //
				{ "Tier(3, <=2, 10, 20)", 20.0, null }, //

				{ "Tier(1, <2, 10, <= 3, 20, 30)", 10.0, null }, //
				{ "Tier(2, <2, 10, <= 3, 20, 30)", 20.0, null }, //
				{ "Tier(3, <2, 10, <= 3, 20, 30)", 20.0, null }, //
				{ "Tier(3.1, <2, 10, <= 3, 20, 30)", 30.0, null }, //
				{ "HH", 5, null }, //
				{ "SHIFT(Brent, 1)", 55.76, LocalDate.of(2017, 6, 1) }, //
				{ "SHIFT(Brent, -1)", 56.16, LocalDate.of(2017, 6, 1) }, //
				{ "Brent[m0]", 56.01, LocalDate.of(2017, 6, 1) }, //
				{ "Brent[m-1]", 55.76, LocalDate.of(2017, 6, 1) }, //
				{ "Brent[m1]", 56.16, LocalDate.of(2017, 6, 1) }, //
				{ "10%HH", 0.5, null }, //
				{ "10%-HH", -0.5, null }, //
				{ "10%(6-HH[301])", 0.1, null }, //
				{ "10%(6-HH[m-1])", 0.1, null }, //
				{ "S(1, 2, 3, 3, 4, 5, 6, 7, 8)", 3 * 1 + 4, null }, //
				{ "S(2, 2, 3, 3, 4, 5, 6, 7, 8)", 5 * 2 + 6, null }, //
				{ "S(3, 2, 3, 3, 4, 5, 6, 7, 8)", 5 * 3 + 6, null }, //
				{ "S(4, 2, 3, 3, 4, 5, 6, 7, 8)", 7 * 4 + 8, null }, //
		});
	}

	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0} = {1}")
	@MethodSource()
	public void testExpressionValues(String expression, double expectedValue, @Nullable LocalDate optionalDate) {
		LocalDateTime pricingDate = optionalDate == null ? LocalDate.of(2020, 1, 1).atStartOfDay() : optionalDate.atStartOfDay();
		Assertions.assertEquals(expectedValue, parseExpression(expression, pricingDate), 0.001);
	}
}
