package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;

@NonNullByDefault
public class DefaultLazyNamedSeriesContainer implements ILazyNamedSeriesContainer {

	private final String seriesName;
	private final ThreadLocal<@Nullable IExpression<ISeries>> curveContainer = new ThreadLocal<>();

	public DefaultLazyNamedSeriesContainer(final String seriesName) {
		this.seriesName = seriesName;
	}
	
	@Override
	public ISeries evaluate() {
		@Nullable
		final IExpression<ISeries> pCurve = curveContainer.get();
		if (pCurve == null) {
			throw new IllegalStateException("Curve must be defined");
		}
		return pCurve.evaluate();
	}

	public String getName() {
		return this.seriesName;
	}

	@Override
	public void setCurve(final IExpression<ISeries> concreteCurve) {
		curveContainer.set(concreteCurve);
	}

	@Deprecated
	@Override
	protected void finalize() throws Throwable {
		clear();
	}

	@Override
	public boolean isInitialised() {
		return curveContainer.get() != null;
	}

	@Override
	public void clear() {
		curveContainer.remove();
	}
}
