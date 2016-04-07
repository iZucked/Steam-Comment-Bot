/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;

/**
 * {@link IFitnessCoreFactory} to create the cargo scheduler fitness function
 * 
 * @author Simon Goodall
 * 
 */
public final class CargoSchedulerFitnessCoreFactory implements IFitnessCoreFactory {

	@NonNull
	public static final String FITNESS_CORE_NAME = "CargoSchedulerCore";

	@NonNull
	public static final String LATENESS_COMPONENT_NAME = "cargo-scheduler-lateness";

	@NonNull
	public static final String CAPACITY_COMPONENT_NAME = "cargo-scheduler-capacity";

	@NonNull
	public static final String IDLE_TIME_HOURS_COMPONENT_NAME = "cargo-scheduler-idle-time";

	@NonNull
	public static final String COST_BASE_COMPONENT_NAME = "cargo-scheduler-cost-base";

	@NonNull
	public static final String COST_LNG_COMPONENT_NAME = "cargo-scheduler-cost-lng";

	@NonNull
	public static final String CHARTER_COST_COMPONENT_NAME = "cargo-scheduler-charter-cost";

	@NonNull
	public static final String ROUTE_PRICE_COMPONENT_NAME = "cargo-scheduler-canal-cost";

	@NonNull
	public static final String COST_COOLDOWN_COMPONENT_NAME = "cargo-scheduler-cost-cooldown";

	@NonNull
	public static final String PORT_COST_COMPONENT_NAME = "cargo-scheduler-port-cost";

	@NonNull
	public static final String PROFIT_COMPONENT_NAME = "cargo-scheduler-group-profit";

	@Inject(optional = true)
	private Iterable<ICargoFitnessComponentProvider> externalComponentProviders;

	@Override
	public Collection<String> getFitnessComponentNames() {

		final List<String> result = CollectionsUtil.makeArrayList(LATENESS_COMPONENT_NAME, CAPACITY_COMPONENT_NAME, COST_BASE_COMPONENT_NAME, COST_LNG_COMPONENT_NAME, CHARTER_COST_COMPONENT_NAME,
				ROUTE_PRICE_COMPONENT_NAME, COST_COOLDOWN_COMPONENT_NAME, PORT_COST_COMPONENT_NAME, PROFIT_COMPONENT_NAME, IDLE_TIME_HOURS_COMPONENT_NAME);

		if (externalComponentProviders != null) {
			for (final ICargoFitnessComponentProvider provider : externalComponentProviders) {
				result.add(provider.getFitnessComponentName());
			}
		}

		return result;
	}

	@Override
	public String getFitnessCoreName() {
		return FITNESS_CORE_NAME;
	}

	@Override
	public CargoSchedulerFitnessCore instantiate() {
		final CargoSchedulerFitnessCore core = new CargoSchedulerFitnessCore(externalComponentProviders);
		return core;
	}

	/**
	 */
	public Iterable<ICargoFitnessComponentProvider> getExternalComponentProviders() {
		return externalComponentProviders;
	}

	/**
	 */
	public void setExternalComponentProviders(final Iterable<ICargoFitnessComponentProvider> externalComponentProviders) {
		this.externalComponentProviders = externalComponentProviders;
	}
}
