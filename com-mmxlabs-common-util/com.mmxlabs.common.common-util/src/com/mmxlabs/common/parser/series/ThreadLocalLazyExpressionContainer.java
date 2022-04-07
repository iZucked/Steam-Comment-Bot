package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;

public class ThreadLocalLazyExpressionContainer implements ILazyExpressionContainer {

	private final ThreadLocal<@Nullable IExpression<ISeries>> localExpression = new ThreadLocal<>();

	@Override
	public ISeries get() {
		@Nullable
		final IExpression<ISeries> pExpression = localExpression.get();
		if (pExpression == null) {
			throw new UninitialisedSeriesException("");
		}
		return pExpression.evaluate();
	}

	@Override
	public boolean canGet() {
		return localExpression.get() != null;
	}

	@Override
	public void clear() {
		localExpression.remove();
	}

	@Override
	public void setExpression(final IExpression<ISeries> expression) {
		localExpression.set(expression);
	}

}
