/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;

/**
 * A {@link IParameterModeCustomiser} custo
 * @since 5.0
 */
public interface IParameterModeCustomiser {

	void customise(@NonNull OptimiserSettings optimiserSettings);
}
