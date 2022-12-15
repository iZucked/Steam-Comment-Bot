/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;
import java.util.Map;

import com.mmxlabs.common.parser.series.ISeries;

public class Mean extends SimpleSeriesFunction {
	public Mean(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {
		double sum = 0;
		for (final ISeries s : arguments) {
			sum += s.evaluate(timePoint, params).doubleValue();
		}
		return sum / arguments.size();
	}
}
