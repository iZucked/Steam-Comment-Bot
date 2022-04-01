package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public interface ILazyNamedSeriesContainer {
	@NonNull
	ISeries evaluate();

	boolean isInitialised();

	void clear();

	void setCurve(@NonNull final IExpression<ISeries> concreteCurve);
}
