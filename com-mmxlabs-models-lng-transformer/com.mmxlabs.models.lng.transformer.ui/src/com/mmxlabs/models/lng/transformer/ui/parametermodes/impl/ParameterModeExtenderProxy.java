/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;

class ParameterModeExtenderProxy implements IParameterModeExtender {

	@NonNull
	private final ParameterModeExtenderExtension parameterModeExtenderExtension;

	private IParameterModeExtender extender;

	public ParameterModeExtenderProxy(@NonNull final ParameterModeExtenderExtension parameterModeExtenderExtension) {
		this.parameterModeExtenderExtension = parameterModeExtenderExtension;
	}

	@Override
	public void extend(@NonNull final OptimiserSettings optimiserSettings, String parameterMode) {

		if (extender == null) {
			extender = parameterModeExtenderExtension.createExtender();
		}

		extender.extend(optimiserSettings, parameterMode);
	}

}
