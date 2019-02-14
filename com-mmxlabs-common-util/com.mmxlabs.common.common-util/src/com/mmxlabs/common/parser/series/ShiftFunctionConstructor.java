/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.ShiftedSeries;

public class ShiftFunctionConstructor implements IExpression<ISeries> {

	private ShiftFunctionMapper shiftMapper;
	private IExpression<ISeries> toShift;
	private int shiftBy;

	public ShiftFunctionConstructor(ShiftFunctionMapper shiftMapper, final List<IExpression<ISeries>> arguments) {
		if (shiftMapper == null) {
			throw new IllegalStateException("No shift mapper function defined");
		}
		this.shiftMapper = shiftMapper;
		this.toShift = arguments.get(0);
		this.shiftBy = arguments.get(1).evaluate().evaluate(0).intValue();
	}

	public ShiftFunctionConstructor(ShiftFunctionMapper shiftMapper, final IExpression<ISeries> expr, IExpression<ISeries> shiftBy) {
		if (shiftMapper == null) {
			throw new IllegalStateException("No shift mapper function defined");
		}
		this.shiftMapper = shiftMapper;
		this.toShift = expr;
		this.shiftBy = shiftBy.evaluate().evaluate(0).intValue();
	}

	public ShiftFunctionConstructor(ShiftFunctionMapper shiftMapper, IExpression<ISeries> expr, final Integer shiftBy) {
		if (shiftMapper == null) {
			throw new IllegalStateException("No shift mapper function defined");
		}
		this.shiftMapper = shiftMapper;
		this.shiftBy = shiftBy.intValue();
		this.toShift = expr;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new ShiftedSeries(toShift.evaluate(), shiftBy, shiftMapper);
	}
	
	@Override
	public @NonNull ISeries evaluate(Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime) {
		return new ShiftedSeries(toShift.evaluate(), shiftBy, shiftMapper);
	}
}
