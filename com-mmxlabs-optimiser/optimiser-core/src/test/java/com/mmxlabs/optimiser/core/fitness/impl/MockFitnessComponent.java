package com.mmxlabs.optimiser.core.fitness.impl;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

public final class MockFitnessComponent<T> implements IFitnessComponent<T> {

	private final String name;

	private final IFitnessCore<T> core;

	public MockFitnessComponent(final String name, final IFitnessCore<T> core) {
		this.name = name;
		this.core = core;
	}

	@Override
	public long getFitness() {
		return 0;
	}

	@Override
	public IFitnessCore<T> getFitnessCore() {
		return core;
	}

	@Override
	public String getName() {
		return name;
	}
}
