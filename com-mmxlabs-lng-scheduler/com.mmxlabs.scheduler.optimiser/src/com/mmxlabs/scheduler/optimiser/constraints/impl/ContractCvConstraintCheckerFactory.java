/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link RestrictedElementsConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public final class ContractCvConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "ContractCvConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new ContractCvConstraintChecker(NAME);
	}
}
