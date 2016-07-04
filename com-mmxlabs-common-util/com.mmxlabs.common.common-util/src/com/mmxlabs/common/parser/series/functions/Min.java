/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mmxlabs.common.parser.series.ISeries;

public class Min extends SimpleSeriesFunction {
	private Comparator<? super Number> comparator = new Comparator<Number>() {
		@Override
		public int compare(Number o1, Number o2) {
			return ((Double) o1.doubleValue()).compareTo(o2.doubleValue());
		}
	};

	public Min(List<ISeries> arguments) {
		super(arguments);
	}

	@Override
	public Number evaluate(int point) {
		final List<Number> values = new ArrayList<Number>(arguments.size());

		for (final ISeries s : arguments) {
			values.add(s.evaluate(point));
		}

		return Collections.min(values, comparator);
	}
}
