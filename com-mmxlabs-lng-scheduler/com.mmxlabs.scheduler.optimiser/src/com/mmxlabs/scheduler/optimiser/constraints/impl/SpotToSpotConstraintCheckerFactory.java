/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link SpotToSpotConstraintChecker} instances.
 * 
 * @author achurchill
 */
public final class SpotToSpotConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
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
