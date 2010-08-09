package com.mmxlabs.optimiser.common.fitness;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;


public final class MatrixProviderFitnessComponent<T> implements
		IFitnessComponent<T> {

	private final String name;
	private final MatrixProviderFitnessCore<T> core;

	public MatrixProviderFitnessComponent(final String name,
			final MatrixProviderFitnessCore<T> core) {
		this.name = name;
		this.core = core;
	}

	@Override
	public long getFitness() {
		return core.getNewFitness();
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
