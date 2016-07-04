/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;

class ParameterModeCustomiserProxy implements IParameterModeCustomiser {

	@NonNull
	private final ParameterModeExtension parameterModeExtension;

	private IParameterModeCustomiser customiser;

	public ParameterModeCustomiserProxy(@NonNull final ParameterModeExtension parameterModeExtension) {
		this.parameterModeExtension = parameterModeExtension;
	}

	@Override
	public void customise(@NonNull final OptimiserSettings optimiserSettings) {

		if (customiser == null) {
			customiser = parameterModeExtension.createCustomiser();
		}

		customiser.customise(optimiserSettings);
	}

}
