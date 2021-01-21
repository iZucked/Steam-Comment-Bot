/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
public final class TimeSortConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "TimeSortConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new TimeSortConstraintChecker(NAME);
	}
}
