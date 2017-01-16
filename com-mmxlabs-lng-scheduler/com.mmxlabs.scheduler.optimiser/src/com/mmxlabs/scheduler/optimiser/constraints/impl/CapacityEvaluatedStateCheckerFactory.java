/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerFactory;

public final class CapacityEvaluatedStateCheckerFactory implements IEvaluatedStateConstraintCheckerFactory {

	public static final @NonNull String Name = "CapacityEvaluatedStateChecker";

	@Override
	public @NonNull String getName() {
		return Name;
	}

	@Override
	public @NonNull IEvaluatedStateConstraintChecker instantiate() {
		return new CapacityEvaluatedStateChecker(Name);
	}

}
