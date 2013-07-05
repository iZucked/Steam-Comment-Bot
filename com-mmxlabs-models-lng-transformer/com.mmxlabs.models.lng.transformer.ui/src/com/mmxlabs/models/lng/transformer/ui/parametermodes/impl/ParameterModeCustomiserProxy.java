package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;

class ParameterModeCustomiserProxy implements IParameterModeCustomiser {

	private final ParameterModeExtension parameterModeExtension;

	private IParameterModeCustomiser customiser;

	public ParameterModeCustomiserProxy(final ParameterModeExtension parameterModeExtension) {
		this.parameterModeExtension = parameterModeExtension;
	}

	@Override
	public void customise(@NonNull final OptimiserSettings optimiserSettings) {

		if (customiser != null) {
			customiser = parameterModeExtension.createCustomiser();
		}

		customiser.customise(optimiserSettings);
	}

}
