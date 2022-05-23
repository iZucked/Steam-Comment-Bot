package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

public class CounterPartyWindowConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "CounterPartyWindowChecker";

	@Override
	public @NonNull String getName() {
		return NAME;
	}

	@Override
	public @NonNull IConstraintChecker instantiate() {
		return new CounterPartyWindowChecker(NAME);
	}

}