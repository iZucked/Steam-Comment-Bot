/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

public class GroupedSlotsConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "GroupedSlotsConstraintChecker";
	
	@Override
	public @NonNull String getName() {
		return NAME;
	}

	@Override
	public @NonNull IConstraintChecker instantiate() {
		return new GroupedSlotsConstraintChecker(NAME);
	}

}
