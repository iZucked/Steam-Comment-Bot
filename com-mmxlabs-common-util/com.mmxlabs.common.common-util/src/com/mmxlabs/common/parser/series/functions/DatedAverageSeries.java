/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.ISeries;

public class DatedAverageSeries implements ISeries {
	private ISeries shiftee;
	private @NonNull CalendarMonthMapper mapper;
	private int months;
	private int lag;
	private int reset;

	public DatedAverageSeries(final ISeries shiftee, int months, int lag, int reset, @NonNull CalendarMonthMapper mapper) {
		this.shiftee = shiftee;
		this.months = months;
		this.lag = lag;
		this.reset = reset;
		this.mapper = mapper;
	}

	@Override
	public boolean isParameterised() {
		return shiftee.isParameterised();
	}

	@Override
	public int[] getChangePoints() {
		return shiftee.getChangePoints();
	}

	@Override
	public Set<String> getParameters() {
		return shiftee.getParameters();
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {

		int currentMonth = mapper.mapChangePointToMonth(timePoint);

		double sum = 0;
		int resetDelta = (currentMonth) % reset;
		int startMonth = currentMonth//
				- months //
				- resetDelta //
				- lag;
		for (int i = 0; i < months; ++i) {
			int m = startMonth + i;
			int time = mapper.mapMonthToChangePoint(m);
			double v = shiftee.evaluate(time, params).doubleValue();
			if (v == 0.0) {
				// No data, cannot create average, return 0
				return 0.0;
			}
			sum += v;
		}
		sum /= (double) months;

		return Double.valueOf(sum);
	}
}
