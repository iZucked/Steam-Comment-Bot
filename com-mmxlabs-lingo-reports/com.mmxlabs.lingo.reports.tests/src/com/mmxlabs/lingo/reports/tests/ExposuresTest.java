/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.OptionalDouble;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.parseutils.CoEff;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.commercial.parseutils.LookupData;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.schedule.ExposureDetail;

/**
 * JUnit tests for the exposures calculations.
 * 
 * @author Simon McGregor
 * 
 */
@RunWith(value = Parameterized.class)

public class ExposuresTest {

	private static final double defaultVolumeInMMBTU = 3_000_000.0;

	@Parameters(name = "{0}")
	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { //
				{ "HH", "HH", "HH", calcExpected(5.0, 1.0), indiciesOf(makeHH()) }, //

				{ "A + 1", "A + 1", "HH", notexpected(), indiciesOf(makeHH(), makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), 5)) }, //

				{ "HH * 3 - 2", "HH * 3 - 2", "HH", calcExpected(5.0, 3.0), indiciesOf(makeHH()) }, //

				{ "2.5 * HH + 2 * A", "2.5 * HH + 2 * A", "HH", calcExpected(5.0, 2.5), indiciesOf(makeHH(), makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), 5)) }, //

				{ "A - HH * 4", "A - HH * 4", "HH", calcExpected(5.0, -4.0), indiciesOf(makeHH(), makeA(5.0)) }, //

				{ "10%HH", "10%HH", "HH", calcExpected(5.0, 0.1), indiciesOf(makeHH()) }, //

				{ "5*HH+6*HH", "5*HH+6*HH", "HH", calcExpected(5.0, 11.0), indiciesOf(makeHH()) }, //

				{ "(5+6)*HH", "(5+6)*HH", "HH", calcExpected(5.0, 11.0), indiciesOf(makeHH()) }, //

				{ "10%100*HH", "10%100*HH", "HH", calcExpected(5.0, 10.0), indiciesOf(makeHH()) }, //

				{ "50%HH+50%NBP (HH)", "50%HH+50%NBP", "HH", calcExpected(5.0, 0.5), indiciesOf(makeHH(), makeIndex("NBP", "$", "mmBtu", YearMonth.of(2000, 1), 7)) }, //

				{ "50%HH+50%NBP (NBP)", "50%HH+50%NBP", "NBP", calcExpected(7.0, 0.5), indiciesOf(makeHH(), makeIndex("NBP", "$", "mmBtu", YearMonth.of(2000, 1), 7)) }, //

				// This form uses a curve -
				{ "A*HH (1)", "A*HH", "HH", calcExpected(5.0, 1.0), indiciesOf(makeHH(), makeIndex("A", "$", "mmBtu", "10")) }, //

				// This form uses A as a curve
				{ "A*HH (2)", "A*HH", "HH", calcExpected(5.0, 1.0), indiciesOf(makeHH(), makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), 10)) }, //

				{ "A*HH (3)", "A*HH", "HH", calcExpected(5.0, 1.0), indiciesOf(makeHH(), makeIndex("A", "$", "mmBtu", "B/2"), makeIndex("B", "$", "mmBtu", "20")) }, //
				/// Complex example with all components - a %, unit conversion and FX curve
				{ "NBP (Units)", "90%NBP*therms_per_mmBtu*FX_p_to_USD", "NBP", calcExpected(30.0, 0.9 * 10.0, 0.9 * 30.0 / 100.0 * 1.3 * 10.0),
						indiciesOf(makeIndex("NBP", "p", "therm", YearMonth.of(2000, 1), 30)) }, //
				/// Another Complex example
				{ "TTF (Units)", "(TTF-0.4)*mwhs_per_mmBtu*FX_EURO_to_USD", "TTF", calcExpected(12.6, 0.293297, (12.6 - 0.4) * 1.111 * 0.293297),
						indiciesOf(makeIndex("TTF", "EURO", "mwh", YearMonth.of(2000, 1), 12.6)) }, //
		});
	}

	private static CommodityIndex makeHH() {
		return makeIndex("HH", "$", "mmBtu", YearMonth.of(2000, 1), 5);
	}

	private static CommodityIndex makeA(double value) {
		return makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), value);
	}

	private @NonNull final String expression;
	private @NonNull final String indexName;
	private @NonNull final CommodityIndex[] indicies;
	private @NonNull ExpectedResult expectedResult;

	public ExposuresTest(final String name, final String expression, final String indexName, final @NonNull ExpectedResult expectedResult, final CommodityIndex... indicies) {
		this.expectedResult = expectedResult;
		this.indicies = indicies;
		this.indexName = indexName;
		this.expression = expression;
	}

	@Test
	public void testPriceExpressionExposureCoefficient() {

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();

		for (final CommodityIndex idx : indicies) {
			pricingModel.getCommodityIndices().add(idx);
		}

		makeConversionFactor("therm", "mmbtu", 10, pricingModel);
		makeConversionFactor("bbl", "mmbtu", 0.180136, pricingModel);
		makeConversionFactor("mwh", "mmbtu", 0.293297, pricingModel);

		makeFXCurve("p", "USD", 1.3 / 100.0, pricingModel);
		makeFXCurve("EURO", "USD", 1.111, pricingModel);

		double volume = defaultVolumeInMMBTU;
		boolean isPurchase = true;
		@NonNull
		final LookupData lookupData = Exposures.createLookupData(pricingModel);

		final ExposureDetail detail = Exposures.calculateExposure(expression, lookupData.commodityMap.get(indexName.toLowerCase()), YearMonth.of(2010, 4), volume, isPurchase, lookupData);
		expectedResult.validate(detail, expression, lookupData);
	}

	private void makeFXCurve(String from, String to, double d, PricingModel pricingModel) {
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

	private static CommodityIndex makeIndex(final String name, final String currencyUnit, final String volumeUnit, final String expression) {

		final DerivedIndex<Double> data = PricingFactory.eINSTANCE.createDerivedIndex();

		data.setExpression(expression);

		final CommodityIndex index = PricingFactory.eINSTANCE.createCommodityIndex();
		index.setName(name);
		index.setData(data);

		index.setCurrencyUnit(currencyUnit);
		index.setVolumeUnit(volumeUnit);

		return index;
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

	private static CommodityIndex[] indiciesOf(final CommodityIndex... indicies) {
		return indicies;
	}

	private static class ExpectedResult {
		private OptionalDouble expectedVolume;
		private OptionalDouble expectedValue;
		private OptionalDouble expectedExpressionValue;
		private boolean expectExposed;

		private static final double delta = 0.01;

		public ExpectedResult() {
			this.expectExposed = false;
			this.expectedVolume = OptionalDouble.empty();
			this.expectedValue = OptionalDouble.empty();
			this.expectedExpressionValue = OptionalDouble.empty();
		}

		public ExpectedResult(double expectedVolume) {
			this.expectExposed = true;
			this.expectedVolume = OptionalDouble.of(expectedVolume);
			this.expectedValue = OptionalDouble.empty();
			this.expectedExpressionValue = OptionalDouble.empty();
		}

		public ExpectedResult(double expectedVolume, double expectedValue) {
			this.expectExposed = true;
			this.expectedVolume = OptionalDouble.of(expectedVolume);
			this.expectedValue = OptionalDouble.of(expectedValue);
			this.expectedExpressionValue = OptionalDouble.empty();
		}

		public ExpectedResult(double expectedVolume, double expectedValue, double expectedExpressionValue) {
			this.expectExposed = true;
			this.expectedVolume = OptionalDouble.of(expectedVolume);
			this.expectedValue = OptionalDouble.of(expectedValue);
			this.expectedExpressionValue = OptionalDouble.of(expectedExpressionValue);
		}

		void validate(@Nullable ExposureDetail detail, String expression, LookupData lookupData) {
			if (!expectExposed) {
				Assert.assertNull(detail);
				return;
			}

			Assert.assertNotNull(detail);
			if (expectedVolume.isPresent()) {
				Assert.assertEquals("Volume", expectedVolume.getAsDouble(), detail.getVolumeInNativeUnits(), delta);
			}
			if (expectedValue.isPresent()) {
				Assert.assertEquals("Value", expectedValue.getAsDouble(), detail.getNativeValue(), delta);
			}
			if (expectedExpressionValue.isPresent()) {
				final SeriesParser p = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
				final ISeries series = p.parse(expression).evaluate();
				// "Magic" date constant used in PriceIndexUtils for date zero
				final Number evaluate = series.evaluate(Hours.between(YearMonth.of(2000, 1), YearMonth.of(2016, 4)));

				double val = evaluate.doubleValue() * defaultVolumeInMMBTU;
				Assert.assertEquals("Expr Value", expectedExpressionValue.getAsDouble(), val, delta);
			}
		}
	}

	private static ExpectedResult notexpected() {
		return new ExpectedResult();
	}

	private static ExpectedResult expected(double expectedCoeff) {
		return new ExpectedResult(defaultVolumeInMMBTU * expectedCoeff);
	}

	private static ExpectedResult expected(double expectedVolume, double expectedValue) {
		return new ExpectedResult(expectedVolume, expectedValue);
	}

	private static ExpectedResult calcExpected(double unitPrice, double factor) {
		return new ExpectedResult(defaultVolumeInMMBTU * factor, defaultVolumeInMMBTU * factor * unitPrice);
	}

	private static ExpectedResult calcExpected(double unitPrice, double factor, double expressionUnitPrice) {
		return new ExpectedResult(defaultVolumeInMMBTU * factor, defaultVolumeInMMBTU * factor * unitPrice, defaultVolumeInMMBTU * expressionUnitPrice);
	}
}
