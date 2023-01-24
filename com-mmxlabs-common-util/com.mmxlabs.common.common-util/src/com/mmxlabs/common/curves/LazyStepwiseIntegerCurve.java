/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;

@NonNullByDefault
public class LazyStepwiseIntegerCurve implements ILazyCurve {

	private final ThreadLocal<IParameterisedCurve> wrappedCurve = new ThreadLocal<>();
	private final IExpression<ISeries> expression;
	private final Function<ISeries, IParameterisedCurve> initialiser;
	private final @Nullable Consumer<ISeries> parsedSeriesConsumer;

	public LazyStepwiseIntegerCurve(final IExpression<ISeries> expression, final Function<@NonNull ISeries, IParameterisedCurve> initialiser, final @Nullable Consumer<ISeries> parsedSeriesConsumer) {
		this.expression = expression;
		this.initialiser = initialiser;
		this.parsedSeriesConsumer = parsedSeriesConsumer;
	}

	@Override
	public boolean hasParameters() {
		return wrappedCurve.get().hasParameters();
	}

	@Override
	public int getValueAtPoint(final int point, final Map<String, String> params) {
		return wrappedCurve.get().getValueAtPoint(point, params);
	}

	@Override
	public void initialise() {
		final ISeries parsed = expression.evaluate();
		final IParameterisedCurve curve = initialiser.apply(parsed);
		wrappedCurve.set(curve);
		if (parsedSeriesConsumer != null) {
			parsedSeriesConsumer.accept(parsed);
		}
	}

	@Deprecated
	@Override
	protected void finalize() throws Throwable {
		clear();
	}

	@Override
	public void clear() {
		wrappedCurve.remove();
	}

}
