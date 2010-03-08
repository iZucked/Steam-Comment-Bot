package com.mmxlabs.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.fitness.IFitnessFunctionFactory;
import com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry;

/**
 * An implementation of {@link IFitnessFunctionRegistry}.
 * 
 * @author Simon Goodall
 * 
 */
public final class FitnessFunctionRegistry implements
		IFitnessFunctionRegistry {

	private final Map<String, IFitnessFunctionFactory> functionFactories;

	public FitnessFunctionRegistry() {
		this.functionFactories = new HashMap<String, IFitnessFunctionFactory>();
	}

	@Override
	public void registerFitnessFunction(final String name,
			final IFitnessFunctionFactory factory) {

		if (functionFactories.containsKey(name)) {
			throw new RuntimeException("Factory already registered");
		}

		functionFactories.put(name, factory);
	}

	@Override
	public Collection<String> getFitnessFunctionFactoryNames() {
		return functionFactories.keySet();
	}

	@Override
	public Collection<IFitnessFunctionFactory> getFitnessFunctionFactories() {
		return functionFactories.values();
	}

	@Override
	public List<IFitnessFunctionFactory> getFitnessFunctionFactories(
			final List<String> names) {

		final List<IFitnessFunctionFactory> factories = new ArrayList<IFitnessFunctionFactory>(
				names.size());

		for (final String name : names) {

			final IFitnessFunctionFactory factory;

			if (functionFactories.containsKey(name)) {
				factory = functionFactories.get(name);
			} else {
				// TODO: Should this just set null, or throw an exception?
				factory = null;
			}

			factories.add(factory);
		}

		return factories;
	}
}
