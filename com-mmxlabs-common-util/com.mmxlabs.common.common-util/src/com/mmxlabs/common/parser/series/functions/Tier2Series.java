/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.parser.astnodes.ComparisonOperators;
import com.mmxlabs.common.parser.astnodes.Tier2FunctionASTNode.ExprSelector;
import com.mmxlabs.common.parser.astnodes.Tier2FunctionASTNode;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;

public class Tier2Series implements ISeries {

	private final ISeries target;
	private final ComparisonOperators lowOp;
	private final double low;
	private final ISeries lowValue;
	private final ISeries highValue;
	private final int[] changePoints;
	private final Set<String> parameters;

	public Tier2Series(final ISeries target, final ComparisonOperators lowOp, final double low, final ISeries lowValue, final ISeries highValue) {
		this.target = target;
		this.lowOp = lowOp;
		this.low = low;
		this.lowValue = lowValue;
		this.highValue = highValue;

		this.changePoints = SeriesUtil.mergeChangePoints(target, lowValue, highValue);

		parameters = new HashSet<>();
		parameters.addAll(target.getParameters());
		parameters.addAll(lowValue.getParameters());
		parameters.addAll(highValue.getParameters());
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

		final Number baseValue = target.evaluate(timePoint, params);
		final ExprSelector selected = Tier2FunctionASTNode.select(baseValue.doubleValue(), lowOp, low);

		return switch (selected) {
		case LOW -> lowValue.evaluate(timePoint, params);
		case HIGH -> highValue.evaluate(timePoint, params);
		};

	}
}
