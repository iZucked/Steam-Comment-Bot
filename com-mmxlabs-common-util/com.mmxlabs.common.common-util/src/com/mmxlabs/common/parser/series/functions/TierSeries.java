/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.astnodes.ComparisonOperators;
import com.mmxlabs.common.parser.astnodes.TierFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.TierFunctionASTNode.ExprSelector;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;

public class TierSeries implements ISeries {

	private final ISeries target;
	private final ComparisonOperators lowOp;
	private final double low;
	private final ISeries lowValue;
	private final ComparisonOperators midOp;
	private final double mid;
	private final ISeries midValue;
	private final ISeries highValue;
	private final int[] changePoints;

	public TierSeries(final ISeries target, final ComparisonOperators lowOp, final double low, final ISeries lowValue, final ComparisonOperators midOp, final double mid, final ISeries midValue,
			final ISeries highValue) {
		this.target = target;
		this.lowOp = lowOp;
		this.low = low;
		this.lowValue = lowValue;
		this.midOp = midOp;
		this.mid = mid;
		this.midValue = midValue;
		this.highValue = highValue;

		int[] accumulator = new int[0];
		for (final ISeries argument : Lists.newArrayList(target, lowValue, midValue, highValue)) {
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
		final ExprSelector selected = TierFunctionASTNode.select(baseValue.doubleValue(), lowOp, low, midOp, mid);

		return switch (selected) {
		case LOW -> lowValue.evaluate(point);
		case MID -> midValue.evaluate(point);
		case HIGH -> highValue.evaluate(point);
		};

	}
}
