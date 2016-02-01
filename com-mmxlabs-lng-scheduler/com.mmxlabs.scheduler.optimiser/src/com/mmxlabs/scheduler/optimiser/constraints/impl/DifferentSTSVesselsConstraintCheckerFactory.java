/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * Factory for the {@link DifferentSTSVesselsConstraintChecker}
 * 
 */
public class DifferentSTSVesselsConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "DifferentSTSVesselsConstraintChecker";

	@Override
	@NonNull
	public String getName() {
		return NAME;
	}

	@Override
	@NonNull
	public IConstraintChecker instantiate() {
		return new DifferentSTSVesselsConstraintChecker(NAME);
	}
}
