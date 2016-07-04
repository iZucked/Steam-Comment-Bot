/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;

import com.mmxlabs.common.parser.series.ISeries;

public class If extends SimpleSeriesFunction {
	public If(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(int point) {
		final Number cond = arguments.get(0).evaluate(point);
		if (cond.doubleValue() == 0) {
			return arguments.get(2).evaluate(point);
		} else {
			return arguments.get(1).evaluate(point);
		}
	}
}
