/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponentInstantiator;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;

/**
 * Class used to obtain {@link IFitnessComponent}s from the {@link IFitnessFunctionRegistry}
 * 
 * @author Simon Goodall
 * 
 */
public final class FitnessComponentInstantiator implements IFitnessComponentInstantiator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.fitness.impl.IFitnessComponentInstantiator#instantiateFitnesses(com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry)
	 */
	@Override
	@NonNull
	public List<IFitnessComponent> instantiateFitnesses(@NonNull final IFitnessFunctionRegistry registry) {

		final List<IFitnessComponent> components = new ArrayList<IFitnessComponent>();

		final Collection<IFitnessCoreFactory> factories = registry.getFitnessCoreFactories();
		for (final IFitnessCoreFactory factory : factories) {
			final IFitnessCore core = factory.instantiate();

			components.addAll(core.getFitnessComponents());
		}
		return components;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.fitness.impl.IFitnessComponentInstantiator#instantiateFitnesses(com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry, java.util.List)
	 */
	@Override
	@NonNull
	public List<IFitnessComponent> instantiateFitnesses(@NonNull final IFitnessFunctionRegistry registry, @NonNull final List<String> componentNames) {

		final List<IFitnessComponent> components = new ArrayList<IFitnessComponent>(componentNames.size());

		// Copy component names into Set for quick lookup.
		final Set<String> componentNamesSet = new HashSet<String>(componentNames);

		// Mapping between component name and component instance
		final Map<String, IFitnessComponent> fitnessComponentsMap = new HashMap<String, IFitnessComponent>();

		// Process factories finding cores which contain a referenced component.
		// Once found, instantiate cores and add the components to the map.
		final Collection<IFitnessCoreFactory> factories = registry.getFitnessCoreFactories();
		LOOP_FACTORY: for (final IFitnessCoreFactory factory : factories) {

			final Collection<String> fitnessComponentNames = factory.getFitnessComponentNames();
			// Loop over component names
			for (final String name : fitnessComponentNames) {
				// Is this component required?
				if (componentNamesSet.contains(name)) {
					// Component is required, so instantiate the core and add
					// all components to the map.
					final IFitnessCore core = factory.instantiate();
					for (final IFitnessComponent component : core.getFitnessComponents()) {
						fitnessComponentsMap.put(component.getName(), component);
					}
					continue LOOP_FACTORY;
				}
			}
		}

		// Generate the ordered list of fitness components.
		for (final String name : componentNames) {

			final IFitnessComponent component;
			if (fitnessComponentsMap.containsKey(name)) {
				component = fitnessComponentsMap.get(name);
				components.add(component);
			} else {
				component = null;
			}
		}

		return components;
	}
}
