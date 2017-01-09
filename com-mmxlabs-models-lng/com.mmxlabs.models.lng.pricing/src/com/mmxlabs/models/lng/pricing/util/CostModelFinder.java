/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.pricing.CostModel;

public class CostModelFinder {
	private final @NonNull CostModel costModel;

	public CostModelFinder(final @NonNull CostModel costModel) {
		this.costModel = costModel;
	}

	@NonNull
	public CostModel getCostModel() {
		return costModel;
	}
}
