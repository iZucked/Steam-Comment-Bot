/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;
import java.util.Map;

import com.mmxlabs.common.parser.series.ISeries;

public class InOrder extends SimpleSeriesFunction {
	public InOrder(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {
		double d = -Double.MAX_VALUE;
		for (final ISeries s : arguments) {
			final Number n = s.evaluate(timePoint, params);
			if (n.doubleValue() < d) {
				return 0;
			}
			d = n.doubleValue();
		}

		return 1;
	}
}
