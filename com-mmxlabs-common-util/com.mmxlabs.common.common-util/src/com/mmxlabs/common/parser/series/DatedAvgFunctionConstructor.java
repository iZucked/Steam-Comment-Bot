/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.DatedAverageSeries;

public class DatedAvgFunctionConstructor implements IExpression<ISeries> {

	private final CalendarMonthMapper calendarMonthMapper;
	private final IExpression<ISeries> series;
	private final int months;
	private final int lag;

	private final int reset;

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

	public int getMonths() {
		return months;
	}

	public int getLag() {
		return lag;
	}

	public int getReset() {
		return reset;
	}

	@Override
	public boolean canEvaluate() {
		return series.canEvaluate();

	}
}
