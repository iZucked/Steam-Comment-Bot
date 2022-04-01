package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class LazyNamedSeriesExpression implements IExpression<ISeries> {
	@NonNull
	final ILazyNamedSeriesContainer namedSeriesContainer;

	public LazyNamedSeriesExpression(@NonNull final ILazyNamedSeriesContainer namedSeriesContainer) {
		this.namedSeriesContainer = namedSeriesContainer;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return namedSeriesContainer.evaluate();
	}

	@Override
	public boolean canEvaluate() {
		return namedSeriesContainer.isInitialised();
	}

}
