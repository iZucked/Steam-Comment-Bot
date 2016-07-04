/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to construct {@link LockedUnusedElementsConstraintChecker} instances.
 * 
 * @author achurchill
 * 
 */
public final class LockedUnusedElementsConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "LockedUnusedElementsConstraintChecker";

	@Override
	@NonNull
	public String getName() {
		return NAME;
	}

	@Override
	@NonNull
	public LockedUnusedElementsConstraintChecker instantiate() {
		return new LockedUnusedElementsConstraintChecker(NAME);
	}

}
