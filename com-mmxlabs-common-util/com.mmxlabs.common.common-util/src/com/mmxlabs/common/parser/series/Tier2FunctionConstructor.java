/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.astnodes.ComparisonOperators;
import com.mmxlabs.common.parser.series.functions.Tier2Series;

public class Tier2FunctionConstructor implements IExpression<ISeries> {

	private final IExpression<ISeries> target;

	private final ComparisonOperators lowOp;
	private final Number low;
	private final IExpression<ISeries> lowValue;

	private final IExpression<ISeries> highValue;

	public Tier2FunctionConstructor(@NonNull final IExpression<@NonNull ISeries> target, final ComparisonOperators lowOp, final Number low, @NonNull final IExpression<@NonNull ISeries> lowValue,
			@NonNull final IExpression<@NonNull ISeries> highValue) {
		this.target = target;
		this.lowOp = lowOp;
		this.low = low;
		this.lowValue = lowValue;
		this.highValue = highValue;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new Tier2Series(target.evaluate(), lowOp, low.doubleValue(), lowValue.evaluate(), highValue.evaluate());
	}

	@Override
	public boolean canEvaluate() {
		return target.canEvaluate() //
				&& lowValue.canEvaluate() //
				&& highValue.canEvaluate() //
		;
	}

}
