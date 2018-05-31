/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.IExpression;

@RunWith(value = Parameterized.class)
public class SeriesParserChangePoints {
	@Parameters(name = "{0}")
	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { //
				// Testing split month function
				{ "splitmonth(HH,HH2, 15)", new ArrayList<Integer>(Arrays.asList(0, 15 * 24, 1, (15 * 24) + 1))} //
		});
	}

	private String expression;
	private List<Integer> expected;

	public SeriesParserChangePoints(String expression, List<Integer> expected) {
		this.expression = expression;
		this.expected = expected;

	}
	
	private boolean compare(List<Integer> a, List<Integer> b) {
		if (a.size() != b.size()) {
			return false;
		}
		
		for(int i = 0; i < a.size(); i++) {
			if (!a.get(i).equals(b.get(i))) {
				return false;
			}
		}

		return true;
	}

	@Test
	public void run() {
		Assert.assertTrue(compare(parse(expression), expected));
	}

	List<Integer> parse(String expression) {
		SeriesParser parser = new SeriesParser();
		parser.addSeriesExpression("HH", "1.0");
		parser.addSeriesExpression("HH2", "2.0");

		parser.setShiftMapper((a, b) -> a);
		parser.setCalendarMonthMapper(new CalendarMonthMapper() {

			@Override
			public int mapMonthToChangePoint(int currentChangePoint) {
				return currentChangePoint;
			}

			@Override
			public int mapChangePointToMonth(int currentChangePoint) {
				return currentChangePoint;
			}
		});

		IExpression<ISeries> parsed = parser.parse(expression);
		ZonedDateTime start = ZonedDateTime.of(LocalDateTime.of(2018, 1, 1, 0, 0), ZoneOffset.UTC);
		ZonedDateTime end = ZonedDateTime.of(LocalDateTime.of(2018, 3, 1, 0, 0), ZoneOffset.UTC);
		Pair<ZonedDateTime, ZonedDateTime> timebox = new Pair<ZonedDateTime, ZonedDateTime>(start, end);
		
		int[] changePoints = parsed.evaluate(timebox).getChangePoints();
		
		return Arrays.stream(changePoints).boxed().collect(Collectors.toList());  
	}
}
