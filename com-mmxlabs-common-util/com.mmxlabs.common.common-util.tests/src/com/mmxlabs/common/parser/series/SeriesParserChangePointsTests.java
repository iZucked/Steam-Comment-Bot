/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.common.Pair;

public class SeriesParserChangePointsTests {

	public static Iterable<Object[]> generateTests() {

		// Testing split month function (First day of month is zero not 1, hence -1)

		List<Integer> expectedChangePoints = new LinkedList<>();
		for (int i = 0; i < 4; ++i) {
			expectedChangePoints.add(i * 30 * 24); // 1st of month
			expectedChangePoints.add(((i * 30) + (15 - 1)) * 24); // 15th of month
		}

		return Arrays.asList(new Object[][] { //
				{ "splitmonth(HH,HH2, 15)", expectedChangePoints } //
		});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateTests")
	public void run(String expression, List<Integer> expected) {
		Assertions.assertEquals(expected, parse(expression));
	}

	List<Integer> parse(final String expression) {
		final SeriesParserData data = new SeriesParserData();

		data.setShiftMapper((a, b) -> a);
		// Assume 30 day month
		data.setCalendarMonthMapper(new CalendarMonthMapper() {

			@Override
			public int mapTimePoint(int point, UnaryOperator<LocalDateTime> mapFunction) {
				return point;
			}

			@Override
			public int mapMonthToChangePoint(final int currentChangePoint) {
				return currentChangePoint * 30 * 24;
			}

			@Override
			public int mapChangePointToMonth(final int currentChangePoint) {
				return currentChangePoint / 30 / 24;
			}
		});

		final ZonedDateTime start = ZonedDateTime.of(LocalDateTime.of(2018, 1, 1, 0, 0), ZoneOffset.UTC);
		final ZonedDateTime end = ZonedDateTime.of(LocalDateTime.of(2018, 3, 1, 0, 0), ZoneOffset.UTC);
		final Pair<ZonedDateTime, ZonedDateTime> timebox = new Pair<>(start, end);
		data.setEarliestAndLatestTime(timebox);

		final SeriesParser parser = new SeriesParser(data);
		parser.addSeriesExpression("HH", SeriesType.COMMODITY, "1.0");
		parser.addSeriesExpression("HH2", SeriesType.COMMODITY, "2.0");

		final ISeries series = parser.asSeries(expression);
		final int[] changePoints = series.getChangePoints();

		return Arrays.stream(changePoints).boxed().collect(Collectors.toList());
	}
}
