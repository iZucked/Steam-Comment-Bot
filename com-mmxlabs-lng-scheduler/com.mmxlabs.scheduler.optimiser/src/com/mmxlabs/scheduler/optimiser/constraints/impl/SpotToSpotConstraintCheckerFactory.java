/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link SpotToSpotConstraintChecker} instances.
 * 
 * @author achurchill
 */
public final class SpotToSpotConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "SpotToSpotConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new SpotToSpotConstraintChecker(NAME);
	}
}
