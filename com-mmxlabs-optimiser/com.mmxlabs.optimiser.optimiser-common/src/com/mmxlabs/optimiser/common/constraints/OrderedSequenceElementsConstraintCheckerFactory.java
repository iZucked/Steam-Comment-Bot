/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link OrderedSequenceElementsConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class OrderedSequenceElementsConstraintCheckerFactory implements IConstraintCheckerFactory {

	@NonNull
	public static final String NAME = "OrderedSequenceElementsConstraintChecker";

	@Override
	@NonNull
	public String getName() {
		return NAME;
	}

	@Override
	@NonNull
	public OrderedSequenceElementsConstraintChecker instantiate() {
		return new OrderedSequenceElementsConstraintChecker(NAME);
	}
}
