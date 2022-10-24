/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import com.mmxlabs.common.parser.IExpression;

public class DefaultExpressionContainer implements IExpressionContainer {

	private IExpression<ISeries> wrappedExpression = null;
	private final String name;
	private final SeriesType seriesType;

	public DefaultExpressionContainer(final String name, final SeriesType seriesType, final IExpression<ISeries> wrappedExpression) {
		this.name = name;
		this.seriesType = seriesType;
		this.wrappedExpression = wrappedExpression;
	}

	@Override
	public ISeries get() {
		return wrappedExpression.evaluate();
	}

	@Override
	public boolean canGet() {
		return wrappedExpression != null && wrappedExpression.canEvaluate();
	}

	@Override
	public void setExpression(final IExpression<ISeries> expression) {
		this.wrappedExpression = expression;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public SeriesType getType() {
		return seriesType;
	}

}
