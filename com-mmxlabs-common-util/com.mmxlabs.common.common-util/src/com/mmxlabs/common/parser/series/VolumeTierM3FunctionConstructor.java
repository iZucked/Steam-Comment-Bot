/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.astnodes.ComparisonOperators;
import com.mmxlabs.common.parser.series.functions.VolumeTierSeries;

public class VolumeTierM3FunctionConstructor implements IExpression<ISeries> {

	private final IExpression<ISeries> tier1Series;
	private final IExpression<ISeries> tier2Series;
	private final double threshold;
	private final @NonNull ComparisonOperators op;

	public VolumeTierM3FunctionConstructor(final SeriesParserData seriesParserData, IExpression<ISeries> tier1Series, final @NonNull ComparisonOperators op, final Number threshold,
			IExpression<ISeries> tier2Series) {
		this.tier1Series = tier1Series;
		this.tier2Series = tier2Series;
		this.op = op;
		this.threshold = threshold.doubleValue();
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new VolumeTierSeries(true, tier1Series.evaluate(), op, threshold, tier2Series.evaluate());
	}

	@Override
	public boolean canEvaluate() {
		return tier1Series.canEvaluate() && tier2Series.canEvaluate();
	}
}
