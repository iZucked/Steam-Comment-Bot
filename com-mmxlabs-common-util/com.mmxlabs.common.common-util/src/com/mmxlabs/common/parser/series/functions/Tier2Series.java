/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.astnodes.ComparisonOperators;
import com.mmxlabs.common.parser.astnodes.Tier2FunctionASTNode;
import com.mmxlabs.common.parser.astnodes.Tier2FunctionASTNode.ExprSelector;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;

public class Tier2Series implements ISeries {

	private final ISeries target;
	private final ComparisonOperators lowOp;
	private final double low;
	private final ISeries lowValue;
	private final ISeries highValue;
	private final int[] changePoints;

	public Tier2Series(final ISeries target, final ComparisonOperators lowOp, final double low, final ISeries lowValue, final ISeries highValue) {
		this.target = target;
		this.lowOp = lowOp;
		this.low = low;
		this.lowValue = lowValue;
		this.highValue = highValue;

		int[] accumulator = new int[0];
		for (final ISeries argument : Lists.newArrayList(target, lowValue, highValue)) {
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

		final Number baseValue = target.evaluate(point);
		final ExprSelector selected = Tier2FunctionASTNode.select(baseValue.doubleValue(), lowOp, low);

		return switch (selected) {
		case LOW -> lowValue.evaluate(point);
		case HIGH -> highValue.evaluate(point);
		};

	}
}
