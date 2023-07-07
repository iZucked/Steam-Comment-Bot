/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.tests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.BasicCommodityCurveData;
import com.mmxlabs.common.curves.BasicUnitConversionData;
import com.mmxlabs.common.exposures.BasicExposureRecord;
import com.mmxlabs.common.exposures.ExposuresLookupData;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.parseutils.PricingDataCache;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.exposures.ExposuresCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IExposureDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IExposureDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IExternalDateProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultExposureDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;

/**
 * JUnit tests for the financial exposures calculations. There should not be curves with one letter as a name
 * 
 * @author Simon McGregor, Simon Goodall, FM
 * @author Mihnea-Teodor
 * 
 * @version 2
 * 
 */
public class BasicExposuresTest {
	private static @NonNull final YearMonth pricingDate = YearMonth.of(2016, 4);
	private static final int pricingDay = 4;

	private static final double defaultVolumeInMMBTU = 3_000_000.0;
	private static final double bbl_to_mmBtu = 0.180136;

	public static Collection generateTests() {
		return Arrays.asList(new Object[][] { //

				{ "AdditionCurve + 1", "AdditionCurve + 1", "HH", single(calcExpected("AdditionCurve", 5.0, 1.0)),
						indicesOf(makeHH(), makeIndex("AdditionCurve", "$", "mmBtu", YearMonth.of(2000, 1), 5)) }, //
				{ "HH", "HH", "HH", single(calcExpected("HH", 5.0, 1.0)), indicesOf(makeHH()) }, //

				{ "10%Brent", "10%Brent", "Brent", single(new ExpectedResult("Brent", defaultVolumeInMMBTU * 0.1 * bbl_to_mmBtu, defaultVolumeInMMBTU * 0.1 * 90 * bbl_to_mmBtu)),
						indicesOf(makeBrent()) }, //

				{ "HH * 3 - 2", "HH * 3 - 2", "HH", single(calcExpected("HH", 5.0, 3.0)), indicesOf(makeHH()) }, //

				{ "HH2 * 3 - 2", "HH2 * 3 - 2", "HH", single(calcExpected("HH", 5.0, 3.0)), indicesOf(makeHH(), makeExpression("HH2", "HH")) }, //

				{ "HH2 * 3 - 2", "HH2 * 3 - 2", "HH", single(calcExpected("HH", 5.0, 3.0)), indicesOf(makeHH(), makeExpression("HH2", "HH")) }, //

				{ "2.5 * HH + 2 * MultiplicationCurve", "2.5 * HH + 2 * MultiplicationCurve", "HH", multi(calcExpected("HH", 5.0, 2.5), calcExpected("MultiplicationCurve", 5.0, 2.0)),
						indicesOf(makeHH(), makeIndex("MultiplicationCurve", "$", "mmBtu", YearMonth.of(2000, 1), 5)) }, //

				{ "SubstractCurve - HH * 4", "SubstractCurve - HH * 4", "HH", multi(calcExpected("HH", 5.0, -4.0), calcExpected("SubstractCurve", 5.0, 1.0)),
						indicesOf(makeHH(), makeIndex("SubstractCurve", "$", "mmBtu", YearMonth.of(2000, 1), 5.0)) }, //

				{ "10%HH", "10%HH", "HH", single(calcExpected("HH", 5.0, 0.1)), indicesOf(makeHH()) }, //

				{ "5*HH+6*HH", "5*HH+6*HH", "HH", single(calcExpected("HH", 5.0, 11.0)), indicesOf(makeHH()) }, //

				{ "(5+6)*HH", "(5+6)*HH", "HH", single(calcExpected("HH", 5.0, 11.0)), indicesOf(makeHH()) }, //

				{ "10%100*HH", "10%100*HH", "HH", single(calcExpected("HH", 5.0, 10.0)), indicesOf(makeHH()) }, //

				{ "50%HH+50%HH (HH)", "50%HH+50%HH", "HH", single(calcExpected("HH", 5.0, 1)), indicesOf(makeHH(), makeIndex("NBP", "$", "mmBtu", YearMonth.of(2000, 1), 7)) }, //
				{ "50%HH-NBP+50%HH (HH)", "50%HH-NBP+50%HH", "HH", single(calcExpected("HH", 5.0, 1), calcExpected("NBP", 7.0, -1)),
						indicesOf(makeHH(), makeIndex("NBP", "$", "mmBtu", YearMonth.of(2000, 1), 7)) }, //

				{ "50%HH+50%NBP (HH)", "50%HH+50%NBP", "HH", single(calcExpected("HH", 5.0, 0.5), calcExpected("NBP", 7.0, 0.5)),
						indicesOf(makeHH(), makeIndex("NBP", "$", "mmBtu", YearMonth.of(2000, 1), 7)) }, //

				// // // This form uses A as expression constant -
				{ "MultiplicationCurve*HH (1)", "MultiplicationCurve*HH", "HH", multi(calcExpected("HH", 5.0, 10.0)), indicesOf(makeHH(), makeIndex("MultiplicationCurve", "$", "mmBtu", "10")) }, //
				// //
				// // // This form uses A as a curve
				{ "MultiplicationCurve*HH (2)", "MultiplicationCurve*HH", "HH", multi(calcExpected("HH", 5.0, 1.0), calcExpected("MultiplicationCurve", 10.0, 1.0)),
						indicesOf(makeHH(), makeIndex("MultiplicationCurve", "$", "mmBtu", YearMonth.of(2000, 1), 10)) },

				{ "MultiplicationCurve*HH (2A)", "HH*MultiplicationCurve", "HH", multi(calcExpected("MultiplicationCurve", 10.0, 1.0), calcExpected("HH", 5.0, 1.0)),
						indicesOf(makeHH(), makeIndex("MultiplicationCurve", "$", "mmBtu", YearMonth.of(2000, 1), 10)) }, //
				// A is derived from constant B
				{ "MultiplicationCurve*HH (3)", "MultiplicationCurve*HH", "HH", multi(calcExpected("HH", 5.0, 20 / 2)),
						indicesOf(makeHH(), makeIndex("MultiplicationCurve", "$", "mmBtu", "BTest/2"), makeIndex("BTest", "$", "mmBtu", "20")) },
				// Curves defined in terms of each other - For now we assume that the user won't
				// introduce cyclical dependencies
				// e.g. Curve A is defined in terms of curve B, B in terms of C and C in terms
				// of A
				{ "UserDefinedCurve (1)", "curveA*HH", "HH", multi(calcExpected("HH", 5.0, 32)),
						indicesOf(makeHH(), makeIndex("curveA", "$", "mmBtu", "curveB+5"), makeIndex("curveB", "$", "mmBtu", "curveC+2"), makeIndex("curveC", "$", "mmBtu", "25")) },

				{ "UserDefinedCurve (2)", "curveA*HH", "HH", multi(calcExpected("HH", 5.0, 30)),
						indicesOf(makeHH(), makeIndex("curveA", "$", "mmBtu", "(curveB+5)*curveC"), makeIndex("curveB", "$", "mmBtu", "20%curveC"), makeIndex("curveC", "$", "mmBtu", "5")) },

				// TODO fix this test, probably failing because of some overflow error?
				// { "UserDefinedCurve (3)", "curveA*HH", "HH", multi(calcExpected("HH", 5.0, 875)), indicesOf(makeHH(), makeIndex("curveA", "$","mmBtu","curveB*curveC"),
				// makeIndex("curveB", "$","mmBtu","curveC+10"), makeIndex("curveC", "$","mmBtu","25"))},

				{ "UserDefinedCurve (4)", "curveA*HH", "HH", multi(calcExpected("HH", 5.0, 30)), indicesOf(makeHH(), makeIndex("curveA", "$", "mmBtu", "MAX(curveB+10, MIN(curveC, curveD))"), // curveD
																																																// = 21,
																																																// C =
																																																// 194,A
																																																// = 30
						makeIndex("curveB", "$", "mmBtu", "20"), makeIndex("curveC", "$", "mmBtu", "S(curveD,1.0,3.0,2.0,2.0,4.0,7.0,9.0,5.0)"),
						makeIndex("curveD", "$", "mmBtu", "S(2,1.0,10.0,4.0,4.0,9.0,3.0,6.0,2.0)")) },

				/// Complex example with all components - a %, unit conversion and FX curve
				{ "NBP (Units)", "90%NBP*therm_to_mmBtu*FX_p_to_USD", "NBP", single(calcExpected("NBP", 30.0, 0.9 * 10.0, 0.9 * 30.0 / 100.0 * 10.0 * 1.3)),
						indicesOf(makeIndex("NBP", "p", "therm", YearMonth.of(2000, 1), 30)) }, //

				/// Another Complex example
				{ "TTF (Units)", "((TTF-0.4))*mwh_to_mmBtu*FX_EURO_to_USD", "TTF", single(calcExpected("TTF", 12.6, 1.0 * 0.293297, (12.6 - 0.4) * 0.293297 * 1.111)),
						indicesOf(makeIndex("TTF", "EURO", "mwh", YearMonth.of(2000, 1), 12.6)) }, //

				// Reverse factor means detected units are still therms and alt conversion code
				// path happends.
				// { "NBP (Units) Reverse Conversion", "90%NBP*mmBtu_to_therm*FX_p_to_USD",
				// "NBP", single(calcExpected("NBP", 30.0, 0.9 / 10.0, 0.9 * 30.0 / 100.0 / 10.0
				// * 1.3)),
				// indiciesOf(makeIndex("NBP", "p", "therm", YearMonth.of(2000, 1), 30)) }, //

				/// Another Complex example
				// { "TTF (Units) Reverse Conversion ", "((TTF-0.4))
				/// *mmBtu_to_MwH*FX_EURO_to_USD", "TTF", single(calcExpected("TTF", 12.6, 1.0 /
				/// 0.293297, (12.6 - 0.4) / 0.293297 * 1.111)),
				// indiciesOf(makeIndex("TTF", "EURO", "mwh", YearMonth.of(2000, 1), 12.6)) },
				/// //

				// { "HH - Shift -1", "SHIFT(HH,0-1)", "HH", calcExpected(8, 1, 8),
				// indiciesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8))
				// }, //
				{ "HH - Shift 0", "SHIFT(HH,0)", "HH", single(calcExpected("HH", YearMonth.of(2016, 4), 7, 1, 7)), indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //
				{ "HH - Shift 1", "SHIFT(HH,1)", "HH", single(calcExpected("HH", YearMonth.of(2016, 3), 6, 1, 6)), indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //
				{ "HH - Shift 2", "SHIFT(HH,2)", "HH", single(calcExpected("HH", YearMonth.of(2016, 2), 5, 1, 5)), indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //
				{ "Var HH - Shift 2", "#A=HH ; SHIFT(#A,2)", "HH", single(calcExpected("HH", YearMonth.of(2016, 2), 5, 1, 5)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH[m-2]", "HH[m-2]", "HH", single(calcExpected("HH", YearMonth.of(2016, 2), 5, 1, 5)), indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "50% HH - Shift 2", "50%SHIFT(HH,2)", "HH", single(calcExpected("HH", YearMonth.of(2016, 2), 5, 0.5, 0.5 * 5)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Double Shift 1", "SHIFT(SHIFT(HH,1),1)", "HH", single(calcExpected("HH", YearMonth.of(2016, 2), 5, 1, 5)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : %", "50%(SHIFT(HH,1) + SHIFT(HH,2))", "HH", multi(calcExpected("HH", YearMonth.of(2016, 2), 5, 0.5), calcExpected("HH", YearMonth.of(2016, 3), 6, 0.5)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : *", "0.5 * (SHIFT(HH,1) + SHIFT(HH,2))", "HH", multi(calcExpected("HH", YearMonth.of(2016, 2), 5, 0.5), calcExpected("HH", YearMonth.of(2016, 3), 6, 0.5)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : /", "((SHIFT(HH,1) + SHIFT(HH,2))) / 2", "HH", multi(calcExpected("HH", YearMonth.of(2016, 2), 5, 0.5), calcExpected("HH", YearMonth.of(2016, 3), 6, 0.5)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : +", "0.5 + (SHIFT(HH,1) + SHIFT(HH,2))", "HH", multi(calcExpected("HH", YearMonth.of(2016, 2), 5, 1), calcExpected("HH", YearMonth.of(2016, 3), 6, 1)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : -", "0.5 - (SHIFT(HH,1) + SHIFT(HH,2))", "HH", multi(calcExpected("HH", YearMonth.of(2016, 2), 5, -1), calcExpected("HH", YearMonth.of(2016, 3), 6, -1)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : (1)", "SHIFT(HH,1) + (SHIFT(HH,1) + SHIFT(HH,2))", "HH",
						multi(calcExpected("HH", YearMonth.of(2016, 2), 5, 1), calcExpected("HH", YearMonth.of(2016, 3), 6, 2)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : (2)", "SHIFT(HH,1) - (SHIFT(HH,1) + SHIFT(HH,2))", "HH",
						multi(calcExpected("HH", YearMonth.of(2016, 3), 5, 0), calcExpected("HH", YearMonth.of(2016, 2), 5, -1)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

				{ "HH - Complex Shift : (3)", "SHIFT(HH,1) - SHIFT(NBP,1) + SHIFT(HH,2)", "HH",
						multi(calcExpected("NBP", YearMonth.of(2016, 3), 16, -1), calcExpected("HH", YearMonth.of(2016, 2), 5, 1), calcExpected("HH", YearMonth.of(2016, 3), 6, 1)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8), makeIndex("NBP", "$", "mmbtu", YearMonth.of(2016, 2), 15, 16, 17, 18)) }, //

				{ "HH - Max: (1)", "MAX(HH, NBP)", "HH", multi(calcExpected("NBP", YearMonth.of(2016, 4), 17, 1)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 4), 7), makeIndex("NBP", "$", "mmbtu", YearMonth.of(2016, 4), 17)) }, //

				{ "HH - Max: (2)", "MAX(HH, 2*HH2)", "HH", multi(calcExpected("HH2", YearMonth.of(2016, 4), 6, 2, 12)),
						indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 4), 5), makeIndex("HH2", "$", "mmbtu", YearMonth.of(2016, 4), 6)) }, //

				{ "Max (with Units - TTF better) ", "MAX(HH, ((TTF-0.4)) *MWh_to_mmBtu*FX_EURO_to_USD)", "TTF", single(calcExpected("TTF", 12.6, 1.0 * 0.293297, (12.6 - 0.4) * 0.293297 * 1.111)),
						indicesOf(makeIndex("TTF", "EURO", "mwh", YearMonth.of(2000, 1), 12.6), makeIndex("HH", "$", "mmbtu", YearMonth.of(2000, 1), 3.9)) }, //

				{ "Max (with Units - HH better) ", "MAX(HH, ((TTF-0.4)) *MWh_to_mmBtu*FX_EURO_to_USD)", "TTF", single(calcExpected("HH", 4, 1.0, 4.0)),
						indicesOf(makeIndex("TTF", "EURO", "mwh", YearMonth.of(2000, 1), 12.6), makeIndex("HH", "$", "mmbtu", YearMonth.of(2000, 1), 4)) }, //

				{ "Min (with Units - TTF better) ", "MIN(HH, ((TTF-0.4)) *MWh_to_mmBtu*FX_EURO_to_USD)", "TTF", single(calcExpected("TTF", 12.6, 1.0 * 0.293297, (12.6 - 0.4) * 0.293297 * 1.111)),
						indicesOf(makeIndex("TTF", "EURO", "mwh", YearMonth.of(2000, 1), 12.6), makeIndex("HH", "$", "mmbtu", YearMonth.of(2000, 1), 4.0)) }, //

				{ "Min (with Units - HH better) ", "MIN(HH, ((TTF-0.4)) *mmBtu_to_MwH*FX_EURO_to_USD)", "TTF", single(calcExpected("HH", 3.9, 1.0, 3.9)),
						indicesOf(makeIndex("TTF", "EURO", "mwh", YearMonth.of(2000, 1), 12.6), makeIndex("HH", "$", "mmbtu", YearMonth.of(2000, 1), 3.9)) }, //

				{ "DatedAvg 3,0,1 ", "DATEDAVG(Brent,3,0,1)", "Brent", multi(//
						calcExpected("Brent", YearMonth.of(2016, 1), 54.89, 1.0 / 3.0), //
						calcExpected("Brent", YearMonth.of(2016, 2), 55.47, 1.0 / 3.0), //
						calcExpected("Brent", YearMonth.of(2016, 3), 55.76, 1.0 / 3.0)), //
						indicesOf(//
								makeIndex("Brent", "$", "mmbtu", YearMonth.of(2016, 1), 54.89, 55.47, 55.76, 56.01, 56.16, 56.26, 56.23, 56.28, 56.23, 56.17)) }, //

				{ "DatedAvg 6,0,1 ", "DATEDAVG(Brent,6,0,1)", "Brent", multi(//
						calcExpected("Brent", YearMonth.of(2015, 10), 54.89, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2015, 11), 55.47, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2015, 12), 55.76, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2016, 1), 56.01, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2016, 2), 56.16, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2016, 3), 56.26, 1.0 / 6.0) //
				), //
						indicesOf(//
								makeIndex("Brent", "$", "mmbtu", YearMonth.of(2015, 10), 54.89, 55.47, 55.76, 56.01, 56.16, 56.26, 56.23, 56.28, 56.23, 56.17)) }, //
				{ "Brent[6,0,1] ", "Brent[6,0,1]", "Brent", multi(//
						calcExpected("Brent", YearMonth.of(2015, 10), 54.89, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2015, 11), 55.47, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2015, 12), 55.76, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2016, 1), 56.01, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2016, 2), 56.16, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2016, 3), 56.26, 1.0 / 6.0) //
				), //
						indicesOf(//
								makeIndex("Brent", "$", "mmbtu", YearMonth.of(2015, 10), 54.89, 55.47, 55.76, 56.01, 56.16, 56.26, 56.23, 56.28, 56.23, 56.17)) }, //
				{ "Brent[601] ", "Brent[601]", "Brent", multi(//
						calcExpected("Brent", YearMonth.of(2015, 10), 54.89, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2015, 11), 55.47, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2015, 12), 55.76, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2016, 1), 56.01, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2016, 2), 56.16, 1.0 / 6.0), //
						calcExpected("Brent", YearMonth.of(2016, 3), 56.26, 1.0 / 6.0) //
				), //
						indicesOf(//
								makeIndex("Brent", "$", "mmbtu", YearMonth.of(2015, 10), 54.89, 55.47, 55.76, 56.01, 56.16, 56.26, 56.23, 56.28, 56.23, 56.17)) }, //
				{ "DatedAvg 3,1,1 ", "DATEDAVG(Brent,3,1,1)", "Brent", multi(//
						calcExpected("Brent", YearMonth.of(2015, 12), 55.76, 1.0 / 3.0), //
						calcExpected("Brent", YearMonth.of(2016, 1), 56.01, 1.0 / 3.0), //
						calcExpected("Brent", YearMonth.of(2016, 2), 56.16, 1.0 / 3.0) //
				), //
						indicesOf(//
								makeIndex("Brent", "$", "mmbtu", YearMonth.of(2015, 10), 54.89, 55.47, 55.76, 56.01, 56.16, 56.26, 56.23, 56.28, 56.23, 56.17)) }, //

				{ "DatedAvg 3,1,3 ", "DATEDAVG(Brent,3,1,3)", "Brent", multi(//
						calcExpected("Brent", YearMonth.of(2015, 12), 55.76, 1.0 / 3.0), //
						calcExpected("Brent", YearMonth.of(2016, 1), 56.01, 1.0 / 3.0), //
						calcExpected("Brent", YearMonth.of(2016, 2), 56.16, 1.0 / 3.0) //
				), //
						indicesOf(//
								makeIndex("Brent", "$", "mmbtu", YearMonth.of(2015, 10), 54.89, 55.47, 55.76, 56.01, 56.16, 56.26, 56.23, 56.28, 56.23, 56.17)) }, //
				{ "SplitMonth Exposure 1", "SPLITMONTH(HH, Brent, 15)", "HH", single(//
						calcExpected("HH", YearMonth.of(2016, 4), 5.0, 1.0, LocalDate.of(2016, 4, 4)) //
				), //
						indicesOf(//
								makeHH(), makeBrent()) }, //
				{ "SplitMonth Exposure 2", "SPLITMONTH(HH, Brent, 2)", "HH", single(//
						calcExpected("Brent", YearMonth.of(2016, 4), 90, 1.0 * bbl_to_mmBtu, LocalDate.of(2016, 4, 4)) //
				), //
						indicesOf(//
								makeHH(), makeBrent()) }, //

				{ "Currency data - missing", "GAS_NBP_GBP*therm_to_mmBtu/p_to_USD", "GAS_NBP_GBP", //
						single(calcExpected("GAS_NBP_GBP", pricingDate, 5, 10.0, 0.0)), //
						indicesOf( //
								makeIndex("GAS_NBP_GBP", "p", "therm", pricingDate, 5, 6, 7, 8, 9), //
								makeCurrencyIndex("p_to_USD", "p", "", pricingDate.plusMonths(2), 1.4) //
						) //
				}, { "Currency data - present", "GAS_NBP_GBP*therm_to_mmBtu/p_to_USD", "GAS_NBP_GBP", //
						single(calcExpected("GAS_NBP_GBP", pricingDate, 5, 10.0, 5.0 * 10.0 / 1.4)), //
						indicesOf( //
								makeIndex("GAS_NBP_GBP", "p", "therm", pricingDate, 5, 6, 7, 8, 9), //
								makeCurrencyIndex("p_to_USD", "p", "", pricingDate, 1.4) //
						) //
				},

				{ "SCurve - (1)", "S(testCurve1, 10.0, 20.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0)", "testCurve1", multi( //
						calcExpected("testCurve1", YearMonth.of(2016, 4), 9 * 1, 1) //
				), //
					//
						indicesOf( //
								makeIndex("testCurve1", "$", "mmbtu", YearMonth.of(2016, 4), 9) //
						) //
				}, //
				{ "SCurve - (2)", "S(testCurve2, 10.0, 20.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0)", "testCurve2", multi( //
						calcExpected("testCurve2", YearMonth.of(2016, 4), 10, 3) //
				), //
					//
						indicesOf( //
								makeIndex("testCurve2", "$", "mmbtu", YearMonth.of(2016, 4), 10) //
						) //
				}, //
				{ "SCurve - (3)", "S(testCurve3, 10.0, 20.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0)", "testCurve3", multi( //
						calcExpected("testCurve3", YearMonth.of(2016, 4), 20, 3) //
				), //
					//
						indicesOf( //
								makeIndex("testCurve3", "$", "mmbtu", YearMonth.of(2016, 4), 20) //

						) //
				}, //
				{ "SCurve - (4)", "S(testCurve4, 10.0, 20.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0)", "testCurve4", multi( //
						calcExpected("testCurve4", YearMonth.of(2016, 4), 21, 5) //
				), //
					//
						indicesOf( //
								makeIndex("testCurve4", "$", "mmbtu", YearMonth.of(2016, 4), 21) //

						) //
				}, //

				// Specific month pricing. More tests in ExpressionPriceTests
				{ "HH[Feb] ", "HH[Feb]", "HH", single(//
						calcExpected("HH", YearMonth.of(2016, 2), 2, 1.0) //
				), //
						indicesOf(//
								makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 1), 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)) }, //

		});
	}
	//
	// { "HH - Shift 0", "SHIFT(HH,0)", "HH", single(calcExpected("HH", YearMonth.of(2016, 4), 7, 1, 7)), indicesOf(makeIndex("HH", "$", "mmbtu", YearMonth.of(2016, 2), 5, 6, 7, 8)) }, //

	public static Collection generateTestsWithDay() {
		return Arrays.asList(new Object[][] { //

				{ "Nested SplitMonth Exposure (w1)", "SPLITMONTH(SPLITMONTH(WEEK_W1,WEEK_W2,7),SPLITMONTH(WEEK_W3,WEEK_W4,21),14)", "WEEK_W1", 1, single(//
						calcExpected("WEEK_W1", YearMonth.of(2016, 4), 10, 1.0, LocalDate.of(2016, 4, 1)) //
				), //
						indicesOf(//
								makeIndex("WEEK_W1", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 10), makeIndex("WEEK_W2", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 20),
								makeIndex("WEEK_W3", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 30), makeIndex("WEEK_W4", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 40)) }, //

				{ "Nested SplitMonth Exposure (w2)", "SPLITMONTH(SPLITMONTH(WEEK_W1,WEEK_W2,7),SPLITMONTH(WEEK_W3,WEEK_W4,21),14)", "WEEK_W2", 7, single(//
						calcExpected("WEEK_W2", YearMonth.of(2016, 4), 20, 1.0, LocalDate.of(2016, 4, 7)) //
				), //
						indicesOf(//
								makeIndex("WEEK_W1", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 10), makeIndex("WEEK_W2", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 20),
								makeIndex("WEEK_W3", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 30), makeIndex("WEEK_W4", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 40)) }, //

				{ "Nested SplitMonth Exposure (w3)", "SPLITMONTH(SPLITMONTH(WEEK_W1,WEEK_W2,7),SPLITMONTH(WEEK_W3,WEEK_W4,21),14)", "WEEK_W3", 14, single(//
						calcExpected("WEEK_W3", YearMonth.of(2016, 4), 30, 1.0, LocalDate.of(2016, 4, 14)) //
				), //
						indicesOf(//
								makeIndex("WEEK_W1", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 10), makeIndex("WEEK_W2", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 20),
								makeIndex("WEEK_W3", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 30), makeIndex("WEEK_W4", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 40)) }, //

				{ "Nested SplitMonth Exposure (w4)", "SPLITMONTH(SPLITMONTH(WEEK_W1,WEEK_W2,7),SPLITMONTH(WEEK_W3,WEEK_W4,21),14)", "WEEK_W4", 21, single(//
						calcExpected("WEEK_W4", YearMonth.of(2016, 4), 40, 1.0, LocalDate.of(2016, 4, 21)) //
				), //
						indicesOf(//
								makeIndex("WEEK_W1", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 10), makeIndex("WEEK_W2", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 20),
								makeIndex("WEEK_W3", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 30), makeIndex("WEEK_W4", "$", "mmBtu", YearMonth.of(2016, 3), /* 2017 */ 40)) } //
		});
	};

	private static CommodityCurve makeBrent() {
		return makeIndex("Brent", "$", "bbl", YearMonth.of(2000, 1), 90);
	}

	private static CommodityCurve makeHH() {
		return makeIndex("HH", "$", "mmBtu", YearMonth.of(2000, 1), 5);
	}

	private static CommodityCurve makeExpression(String name, String expression) {
		final CommodityCurve index = PricingFactory.eINSTANCE.createCommodityCurve();
		index.setName(name);
		index.setExpression(expression);
		index.setCurrencyUnit("$");
		index.setVolumeUnit("mmBtu");

		return index;
	}

	private static CommodityCurve makeA(final double value) {
		// makeIndex("MultiplicationCurve", "$", "mmBtu", "10")
		return makeIndex("A", "$", "mmBtu", YearMonth.of(2000, 1), value);

	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateTests")
	public void testPriceExpressionExposureCoefficient(final String name, final String expression, final String indexName, final @NonNull ResultChecker checker,
			final AbstractYearMonthCurve[] indicies) {

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();

		for (final AbstractYearMonthCurve idx : indicies) {
			if (idx instanceof CommodityCurve) {
				pricingModel.getCommodityCurves().add((CommodityCurve) idx);
			} else if (idx instanceof CurrencyCurve) {
				pricingModel.getCurrencyCurves().add((CurrencyCurve) idx);
			} else if (idx instanceof CharterCurve) {
				pricingModel.getCharterCurves().add((CharterCurve) idx);
			} else if (idx instanceof BunkerFuelCurve) {
				pricingModel.getBunkerFuelCurves().add((BunkerFuelCurve) idx);
			} else {
				Assertions.fail("Unknown index type");
			}
		}

		makeConversionFactor("therm", "mmbtu", 10, pricingModel);
		makeConversionFactor("bbl", "mmbtu", bbl_to_mmBtu, pricingModel);
		makeConversionFactor("mwh", "mmbtu", 0.293297, pricingModel);

		makeFXCurve("p", "USD", 1.3 / 100.0, pricingModel);
		makeFXCurve("EURO", "USD", 1.111, pricingModel);

		final double volume = defaultVolumeInMMBTU;

		// Sale
		final boolean isPurchase = false;

		final @NonNull PricingDataCache lookupData = PricingDataCache.createLookupData(pricingModel);

		// Creating exposures lookup data
		final ExposuresLookupData exLookupData = new ExposuresLookupData();
		pricingModel.getCommodityCurves()
				.stream()
				.filter(idx -> idx.getName() != null)//
				.forEach(idx -> exLookupData.commodityMap.put(idx.getName().toLowerCase(), new BasicCommodityCurveData(//
						idx.getName().toLowerCase(), idx.getVolumeUnit(), idx.getCurrencyUnit(), idx.getExpression(), idx.getAdjustment())));
		pricingModel.getCurrencyCurves()
				.stream()
				.filter(idx -> idx.getName() != null)//
				.forEach(idx -> exLookupData.currencyList.add(idx.getName().toLowerCase()));
		pricingModel.getConversionFactors()
				.forEach(f -> exLookupData.conversionMap.put(//
						PriceIndexUtils.createConversionFactorName(f).toLowerCase(), new BasicUnitConversionData(f.getFrom(), f.getTo(), f.getFactor())));
		pricingModel.getConversionFactors()
				.forEach(f -> exLookupData.reverseConversionMap.put(//
						PriceIndexUtils.createReverseConversionFactorName(f).toLowerCase(), new BasicUnitConversionData(f.getFrom(), f.getTo(), f.getFactor())));

		final SeriesParser commodityParser = makeCommodityParser(lookupData);
		final SeriesParser currencyParser = makeCurrencyParser(lookupData);
		final Injector injector = createInjector(commodityParser, currencyParser);
		final ExposuresCalculator calc = injector.getInstance(ExposuresCalculator.class);
		final LocalDate pricingLocalDate = LocalDate.of(pricingDate.getYear(), pricingDate.getMonthValue(), pricingDay);
		final long volumeInMMBTU = OptimiserUnitConvertor.convertToInternalVolume(volume);

		int cargoCV = OptimiserUnitConvertor.convertToInternalConversionFactor(22.6);

		final List<BasicExposureRecord> exposureRecords = calc.calculateExposuresForITS(volumeInMMBTU, expression, pricingLocalDate, isPurchase, cargoCV, exLookupData);
		//

		checker.validate(exposureRecords, expression, commodityParser);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateTestsWithDay")
	public void testPriceExpressionNestedSplitMonths(final String name, final String expression, final String indexName, int dayOfMonth, final @NonNull ResultChecker checker,
			final AbstractYearMonthCurve[] indicies) {

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();

		for (final AbstractYearMonthCurve idx : indicies) {
			if (idx instanceof CommodityCurve) {
				pricingModel.getCommodityCurves().add((CommodityCurve) idx);
			} else if (idx instanceof CurrencyCurve) {
				pricingModel.getCurrencyCurves().add((CurrencyCurve) idx);
			} else if (idx instanceof CharterCurve) {
				pricingModel.getCharterCurves().add((CharterCurve) idx);
			} else if (idx instanceof BunkerFuelCurve) {
				pricingModel.getBunkerFuelCurves().add((BunkerFuelCurve) idx);
			} else {
				Assertions.fail("Unknown index type");
			}
		}

		makeConversionFactor("therm", "mmbtu", 10, pricingModel);
		makeConversionFactor("bbl", "mmbtu", 0.180136, pricingModel);
		makeConversionFactor("mwh", "mmbtu", 0.293297, pricingModel);

		makeFXCurve("p", "USD", 1.3 / 100.0, pricingModel);
		makeFXCurve("EURO", "USD", 1.111, pricingModel);

		final double volume = defaultVolumeInMMBTU;

		// Sale
		final boolean isPurchase = false;

		final @NonNull PricingDataCache lookupData = PricingDataCache.createLookupData(pricingModel);

		// Creating exposures lookup data
		final ExposuresLookupData exLookupData = new ExposuresLookupData();
		pricingModel.getCommodityCurves()
				.stream()
				.filter(idx -> idx.getName() != null)//
				.forEach(idx -> exLookupData.commodityMap.put(idx.getName().toLowerCase(), new BasicCommodityCurveData(//
						idx.getName().toLowerCase(), idx.getVolumeUnit(), idx.getCurrencyUnit(), idx.getExpression(), idx.getAdjustment())));
		pricingModel.getCurrencyCurves()
				.stream()
				.filter(idx -> idx.getName() != null)//
				.forEach(idx -> exLookupData.currencyList.add(idx.getName().toLowerCase()));
		pricingModel.getConversionFactors()
				.forEach(f -> exLookupData.conversionMap.put(//
						PriceIndexUtils.createConversionFactorName(f).toLowerCase(), new BasicUnitConversionData(f.getFrom(), f.getTo(), f.getFactor())));
		pricingModel.getConversionFactors()
				.forEach(f -> exLookupData.reverseConversionMap.put(//
						PriceIndexUtils.createReverseConversionFactorName(f).toLowerCase(), new BasicUnitConversionData(f.getFrom(), f.getTo(), f.getFactor())));

		final SeriesParser commodityParser = makeCommodityParser(lookupData);
		final SeriesParser currencyParser = makeCurrencyParser(lookupData);
		final Injector injector = createInjector(commodityParser, currencyParser);
		final ExposuresCalculator calc = injector.getInstance(ExposuresCalculator.class);
		final LocalDate pricingLocalDate = LocalDate.of(pricingDate.getYear(), pricingDate.getMonthValue(), dayOfMonth);
		final long volumeInMMBTU = OptimiserUnitConvertor.convertToInternalVolume(volume);
		int cargoCV = OptimiserUnitConvertor.convertToInternalConversionFactor(22.6);

		final List<BasicExposureRecord> exposureRecords = calc.calculateExposuresForITS(volumeInMMBTU, expression, pricingLocalDate, isPurchase, cargoCV, exLookupData);
		//

		checker.validate(exposureRecords, expression, commodityParser);
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

	private static CommodityCurve makeIndex(final String name, final String currencyUnit, final String volumeUnit, final String expression) {

		final CommodityCurve index = PricingFactory.eINSTANCE.createCommodityCurve();
		index.setName(name);
		index.setExpression(expression);

		index.setCurrencyUnit(currencyUnit);
		index.setVolumeUnit(volumeUnit);

		return index;
	}

	private static CommodityCurve makeIndex(final String name, final String currencyUnit, final String volumeUnit, final YearMonth date, final double value) {

		final YearMonthPoint pt = PricingFactory.eINSTANCE.createYearMonthPoint();
		pt.setDate(date);
		pt.setValue(value);

		final CommodityCurve index = PricingFactory.eINSTANCE.createCommodityCurve();
		index.setName(name);
		index.getPoints().add(pt);

		index.setCurrencyUnit(currencyUnit);
		index.setVolumeUnit(volumeUnit);

		return index;
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

	private static SeriesParser makeCommodityParser(final PricingDataCache lookupData) {
		return lookupData.getSeriesParser(PriceIndexType.COMMODITY);
	}

	private static SeriesParser makeCurrencyParser(final PricingDataCache lookupData) {
		return lookupData.getSeriesParser(PriceIndexType.CURRENCY);
	}

	private static CurrencyCurve makeCurrencyIndex(final String name, final String currencyUnit, final String volumeUnit, final YearMonth startDate, final double... values) {

		final CurrencyCurve index = PricingFactory.eINSTANCE.createCurrencyCurve();
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

	private static AbstractYearMonthCurve[] indicesOf(final AbstractYearMonthCurve... indices) {
		return indices;
	}

	private static class ResultChecker {

		List<ExpectedResult> results;

		public ResultChecker(final ExpectedResult... multiple) {
			results = new LinkedList<>();
			for (final ExpectedResult r : multiple) {
				results.add(r);
			}
		}

		public ResultChecker(final Collection<ExpectedResult> multiple) {
			results = new LinkedList<>(multiple);
		}

		void validate(@Nullable final List<BasicExposureRecord> records, final String expression, final SeriesParser seriesParser) {

			final Map<Pair<String, YearMonth>, ExpectedResult> m = new HashMap<>();
			for (final ExpectedResult r : results) {
				assert r.expectedDate != null;
				m.put(new Pair<>(r.index.toLowerCase(), r.expectedDate), r);
			}

			if (records != null) {
				for (final BasicExposureRecord record : records) {
					final YearMonth month = YearMonth.from(record.getTime());
					final ExpectedResult r = m.remove(new Pair<>(record.getIndexName().toLowerCase(), month));
					Assertions.assertNotNull(r);
					r.validate(record, expression, seriesParser);
				}
			}
			Assertions.assertTrue(m.isEmpty());
		}
	}

	private static class ExpectedResult {
		private String index;
		private YearMonth expectedDate = pricingDate;
		private LocalDate pricingDateOverride = null;
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

		public ExpectedResult(String index, final YearMonth date, final double expectedVolume, final double expectedValue, LocalDate pricingDate) {
			this.index = index;
			expectedDate = date;
			this.expectExposed = true;
			this.expectedVolume = OptionalDouble.of(expectedVolume);
			this.expectedValue = OptionalDouble.of(expectedValue);
			this.expectedExpressionValue = OptionalDouble.empty();
			this.pricingDateOverride = pricingDate;
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

		void validate(@Nullable final BasicExposureRecord record, final String expression, final SeriesParser seriesParser) {
			if (!expectExposed) {
				Assertions.assertTrue(record == null);
				return;
			}

			YearMonth month = YearMonth.from(record.getTime());

			Assertions.assertEquals(expectedDate, month);

			Assertions.assertNotNull(record);
			if (expectedVolume.isPresent()) {
				final double volumeNative = ((double) record.getVolumeNative()) / Calculator.ScaleFactor;
				Assertions.assertEquals(expectedVolume.getAsDouble(), volumeNative, delta, "Volume");
			}
			if (expectedValue.isPresent()) {
				final double volumeValueNative = ((double) record.getVolumeValueNative()) / Calculator.ScaleFactor;
				Assertions.assertEquals(expectedValue.getAsDouble(), volumeValueNative, delta, "Value");
			}
			if (expectedExpressionValue.isPresent()) {
				final ISeries series = seriesParser.asSeries(expression);

				Number evaluate;
				// "Magic" date constant used in PriceIndexUtils for date zero
				if (pricingDateOverride != null) {
					evaluate = series.evaluate(Hours.between(PriceIndexUtils.dateTimeZero.toLocalDate(), pricingDateOverride), Collections.emptyMap());
				} else {
					evaluate = series.evaluate(Hours.between(PriceIndexUtils.dateZero, pricingDate), Collections.emptyMap());
				}

				final double val = evaluate.doubleValue() * defaultVolumeInMMBTU;
				Assertions.assertEquals(expectedExpressionValue.getAsDouble(), val, delta, "Expr Value");
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

	private static ExpectedResult calcExpected(String index, final YearMonth date, final double unitPrice, final double factor, final LocalDate pricingDate) {
		return new ExpectedResult(index, date, defaultVolumeInMMBTU * factor, defaultVolumeInMMBTU * factor * unitPrice, pricingDate);
	}

	private static ExpectedResult calcExpected(String index, final YearMonth date, final double unitPrice, final double factor, final double expressionUnitPrice) {
		return new ExpectedResult(index, date, defaultVolumeInMMBTU * factor, defaultVolumeInMMBTU * factor * unitPrice, defaultVolumeInMMBTU * expressionUnitPrice);
	}

	private @NonNull Injector createInjector(final SeriesParser commodityParser, final SeriesParser currencyParser) {
		return Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ExposuresCalculator.class);

				bind(TimeZoneToUtcOffsetProvider.class).in(Singleton.class);
				bind(ITimeZoneToUtcOffsetProvider.class).to(TimeZoneToUtcOffsetProvider.class);

				bind(DefaultExposureDataProvider.class).in(Singleton.class);
				bind(IExposureDataProvider.class).to(DefaultExposureDataProvider.class);
				bind(IExposureDataProviderEditor.class).to(DefaultExposureDataProvider.class);

				bind(SeriesParser.class).annotatedWith(Names.named(SchedulerConstants.Parser_Commodity)).toInstance(commodityParser);
				bind(SeriesParser.class).annotatedWith(Names.named(SchedulerConstants.Parser_Currency)).toInstance(currencyParser);

				bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.COMPUTE_EXPOSURES)).toInstance(Boolean.TRUE);
				bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.EXPOSURES_CUTOFF_AT_PROMPT_START)).toInstance(Boolean.TRUE);
				bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.INDIVIDUAL_EXPOSURES)).toInstance(Boolean.FALSE);
			}

			@Provides
			public IExternalDateProvider getIExternalDateProvider() {
				return new SimpleExternalDateProvider();
			}

		});
	}

	private class SimpleExternalDateProvider implements IExternalDateProvider {

		final LocalDateTime dateZero = LocalDate.of(2000, 1, 1).atStartOfDay();

		@SuppressWarnings("null")
		@Override
		public @NonNull ZonedDateTime getDateFromHours(int hours, String tz) {
			String timeZone = tz;
			if (timeZone == null) {
				timeZone = "UTC";
			}
			final LocalDateTime dateTime = dateZero.plusHours(hours);
			return ZonedDateTime.of(dateTime, ZoneId.of(timeZone));
		}

		@Override
		public @NonNull ZonedDateTime getDateFromHours(int hours, @Nullable IPort port) {
			if (port == null) {
				return getDateFromHours(hours, "UTC");
			}
			return getDateFromHours(hours, port.getTimeZoneId());
		}

		@Override
		public @NonNull ZonedDateTime getEarliestTime() {
			return ZonedDateTime.of(dateZero, ZoneId.of("UTC"));
		}

		@Override
		public @NonNull ZonedDateTime getLatestTime() {
			return ZonedDateTime.now();
		}
	}
}
