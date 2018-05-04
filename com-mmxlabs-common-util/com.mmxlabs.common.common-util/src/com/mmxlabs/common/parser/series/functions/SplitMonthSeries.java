/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.ISeries;

public class SplitMonthSeries implements ISeries {
	private @NonNull CalendarMonthMapper mapper;
	private ISeries series1;
	private ISeries series2;
	private int splitPoint;

	public SplitMonthSeries(final ISeries series1, final ISeries series2, final int splitPoint, @NonNull CalendarMonthMapper mapper) {
		this.series1 = series1;
		this.series2 = series2;
		this.splitPoint = splitPoint;
		this.mapper = mapper;
	}
	
	private int daysToPoint(int days) {
		// A point unit is currently an hour
		int pointEquivalenceFactor = 24;
		
		return days * pointEquivalenceFactor;
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
	    	
		int currentMonth = mapper.mapChangePointToMonth(point);
		
		// Should be the start of the month in 'unit' (hour)  
		int monthStartPoint = mapper.mapMonthToChangePoint(currentMonth);
		
		// Convert the 'days' to the point in the month
		int actualSplitPoint = monthStartPoint + daysToPoint(splitPoint);
		
		//Use first curve
		if (point < actualSplitPoint) {
			value = series1.evaluate(point);
		} else {
			// Use second curve
			value = series2.evaluate(point);
		}
		
		return value;
	}
}
