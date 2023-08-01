/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

public class VesselUsageSlotGroupConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "VesselUsageSlotGroupChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new VesselUsageSlotGroupConstraintChecker(NAME);
	}
}