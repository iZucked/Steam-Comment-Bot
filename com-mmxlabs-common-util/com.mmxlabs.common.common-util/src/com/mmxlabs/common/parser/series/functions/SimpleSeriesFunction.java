/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;

public abstract class SimpleSeriesFunction implements ISeries {

	protected List<ISeries> arguments;
	protected Set<String> parameters;
	protected int[] changePoints;

	protected SimpleSeriesFunction(final List<ISeries> arguments) {
		this.arguments = arguments;
		parameters = new HashSet<>();
		for (final ISeries s : arguments) {
			parameters.addAll(s.getParameters());
		}
		this.changePoints = SeriesUtil.mergeChangePoints(arguments);
	}

	@Override
	public Set<String> getParameters() {
		return parameters;
	}

	@Override
	public int[] getChangePoints() {
		return changePoints;
	}

	@Override
	public boolean isParameterised() {
		return !parameters.isEmpty();
	}
}
