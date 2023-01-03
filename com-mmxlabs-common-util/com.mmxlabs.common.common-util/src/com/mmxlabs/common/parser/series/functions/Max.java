/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.parser.series.ISeries;

public class Max extends SimpleSeriesFunction {
	private Comparator<? super Number> comparator = (o1, o2) -> Double.compare(o1.doubleValue(), o2.doubleValue());

	public Max(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {
		final List<Number> values = new ArrayList<>(arguments.size());

		for (final ISeries s : arguments) {
			values.add(s.evaluate(timePoint, params));
		}

		return Collections.max(values, comparator);
	}

}
