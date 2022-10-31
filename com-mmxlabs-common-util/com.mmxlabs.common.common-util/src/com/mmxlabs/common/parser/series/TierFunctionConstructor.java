/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.astnodes.ComparisonOperators;
import com.mmxlabs.common.parser.series.functions.TierSeries;

public class TierFunctionConstructor implements IExpression<ISeries> {

	private IExpression<ISeries> target;

	private ComparisonOperators lowOp;
	private Number low;
	private IExpression<ISeries> lowValue;

	private ComparisonOperators midOp;
	private Number mid;
	private IExpression<ISeries> midValue;

	private IExpression<ISeries> highValue;

	public TierFunctionConstructor(@NonNull IExpression<@NonNull ISeries> target, ComparisonOperators lowOp, Number low, @NonNull IExpression<@NonNull ISeries> lowValue, ComparisonOperators midOp,
			Number mid, @NonNull IExpression<@NonNull ISeries> midValue, @NonNull IExpression<@NonNull ISeries> highValue) {
		this.target = target;
		this.lowOp = lowOp;
		this.low = low;
		this.lowValue = lowValue;
		this.midOp = midOp;
		this.mid = mid;
		this.midValue = midValue;
		this.highValue = highValue;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new TierSeries(target.evaluate(), lowOp, low.doubleValue(), lowValue.evaluate(), midOp, mid.doubleValue(), midValue.evaluate(), highValue.evaluate());
	}

	@Override
	public boolean canEvaluate() {
		return target.canEvaluate() //
				&& lowValue.canEvaluate() //
				&& midValue.canEvaluate() //
				&& highValue.canEvaluate() //
		;
	}

}
