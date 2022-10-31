/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.astnodes.SCurveFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.SCurveFunctionASTNode.ExprSelector;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;

public class SCurveSeries implements ISeries {
	private final ISeries base;
	private final double firstThreshold;
	private final double secondThreshold;
	private final ISeries lowerSeries;
	private final ISeries middleSeries;
	private final ISeries higherSeries;
	private int[] changePoints;

	public SCurveSeries(final ISeries base, final double firstThreshold, final double secondThreshold, final ISeries lowerSeries, final ISeries middleSeries, final ISeries higherSeries) {
		this.base = base;
		this.firstThreshold = firstThreshold;
		this.secondThreshold = secondThreshold;
		this.lowerSeries = lowerSeries;
		this.middleSeries = middleSeries;
		this.higherSeries = higherSeries;

		int[] accumulator = new int[0];
		for (final ISeries argument : Lists.newArrayList(base, lowerSeries, middleSeries, higherSeries)) {
			accumulator = SeriesUtil.mergeChangePoints(accumulator, argument.getChangePoints());
		}
		this.changePoints = accumulator;
	}

	@Override
	public int[] getChangePoints() {
		return changePoints;
	}

	@Override
	public Number evaluate(final int point) {

		final Number baseValue = base.evaluate(point);
		final ExprSelector selected = SCurveFunctionASTNode.select(baseValue.doubleValue(), firstThreshold, secondThreshold);

		return switch (selected) {
		case LOW -> lowerSeries.evaluate(point);
		case MID -> middleSeries.evaluate(point);
		case HIGH -> higherSeries.evaluate(point);
		};
	}
}
