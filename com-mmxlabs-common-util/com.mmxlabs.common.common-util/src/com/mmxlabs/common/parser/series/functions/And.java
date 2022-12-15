/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;
import java.util.Map;

import com.mmxlabs.common.parser.series.ISeries;

public class And extends SimpleSeriesFunction {

	public And(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {
		for (final ISeries s : arguments) {
			final Number n = s.evaluate(timePoint, params);
			if (n.doubleValue() == 0) {
				return 0;
			}
		}
		return 1;
	}

}
