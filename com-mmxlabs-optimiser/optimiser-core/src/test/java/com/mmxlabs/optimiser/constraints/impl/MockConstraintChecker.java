package com.mmxlabs.optimiser.constraints.impl;

import java.util.List;

import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

public class MockConstraintChecker<T> implements IConstraintChecker<T> {

	private final String name;

	public MockConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public boolean checkConstraints(final ISequences<T> sequences) {
		return true;
	}

	@Override
	public boolean checkConstraints(final ISequences<T> sequences,
			final List<String> messages) {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setOptimisationData(final IOptimisationData<T> optimisationData) {

	}
}
