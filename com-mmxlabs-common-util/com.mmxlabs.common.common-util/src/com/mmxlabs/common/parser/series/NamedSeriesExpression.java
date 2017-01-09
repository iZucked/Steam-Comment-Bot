/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class NamedSeriesExpression implements IExpression<ISeries> {
	private @NonNull final ISeries series;

	public NamedSeriesExpression(final @NonNull ISeries series) {
		this.series = series;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return series;
	}
}
