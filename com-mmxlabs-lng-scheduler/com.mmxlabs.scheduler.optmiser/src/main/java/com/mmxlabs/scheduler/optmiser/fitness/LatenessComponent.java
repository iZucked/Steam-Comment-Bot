package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;

public class LatenessComponent<T> implements IFitnessComponent<T> {

	private final CargoSchedulerFitnessCore<T> core;
	private final String name;

	public LatenessComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		this.name = name;
		this.core = core;
	}

	@Override
	public long getFitness() {
		return core.getLatenessFitness();
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
