package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;

public class DistanceComponent<T> implements IFitnessComponent<T> {

	private final CargoSchedulerFitnessCore<T> core;
	private final String name;

	public DistanceComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		this.name = name;
		this.core = core;
	}

	@Override
	public long getFitness() {
		return core.getDistanceFitness();
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
