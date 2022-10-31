/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;

public class LazyStepwiseIntegerCurve implements ILazyCurve {

	private ThreadLocal<ICurve> wrappedCurve = new ThreadLocal<>();
	private IExpression<ISeries> expression;
	private Function<@NonNull ISeries, @NonNull ICurve> initialiser;
	private Consumer<@NonNull ISeries> parsedSeriesConsumer;

	public LazyStepwiseIntegerCurve(final IExpression<ISeries> expression, final Function<@NonNull ISeries, @NonNull ICurve> initialiser, final Consumer<@NonNull ISeries> parsedSeriesConsumer) {
		this.expression = expression;
		this.initialiser = initialiser;
		this.parsedSeriesConsumer = parsedSeriesConsumer;
	}

	@Override
	public int getValueAtPoint(int point) {
		return wrappedCurve.get().getValueAtPoint(point);
	}

	@Override
	public void initialise() {
		final ISeries parsed = expression.evaluate();
		final ICurve curve = initialiser.apply(parsed);
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
