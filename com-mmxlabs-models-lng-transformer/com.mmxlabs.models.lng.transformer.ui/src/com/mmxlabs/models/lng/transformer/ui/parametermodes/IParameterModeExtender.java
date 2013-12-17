package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;

/**
 * @since 6.2
 */
public interface IParameterModeExtender {

	void extend(@NonNull OptimiserSettings settings, String parameterMode);
}
