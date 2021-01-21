/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimisationPlan;

/**
 * A {@link IParameterModeCustomiser} customiser is intended to set default parameter overrides for a client. E.g. iterations. These should not be reun when using e.g. the headless optimiser where
 * these kind of settings are specified via alternative inputs.
 */
public interface IParameterModeCustomiser {

	void customise(@NonNull OptimisationPlan optimisationPlan);
}
