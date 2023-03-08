/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.parser.IExpression;

@NonNullByDefault
public class NamedSeriesExpression implements IExpression<ISeries> {

	private final ISeriesContainer seriesContainer;
	private final String name;

	public NamedSeriesExpression(final ISeries series, SeriesType seriesType, final String name) {
		this(new DefaultSeriesContainer(name, seriesType, series), name);
	}

	public NamedSeriesExpression(final ISeriesContainer seriesContainer, final String name) {
		this.seriesContainer = seriesContainer;
		this.name = name;
	}

	@Override
	public ISeries evaluate() {
		return seriesContainer.get();
	}

	@Override
	public boolean canEvaluate() {
		return seriesContainer.canGet();
	}

	public String getName() {
		return name;
	}

}
