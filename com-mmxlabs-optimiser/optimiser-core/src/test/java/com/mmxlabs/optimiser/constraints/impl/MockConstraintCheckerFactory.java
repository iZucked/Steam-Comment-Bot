package com.mmxlabs.optimiser.constraints.impl;

import java.util.Collection;

import com.mmxlabs.optimiser.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.fitness.IFitnessCoreFactory;

/**
 * Mock implementation of an {@link IFitnessCoreFactory} which just return the
 * core name and a {@link Collection} of component names.
 * 
 * @author Simon Goodall
 * 
 */
public class MockConstraintCheckerFactory implements IConstraintCheckerFactory {

	private final String checkerName;

	public MockConstraintCheckerFactory(final String checkerName) {
		this.checkerName = checkerName;
	}

	@Override
	public <T> IConstraintChecker<T> instantiate() {
		return new MockConstraintChecker<T>(checkerName);
	}

	@Override
	public String getName() {
		return checkerName;
	}
}