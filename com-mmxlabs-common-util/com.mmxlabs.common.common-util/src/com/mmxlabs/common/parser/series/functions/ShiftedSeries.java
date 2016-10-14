/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.ShiftFunctionMapper;

public class ShiftedSeries implements ISeries {
	private int shift;
	private ISeries shiftee;
	private @NonNull ShiftFunctionMapper shiftMapper;

	public ShiftedSeries(final ISeries shiftee, int shift, @NonNull ShiftFunctionMapper shiftMapper) {
		this.shiftee = shiftee;
		this.shift = shift;
		this.shiftMapper = shiftMapper;
	}

	@Override
	public int[] getChangePoints() {
		final int[] shifteePoints = shiftee.getChangePoints().clone();

		for (int i = 0; i < shifteePoints.length; i++) {
			shifteePoints[i] = shiftMapper.mapChangePoint(shifteePoints[i], shift);
		}
		return shifteePoints;

	}

	@Override
	public Number evaluate(int point) {
		int newPoint = shiftMapper.mapChangePoint(point, shift);
		return shiftee.evaluate(newPoint);
	}

}
