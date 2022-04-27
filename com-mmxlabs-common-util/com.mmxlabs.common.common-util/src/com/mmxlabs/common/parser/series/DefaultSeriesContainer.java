/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

public class DefaultSeriesContainer implements ISeriesContainer {

	@NonNull
	private final ISeries series;

	public DefaultSeriesContainer(@NonNull final ISeries series) {
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

}
