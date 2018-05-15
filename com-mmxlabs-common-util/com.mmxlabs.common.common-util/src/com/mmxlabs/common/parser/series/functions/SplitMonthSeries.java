/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;



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
		int period = 15;
		int ic = -1;
		
		if (earliestAndLatestTime == null) {
			return NONE;
		}

		long seriesDurationInMonth = ChronoUnit.MONTHS.between(earliestAndLatestTime.getFirst(), earliestAndLatestTime.getSecond());
		int changePoints[] = new int[(int) seriesDurationInMonth * 2];

		for (int i = 0; i < seriesDurationInMonth; i++) {
			// Should be the start of the month in 'unit' (hour)
			int monthStartPoint = mapper.mapMonthToChangePoint(i);

			changePoints[++ic] = monthStartPoint;
			changePoints[++ic] = monthStartPoint + daysToPoint(period);
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
		int actualSplitPoint = monthStartPoint + daysToPoint(splitPoint);

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
