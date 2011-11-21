/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
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

	public static final String ROUTE_PRICE_COMPONENT_NAME = "cargo-scheduler-canal-cost";

	// public static final String CARGO_ALLOCATION_COMPONENT_NAME = "cargo-scheduler-volume-allocation";

	public static final String CHARTER_REVENUE_COMPONENT_NAME = "cargo-scheduler-charter-revenue";

	public static final String COST_COOLDOWN_COMPONENT_NAME = "cargo-scheduler-cost-cooldown";

	public static final String PROFIT_COMPONENT_NAME = "cargo-scheduler-group-profit";

	/* default scheduler factory creates default GA scheduler */
	// TODO: Make static class
	private ISchedulerFactory<ISequenceElement> schedulerFactory = new ISchedulerFactory<ISequenceElement>() {
		@Override
		public ISequenceScheduler<ISequenceElement> createScheduler(IOptimisationData<ISequenceElement> data, Collection<ICargoSchedulerFitnessComponent<ISequenceElement>> schedulerComponents,
				Collection<ICargoAllocationFitnessComponent<ISequenceElement>> allocationComponents) {
			return SchedulerUtils.createDirectRandomSequenceScheduler(data, schedulerComponents, allocationComponents);
			// return SchedulerUtils.createRelaxingSequenceScheduler(data,
			// components);
		}
	};

	@Override
	public Collection<String> getFitnessComponentNames() {
		return CollectionsUtil.makeArrayList(DISTANCE_COMPONENT_NAME,
				LATENESS_COMPONENT_NAME, COST_BASE_COMPONENT_NAME,
				COST_LNG_COMPONENT_NAME, CHARTER_COST_COMPONENT_NAME,
				ROUTE_PRICE_COMPONENT_NAME,
 COST_COOLDOWN_COMPONENT_NAME,
 CHARTER_REVENUE_COMPONENT_NAME, PROFIT_COMPONENT_NAME);
	}

	@Override
	public String getFitnessCoreName() {
		return FITNESS_CORE_NAME;
	}

	@Override
	public <T> CargoSchedulerFitnessCore<T> instantiate() {
		CargoSchedulerFitnessCore<T> core = new CargoSchedulerFitnessCore<T>();
		core.setSchedulerFactory((ISchedulerFactory<T>) schedulerFactory);
		return core;
	}

	public void setSchedulerFactory(final ISchedulerFactory schedulerFactory) {
		this.schedulerFactory = schedulerFactory;
	}

	public ISchedulerFactory getSchedulerFactory() {
		return schedulerFactory;
	}
}
