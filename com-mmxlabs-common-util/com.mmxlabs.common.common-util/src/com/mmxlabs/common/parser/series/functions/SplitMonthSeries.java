/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParserData;

public class SplitMonthSeries implements ISeries {

	protected static final int[] NONE = new int[0];

	// A point unit is currently an hour
	private static final int pointEquivalenceFactor = 24;

	private final @NonNull CalendarMonthMapper mapper;
	private final ISeries series1;
	private final ISeries series2;
	private final int splitPoint;
	private final Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime;

	private final Set<String> parameters;

	private int[] changePoints;

	public SplitMonthSeries(@NonNull final SeriesParserData seriesParserData, final ISeries series1, final ISeries series2, final int splitPoint) {
		this.series1 = series1;
		this.series2 = series2;

		this.splitPoint = splitPoint;
		this.mapper = seriesParserData.calendarMonthMapper;
		this.earliestAndLatestTime = seriesParserData.earliestAndLatestTime;

		parameters = new HashSet<>();
		parameters.addAll(series1.getParameters());
		parameters.addAll(series2.getParameters());
		
		if (earliestAndLatestTime == null) {
			this.changePoints = NONE;
		} else {

			int startMonth = mapper.mapChangePointToMonth(0);
			// Add some extra months past the latest date to catch stragglers (i.e.
			// lateness).
			final int endMonth = 2 + startMonth + ((int) ChronoUnit.MONTHS.between(earliestAndLatestTime.getFirst(), earliestAndLatestTime.getSecond()));

			// Find the earliest date in the source date
			if (series1.getChangePoints().length != 0 && series2.getChangePoints().length != 0) {
				final int startTime = Math.min(series1.getChangePoints()[0], series2.getChangePoints()[0]);
				startMonth = mapper.mapChangePointToMonth(startTime);
			}

			// Merge in the change points from the source arrays....
			// Use a set to remove duplicates, we will sort it later
			final Set<Integer> changePointsSet = new HashSet<>();
			for (final int p : series1.getChangePoints()) {
				changePointsSet.add(p);
			}
			for (final int p : series2.getChangePoints()) {
				changePointsSet.add(p);
			}
			// ... and and in the changes points based on the month split
			for (int i = startMonth; i < endMonth; i++) {
				// Should be the start of the month in 'unit' (hour)
				final int monthStartPoint = mapper.mapMonthToChangePoint(i);

				changePointsSet.add(monthStartPoint);
				changePointsSet.add(monthStartPoint + daysToPoint(splitPoint - 1));
			}

			changePoints = CollectionsUtil.integersToIntArray(changePointsSet);
			Arrays.sort(changePoints);
		}
	}

	private int daysToPoint(final int days) {

		return days * pointEquivalenceFactor;
	}

	@Override
	public boolean isParameterised() {
		return series1.isParameterised() || series2.isParameterised();
	}

	@Override
	public Set<String> getParameters() {
		return parameters;
	}

	@Override
	public int[] getChangePoints() {

		return changePoints;
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {
		Number value = null;

		final int currentMonth = mapper.mapChangePointToMonth(timePoint);

		// Should be the start of the month in 'unit' (hour)
		final int monthStartPoint = mapper.mapMonthToChangePoint(currentMonth);

		// Convert the 'days' to the point in the month
		final int actualSplitPoint = monthStartPoint + daysToPoint(splitPoint - 1);

		// Use first curve
		if (timePoint < actualSplitPoint) {
			value = series1.evaluate(timePoint, params);
		} else {
			// Use second curve
			value = series2.evaluate(timePoint, params);
		}

		return value;
	}
}
