/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to construct {@link ResourceAllocationConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class ResourceAllocationConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "ResourceAllocationConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public ResourceAllocationConstraintChecker instantiate() {
		return new ResourceAllocationConstraintChecker(NAME);
	}

}
