/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints.impl;

import java.util.List;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public class MockConstraintChecker implements IConstraintChecker {

	private final String name;

	public MockConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return true;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}
}
