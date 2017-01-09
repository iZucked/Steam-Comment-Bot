/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.tests;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.commercial.parseutils.LookupData;
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
	private static final @NonNull YearMonth pricingDate = YearMonth.of(2016, 4);

	private static final double defaultVolumeInMMBTU = 3_000_000.0;

	@Parameters(name = "{0}")
	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { //
				{ "HH", "HH", "HH", single(calcExpected("HH", 5.0, 1.0)), indiciesOf(makeHH()) }, //

				{ "A + 1", "A + 1", "HH", single(calcExpected("A", 5.0, 1.0)), indiciesOf(makeHH(), makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), 5)) }, //

				{ "HH * 3 - 2", "HH * 3 - 2", "HH", single(calcExpected("HH", 5.0, 3.0)), indiciesOf(makeHH()) }, //

				{ "2.5 * HH + 2 * A", "2.5 * HH + 2 * A", "HH", multi(calcExpected("HH", 5.0, 2.5), calcExpected("A", 5.0, 2.0)),
						indiciesOf(makeHH(), makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), 5)) }, //

				{ "A - HH * 4", "A - HH * 4", "HH", multi(calcExpected("HH", 5.0, -4.0), calcExpected("A", 5.0, 1.0)), indiciesOf(makeHH(), makeA(5.0)) }, //

				{ "10%HH", "10%HH", "HH", single(calcExpected("HH", 5.0, 0.1)), indiciesOf(makeHH()) }, //

				{ "5*HH+6*HH", "5*HH+6*HH", "HH", single(calcExpected("HH", 5.0, 11.0)), indiciesOf(makeHH()) }, //

				{ "(5+6)*HH", "(5+6)*HH", "HH", single(calcExpected("HH", 5.0, 11.0)), indiciesOf(makeHH()) }, //

				{ "10%100*HH", "10%100*HH", "HH", single(calcExpected("HH", 5.0, 10.0)), indiciesOf(makeHH()) }, //

				{ "50%HH+50%HH (HH)", "50%HH+50%HH", "HH", single(calcExpected("HH", 5.0, 1)), indiciesOf(makeHH(), makeIndex("NBP", "$", "mmBtu", YearMonth.of(2000, 1), 7)) }, //
				{ "50%HH-NBP+50%HH (HH)", "50%HH-NBP+50%HH", "HH", single(calcExpected("HH", 5.0, 1), calcExpected("NBP", 7.0, -1)),
						indiciesOf(makeHH(), makeIndex("NBP", "$", "mmBtu", YearMonth.of(2000, 1), 7)) }, //

				{ "50%HH+50%NBP (HH)", "50%HH+50%NBP", "HH", single(calcExpected("HH", 5.0, 0.5), calcExpected("NBP", 7.0, 0.5)),
						indiciesOf(makeHH(), makeIndex("NBP", "$", "mmBtu", YearMonth.of(2000, 1), 7)) }, //

				// // // This form uses A as expression constant -
				{ "A*HH (1)", "A*HH", "HH", multi(calcExpected("HH", 5.0, 10.0)), indiciesOf(makeHH(), makeIndex("A", "$", "mmBtu", "10")) }, //
				// //
				// // // This form uses A as a curve
				{ "A*HH (2)", "A*HH", "HH", multi(calcExpected("HH", 5.0, 1.0), calcExpected("A", 10.0, 1.0)), indiciesOf(makeHH(), makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), 10)) }, //
				// A is derived from constant B
				{ "A*HH (3)", "A*HH", "HH", multi(calcExpected("HH", 5.0, 20 / 2)), indiciesOf(makeHH(), makeIndex("A", "$", "mmBtu", "B/2"), makeIndex("B", "$", "mmBtu", "20")) }, //

				/// Complex example with all components - a %, unit conversion and FX curve
				{ "NBP (Units)", "90%NBP/therms_per_mmBtu*FX_p_to_USD", "NBP", single(calcExpected("NBP", 30.0, 0.9 / 10.0, 0.9 * 30.0 / 100.0 / 10.0 * 1.3)),
						indiciesOf(makeIndex("NBP", "p", "therm", YearMonth.of(2000, 1), 30)) }, //

				/// Another Complex example
				{ "TTF (Units)", "((TTF-0.4)) /mwhs_per_mmBtu*FX_EURO_to_USD", "TTF", single(calcExpected("TTF", 12.6, 1.0 / 3.409511, (12.6 - 0.4) / 3.409511 * 1.111)),
						indiciesOf(makeIndex("TTF", "EURO", "mwh", YearMonth.of(2000, 1), 12.6)) }, //

				{ "NBP (Units) Reverse Conversion", "90%NBP*mmBtus_per_therm*FX_p_to_USD", "NBP", single(calcExpected("NBP", 30.0, 0.9 / 10.0, 0.9 * 30.0 / 100.0 / 10.0 * 1.3)),
						indiciesOf(makeIndex("NBP", "p", "therm", YearMonth.of(2000, 1), 30)) }, //

				/// Another Complex example
				{ "TTF (Units) Reverse Conversion ", "((TTF-0.4)) *mmBtus_per_MwH*FX_EURO_to_USD", "TTF", single(calcExpected("TTF", 12.6, 1.0 / 3.409511, (12.6 - 0.4) / 3.409511 * 1.111)),
						indiciesOf(makeIndex("TTF", "EURO", "mwh", YearMonth.of(2000, 1), 12.6)) }, //

				// { "HH - Shift -1", "SHIFT(HH,0-1)", "HH", calcExpected(8, 1, 8), indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //
				{ "HH - Shift 0", "SHIFT(HH,0)", "HH", single(calcExpected("HH", YearMonth.of(2016, 4), 7, 1, 7)), indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //
				{ "HH - Shift 1", "SHIFT(HH,1)", "HH", single(calcExpected("HH", YearMonth.of(2016, 3), 6, 1, 6)), indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //
				{ "HH - Shift 2", "SHIFT(HH,2)", "HH", single(calcExpected("HH", YearMonth.of(2016, 2), 5, 1, 5)), indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "50% HH - Shift 2", "50%SHIFT(HH,2)", "HH", single(calcExpected("HH", YearMonth.of(2016, 2), 5, 0.5, 0.5 * 5)),
						indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Double Shift 1", "SHIFT(SHIFT(HH,1),1)", "HH", single(calcExpected("HH", YearMonth.of(2016, 2), 5, 1, 5)),
						indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : %", "50%(SHIFT(HH,1) + SHIFT(HH,2))", "HH", multi(calcExpected("HH", YearMonth.of(2016, 2), 5, 0.5), calcExpected("HH", YearMonth.of(2016, 3), 6, 0.5)),
						indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : *", "0.5 * (SHIFT(HH,1) + SHIFT(HH,2))", "HH", multi(calcExpected("HH", YearMonth.of(2016, 2), 5, 0.5), calcExpected("HH", YearMonth.of(2016, 3), 6, 0.5)),
						indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : /", "((SHIFT(HH,1) + SHIFT(HH,2))) / 2", "HH", multi(calcExpected("HH", YearMonth.of(2016, 2), 5, 0.5), calcExpected("HH", YearMonth.of(2016, 3), 6, 0.5)),
						indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : +", "0.5 + (SHIFT(HH,1) + SHIFT(HH,2))", "HH", multi(calcExpected("HH", YearMonth.of(2016, 2), 5, 1), calcExpected("HH", YearMonth.of(2016, 3), 6, 1)),
						indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : -", "0.5 - (SHIFT(HH,1) + SHIFT(HH,2))", "HH", multi(calcExpected("HH", YearMonth.of(2016, 2), 5, -1), calcExpected("HH", YearMonth.of(2016, 3), 6, -1)),
						indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : (1)", "SHIFT(HH,1) + (SHIFT(HH,1) + SHIFT(HH,2))", "HH",
						multi(calcExpected("HH", YearMonth.of(2016, 2), 5, 1), calcExpected("HH", YearMonth.of(2016, 3), 6, 2)),
						indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : (2)", "SHIFT(HH,1) - (SHIFT(HH,1) + SHIFT(HH,2))", "HH",
						multi(calcExpected("HH", YearMonth.of(2016, 3), 5, 0), calcExpected("HH", YearMonth.of(2016, 2), 5, -1)),
						indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : (3)", "SHIFT(HH,1) - SHIFT(NBP,1) + SHIFT(HH,2)", "HH",
						multi(calcExpected("NBP", YearMonth.of(2016, 3), 16, -1), calcExpected("HH", YearMonth.of(2016, 2), 5, 1), calcExpected("HH", YearMonth.of(2016, 3), 6, 1)),
						indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8), makeIndex("NBP", "$", "mmbtu", YearMonth.of(2016, 2), 15, 16, 17, 18)) }, //
		});
	}

	private static CommodityIndex makeHH() {
		return makeIndex("HH", "$", "mmBtu", YearMonth.of(2000, 1), 5);
	}

	private static CommodityIndex makeA(final double value) {
		return makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), value);
	}

	private @NonNull final String expression;
	private @NonNull final String indexName;
	private @NonNull final CommodityIndex[] indicies;
	private @NonNull final ResultChecker checker;

	public ExposuresTest(final String name, final String expression, final String indexName, final @NonNull ResultChecker expectedResult, final CommodityIndex... indicies) {
		this.checker = expectedResult;
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
		makeConversionFactor("mwh", "mmbtu", 3.409511, pricingModel);

		makeFXCurve("p", "USD", 1.3 / 100.0, pricingModel);
		makeFXCurve("EURO", "USD", 1.111, pricingModel);

		final double volume = defaultVolumeInMMBTU;
		final boolean isPurchase = true;
		@NonNull
		final LookupData lookupData = Exposures.createLookupData(pricingModel);

		final Collection<ExposureDetail> details = Exposures.calculateExposure(expression, pricingDate, volume, isPurchase, lookupData);
		checker.validate(details, expression, lookupData);
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

	private static CommodityIndex[] indiciesOf(final CommodityIndex... indicies) {
		return indicies;
	}

	private static class ResultChecker {

		List<ExpectedResult> results;

		public ResultChecker(final ExpectedResult single) {
			results = Collections.singletonList(single);
		}

		public ResultChecker(final ExpectedResult... multiple) {
			results = new LinkedList<>();
			for (final ExpectedResult r : multiple) {
				results.add(r);
			}
		}

		public ResultChecker(final Collection<ExpectedResult> multiple) {
			results = new LinkedList<>(multiple);
		}

		void validate(@Nullable final Collection<ExposureDetail> details, final String expression, final LookupData lookupData) {

			final Map<Pair<String, YearMonth>, ExpectedResult> m = new HashMap<>();
			for (final ExpectedResult r : results) {
				assert r.expectedDate != null;
				m.put(new Pair<>(r.index, r.expectedDate), r);
			}
			if (details != null) {
				for (final ExposureDetail detail : details) {
					final ExpectedResult r = m.remove(new Pair<>(detail.getIndex().getName(), detail.getDate()));
					Assert.assertNotNull(r);
					r.validate(detail, expression, lookupData);
				}
			}
			Assert.assertTrue(m.isEmpty());
		}
	}

	private static class ExpectedResult {
		private String index;
		private YearMonth expectedDate = pricingDate;
		private final OptionalDouble expectedVolume;
		private final OptionalDouble expectedValue;
		private final OptionalDouble expectedExpressionValue;
		private final boolean expectExposed;

		private static final double delta = 0.01;

		public ExpectedResult(String index, final double expectedVolume) {
			this.index = index;
			this.expectExposed = true;
			this.expectedVolume = OptionalDouble.of(expectedVolume);
			this.expectedValue = OptionalDouble.empty();
			this.expectedExpressionValue = OptionalDouble.empty();
		}

		public ExpectedResult(String index, final double expectedVolume, final double expectedValue) {
			this.index = index;
			this.expectExposed = true;
			this.expectedVolume = OptionalDouble.of(expectedVolume);
			this.expectedValue = OptionalDouble.of(expectedValue);
			this.expectedExpressionValue = OptionalDouble.empty();
		}

		public ExpectedResult(String index, final YearMonth date, final double expectedVolume, final double expectedValue) {
			this.index = index;
			expectedDate = date;
			this.expectExposed = true;
			this.expectedVolume = OptionalDouble.of(expectedVolume);
			this.expectedValue = OptionalDouble.of(expectedValue);
			this.expectedExpressionValue = OptionalDouble.empty();
		}

		public ExpectedResult(String index, final double expectedVolume, final double expectedValue, final double expectedExpressionValue) {
			this.index = index;
			this.expectExposed = true;
			this.expectedVolume = OptionalDouble.of(expectedVolume);
			this.expectedValue = OptionalDouble.of(expectedValue);
			this.expectedExpressionValue = OptionalDouble.of(expectedExpressionValue);
		}

		public ExpectedResult(String index, final YearMonth date, final double expectedVolume, final double expectedValue, final double expectedExpressionValue) {
			this.index = index;
			expectedDate = date;
			this.expectExposed = true;
			this.expectedVolume = OptionalDouble.of(expectedVolume);
			this.expectedValue = OptionalDouble.of(expectedValue);
			this.expectedExpressionValue = OptionalDouble.of(expectedExpressionValue);
		}

		void validate(@Nullable final ExposureDetail detail, final String expression, final LookupData lookupData) {
			if (!expectExposed) {
				Assert.assertTrue(detail == null);
				return;
			}

			Assert.assertEquals(expectedDate, detail.getDate());

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
				final Number evaluate = series.evaluate(Hours.between(PriceIndexUtils.dateZero, pricingDate));

				final double val = evaluate.doubleValue() * defaultVolumeInMMBTU;
				Assert.assertEquals("Expr Value", expectedExpressionValue.getAsDouble(), val, delta);
			}
		}
	}

	private static ResultChecker single(final ExpectedResult... expectedResults) {
		return new ResultChecker(expectedResults);
	}

	private static ResultChecker multi(final ExpectedResult... expectedResults) {
		return new ResultChecker(expectedResults);
	}

	private static ResultChecker notexpected() {
		return new ResultChecker();
	}

	private static ExpectedResult expected(String index, final double expectedCoeff) {
		return new ExpectedResult(index, defaultVolumeInMMBTU * expectedCoeff);
	}

	private static ExpectedResult expected(String index, final double expectedVolume, final double expectedValue) {
		return new ExpectedResult(index, expectedVolume, expectedValue);
	}

	private static ExpectedResult calcExpected(String index, final double unitPrice, final double factor) {
		return new ExpectedResult(index, defaultVolumeInMMBTU * factor, defaultVolumeInMMBTU * factor * unitPrice);
	}

	private static ExpectedResult calcExpected(String index, final double unitPrice, final double factor, final double expressionUnitPrice) {
		return new ExpectedResult(index, defaultVolumeInMMBTU * factor, defaultVolumeInMMBTU * factor * unitPrice, defaultVolumeInMMBTU * expressionUnitPrice);
	}

	private static ExpectedResult calcExpected(String index, final YearMonth date, final double unitPrice, final double factor) {
		return new ExpectedResult(index, date, defaultVolumeInMMBTU * factor, defaultVolumeInMMBTU * factor * unitPrice);
	}

	private static ExpectedResult calcExpected(String index, final YearMonth date, final double unitPrice, final double factor, final double expressionUnitPrice) {
		return new ExpectedResult(index, date, defaultVolumeInMMBTU * factor, defaultVolumeInMMBTU * factor * unitPrice, defaultVolumeInMMBTU * expressionUnitPrice);
	}
}
