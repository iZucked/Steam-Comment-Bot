/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimisationPlan;

/**
 */
public interface IParameterModeExtender {

	void extend(@NonNull OptimisationPlan optimisationPlan);
}
