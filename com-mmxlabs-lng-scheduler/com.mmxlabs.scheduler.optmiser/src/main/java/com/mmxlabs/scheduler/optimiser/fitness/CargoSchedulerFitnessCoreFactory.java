package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.fitness.IFitnessCoreFactory;

/**
 * {@link IFitnessCoreFactory} to create the cargo scheduler fitness function
 * 
 * @author Simon Goodall
 * 
 */
public final class CargoSchedulerFitnessCoreFactory implements
		IFitnessCoreFactory {

	public static final String FITNESS_CORE_NAME = "CargoSchedulerCore";

	public static final String DISTANCE_COMPONENT_NAME = "cargo-scheduler-distance";

	public static final String LATENESS_COMPONENT_NAME = "cargo-scheduler-lateness";
	
	public static final String COST_BASE_COMPONENT_NAME = "cargo-scheduler-cost-base";
	
	public static final String COST_LNG_COMPONENT_NAME = "cargo-scheduler-cost-lng";

	@Override
	public Collection<String> getFitnessComponentNames() {
		return CollectionsUtil.makeArrayList(DISTANCE_COMPONENT_NAME,
				LATENESS_COMPONENT_NAME, COST_BASE_COMPONENT_NAME, COST_LNG_COMPONENT_NAME);
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
