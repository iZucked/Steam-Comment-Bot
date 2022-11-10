/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.time.Month;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.astnodes.MonthFunctionASTNode;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.ISeries;

public class MonthSeries implements ISeries {
	private final ISeries shiftee;
	private final @NonNull CalendarMonthMapper mapper;
	private final Month month;
	private int[] changePoints;

	public MonthSeries(final ISeries shiftee, final Month month, @NonNull final CalendarMonthMapper mapper) {
		this.shiftee = shiftee;
		this.month = month;
		this.mapper = mapper;
		this.changePoints = shiftee.getChangePoints();

		if (shiftee.getChangePoints().length > 0) {
			final int[] shifteeChangePoints = shiftee.getChangePoints();
			if (shifteeChangePoints[0] > 0) {

				// As this implementation looks forward, we need to make time between time 0 and
				// the start of the delegated curve is covered.
				final List<Integer> extraChangePoints = new LinkedList<>();
				final int firstCp = mapper.mapMonthToChangePoint(0);
				int m = mapper.mapChangePointToMonth(firstCp);
				int time = mapper.mapMonthToChangePoint(m);
				while (time < shifteeChangePoints[0]) {
					extraChangePoints.add(time);
					m += 1;
					time = mapper.mapMonthToChangePoint(m);
				}

				if (!extraChangePoints.isEmpty()) {
					this.changePoints = new int[extraChangePoints.size() + shifteeChangePoints.length];
					for (int i = 0; i < extraChangePoints.size(); ++i) {
						this.changePoints[i] = extraChangePoints.get(i);
					}
					for (int i = 0; i < shifteeChangePoints.length; ++i) {
						this.changePoints[i + extraChangePoints.size()] = shifteeChangePoints[i];
					}
				}

			}

		}
	}

	@Override
	public int[] getChangePoints() {
		return this.changePoints;
	}

	@Override
	public Number evaluate(final int point) {
		final int newTimePoint = mapper.mapTimePoint(point, ldt -> MonthFunctionASTNode.mapTime(ldt, month));
		return shiftee.evaluate(newTimePoint);
	}
}
