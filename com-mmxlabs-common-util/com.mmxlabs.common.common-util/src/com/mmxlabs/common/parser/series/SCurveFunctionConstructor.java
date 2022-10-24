/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.SCurveSeries;

public class SCurveFunctionConstructor implements IExpression<ISeries> {

	private final IExpression<ISeries> base;
	private final double firstThreshold;
	private final double secondThreshold;
	private final IExpression<ISeries> lowerSeries;
	private final IExpression<ISeries> middleSeries;
	private final IExpression<ISeries> higherSeries;

	public SCurveFunctionConstructor(final IExpression<ISeries> base, final double lowerThan, final double higherThan, final IExpression<ISeries> lowerSeries, final IExpression<ISeries> series,
			final IExpression<ISeries> higherSeries) {
		this.base = base;
		this.firstThreshold = lowerThan;
		this.secondThreshold = higherThan;
		this.lowerSeries = lowerSeries;
		this.middleSeries = series;
		this.higherSeries = higherSeries;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new SCurveSeries(base.evaluate(), firstThreshold, secondThreshold, lowerSeries.evaluate(), middleSeries.evaluate(), higherSeries.evaluate());
	}

	private static IExpression<ISeries> makeExpression(final IExpression<ISeries> x, final double a, final double b) {
		return new SeriesOperatorExpression('+', //
				new SeriesOperatorExpression('*', //
						x, new ConstantSeriesExpression(Double.valueOf(a))), //
				new ConstantSeriesExpression(Double.valueOf(b)));
	}

	@Override
	public boolean canEvaluate() {
		return base.canEvaluate() && lowerSeries.canEvaluate() && middleSeries.canEvaluate() && higherSeries.canEvaluate();
	}

	public double getFirstThreshold() {
		return firstThreshold;
	}

	public double getSecondThreshold() {
		return secondThreshold;
	}

	public IExpression<ISeries> getLowerSeries() {
		return lowerSeries;
	}

	public IExpression<ISeries> getMiddleSeries() {
		return middleSeries;
	}

	public IExpression<ISeries> getHigherSeries() {
		return higherSeries;
	}

	public IExpression<ISeries> getBase() {
		return base;
	}
}
