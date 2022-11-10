/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.time.Month;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.DatedAverageSeries;
import com.mmxlabs.common.parser.series.functions.MonthSeries;

public class MonthFunctionConstructor implements IExpression<ISeries> {

	private final CalendarMonthMapper calendarMonthMapper;
	private final IExpression<ISeries> series;
	private final Month month;

	public CalendarMonthMapper getCalendarMonthMapper() {
		return calendarMonthMapper;
	}

	public IExpression<ISeries> getSeries() {
		return series;
	}

	public MonthFunctionConstructor(final SeriesParserData seriesParserData, IExpression<ISeries> series, Month month) {
		this.month = month;
		this.calendarMonthMapper = seriesParserData.calendarMonthMapper;
		if (calendarMonthMapper == null) {
			throw new IllegalStateException("No calender mapper function defined");
		}
		this.series = series;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new MonthSeries(series.evaluate(), month, calendarMonthMapper);
	}

	@Override
	public boolean canEvaluate() {
		return series.canEvaluate();

	}
}
