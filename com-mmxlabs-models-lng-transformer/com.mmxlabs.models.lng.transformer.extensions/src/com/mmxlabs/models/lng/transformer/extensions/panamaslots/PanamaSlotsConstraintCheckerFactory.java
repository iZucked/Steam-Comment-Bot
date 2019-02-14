/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link PanamaSlotsConstraintChecker} instances.
 * 
 * @author Simon Goodall
 */
public final class PanamaSlotsConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final @NonNull String NAME = "PanamaSlotConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new PanamaSlotsConstraintChecker(NAME);
	}
}
