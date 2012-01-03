/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;

/**
 * An implementation of {@link IFitnessFunctionRegistry}.
 * 
 * @author Simon Goodall
 * 
 */
public final class FitnessFunctionRegistry implements IFitnessFunctionRegistry {

	private final Map<String, IFitnessCoreFactory> coreFactoriesByCoreName;
	private final Map<String, IFitnessCoreFactory> coreFactoriesByComponentName;

	public FitnessFunctionRegistry() {
		this.coreFactoriesByCoreName = new HashMap<String, IFitnessCoreFactory>();
		this.coreFactoriesByComponentName = new HashMap<String, IFitnessCoreFactory>();
	}

	@Override
	public void registerFitnessCoreFactory(final IFitnessCoreFactory factory) {

		if (coreFactoriesByCoreName.containsKey(factory.getFitnessCoreName())) {
			throw new RuntimeException("Fitness core name already registered: " + factory.getFitnessCoreName());
		}

		coreFactoriesByCoreName.put(factory.getFitnessCoreName(), factory);

		for (final String componentName : factory.getFitnessComponentNames()) {
			if (coreFactoriesByComponentName.containsKey(componentName)) {
				throw new RuntimeException("Fitness component name already registered: " + componentName);
			}
			coreFactoriesByComponentName.put(componentName, factory);
		}
	}

	@Override
	public void deregisterFitnessCoreFactory(final IFitnessCoreFactory factory) {

		coreFactoriesByCoreName.remove(factory.getFitnessCoreName());

		for (final String componentName : factory.getFitnessComponentNames()) {
			coreFactoriesByComponentName.remove(componentName);
		}
	}

	@Override
	public Collection<String> getFitnessCoreFactoryNames() {
		return coreFactoriesByCoreName.keySet();
	}

	@Override
	public Collection<String> getFitnessComponentNames() {
		return coreFactoriesByComponentName.keySet();
	}

	@Override
	public Collection<IFitnessCoreFactory> getFitnessCoreFactories() {
		return coreFactoriesByCoreName.values();
	}

	@Override
	public Set<IFitnessCoreFactory> getFitnessCoreFactories(final Collection<String> names) {

		final Set<IFitnessCoreFactory> factories = new HashSet<IFitnessCoreFactory>(names.size());

		for (final String name : names) {

			if (coreFactoriesByComponentName.containsKey(name)) {
				final IFitnessCoreFactory factory = coreFactoriesByComponentName.get(name);
				factories.add(factory);
			}
		}

		return factories;
	}

	/**
	 * Setter to register a {@link Collection} of {@link IFitnessCoreFactory} instances.
	 * 
	 * @param factories
	 */
	public void setFitnessCoreFactories(final Collection<IFitnessCoreFactory> factories) {

		for (final IFitnessCoreFactory factory : factories) {
			registerFitnessCoreFactory(factory);
		}
	}
}
