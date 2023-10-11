package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

public class MinLadenTimeConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "MinLadenTimeChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new MinLadenTimeConstraintChecker(NAME);
	}
}