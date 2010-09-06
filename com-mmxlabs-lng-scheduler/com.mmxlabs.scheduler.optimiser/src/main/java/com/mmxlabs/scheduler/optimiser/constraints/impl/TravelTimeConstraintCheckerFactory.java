package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

public class TravelTimeConstraintCheckerFactory implements IConstraintCheckerFactory {
	public static final String NAME = "TravelTimeChecker";
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public <T> IConstraintChecker<T> instantiate() {
		return new TravelTimeConstraintChecker<T>(NAME);
	}

}
