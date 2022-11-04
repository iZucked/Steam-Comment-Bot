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
		this.changePoints = SeriesUtil.mergeChangePoints(arguments);
	}

	@Override
	public int[] getChangePoints() {
		return changePoints;
	}
}
