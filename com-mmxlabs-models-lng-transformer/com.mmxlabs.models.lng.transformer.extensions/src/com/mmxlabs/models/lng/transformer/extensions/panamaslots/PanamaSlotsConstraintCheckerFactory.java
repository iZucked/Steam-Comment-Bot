/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link PanamaSlotsConstraintChecker} instances.
 * 
 * @author Simon Goodall
 */
public final class PanamaSlotsConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "PanamaSlotConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new PanamaSlotsConstraintChecker(NAME);
	}
}
