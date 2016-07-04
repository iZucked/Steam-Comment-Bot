/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public class FitnessFunctionInstantiatorModule extends AbstractModule {

	public static final String ENABLED_FITNESS_NAMES = "EnabledFitnessNames";

	@Override
	protected void configure() {

	}

	@Provides
	private List<IFitnessComponent> provideFitnessComponents(@NonNull final Injector injector, @NonNull final IFitnessHelper fitnessHelper,
			@NonNull final IFitnessFunctionRegistry fitnessFunctionRegistry, @Named(ENABLED_FITNESS_NAMES) @NonNull final List<String> enabledFitnessNames,
			final @NonNull IOptimisationData optimisationData) {

		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
		final List<IFitnessComponent> fitnessComponents = fitnessComponentInstantiator.instantiateFitnesses(fitnessFunctionRegistry, enabledFitnessNames);
		final Set<IFitnessCore> cores = new HashSet<IFitnessCore>();

		final List<IFitnessComponent> result = new ArrayList<IFitnessComponent>(fitnessComponents.size());
		for (final IFitnessComponent c : fitnessComponents) {
			if (c != null) {
				result.add(c);
				injector.injectMembers(c);
				cores.add(c.getFitnessCore());
			}
		}

		for (final IFitnessCore c : cores) {
			injector.injectMembers(c);
		}

		// Initialise the fitness functions
		fitnessHelper.initFitnessComponents(result, optimisationData);

		return result;
	}

}
