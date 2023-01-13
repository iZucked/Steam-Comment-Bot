/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;
import java.util.Map;

import com.mmxlabs.common.parser.series.ISeries;

public class If extends SimpleSeriesFunction {
	public If(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {
		final Number cond = arguments.get(0).evaluate(timePoint, params);
		if (cond.doubleValue() == 0) {
			return arguments.get(2).evaluate(timePoint, params);
		} else {
			return arguments.get(1).evaluate(timePoint, params);
		}
	}
}
