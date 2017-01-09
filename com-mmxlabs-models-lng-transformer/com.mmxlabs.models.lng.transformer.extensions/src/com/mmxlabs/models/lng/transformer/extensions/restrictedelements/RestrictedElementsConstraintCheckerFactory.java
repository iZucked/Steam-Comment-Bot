/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link RestrictedElementsConstraintChecker} instances.
 * 
 * @author Simon Goodall
 */
public final class RestrictedElementsConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "RestrictedElementsConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new RestrictedElementsConstraintChecker(NAME);
	}
}
