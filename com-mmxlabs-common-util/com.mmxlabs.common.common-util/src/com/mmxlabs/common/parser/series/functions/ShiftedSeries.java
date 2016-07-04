/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.SortedSet;
import java.util.TreeSet;

import com.mmxlabs.common.parser.series.ISeries;

public class ShiftedSeries implements ISeries {
	private ISeries shifter;
	private ISeries shiftee;

	public ShiftedSeries(final ISeries shiftee, final ISeries shifter) {
		this.shiftee = shiftee;
		this.shifter = shifter;
	}

	@Override
	public int[] getChangePoints() {
		final int[] shifteePoints = shiftee.getChangePoints().clone();

		final int[] shifterChangePoints = shifter.getChangePoints();

		if (shifterChangePoints.length == 0) {
			final int shift = shifter.evaluate(0).intValue();
			for (int i = 0; i < shifteePoints.length; i++) {
				shifteePoints[i] -= shift;
			}
			return shifteePoints;
		}

		final SortedSet<Integer> points = new TreeSet<Integer>();
		for (int i = 0; i < shifterChangePoints.length - 1; i++) {
			final int ws = shifterChangePoints[i];
			final int we = shifterChangePoints[i + 1];
			final int shift = shifter.evaluate(ws).intValue();
			points.add(ws);
			points.add(we);
			// we need all the points from shifterChangePoints in the window + shift
			// and we need to translate them back to this range
			for (final int change : shifteePoints) {
				if (change >= (ws + shift) || change < (we + shift)) {
					// this change is in the window
					points.add(change - shift);
				}
			}
		}
		final int[] result = new int[points.size()];
		int k = 0;
		for (final Integer i : points) {
			result[k++] = i;
		}
		return result;
	}

	@Override
	public Number evaluate(int point) {
		return shiftee.evaluate(point + shifter.evaluate(point).intValue());
	}

}
