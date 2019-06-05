/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.ISeries;



public class SplitMonthSeries implements ISeries {
	protected static final int[] NONE = new int[0];

	private @NonNull CalendarMonthMapper mapper;
	private ISeries series1;
	private ISeries series2;
	private int splitPoint;
	private Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime;
	
	public SplitMonthSeries(final ISeries series1, final ISeries series2, final int splitPoint, //
						    @NonNull CalendarMonthMapper mapper, Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime) {
		this.series1 = series1;
		this.series2 = series2;
		
		this.splitPoint = splitPoint;
		this.mapper = mapper;
		this.earliestAndLatestTime = earliestAndLatestTime;
	}

	private int daysToPoint(int days) {
		// A point unit is currently an hour
		int pointEquivalenceFactor = 24;

		return days * pointEquivalenceFactor;
	}
	
	/*
	private int compareNumbers(Number a, Number b) {
		return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
	}
	
	private int getSeriesDurationInMonth(ISeries series) {
		return 0;
	}
	*/
	
	@Override
	public int[] getChangePoints() {
		int ic = -1;
		
		if (earliestAndLatestTime == null) {
			return NONE;
		}

int ii = 0;
		int startMonth = mapper.mapChangePointToMonth(0);
		int 	endMonth = 2 + startMonth + ((int) ChronoUnit.MONTHS.between(earliestAndLatestTime.getFirst(), earliestAndLatestTime.getSecond()));
		
		if (series1.getChangePoints().length != 0 && series2.getChangePoints().length != 0) {
			int startTime = Math.min(series1.getChangePoints()[0],
				 series2.getChangePoints()[0]);
		
//			int endTime = Math.max(series1.getChangePoints()[series1.getChangePoints().length - 1],
//				 series2.getChangePoints()[series2.getChangePoints().length - 1] + daysToPoint(splitPoint - 1));

			startMonth = mapper.mapChangePointToMonth(startTime);
//			endMonth = mapper.mapChangePointToMonth(endTime) + 1;
		}


		//long seriesDurationInMonth = ChronoUnit.MONTHS.between(earliestAndLatestTime.getFirst(), earliestAndLatestTime.getSecond());
		int changePoints[] = new int[(endMonth - startMonth) * 2];
		
		for (int i = startMonth; i < endMonth; i++) {
			// Should be the start of the month in 'unit' (hour)
			int monthStartPoint = mapper.mapMonthToChangePoint(i);

			changePoints[++ic] = monthStartPoint;
			changePoints[++ic] = monthStartPoint + daysToPoint(splitPoint - 1);
		}

		return changePoints;
	}

	@Override
	public Number evaluate(int point) {
		Number value = null;

		int currentMonth = mapper.mapChangePointToMonth(point);

		// Should be the start of the month in 'unit' (hour)
		int monthStartPoint = mapper.mapMonthToChangePoint(currentMonth);

		// Convert the 'days' to the point in the month
		int actualSplitPoint = monthStartPoint + daysToPoint(splitPoint - 1);

		// Use first curve
		if (point < actualSplitPoint) {
			value = series1.evaluate(point);
		} else {
			// Use second curve
			value = series2.evaluate(point);
		}

		return value;
	}
}
