/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.List;

import com.mmxlabs.common.parser.series.ISeries;

public class And extends SimpleSeriesFunction {
	public And(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(final int point) {
		for (final ISeries s : arguments) {
			final Number n = s.evaluate(point);
			if (n.doubleValue() == 0) {
				return 0;
			}
		}
		return 1;
	}

}
