/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import com.mmxlabs.common.parser.IExpression;

public class NamedSeriesExpression implements IExpression<ISeries> {
	private ISeries series;

	public NamedSeriesExpression(final ISeries series) {
		this.series = series;
	}

	@Override
	public ISeries evaluate() {
		return series;
	}
}
