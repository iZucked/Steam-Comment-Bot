/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link OrderedSequenceElementsConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class OrderedSequenceElementsConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "OrderedSequenceElementsConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public OrderedSequenceElementsConstraintChecker instantiate() {
		return new OrderedSequenceElementsConstraintChecker(NAME);
	}
}
