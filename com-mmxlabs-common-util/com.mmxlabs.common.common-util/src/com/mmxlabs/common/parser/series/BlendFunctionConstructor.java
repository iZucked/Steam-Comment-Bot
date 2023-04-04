/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.astnodes.ComparisonOperators;
import com.mmxlabs.common.parser.series.functions.BlendSeries;

public class BlendFunctionConstructor implements IExpression<ISeries> {

	private final IExpression<ISeries> targetSeries;
	private final IExpression<ISeries> tier1Series;
	private final IExpression<ISeries> tier2Series;
	private final double threshold;

	public BlendFunctionConstructor(final IExpression<ISeries> targetSeries, IExpression<ISeries> tier1Series, final Number threshold, IExpression<ISeries> tier2Series) {
		this.targetSeries = targetSeries;
		this.tier1Series = tier1Series;
		this.tier2Series = tier2Series;
		this.threshold = threshold.doubleValue();
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new BlendSeries(targetSeries.evaluate(), tier1Series.evaluate(), threshold, tier2Series.evaluate());
	}

	@Override
	public boolean canEvaluate() {
		return targetSeries.canEvaluate() && tier1Series.canEvaluate() && tier2Series.canEvaluate();
	}
}
