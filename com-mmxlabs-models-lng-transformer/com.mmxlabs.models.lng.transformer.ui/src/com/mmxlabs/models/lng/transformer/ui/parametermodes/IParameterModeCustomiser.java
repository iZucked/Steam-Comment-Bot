package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;

/**
 * @since 5.0
 */
public interface IParameterModeCustomiser {

	void customise(@NonNull OptimiserSettings optimiserSettings);
}
