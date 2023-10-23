/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.UntilSeries;

public class UntilFunctionConstructor implements IExpression<ISeries> {

	private final CalendarMonthMapper calendarMonthMapper;
	private final IExpression<ISeries> lhsSeries;
	private final IExpression<ISeries> rhsSeries;
	private final LocalDateTime threshold;
	private final ZonedDateTime firstDate;

	public CalendarMonthMapper getCalendarMonthMapper() {
		return calendarMonthMapper;
	}

	public UntilFunctionConstructor(final SeriesParserData seriesParserData, IExpression<ISeries> lhsSeries, LocalDateTime threshold, IExpression<ISeries> rhsSeries) {
		this.calendarMonthMapper = seriesParserData.calendarMonthMapper;
		firstDate = seriesParserData.earliestAndLatestTime.getFirst();
		if (calendarMonthMapper == null) {
			throw new IllegalStateException("No calender mapper function defined");
		}
		this.lhsSeries = lhsSeries;
		this.threshold = threshold;
		this.rhsSeries = rhsSeries;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new UntilSeries(firstDate, lhsSeries.evaluate(), threshold, rhsSeries.evaluate(), calendarMonthMapper);
	}

	@Override
	public boolean canEvaluate() {
		return lhsSeries.canEvaluate() && rhsSeries.canEvaluate();

	}
}
