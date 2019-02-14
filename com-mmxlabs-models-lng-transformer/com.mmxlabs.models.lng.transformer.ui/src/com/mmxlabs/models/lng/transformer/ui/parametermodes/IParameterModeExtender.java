/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimisationPlan;

/**
 * An extender is intended to add in client specific constraints, fitnesses that should always be present in an optimisation.
 */
public interface IParameterModeExtender {

	void extend(@NonNull OptimisationPlan optimisationPlan);
}
