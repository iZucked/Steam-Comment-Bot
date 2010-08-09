package com.mmxlabs.optimiser.common.constraints;

import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

public final class OrderedSequenceElementsConstraintCheckerFactory implements
		IConstraintCheckerFactory {

	public static final String NAME = "OrderdSequenceElementsConstraintChecker";

	private final String key;

	public OrderedSequenceElementsConstraintCheckerFactory(String key) {
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
