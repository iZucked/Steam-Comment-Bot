package com.acme.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acme.optimiser.fitness.IFitnessFunctionFactory;
import com.acme.optimiser.fitness.IFitnessFunctionRegistery;

/**
 * An implementation of {@link IFitnessFunctionRegistery}.
 * 
 * @author Simon Goodall
 * 
 */
public final class FitnessFunctionRegistery implements
		IFitnessFunctionRegistery {

	private final Map<String, IFitnessFunctionFactory> functionFactories;

	public FitnessFunctionRegistery() {
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
