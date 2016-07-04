/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.preset;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;

public class StandardMode implements IParameterModeCustomiser {

	@Override
	public void customise(@NonNull OptimiserSettings optimiserSettings) {

		optimiserSettings.setGenerateCharterOuts(false);
		optimiserSettings.setShippingOnly(false);
	}
}
