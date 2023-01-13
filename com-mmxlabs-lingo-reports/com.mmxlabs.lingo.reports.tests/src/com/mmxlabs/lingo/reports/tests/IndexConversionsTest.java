/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.tests;

import java.time.YearMonth;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.parser.ExposuresIndexConversion.BreakEvenType;
import com.mmxlabs.common.parser.ExposuresIndexConversion.Form;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesOperatorExpression;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.parseutils.IndexConversion;
import com.mmxlabs.models.lng.pricing.parseutils.LookupData;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class IndexConversionsTest {
	@NonNull
	final static CommodityCurve[] indicies = new CommodityCurve[] { makeHH() };

	private static CommodityCurve makeHH() {
		return makeIndex("HH", "$", "mmBtu", YearMonth.of(2000, 1), 5);
	}

	private static CommodityCurve makeA(final double value) {
		return makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), value);
	}

	@Test
	public void test__X() {
		final String expression = "HH";
		final Form form = testExpression(expression);
		assert form == null;
	}

	@Test
	public void test__X_PLUS_C() {
		final String expression = "HH+1";
		final Form form = testExpression(expression);
		assert form == Form.X_PLUS_C;
	}

	@Test
	public void test__X_PLUS_QM() {
		final String expression = "HH+?";
		final Form form = testExpression(expression);
		assert form == Form.X_PLUS_C;
	}

	@Test
	public void test__X_PLUS_C_COMPLEX() {
		final String expression = "HH+1";
		final Form form = testExpression(expression);
		assert form == Form.X_PLUS_C;
	}

	@Test
	public void test__M_X() {
		final String expression = "5%HH";
		final Form form = testExpression(expression);
		assert form == Form.M_X;
	}

	@Test
	public void test__M_X_PLUS_C() {
		final String expression = "5%HH+25";
		final Form form = testExpression(expression);
		assert form == Form.M_X_PLUS_C;
	}

	@Test
	public void test__M_X_PLUS_C_PLUS_C() {
		final String expression = "5%HH+25+25";
		final Form form = testExpression(expression);
		assert form == null;
	}

	@Test
	public void test__BET_M_X_PLUS_QM() {
		final String expression = "5%HH+?";
		final BreakEvenType breakEvenType = testExpressionForBreakEvenType(expression);
		assert breakEvenType == BreakEvenType.INTERCEPT;
	}

	@Test
	public void test__BET_QM_X_PLUS_C() {
		final String expression = "?%HH+?";
		final BreakEvenType breakEvenType = testExpressionForBreakEvenType(expression);
		assert breakEvenType == BreakEvenType.COEFFICIENT;
	}

	@Test
	public void test__GraphRearrangement__MX() {
		final String expression = "?%HH";
		@Nullable
		final ASTNode testGraphRearrangement = testGraphRearrangement(expression, Form.M_X, 10.0);
		Assertions.assertEquals("10.0/(1.0%HH)", IndexConversion.getExpression(testGraphRearrangement));
	}

	@Test
	public void test__GraphRearrangement__MX_PLUS_C_1() {
		final String expression = "?%HH+5";
		@Nullable
		final ASTNode testGraphRearrangement = testGraphRearrangement(expression, Form.M_X_PLUS_C, 10.0);
		Assertions.assertEquals("(10.0-5)/(1.0%HH)", IndexConversion.getExpression(testGraphRearrangement));
	}

	@Test
	public void test__GraphRearrangement__MX_PLUS_C_2() {
		final String expression = "100%HH+?";
		@Nullable
		final ASTNode testGraphRearrangement = testGraphRearrangement(expression, Form.M_X_PLUS_C, 10.0);
		Assertions.assertEquals("10.0-(100%HH)", IndexConversion.getExpression(testGraphRearrangement));
	}

	@Test
	public void test__expressionCreation() {
		final String expression = "115%HH+5";
		final ASTNode markedUpNode = getParentMarkedUpNode(expression);
		final String rearrangedExpression = IndexConversion.getExpression(markedUpNode);
		System.out.println(rearrangedExpression);
		Assertions.assertEquals("(115%HH)+5", rearrangedExpression);
	}

	@Test
	public void test__calculation_QX_PLUS_C() {
		final String expression = "?%HH+3";
		final @Nullable ASTNode testGraphRearrangement = testGraphRearrangement(expression, Form.M_X_PLUS_C, 10);
		final String rearrangedExpression = IndexConversion.getExpression(testGraphRearrangement);
		Assertions.assertEquals("(10.0-3)/(1.0%HH)", rearrangedExpression);
		final double parseExpression = parseExpression(rearrangedExpression);
		Assertions.assertEquals(140, parseExpression, 0.0001);
	}

	@Test
	public void test__calculation_MX_PLUS_Q() {
		final String expression = "100%(HH*FX_EURO_to_USD*mwh_to_mmbtu)+?";
		final @Nullable ASTNode testGraphRearrangement = testGraphRearrangement(expression, Form.M_X_PLUS_C, 10);
		final String rearrangedExpression = IndexConversion.getExpression(testGraphRearrangement);
		Assertions.assertEquals("10.0-(100%((HH*FX_EURO_to_USD)*mwh_to_mmbtu))", rearrangedExpression);
		System.out.println(rearrangedExpression);
		final double parseExpression = parseExpression(rearrangedExpression);
		Assertions.assertEquals(8.37, parseExpression, 0.001);
	}

	@Test
	public void test__calculation_X_PLUS_Q() {
		final String expression = "HH+?";
		final @Nullable ASTNode testGraphRearrangement = testGraphRearrangement(expression, Form.X_PLUS_C, 10);
		final String rearrangedExpression = IndexConversion.getExpression(testGraphRearrangement);
		System.out.println(rearrangedExpression);
		Assertions.assertEquals("10.0-(1*HH)", rearrangedExpression);
		final double parseExpression = parseExpression(rearrangedExpression);
		Assertions.assertEquals(5.0, parseExpression, 0.001);
	}

	@Test
	public void test__M_X_PLUS_C_PLUS_Q() {
		final String expression = "12%HH+250+?";
		final Form form = testExpression(expression);
		assert form == null;
	}

	@Test
	public void test__M_X_PLUS_3C() {
		final String expression = "12%HH+250+1+100";
		final Form form = testExpression(expression);
		assert form == null;
	}

	@Test
	public void test__M_X_PLUS_BRACKETED_C() {
		final String expression = "12%HH+(250+1+100)";
		final Form form = testExpression(expression);
		assert form == null;
	}

	private Form testExpression(final String expression) {
		final ASTNode markedUpNode = getParentMarkedUpNode(expression);
		final Form form = IndexConversion.getForm(markedUpNode);
		return form;
	}

	private BreakEvenType testExpressionForBreakEvenType(final String expression) {
		final ASTNode markedUpNode = getParentMarkedUpNode(expression);
		final BreakEvenType breakEvenType = IndexConversion.getBreakEvenType(markedUpNode);
		return breakEvenType;
	}

	private @Nullable ASTNode testGraphRearrangement(final String expression, final Form form, final double price) {
		final ASTNode markedUpNode = getParentMarkedUpNode(expression);
		System.out.println(IndexConversion.getExpression(markedUpNode));
		final @Nullable ASTNode rearrangedGraph = IndexConversion.rearrangeGraph(price, markedUpNode, form);
		return rearrangedGraph;
	}

	private ASTNode getParentMarkedUpNode(final String expression) {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();

		for (final CommodityCurve idx : indicies) {
			pricingModel.getCommodityCurves().add(idx);
		}

		makeConversionFactor("therm", "mmbtu", 10, pricingModel);
		makeConversionFactor("bbl", "mmbtu", 0.180136, pricingModel);
		makeConversionFactor("mwh", "mmbtu", 0.293297, pricingModel);

		makeFXCurve("p", "USD", 1.3 / 100.0, pricingModel);
		makeFXCurve("EURO", "USD", 1.111, pricingModel);

		@NonNull
		final LookupData lookupData = LookupData.createLookupData(pricingModel);
//		// Parse the expression
		final SeriesParser commodityIndices = makeCommodityParser(lookupData);
		return commodityIndices.parse(expression);
	}

	private double parseExpression(final String expression) {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();

		for (final CommodityCurve idx : indicies) {
			pricingModel.getCommodityCurves().add(idx);
		}

		makeConversionFactor("therm", "mmbtu", 10, pricingModel);
		makeConversionFactor("bbl", "mmbtu", 0.180136, pricingModel);
		makeConversionFactor("mwh", "mmbtu", 0.293297, pricingModel);

		makeFXCurve("p", "USD", 1.3 / 100.0, pricingModel);
		makeFXCurve("EURO", "USD", 1.111, pricingModel);

		final @NonNull LookupData lookupData = LookupData.createLookupData(pricingModel);

		final SeriesParser p = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
		final IExpression<ISeries> series = p.asIExpression(expression);
		double unitPrice = 0.0;
		if (series instanceof final SeriesOperatorExpression opExpr) {
			final @NonNull ISeries opSeries = opExpr.evaluate();
			final Number evaluate = opSeries.evaluate(Hours.between(PriceIndexUtils.dateZero, YearMonth.now()), Collections.emptyMap());
			unitPrice = evaluate.doubleValue();
		}

		return unitPrice;
	}

	private void makeFXCurve(final String from, final String to, final double d, final PricingModel pricingModel) {
		final CurrencyCurve factor = PricingFactory.eINSTANCE.createCurrencyCurve();
		factor.setName(String.format("FX_%s_to_%s", from, to));
		factor.setExpression(Double.toString(d));

		pricingModel.getCurrencyCurves().add(factor);
	}

	private void makeConversionFactor(final String from, final String to, final double f, final PricingModel pricingModel) {
		{
			final UnitConversion factor = PricingFactory.eINSTANCE.createUnitConversion();
			factor.setFrom(from);
			factor.setTo(to);
			factor.setFactor(f);
			pricingModel.getConversionFactors().add(factor);
		}
		{
			final UnitConversion factor = PricingFactory.eINSTANCE.createUnitConversion();
			factor.setFrom(to);
			factor.setTo(from);
			factor.setFactor(1.0 / f);
			pricingModel.getConversionFactors().add(factor);
		}
	}

	private static CommodityCurve makeIndex(final String name, final String currencyUnit, final String volumeUnit, final YearMonth date, final double value) {

		final CommodityCurve index = PricingFactory.eINSTANCE.createCommodityCurve();
		index.setName(name);

		index.setCurrencyUnit(currencyUnit);
		index.setVolumeUnit(volumeUnit);
		final YearMonthPoint pt = PricingFactory.eINSTANCE.createYearMonthPoint();
		pt.setDate(date);
		pt.setValue(value);

		index.getPoints().add(pt);

		return index;
	}

	private static SeriesParser makeCommodityParser(final LookupData lookupData) {
		return PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
	}
}
