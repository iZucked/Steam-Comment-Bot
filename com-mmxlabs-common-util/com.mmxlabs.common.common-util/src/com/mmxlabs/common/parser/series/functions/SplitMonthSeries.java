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
		
		return series1.getChangePoints();
	}

	@Override
	public Number evaluate(int point) {
		Number value = null;
		
		//Use first curve
		if (point < splitPoint) {
			value = series1.evaluate(point);
		} else {
			// Use second curve
			value = series2.evaluate(point);
		}
		
		return value;
	}
}
