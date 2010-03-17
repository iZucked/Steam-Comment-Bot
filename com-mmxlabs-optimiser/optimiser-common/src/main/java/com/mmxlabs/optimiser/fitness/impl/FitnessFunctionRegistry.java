package com.mmxlabs.optimiser.fitness.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.optimiser.fitness.IFitnessCoreFactory;
import com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry;

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
			throw new RuntimeException("Fitness core name already registered: "
					+ factory.getFitnessCoreName());
		}

		coreFactoriesByCoreName.put(factory.getFitnessCoreName(), factory);

		for (final String componentName : factory.getFitnessComponentNames()) {
			if (coreFactoriesByComponentName.containsKey(factory
					.getFitnessCoreName())) {
				throw new RuntimeException(
						"Fitness component name already registered: "
								+ componentName);
			}
			coreFactoriesByComponentName.put(componentName, factory);
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
	public Set<IFitnessCoreFactory> getFitnessCoreFactories(
			final Collection<String> names) {

		final Set<IFitnessCoreFactory> factories = new HashSet<IFitnessCoreFactory>(
				names.size());

		for (final String name : names) {

			final IFitnessCoreFactory factory;

			if (coreFactoriesByComponentName.containsKey(name)) {
				factory = coreFactoriesByComponentName.get(name);
				factories.add(factory);
			}

		}

		return factories;
	}
}
