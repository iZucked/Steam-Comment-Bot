package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class DefaultExpressionContainer implements IExpressionContainer {

	private IExpression<ISeries> wrappedExpression = null;

	public DefaultExpressionContainer() {
	}

	public DefaultExpressionContainer(@NonNull final IExpression<ISeries> wrappedExpression) {
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
	public void setExpression(@NonNull IExpression<ISeries> expression) {
		this.wrappedExpression = expression;
	}

}
