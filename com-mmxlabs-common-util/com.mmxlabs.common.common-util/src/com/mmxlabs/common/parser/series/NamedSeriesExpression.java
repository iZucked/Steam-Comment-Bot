/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class NamedSeriesExpression implements IExpression<ISeries> {
	private final @NonNull ISeries series;

	public NamedSeriesExpression(final @NonNull ISeries series) {
		this.series = series;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return series;
	}
}
