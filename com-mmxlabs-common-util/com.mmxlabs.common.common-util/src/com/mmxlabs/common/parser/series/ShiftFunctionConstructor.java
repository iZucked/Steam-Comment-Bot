/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.ShiftedSeries;

public class ShiftFunctionConstructor implements IExpression<ISeries> {

	private SeriesParserData seriesParserData;
	private IExpression<ISeries> toShift;
	private int shiftBy;

	public ShiftFunctionConstructor(@NonNull SeriesParserData seriesParserData, IExpression<ISeries> expr, final int shiftBy) {
		this.seriesParserData = seriesParserData;
		this.shiftBy = shiftBy;
		this.toShift = expr;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new ShiftedSeries(seriesParserData, toShift.evaluate(), shiftBy);
	}

	public IExpression<ISeries> getShiftedExpression() {
		return toShift;
	}

	public int getShiftBy() {
		return shiftBy;
	}

	@Override
	public boolean canEvaluate() {
		return toShift.canEvaluate();
	}
	
	
}
