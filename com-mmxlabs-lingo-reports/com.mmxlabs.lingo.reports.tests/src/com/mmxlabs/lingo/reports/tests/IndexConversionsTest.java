/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.tests;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesOperatorExpression;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.commercial.parseutils.IndexConversion;
import com.mmxlabs.models.lng.commercial.parseutils.IndexConversion.BreakEvenType;
import com.mmxlabs.models.lng.commercial.parseutils.IndexConversion.Form;
import com.mmxlabs.models.lng.commercial.parseutils.LookupData;
import com.mmxlabs.models.lng.commercial.parseutils.MarkedUpNode;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.parser.Node;
import com.mmxlabs.models.lng.pricing.parser.RawTreeParser;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class IndexConversionsTest {
	@NonNull
	final static CommodityIndex[] indicies = new CommodityIndex[] { makeHH() };

	private static CommodityIndex makeHH() {
		return makeIndex("HH", "$", "mmBtu", YearMonth.of(2000, 1), 5);
	}

	private static CommodityIndex makeA(final double value) {
		return makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), value);
	}

	@Test
	public void test__X() {
		final String expression = "HH";
		Form form = testExpression(expression);
		assert form == null;
	}

	@Test
	public void test__X_PLUS_C() {
		final String expression = "HH+1";
		Form form = testExpression(expression);
		assert form == Form.X_PLUS_C;
	}
	
	@Test
	public void test__X_PLUS_QM() {
		final String expression = "HH+?";
		Form form = testExpression(expression);
		assert form == Form.X_PLUS_C;
	}
	

	@Test
	public void test__X_PLUS_C_COMPLEX() {
		final String expression = "HH+1";
		Form form = testExpression(expression);
		assert form == Form.X_PLUS_C;
	}

	
	@Test
	public void test__M_X() {
		final String expression = "5%HH";
		Form form = testExpression(expression);
		assert form == Form.M_X;
	}

	@Test
	public void test__M_X_PLUS_C() {
		final String expression = "5%HH+25";
		Form form = testExpression(expression);
		assert form == Form.M_X_PLUS_C;
	}

	@Test
	public void test__M_X_PLUS_C_PLUS_C() {
		final String expression = "5%HH+25+25";
		Form form = testExpression(expression);
		assert form == null;
	}

	@Test
	public void test__BET_M_X_PLUS_QM() {
		final String expression = "5%HH+?";
		BreakEvenType breakEvenType = testExpressionForBreakEvenType(expression);
		assert breakEvenType == BreakEvenType.INTERCEPT;
	}
	
	@Test
	public void test__BET_QM_X_PLUS_C() {
		final String expression = "?%HH+?";
		BreakEvenType breakEvenType = testExpressionForBreakEvenType(expression);
		assert breakEvenType == BreakEvenType.COEFFICIENT;
	}

	@Test
	public void test__GraphRearrangement__MX() {
		final String expression = "?%HH";
		@Nullable
		MarkedUpNode testGraphRearrangement = testGraphRearrangement(expression, Form.M_X, 10.0);
		Assert.assertEquals("((10.0)/((1.0)%(HH)))", IndexConversion.getExpression(testGraphRearrangement));
	}

	@Test
	public void test__GraphRearrangement__MX_PLUS_C_1() {
		final String expression = "?%HH+5";
		@Nullable
		MarkedUpNode testGraphRearrangement = testGraphRearrangement(expression, Form.M_X_PLUS_C, 10.0);
		Assert.assertEquals("(((10.0)-(5.0))/((1.0)%(HH)))", IndexConversion.getExpression(testGraphRearrangement));
	}
	
	@Test
	public void test__GraphRearrangement__MX_PLUS_C_2() {
		final String expression = "100%HH+?";
		@Nullable
		MarkedUpNode testGraphRearrangement = testGraphRearrangement(expression, Form.M_X_PLUS_C, 10.0);
		Assert.assertEquals("((10.0)-((100.0)%(HH)))", IndexConversion.getExpression(testGraphRearrangement));
	}
	
	@Test
	public void test__expressionCreation() {
		final String expression = "115%HH+5";
		final MarkedUpNode markedUpNode = getParentMarkedUpNode(expression);
		String rearrangedExpression = IndexConversion.getExpression(markedUpNode);
		System.out.println(rearrangedExpression);
		Assert.assertEquals("(((115.0)%(HH))+(5.0))", rearrangedExpression);
	}

	@Test
	public void test__calculation_QX_PLUS_C() {
		final String expression = "?%HH+3";
		@Nullable
		MarkedUpNode testGraphRearrangement = testGraphRearrangement(expression, Form.M_X_PLUS_C, 10);
		String rearrangedExpression = IndexConversion.getExpression(testGraphRearrangement);
		Assert.assertEquals("(((10.0)-(3.0))/((1.0)%(HH)))", rearrangedExpression);
		double parseExpression = parseExpression(rearrangedExpression);
		Assert.assertEquals(140, parseExpression, 0.0);
	}
	
	@Test
	public void test__calculation_MX_PLUS_Q() {
		final String expression = "100%(HH*FX_EURO_to_USD*mwhs_per_mmBtu)+?";
		@Nullable
		MarkedUpNode testGraphRearrangement = testGraphRearrangement(expression, Form.M_X_PLUS_C, 10);
		String rearrangedExpression = IndexConversion.getExpression(testGraphRearrangement);
		Assert.assertEquals("((10.0)-((100.0)%((HH)*((FX_EURO_to_USD)*(mwhs_per_mmBtu)))))", rearrangedExpression);
		System.out.println(rearrangedExpression);
		double parseExpression = parseExpression(rearrangedExpression);
		Assert.assertEquals(8.37, parseExpression, 0.001);
	}

	@Test
	public void test__calculation_X_PLUS_Q() {
		final String expression = "HH+?";
		@Nullable
		MarkedUpNode testGraphRearrangement = testGraphRearrangement(expression, Form.X_PLUS_C, 10);
		String rearrangedExpression = IndexConversion.getExpression(testGraphRearrangement);
		System.out.println(rearrangedExpression);
		Assert.assertEquals("((10.0)-((1.0)*(HH)))", rearrangedExpression);
		double parseExpression = parseExpression(rearrangedExpression);
		Assert.assertEquals(5.0, parseExpression, 0.001);
	}

	
	private Form testExpression(final String expression) {
		final MarkedUpNode markedUpNode = getParentMarkedUpNode(expression);
		Form form = IndexConversion.getForm(markedUpNode);
		return form;
	}
	
	private BreakEvenType testExpressionForBreakEvenType(final String expression) {
		final MarkedUpNode markedUpNode = getParentMarkedUpNode(expression);
		BreakEvenType breakEvenType = IndexConversion.getBreakEvenType(markedUpNode);
		return breakEvenType;
	}
	
	private @Nullable MarkedUpNode testGraphRearrangement(final String expression, Form form, double price) {
		final MarkedUpNode markedUpNode = getParentMarkedUpNode(expression);
		System.out.println(IndexConversion.getExpression(markedUpNode));
		@Nullable MarkedUpNode rearrangedGraph = IndexConversion.rearrangeGraph(price, markedUpNode, form);
		return rearrangedGraph;
	}


	private MarkedUpNode getParentMarkedUpNode(final String expression) {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();

		for (final CommodityIndex idx : indicies) {
			pricingModel.getCommodityIndices().add(idx);
		}

		makeConversionFactor("therm", "mmbtu", 10, pricingModel);
		makeConversionFactor("bbl", "mmbtu", 0.180136, pricingModel);
		makeConversionFactor("mwh", "mmbtu", 0.293297, pricingModel);

		makeFXCurve("p", "USD", 1.3 / 100.0, pricingModel);
		makeFXCurve("EURO", "USD", 1.111, pricingModel);

		@NonNull
		final LookupData lookupData = Exposures.createLookupData(pricingModel);
		// Parse the expression
		final IExpression<Node> parse = new RawTreeParser().parse(expression);
		final Node p = parse.evaluate();
		final Node node = Exposures.expandNode(p, lookupData);
		final MarkedUpNode markedUpNode = Exposures.markupNodes(node, lookupData);
		return markedUpNode;
	}
	
	private double parseExpression(final String expression) {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		
		for (final CommodityIndex idx : indicies) {
			pricingModel.getCommodityIndices().add(idx);
		}
		
		makeConversionFactor("therm", "mmbtu", 10, pricingModel);
		makeConversionFactor("bbl", "mmbtu", 0.180136, pricingModel);
		makeConversionFactor("mwh", "mmbtu", 0.293297, pricingModel);
		
		makeFXCurve("p", "USD", 1.3 / 100.0, pricingModel);
		makeFXCurve("EURO", "USD", 1.111, pricingModel);
		
		@NonNull
		final LookupData lookupData = Exposures.createLookupData(pricingModel);
		
		final SeriesParser p = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
		final IExpression<ISeries> series = p.parse(expression);
		double unitPrice = 0.0;
		if (series instanceof SeriesOperatorExpression) {
			@NonNull
			ISeries opSeries = ((SeriesOperatorExpression) series).evaluate();
			final Number evaluate = opSeries.evaluate(Hours.between(PriceIndexUtils.dateZero, YearMonth.now()));
			unitPrice = evaluate.doubleValue();
		}

		return unitPrice;
	}


	private void makeFXCurve(final String from, final String to, final double d, final PricingModel pricingModel) {
		final CurrencyIndex factor = PricingFactory.eINSTANCE.createCurrencyIndex();
		factor.setName(String.format("FX_%s_to_%s", from, to));
		final DerivedIndex<Double> data = PricingFactory.eINSTANCE.createDerivedIndex();
		data.setExpression(Double.toString(d));

		factor.setData(data);
		pricingModel.getCurrencyIndices().add(factor);
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

	private static CommodityIndex makeIndex(final String name, final String currencyUnit, final String volumeUnit, final YearMonth date, final double value) {

		final DataIndex<Double> data = PricingFactory.eINSTANCE.createDataIndex();

		final IndexPoint<Double> pt = PricingFactory.eINSTANCE.createIndexPoint();
		pt.setDate(date);
		pt.setValue(value);

		data.getPoints().add(pt);

		final CommodityIndex index = PricingFactory.eINSTANCE.createCommodityIndex();
		index.setName(name);
		index.setData(data);

		index.setCurrencyUnit(currencyUnit);
		index.setVolumeUnit(volumeUnit);

		return index;
	}
}
