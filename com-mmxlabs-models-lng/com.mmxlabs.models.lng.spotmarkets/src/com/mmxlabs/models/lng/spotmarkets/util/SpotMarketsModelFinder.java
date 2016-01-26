/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;

public class SpotMarketsModelFinder {
	private final @NonNull SpotMarketsModel spotMarketsModel;

	public SpotMarketsModelFinder(final @NonNull SpotMarketsModel spotMarketsModel) {
		this.spotMarketsModel = spotMarketsModel;
	}

	@NonNull
	public SpotMarketsModel getSpotMarketsModel() {
		return spotMarketsModel;
	}
}
