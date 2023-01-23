/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.VolumeTierSeries;

public class VolumeTierM3FunctionConstructor implements IExpression<ISeries> {

	private final CalendarMonthMapper calendarMonthMapper;
	private final IExpression<ISeries> tier1Series;
	private final IExpression<ISeries> tier2Series;
	private final double threshold;

	public VolumeTierM3FunctionConstructor(final SeriesParserData seriesParserData, IExpression<ISeries> tier1Series, final Number threshold, IExpression<ISeries> tier2Series) {
		this.calendarMonthMapper = seriesParserData.calendarMonthMapper;
		if (calendarMonthMapper == null) {
			throw new IllegalStateException("No calender mapper function defined");
		}
		this.tier1Series = tier1Series;
		this.tier2Series = tier2Series;
		this.threshold = threshold.doubleValue();
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new VolumeTierSeries(true, tier1Series.evaluate(), threshold, tier2Series.evaluate());
	}

	@Override
	public boolean canEvaluate() {
		return tier1Series.canEvaluate() && tier2Series.canEvaluate();
	}
}
