/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.List;

import org.eclipse.jdt.annotation.DefaultLocation;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.TierBlendSeries;

@NonNullByDefault
public class TierBlendFunctionConstructor implements IExpression<ISeries> {

	@NonNullByDefault({ DefaultLocation.FIELD, DefaultLocation.RETURN_TYPE }) // Override the class level annotation to avoid compile error
	public record ThresholdExprSeries(IExpression<ISeries> series, Number threshold) {
	}

	private final IExpression<ISeries> targetSeries;
	private final List<ThresholdExprSeries> thresholds;

	public TierBlendFunctionConstructor(final IExpression<ISeries> targetSeries, final List<ThresholdExprSeries> thresholds) {
		this.targetSeries = targetSeries;
		this.thresholds = thresholds;
	}

	@Override
	public @NonNull ISeries evaluate() {
		final List<TierBlendSeries.ThresholdISeries> list = thresholds.stream().map(t -> new TierBlendSeries.ThresholdISeries(t.series().evaluate(), t.threshold().doubleValue())).toList();

		return new TierBlendSeries(targetSeries.evaluate(), list);
	}

	@Override
	public boolean canEvaluate() {
		for (final var t : thresholds) {
			if (!t.series().canEvaluate()) {
				return false;
			}
		}
		return targetSeries.canEvaluate();
	}
}
