/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.ShiftedSeries;

public class ShiftFunctionConstructor implements IExpression<ISeries> {

	private SeriesParserData seriesParserData;
	private IExpression<ISeries> toShift;
	private int shiftBy;

	public ShiftFunctionConstructor(@NonNull SeriesParserData seriesParserData, final List<IExpression<ISeries>> arguments) {
		this.seriesParserData = seriesParserData;
		this.toShift = arguments.get(0);
		this.shiftBy = arguments.get(1).evaluate().evaluate(0).intValue();
	}

	public ShiftFunctionConstructor(@NonNull SeriesParserData seriesParserData, final IExpression<ISeries> expr, IExpression<ISeries> shiftBy) {
		this.seriesParserData = seriesParserData;
		this.toShift = expr;
		this.shiftBy = shiftBy.evaluate().evaluate(0).intValue();
	}

	public ShiftFunctionConstructor(@NonNull SeriesParserData seriesParserData, IExpression<ISeries> expr, final Integer shiftBy) {
		this.seriesParserData = seriesParserData;
		this.shiftBy = shiftBy.intValue();
		this.toShift = expr;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new ShiftedSeries(seriesParserData, toShift.evaluate(), shiftBy);
	}
}
