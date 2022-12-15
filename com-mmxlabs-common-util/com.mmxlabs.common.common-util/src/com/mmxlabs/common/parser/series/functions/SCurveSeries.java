/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	private Set<String> parameters;

	public SCurveSeries(final ISeries base, final double firstThreshold, final double secondThreshold, final ISeries lowerSeries, final ISeries middleSeries, final ISeries higherSeries) {
		this.base = base;
		this.firstThreshold = firstThreshold;
		this.secondThreshold = secondThreshold;
		this.lowerSeries = lowerSeries;
		this.middleSeries = middleSeries;
		this.higherSeries = higherSeries;

		this.changePoints = SeriesUtil.mergeChangePoints(base, lowerSeries, middleSeries, higherSeries);

		parameters = new HashSet<>();
		parameters.addAll(base.getParameters());
		parameters.addAll(lowerSeries.getParameters());
		parameters.addAll(middleSeries.getParameters());
		parameters.addAll(higherSeries.getParameters());
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

		final Number baseValue = base.evaluate(timePoint, params);
		final ExprSelector selected = SCurveFunctionASTNode.select(baseValue.doubleValue(), firstThreshold, secondThreshold);

		return switch (selected) {
		case LOW -> lowerSeries.evaluate(timePoint, params);
		case MID -> middleSeries.evaluate(timePoint, params);
		case HIGH -> higherSeries.evaluate(timePoint, params);
		};
	}
}
