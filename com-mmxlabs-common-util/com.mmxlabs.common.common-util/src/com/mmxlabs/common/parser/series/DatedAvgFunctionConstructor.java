/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.DatedAverageSeries;

public class DatedAvgFunctionConstructor implements IExpression<ISeries> {

	private final CalendarMonthMapper calendarMonthMapper;
	private final IExpression<ISeries> series;
	private final int months;
	private final int lag;
	private final int reset;

	public DatedAvgFunctionConstructor(final SeriesParserData seriesParserData, final List<IExpression<ISeries>> arguments) {
		this.calendarMonthMapper = seriesParserData.calendarMonthMapper;
		if (calendarMonthMapper == null) {
			throw new IllegalStateException("No calender mapper function defined");
		}
		this.series = arguments.get(0);
		this.months = arguments.get(1).evaluate().evaluate(0).intValue();
		this.lag = arguments.get(2).evaluate().evaluate(0).intValue();
		this.reset = arguments.get(3).evaluate().evaluate(0).intValue();
	}

	public DatedAvgFunctionConstructor(final SeriesParserData seriesParserData, IExpression<ISeries> series, final Integer months, final Integer lag, final Integer reset) {
		this.calendarMonthMapper = seriesParserData.calendarMonthMapper;
		if (calendarMonthMapper == null) {
			throw new IllegalStateException("No calender mapper function defined");
		}
		this.series = series;
		this.months = months.intValue();
		this.lag = lag.intValue();
		this.reset = reset.intValue();
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new DatedAverageSeries(series.evaluate(), months, lag, reset, calendarMonthMapper);
	}
}
