/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to construct {@link ResourceAllocationConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class ResourceAllocationConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "ResourceAllocationConstraintChecker";

	@Override
	@NonNull
	public String getName() {
		return NAME;
	}

	@Override
	@NonNull
	public ResourceAllocationConstraintChecker instantiate() {
		return new ResourceAllocationConstraintChecker(NAME);
	}

}
