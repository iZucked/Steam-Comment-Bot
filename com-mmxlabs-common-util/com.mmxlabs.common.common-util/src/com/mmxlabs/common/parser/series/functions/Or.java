/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;
import java.util.Map;

import com.mmxlabs.common.parser.series.ISeries;

public class Or extends SimpleSeriesFunction {
	public Or(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {
		boolean value = false;
		for (final ISeries s : arguments) {
			final Number n = s.evaluate(timePoint, params);
			if (n.doubleValue() != 0) {
				value = true;
			}
		}
		if (value)
			return 1.0;
		else
			return 0;
	}
}
