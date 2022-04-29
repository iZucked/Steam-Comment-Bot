/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;


public record PriceCurveKey(@NonNull String indexName, @Nullable String curveName) {

	@NonNull
	public String getIndexName() {
		return indexName;
	}

	@Nullable
	public String getCurveName() {
		return curveName;
	}
}
