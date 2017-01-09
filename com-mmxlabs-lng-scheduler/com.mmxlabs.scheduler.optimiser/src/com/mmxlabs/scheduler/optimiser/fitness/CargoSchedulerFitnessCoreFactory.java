/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
	public static final String PROFIT_COMPONENT_NAME = "cargo-scheduler-group-profit";

	@Inject(optional = true)
	private Iterable<ICargoFitnessComponentProvider> externalComponentProviders;

	@Override
	public Collection<String> getFitnessComponentNames() {

		final List<String> result = CollectionsUtil.makeArrayList(LATENESS_COMPONENT_NAME, CAPACITY_COMPONENT_NAME, PROFIT_COMPONENT_NAME, IDLE_TIME_HOURS_COMPONENT_NAME);

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
