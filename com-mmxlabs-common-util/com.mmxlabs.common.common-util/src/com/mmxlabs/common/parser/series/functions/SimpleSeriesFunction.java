/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;

public abstract class SimpleSeriesFunction implements ISeries {

	protected List<ISeries> arguments;

	public SimpleSeriesFunction(final List<ISeries> arguments) {
		this.arguments = arguments;
	}

	@Override
	public int[] getChangePoints() {
		int accumulator[] = new int[0];
		for (final ISeries argument : arguments) {
			accumulator = SeriesUtil.mergeChangePoints(accumulator, argument.getChangePoints());
		}
		return accumulator;
	}
}
