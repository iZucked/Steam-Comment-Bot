/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;

/**
 */
public interface IParameterModeExtender {

	void extend(@NonNull OptimiserSettings settings, @Nullable String parameterMode);
}
