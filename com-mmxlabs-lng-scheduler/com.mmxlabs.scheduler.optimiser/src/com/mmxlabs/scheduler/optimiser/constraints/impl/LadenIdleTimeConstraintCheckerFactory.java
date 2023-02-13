/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link TimeSortConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class LadenIdleTimeConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "LadenIdleTimeConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new LadenIdleTimeConstraintChecker(NAME);
	}
}
