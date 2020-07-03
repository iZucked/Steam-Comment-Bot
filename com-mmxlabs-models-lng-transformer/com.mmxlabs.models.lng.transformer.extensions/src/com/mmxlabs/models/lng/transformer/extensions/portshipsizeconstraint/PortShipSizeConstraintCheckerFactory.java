/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link PortShipSizeConstraintChecker} instances.
 */
public final class PortShipSizeConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "PortShipSizeConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new PortShipSizeConstraintChecker(NAME);
	}
}
