/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import com.mmxlabs.common.parser.series.ISeries;

public class SCurveSeries implements ISeries {
	private final ISeries base;
	private final double lowerThan;
	private final double higherThan;
	private final ISeries lowerSeries;
	private final ISeries series;
	private final ISeries higherSeries;

	public SCurveSeries(final ISeries base, final double lowerThan, final double higherThan, final ISeries lowerSeries, final ISeries series, final ISeries higherSeries) {
		this.base = base;
		this.lowerThan = lowerThan;
		this.higherThan = higherThan;
		this.lowerSeries = lowerSeries;
		this.series = series;
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
		if (baseValue.doubleValue() < lowerThan) {
			return lowerSeries.evaluate(point);
		} else if (baseValue.doubleValue() > higherThan) {
			return higherSeries.evaluate(point);
		}
		return series.evaluate(point);
	}
}
