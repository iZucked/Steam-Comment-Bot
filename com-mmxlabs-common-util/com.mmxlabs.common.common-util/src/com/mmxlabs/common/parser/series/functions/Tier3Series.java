/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.astnodes.ComparisonOperators;
import com.mmxlabs.common.parser.astnodes.Tier3FunctionASTNode;
import com.mmxlabs.common.parser.astnodes.Tier3FunctionASTNode.ExprSelector;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;

public class Tier3Series implements ISeries {

	private final ISeries target;
	private final ComparisonOperators lowOp;
	private final double low;
	private final ISeries lowValue;
	private final ComparisonOperators midOp;
	private final double mid;
	private final ISeries midValue;
	private final ISeries highValue;
	private final int[] changePoints;
	private final Set<String> parameters;

	public Tier3Series(final ISeries target, final ComparisonOperators lowOp, final double low, final ISeries lowValue, final ComparisonOperators midOp, final double mid, final ISeries midValue,
			final ISeries highValue) {
		this.target = target;
		this.lowOp = lowOp;
		this.low = low;
		this.lowValue = lowValue;
		this.midOp = midOp;
		this.mid = mid;
		this.midValue = midValue;
		this.highValue = highValue;

		this.changePoints = SeriesUtil.mergeChangePoints(target, lowValue, midValue, highValue);
		parameters = new HashSet<>();
		parameters.addAll(target.getParameters());
		parameters.addAll(lowValue.getParameters());
		parameters.addAll(midValue.getParameters());
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
		final ExprSelector selected = Tier3FunctionASTNode.select(baseValue.doubleValue(), lowOp, low, midOp, mid);

		return switch (selected) {
		case LOW -> lowValue.evaluate(timePoint, params);
		case MID -> midValue.evaluate(timePoint, params);
		case HIGH -> highValue.evaluate(timePoint, params);
		};

	}
}
