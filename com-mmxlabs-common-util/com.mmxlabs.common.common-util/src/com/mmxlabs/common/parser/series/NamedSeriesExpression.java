/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
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

	@Override
	public @NonNull ISeries evaluate(Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime) {
		return series;
	}
}
