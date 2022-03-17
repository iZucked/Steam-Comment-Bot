/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link PortTypeConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class PortTypeConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "PortTypeConstraintChecker";

	@Override
	@NonNull
	public String getName() {
		return NAME;
	}

	@Override
	@NonNull
	public IConstraintChecker instantiate() {
		return new PortTypeConstraintChecker(NAME);
	}
}
