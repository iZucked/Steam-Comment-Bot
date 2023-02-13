/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link MaxSlotsOnSpotChartersConstraintChecker} instances.
 * 
 * @author achurchill
 */
public final class MaxSlotsOnSpotCharterConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "MaxSlotsOnSpotChartersConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new MaxSlotsOnSpotChartersConstraintChecker(NAME);
	}
}
