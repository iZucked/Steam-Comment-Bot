/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class NamedSeriesExpression implements IExpression<ISeries> {
	
	@NonNull
	private final ISeriesContainer seriesContainer;

	public NamedSeriesExpression(final @NonNull ISeries series) {
		this.seriesContainer = new DefaultSeriesContainer(series);
	}

	public NamedSeriesExpression(@NonNull final ISeriesContainer seriesContainer) {
		this.seriesContainer = seriesContainer;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return seriesContainer.get();
	}

	@Override
	public boolean canEvaluate() {
		return seriesContainer.canGet();
	}
}
