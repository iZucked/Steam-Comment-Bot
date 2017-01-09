/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;

import com.mmxlabs.common.parser.series.ISeries;

public class Mean extends SimpleSeriesFunction {
	public Mean(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(int point) {
		double sum = 0;
		for (final ISeries s : arguments) {
			sum += s.evaluate(point).doubleValue();
		}
		return sum / arguments.size();
	}
}
