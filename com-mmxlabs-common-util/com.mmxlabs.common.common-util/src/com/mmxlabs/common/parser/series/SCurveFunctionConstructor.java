/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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

	public SCurveFunctionConstructor(final SeriesParserData seriesParserData, final IExpression<ISeries> base, final double lowerThan, final double higherThan, final double a1, final double b1,
			final double a2, final double b2, final double a3, final double b3) {
		this(base, lowerThan, higherThan, makeExpression(base, a1, b1), makeExpression(base, a2, b2), makeExpression(base, a3, b3));
	}

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
}
