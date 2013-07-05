package com.mmxlabs.models.lng.transformer.ui.parametermodes.preset;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;

class GenerateCharterOutsMode implements IParameterModeCustomiser {

	@Override
	public void customise(@NonNull OptimiserSettings optimiserSettings) {

		optimiserSettings.setGenerateCharterOuts(true);
		optimiserSettings.setShippingOnly(false);
	}
}
