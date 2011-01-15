/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * {@link IConstraintCheckerFactory} implementation to create
 * {@link OrderedSequenceElementsConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class OrderedSequenceElementsConstraintCheckerFactory implements
		IConstraintCheckerFactory {

	public static final String NAME = "OrderedSequenceElementsConstraintChecker";

	private final String key;

	/**
	 * Constructor taking the key to use in {@link IOptimisationData} to find
	 * the {@link IOrderedSequenceElementsDataComponentProvider}.
	 * 
	 * @param key
	 */
	public OrderedSequenceElementsConstraintCheckerFactory(final String key) {
		this.key = key;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public <T> OrderedSequenceElementsConstraintChecker<T> instantiate() {
		return new OrderedSequenceElementsConstraintChecker<T>(NAME, key);
	}

}
