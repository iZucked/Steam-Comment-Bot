/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedslots;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link RestrictedSlotsConstraintChecker} instances.
 * 
 * @author Simon Goodall, FM
 */
public final class RestrictedSlotsConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "RestrictedSlotsConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new RestrictedSlotsConstraintChecker(NAME);
	}
}
