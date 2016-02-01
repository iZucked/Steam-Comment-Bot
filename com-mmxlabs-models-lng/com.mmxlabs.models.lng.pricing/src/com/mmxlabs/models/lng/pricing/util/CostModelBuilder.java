/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.pricing.CostModel;

public class CostModelBuilder {

	private final @NonNull CostModel costModel;

	public CostModelBuilder(@NonNull final CostModel costModel) {
		this.costModel = costModel;
	}
}
