/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.astnodes.ShiftFunctionASTNode;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParserData;

public class ShiftedSeries implements ISeries {
	private final int shift;
	private final ISeries shiftee;
	private final @NonNull CalendarMonthMapper mapper;
	private final int[] changePoints;

	public ShiftedSeries(@NonNull final SeriesParserData seriesParserData, final ISeries shiftee, final int shift) {
		this.shiftee = shiftee;
		this.shift = shift;
		this.mapper = seriesParserData.calendarMonthMapper;

		final int[] shifteePoints = shiftee.getChangePoints().clone();
		// Adapt the date range by the shift amount. If Jan-Dec and shift is 1 (I.e. Feb
		// looks up Jan price) then new range is Feb->Jan
		// Note different sign to the evaluate method
		for (int i = 0; i < shifteePoints.length; i++) {

			int point = shifteePoints[i];
			final int newTimePoint = mapper.mapTimePoint(point, ldt -> ShiftFunctionASTNode.mapTime(ldt, -shift));
			shifteePoints[i] = newTimePoint;
		}
		this.changePoints = shifteePoints;
	}

	@Override
	public boolean isParameterised() {
		return shiftee.isParameterised();
	}

	@Override
	public Set<String> getParameters() {
		return shiftee.getParameters();
	}

	@Override
	public int[] getChangePoints() {
		return changePoints;
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {
		final int newTimePoint = mapper.mapTimePoint(timePoint, ldt -> ShiftFunctionASTNode.mapTime(ldt, shift));
		return shiftee.evaluate(newTimePoint, params);
	}

}
