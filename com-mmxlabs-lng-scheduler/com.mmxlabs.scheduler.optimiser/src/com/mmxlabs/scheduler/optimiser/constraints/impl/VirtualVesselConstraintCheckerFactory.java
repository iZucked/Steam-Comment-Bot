/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link VirtualVesselConstraintChecker} instances.
 * 
 * @author Tom Hinton
 * 
 */
public final class VirtualVesselConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "VirtualVesselConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new VirtualVesselConstraintChecker(NAME);
	}
}
