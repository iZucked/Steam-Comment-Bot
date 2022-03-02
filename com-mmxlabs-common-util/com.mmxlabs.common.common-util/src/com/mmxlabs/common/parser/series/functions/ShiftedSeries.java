/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParserData;
import com.mmxlabs.common.parser.series.ShiftFunctionMapper;

public class ShiftedSeries implements ISeries {
	private int shift;
	private ISeries shiftee;
	private ShiftFunctionMapper shiftMapper;

	public ShiftedSeries(@NonNull SeriesParserData seriesParserData, final ISeries shiftee, int shift) {
		this.shiftee = shiftee;
		this.shift = shift;
		this.shiftMapper = seriesParserData.shiftMapper;
	}

	@Override
	public int[] getChangePoints() {
		final int[] shifteePoints = shiftee.getChangePoints().clone();
		// Adapt the date range by the shift amount. If Jan-Dec and shift is 1 (I.e. Feb looks up Jan price) then new range is Feb->Jan
		// Note different sign to the evaluate method
		for (int i = 0; i < shifteePoints.length; i++) {
			shifteePoints[i] = shiftMapper.mapChangePoint(shifteePoints[i], -shift);
		}
		return shifteePoints;

	}

	@Override
	public Number evaluate(int point) {
		int newPoint = shiftMapper.mapChangePoint(point, shift);
		return shiftee.evaluate(newPoint);
	}

}
