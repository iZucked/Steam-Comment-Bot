/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.actuals.ActualsModel;

public class ActualsModelFinder {
	private final @NonNull ActualsModel actualsModel;

	public ActualsModelFinder(final @NonNull ActualsModel actualsModel) {
		this.actualsModel = actualsModel;
	}

	@NonNull
	public ActualsModel getActualsModel() {
		return actualsModel;
	}
}
