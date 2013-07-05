package com.mmxlabs.models.lng.transformer.ui.parametermodes.preset;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;

class ShippingOnlyMode implements IParameterModeCustomiser {

	@Override
	public void customise(@NonNull OptimiserSettings optimiserSettings) {

		optimiserSettings.setGenerateCharterOuts(false);
		optimiserSettings.setShippingOnly(true);
	}
}
