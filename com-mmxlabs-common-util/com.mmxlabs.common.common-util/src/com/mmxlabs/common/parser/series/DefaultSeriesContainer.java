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
