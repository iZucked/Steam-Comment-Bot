/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.SplitMonthSeries;

public class SplitMonthFunctionConstructor implements IExpression<ISeries> {

	private final IExpression<ISeries> series1;
	private final IExpression<ISeries> series2;
	private final int splitPoint;

	private @NonNull SeriesParserData seriesParserData;

	public SplitMonthFunctionConstructor(@NonNull SeriesParserData seriesParserData, IExpression<ISeries> series1, IExpression<ISeries> series2, final Integer splitPoint) {
		this.seriesParserData = seriesParserData;

		this.series1 = series1;
		this.series2 = series2;
		this.splitPoint = splitPoint.intValue();
	}

	public SplitMonthFunctionConstructor(@NonNull SeriesParserData seriesParserData, IExpression<ISeries> series1, IExpression<ISeries> series2, final int splitPoint) {
		this.seriesParserData = seriesParserData;

		this.series1 = series1;
		this.series2 = series2;
		this.splitPoint = splitPoint;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new SplitMonthSeries(seriesParserData, series1.evaluate(), series2.evaluate(), splitPoint);
	}

	@Override
	public boolean canEvaluate() {
		return series1.canEvaluate() && series2.canEvaluate();
	}

	public IExpression<ISeries> getSeries1() {
		return series1;
	}

	public IExpression<ISeries> getSeries2() {
		return series2;
	}

	public int getSplitPoint() {
		return splitPoint;
	}

}
