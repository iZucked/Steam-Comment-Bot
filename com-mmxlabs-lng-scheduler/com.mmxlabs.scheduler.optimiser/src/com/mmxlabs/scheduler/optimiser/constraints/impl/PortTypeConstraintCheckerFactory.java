/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link PortTypeConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class PortTypeConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "PortTypeConstraintChecker";


	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new PortTypeConstraintChecker(NAME);
	}
}
