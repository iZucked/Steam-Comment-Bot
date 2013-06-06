/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

public class DifferentSTSVesselsConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "DifferentSTSVesselsConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new DifferentSTSVesselsConstraintChecker(NAME);
	}
}
