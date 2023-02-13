/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class SeriesParserTest {

	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { //
				{ "-1", -1.0 }, //
				{ "+1", 1.0 }, //
				{ "5-1", 4.0 }, //
				{ "(5)", 5.0 }, //
				{ "(5.0)", 5.0 }, //
				{ "(5.0)-(1.0)", 4.0 }, //
				{ "((5.0)-(1.0))", 4.0 }, //
				{ "(((5.0)-(1.0)))", 4.0 }, //
				{ "MAX(1,MAX(2,3))", 3.0 }, //
				{ "MIN(1,MAX(2,3))", 1.0 }, //
				{ "36.5*1.304/10-0.2", 4.5 }, // Operator precedence test
				{ "HH", 1.0 }, //
				{ "10%HH", 0.1 }, //
				{ "10.0%HH", 0.1 }, //
				{ "(HH)", 1.0 }, //
				{ "(10%HH)", 0.1 }, //
				// { "((10)%HH)", 0.1 }, // Illegal syntax, cannot have () before %
				{ "(10.0%HH)", 0.1 }, //
				{ "10%(HH)", 0.1 }, //
				{ "(((10.0)-(3.0))/(1.0%(HH)))", 700.0 }, //

				{ "SHIFT(HH,-4)", 1.0 }, //
				{ "SHIFT(HH,4)", 1.0 }, //

				{ "DATEDAVG(HH,1,2,3)", 1.0 }, //

				// Testing the lowercase keyword
				{ "max(1,max(2,3))", 3.0 }, //
				{ "min(1,max(2,3))", 1.0 }, //
				{ "shift(HH,-4)", 1.0 }, //
				{ "shift(HH,4)", 1.0 }, //

				{ "datedavg(HH,1,2,3)", 1.0 }, //

				// Testing mixed case
				{ "Max(1,mAX(2,3))", 3.0 }, //
				{ "miN(1,MAX(2,3))", 1.0 }, //
				{ "shIft(HH,-4)", 1.0 }, //
				{ "ShIfT(HH,4)", 1.0 }, //

				{ "datedAVG(HH,1,2,3)", 1.0 }, //

				// Testing split month function
				{ "splitmonth(HH,HH2, 15)", 1.0 }, //
				// S-Curve
				{ "s(9, 10.0, 20.0, 1.0, 2.0, 3.0,4.0,5.0,6.0)", 9.0 * 1.0 + 2.0 }, //
				{ "s(10, 10.0, 20.0, 1.0, 2.0, 3.0,4.0,5.0,6.0)", 10.0 * 3.0 + 4.0 }, //
				{ "s(20, 10.0, 20.0, 1.0, 2.0, 3.0,4.0,5.0,6.0)", 20.0 * 3.0 + 4.0 }, //
				{ "s(21, 10.0, 20.0, 1.0, 2.0, 3.0,4.0,5.0,6.0)", 21.0 * 5.0 + 6.0 }, //
		});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateTests")
	public void run(String expression, double expected) {
		Assertions.assertEquals(expected, parse(expression), 0.1);
	}

	double parse(String expression) {

		SeriesParserData data = new SeriesParserData();
		data.setShiftMapper((a, b) -> a);
		data.setCalendarMonthMapper(new CalendarMonthMapper() {

			@Override
			public int mapTimePoint(int point, UnaryOperator<LocalDateTime> mapFunction) {
				return point;
			}

			@Override
			public int mapMonthToChangePoint(int currentChangePoint) {
				return currentChangePoint;
			}

			@Override
			public int mapChangePointToMonth(int currentChangePoint) {
				return currentChangePoint;
			}
		});

		SeriesParser parser = new SeriesParser(data);
		parser.addSeriesExpression("HH", SeriesType.COMMODITY, "1.0");
		parser.addSeriesExpression("HH2", SeriesType.COMMODITY, "2.0");
		parser.addSeriesExpression("HH3", SeriesType.COMMODITY, "3.0");
		parser.addSeriesExpression("HH4", SeriesType.COMMODITY, "4.0");

		final ISeries series = parser.asSeries(expression);
		return series.evaluate(0, Collections.emptyMap()).doubleValue();
	}
}
