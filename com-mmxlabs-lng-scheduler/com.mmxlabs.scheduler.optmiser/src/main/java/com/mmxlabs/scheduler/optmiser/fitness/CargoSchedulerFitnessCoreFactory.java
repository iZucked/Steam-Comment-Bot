package com.mmxlabs.scheduler.optmiser.fitness;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.fitness.IFitnessCoreFactory;

public class CargoSchedulerFitnessCoreFactory implements IFitnessCoreFactory {

	public static final String FITNESS_CORE_NAME = "CargoSchedulerCore";

	public static final String DISTANCE_COMPONENT_NAME = "cargo-scheduler-distance";

	@Override
	public Collection<String> getFitnessComponentNames() {
		return Collections.singletonList(DISTANCE_COMPONENT_NAME);
	}

	@Override
	public String getFitnessCoreName() {
		return FITNESS_CORE_NAME;
	}

	@Override
	public <T> IFitnessCore<T> instantiate() {
		return new CargoSchedulerFitnessCore<T>();
	}

}
