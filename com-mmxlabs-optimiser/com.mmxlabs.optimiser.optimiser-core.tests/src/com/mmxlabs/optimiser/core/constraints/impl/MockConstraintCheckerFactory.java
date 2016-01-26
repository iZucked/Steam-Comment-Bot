/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;

/**
 * Mock implementation of an {@link IFitnessCoreFactory} which just return the core name and a {@link Collection} of component names.
 * 
 * @author Simon Goodall
 * 
 */
public class MockConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	private final String checkerName;

	public MockConstraintCheckerFactory(@NonNull final String checkerName) {
		this.checkerName = checkerName;
	}

	@Override
	@NonNull
	public IConstraintChecker instantiate() {
		return new MockConstraintChecker(checkerName);
	}

	@Override
	@NonNull
	public String getName() {
		return checkerName;
	}
}