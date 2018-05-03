/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.SplitMonthSeries;

public class SplitMonthFunctionConstructor implements IExpression<ISeries> {

	private final IExpression<ISeries> series1;
	private final IExpression<ISeries> series2;
	private final int splitPoint;

	public SplitMonthFunctionConstructor(final List<IExpression<ISeries>> arguments) {
		this.series1 = arguments.get(0);
		this.series2 = arguments.get(1);
		this.splitPoint = arguments.get(2).evaluate().evaluate(0).intValue();
	}

	public SplitMonthFunctionConstructor(IExpression<ISeries> series1, IExpression<ISeries> series2, final Integer splitPoint) {
		this.series1 = series1;
		this.series2 = series2;
		this.splitPoint = splitPoint.intValue();
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new SplitMonthSeries(series1.evaluate(), series2.evaluate(), splitPoint);
	}
}
