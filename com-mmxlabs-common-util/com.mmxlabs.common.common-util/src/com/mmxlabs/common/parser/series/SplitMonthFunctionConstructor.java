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
import com.mmxlabs.common.parser.series.functions.SplitMonthSeries;

public class SplitMonthFunctionConstructor implements IExpression<ISeries> {

	private final CalendarMonthMapper calendarMonthMapper;
	private final IExpression<ISeries> series1;
	private final IExpression<ISeries> series2;
	private final int splitPoint;

	public SplitMonthFunctionConstructor(final CalendarMonthMapper calendarMonthMapper, final List<IExpression<ISeries>> arguments) {
		if (calendarMonthMapper == null) {
			throw new IllegalStateException("No calendar mapper function defined");
		}
		
		this.series1 = arguments.get(0);
		this.series2 = arguments.get(1);
		this.splitPoint = arguments.get(2).evaluate().evaluate(0).intValue();
		this.calendarMonthMapper = calendarMonthMapper;
	}

	public SplitMonthFunctionConstructor(final CalendarMonthMapper calendarMonthMapper, IExpression<ISeries> series1, IExpression<ISeries> series2, final Integer splitPoint) {
		if (calendarMonthMapper == null) {
			throw new IllegalStateException("No calendar mapper function defined");
		}
		
		this.series1 = series1;
		this.series2 = series2;
		this.splitPoint = splitPoint.intValue();
		this.calendarMonthMapper = calendarMonthMapper;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new SplitMonthSeries(series1.evaluate(), series2.evaluate(), splitPoint, calendarMonthMapper, null);
	}
	
	@Override
	public @NonNull ISeries evaluate(Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime) {
		return new SplitMonthSeries(series1.evaluate(earliestAndLatestTime), series2.evaluate(earliestAndLatestTime), splitPoint, calendarMonthMapper, earliestAndLatestTime);
	}
}
