/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;

import com.mmxlabs.common.parser.series.ISeries;

public class Equal extends SimpleSeriesFunction {

	public Equal(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(int point) {
		final Number n1 = arguments.get(0).evaluate(point);
		final Number n2 = arguments.get(1).evaluate(point);
		if (n1.doubleValue() == n2.doubleValue()) {
			return 1;
		} else {
			return 0;
		}
	}
}
