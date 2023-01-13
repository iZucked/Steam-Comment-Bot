/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class DefaultSeriesContainer implements ISeriesContainer {

	private final ISeries series;
	private final String name;
	private final SeriesType seriesType;

	public DefaultSeriesContainer(final String name, final SeriesType seriesType, final ISeries series) {
		this.name = name;
		this.seriesType = seriesType;
		this.series = series;
	}

	@Override
	public ISeries get() {
		return this.series;
	}

	@Override
	public boolean canGet() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public SeriesType getType() {
		return seriesType;
	}
}
