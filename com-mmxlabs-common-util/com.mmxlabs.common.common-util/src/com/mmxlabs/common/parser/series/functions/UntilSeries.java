/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.ISeries;

public class UntilSeries implements ISeries {
	private ISeries lhs;
	private int switchPoint;
	private ISeries rhs;
	private final int[] changePoints;
	private final Set<String> parameters;

	public UntilSeries(final ISeries lhs, final LocalDateTime ldt, final ISeries rhs, @NonNull final CalendarMonthMapper mapper) {
		this.lhs = lhs;
		this.switchPoint = mapper.mapTimePoint(ldt);
		this.rhs = rhs;
		// Technically, we don't need to merge the two sets, but take the relevant segment from each.
		final Set<Integer> changePointsSet = new TreeSet<>();
		if (lhs.getChangePoints().length == 0) {
			// Earliest date?
			changePointsSet.add(Integer.MIN_VALUE);
		} else {
			for (int pt : lhs.getChangePoints()) {
				if (pt < switchPoint) {
					changePointsSet.add(pt);
				}
			}
		}
		// Make sure the switch point in here
		changePointsSet.add(switchPoint);
		for (int pt : rhs.getChangePoints()) {
			if (pt > switchPoint) {
				changePointsSet.add(pt);
			}
		}
		this.changePoints = changePointsSet.stream().mapToInt(Integer::intValue).toArray();

		parameters = new HashSet<>();
		parameters.addAll(lhs.getParameters());
		parameters.addAll(rhs.getParameters());
	}

	@Override
	public Set<String> getParameters() {
		return parameters;
	}

	@Override
	public boolean isParameterised() {
		return !parameters.isEmpty();
	}

	@Override
	public int[] getChangePoints() {
		return changePoints;
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {
		if (timePoint < switchPoint) {
			return lhs.evaluate(timePoint, params);
		} else {
			return rhs.evaluate(timePoint, params);
		}
	}
}
