/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import com.mmxlabs.common.parser.series.ISeries;

public class SCurveSeries implements ISeries {
	private final ISeries base;
	private final double firstThreshold;
	private final double secondThreshold ;
	private final ISeries lowerSeries;
	private final ISeries middleSeries;
	private final ISeries higherSeries;

	public SCurveSeries(final ISeries base, final double firstThreshold, final double secondThreshold, final ISeries lowerSeries, final ISeries series, final ISeries higherSeries) {
		this.base = base;
		this.firstThreshold = firstThreshold;
		this.secondThreshold = secondThreshold;
		this.lowerSeries = lowerSeries;
		this.middleSeries = series;
		this.higherSeries = higherSeries;
	}

	@Override
	public int[] getChangePoints() {
		// TODO: Should be a merge across all inputs
		return base.getChangePoints();
	}

	@Override
	public Number evaluate(final int point) {

		final Number baseValue = base.evaluate(point);
		if (baseValue.doubleValue() < firstThreshold) {
			return lowerSeries.evaluate(point);
		} else if (baseValue.doubleValue() > secondThreshold ) {
			return higherSeries.evaluate(point);
		}
		return middleSeries.evaluate(point);
	}
}
