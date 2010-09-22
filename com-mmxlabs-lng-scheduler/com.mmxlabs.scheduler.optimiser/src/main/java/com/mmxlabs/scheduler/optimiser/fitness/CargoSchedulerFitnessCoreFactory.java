package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SchedulerUtils;

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

	public static final String CHARTER_COST_COMPONENT_NAME = "cargo-scheduler-charter-cost";

	public static final String ROUTE_PRICE_COMPONENT_NAME = "cargo-scheduler-route-price";

	/* default scheduler factory creates default GA scheduler */
	private ISchedulerFactory schedulerFactory =
		new ISchedulerFactory() {

			@Override
			public ISequenceScheduler createScheduler(IOptimisationData data,
					Collection components) {
				return SchedulerUtils.createGASequenceScheduler(data, components);
			}
	};

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
	public <T> CargoSchedulerFitnessCore<T> instantiate() {
		CargoSchedulerFitnessCore<T> core = new CargoSchedulerFitnessCore<T>();
		core.setSchedulerFactory(schedulerFactory);
		return core;
	}
	
	public void setSchedulerFactory(final ISchedulerFactory schedulerFactory) {
		this.schedulerFactory = schedulerFactory;
	}
}
