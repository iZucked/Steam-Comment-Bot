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
	private final @NonNull String name;

	public NamedSeriesExpression(final @NonNull ISeries series, SeriesType seriesType, final String name) {
		this(new DefaultSeriesContainer(name, seriesType, series), name);
	}

	public NamedSeriesExpression(@NonNull final ISeriesContainer seriesContainer, final String name) {
		this.seriesContainer = seriesContainer;
		this.name = name;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return seriesContainer.get();
	}

	@Override
	public boolean canEvaluate() {
		return seriesContainer.canGet();
	}

	public @NonNull String getName() {
		return name;
	}

}
