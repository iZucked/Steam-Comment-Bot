/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.ISeries;

public class SplitMonthSeries implements ISeries {
	private ISeries series1;
	private ISeries series2;
	private int splitPoint;

	public SplitMonthSeries(final ISeries series1, final ISeries series2, final int splitPoint) {
		this.series1 = series1;
		this.series2 = series2;
		this.splitPoint = splitPoint;
	}

	@Override
	public int[] getChangePoints() {
		// TODO: Add the logic !!!
		// !!!!!!!!!!!!!!!!!!!!!!!
		
		/*
		return shiftee.getChangePoints();
		 */
		return new int[0];
	}

	@Override
	public Number evaluate(int point) {
		// TODO: Add the logic !!!
		// !!!!!!!!!!!!!!!!!!!!!!!
		
		/*
		double sum = 0;
		int resetDelta = (currentMonth ) % reset;
		int startMonth = currentMonth//
				- months //
				- resetDelta //
				- lag;
		for (int i = 0; i < months; ++i) {
			int m = startMonth + i;
			int time = mapper.mapMonthToChangePoint(m);
			double v = shiftee.evaluate(time).doubleValue();
			if (v == 0.0) {
				// No data, cannot create average, return 0
				return 0.0;
			}
			sum += v;
		}
		sum /= (double) months;
		return Double.valueOf(sum);
		 */
		return Double.valueOf(0);
	}
}
