/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

public final class MockFitnessComponent implements IFitnessComponent {

	private final String name;

	private final IFitnessCore core;

	public MockFitnessComponent(final String name, final IFitnessCore core) {
		this.name = name;
		this.core = core;
	}

	@Override
	public long getFitness() {
		return 0;
	}

	@Override
	public IFitnessCore getFitnessCore() {
		return core;
	}

	@Override
	public String getName() {
		return name;
	}
}
