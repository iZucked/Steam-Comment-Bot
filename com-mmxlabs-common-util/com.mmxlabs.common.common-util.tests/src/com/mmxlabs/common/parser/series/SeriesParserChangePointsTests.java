/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.IExpression;

public class SeriesParserChangePointsTests {

	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { //
				// Testing split month function (First day of month is zero not 1, hence -1)
				{ "splitmonth(HH,HH2, 15)", new ArrayList<>(Arrays.asList(0, (15 - 1) * 24, 1, ((15 - 1) * 24) + 1)) } //
		});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateTests")
	public void run(String expression, List<Integer> expected) {
		Assertions.assertTrue(compare(parse(expression), expected));
	}

	private boolean compare(final List<Integer> a, final List<Integer> b) {
		if (a.size() != b.size()) {
			return false;
		}

		for (int i = 0; i < a.size(); i++) {
			if (!a.get(i).equals(b.get(i))) {
				return false;
			}
		}

		return true;
	}
 

	List<Integer> parse(final String expression) {
		final SeriesParser parser = new SeriesParser();
		parser.addSeriesExpression("HH", "1.0");
		parser.addSeriesExpression("HH2", "2.0");

		parser.setShiftMapper((a, b) -> a);
		parser.setCalendarMonthMapper(new CalendarMonthMapper() {

			@Override
			public int mapMonthToChangePoint(final int currentChangePoint) {
				return currentChangePoint;
			}

			@Override
			public int mapChangePointToMonth(final int currentChangePoint) {
				return currentChangePoint;
			}
		});

		final IExpression<ISeries> parsed = parser.parse(expression);
		final ZonedDateTime start = ZonedDateTime.of(LocalDateTime.of(2018, 1, 1, 0, 0), ZoneOffset.UTC);
		final ZonedDateTime end = ZonedDateTime.of(LocalDateTime.of(2018, 3, 1, 0, 0), ZoneOffset.UTC);
		final Pair<ZonedDateTime, ZonedDateTime> timebox = new Pair<>(start, end);

		final int[] changePoints = parsed.evaluate(timebox).getChangePoints();

		return Arrays.stream(changePoints).boxed().collect(Collectors.toList());
	}
}
