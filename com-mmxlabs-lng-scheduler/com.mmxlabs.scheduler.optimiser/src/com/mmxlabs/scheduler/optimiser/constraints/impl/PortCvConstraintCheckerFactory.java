/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link RestrictedElementsConstraintChecker} instances.
 * 
 * @author Simon Goodall
 */
public final class PortCvConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "PortCvConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new PortCvConstraintChecker(NAME);
	}
}
