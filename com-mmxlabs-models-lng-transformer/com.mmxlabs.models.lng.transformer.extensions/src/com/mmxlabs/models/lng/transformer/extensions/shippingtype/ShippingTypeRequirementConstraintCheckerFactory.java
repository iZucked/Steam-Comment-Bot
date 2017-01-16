/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link ShippingTypeRequirementConstraintChecker} instances.
 * 
 * @author Simon Goodall
 */
public final class ShippingTypeRequirementConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "ShippingTypeRequirementConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new ShippingTypeRequirementConstraintChecker(NAME);
	}
}
