/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;

public abstract class SimpleSeriesFunction implements ISeries {

	protected List<ISeries> arguments;
	protected int[] changePoints;

	protected SimpleSeriesFunction(final List<ISeries> arguments) {
		this.arguments = arguments;
		int[] accumulator = new int[0];
		for (final ISeries argument : arguments) {
			accumulator = SeriesUtil.mergeChangePoints(accumulator, argument.getChangePoints());
		}
		this.changePoints = accumulator;
	}

	@Override
	public int[] getChangePoints() {
		return changePoints;
	}
}
