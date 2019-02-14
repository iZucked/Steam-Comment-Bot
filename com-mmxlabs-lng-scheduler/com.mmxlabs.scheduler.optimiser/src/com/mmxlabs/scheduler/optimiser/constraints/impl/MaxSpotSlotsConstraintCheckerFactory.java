/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link MaxSpotSlotsConstraintChecker} instances.
 * 
 * @author achurchill
 */
public final class MaxSpotSlotsConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "MaxSpotSlotsConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new MaxSpotSlotsConstraintChecker(NAME);
	}
}
