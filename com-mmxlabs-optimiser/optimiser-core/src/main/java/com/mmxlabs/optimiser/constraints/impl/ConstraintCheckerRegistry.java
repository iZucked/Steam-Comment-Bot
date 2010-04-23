package com.mmxlabs.optimiser.constraints.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.optimiser.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;

/**
 * An implementation of {@link IConstraintCheckerRegistry}.
 * 
 * @author Simon Goodall
 * 
 */
public final class ConstraintCheckerRegistry implements
		IConstraintCheckerRegistry {

	private final Map<String, IConstraintCheckerFactory> constraintFactoriesByName;

	public ConstraintCheckerRegistry() {
		this.constraintFactoriesByName = new HashMap<String, IConstraintCheckerFactory>();
	}

	@Override
	public void registerConstraintCheckerFactory(
			final IConstraintCheckerFactory factory) {

		if (constraintFactoriesByName.containsKey(factory.getName())) {
			throw new RuntimeException(
					"Constraint checker name already registered: "
							+ factory.getName());
		}

		constraintFactoriesByName.put(factory.getName(), factory);
	}

	@Override
	public Collection<String> getConstraintCheckerNames() {
		return constraintFactoriesByName.keySet();
	}

	@Override
	public Collection<IConstraintCheckerFactory> getConstraintCheckerFactories() {
		return constraintFactoriesByName.values();
	}

	@Override
	public Set<IConstraintCheckerFactory> getConstraintCheckerFactories(
			final Collection<String> names) {

		final Set<IConstraintCheckerFactory> factories = new HashSet<IConstraintCheckerFactory>(
				names.size());

		for (final String name : names) {

			if (constraintFactoriesByName.containsKey(name)) {
				final IConstraintCheckerFactory factory = constraintFactoriesByName
						.get(name);
				factories.add(factory);
			}
		}

		return factories;
	}

	/**
	 * Setter to register a {@link Collection} of
	 * {@link IConstraintCheckerFactory} instances.
	 * 
	 * @param factories
	 */
	public void setConstraintCheckerFactories(
			final Collection<IConstraintCheckerFactory> factories) {

		for (final IConstraintCheckerFactory factory : factories) {
			registerConstraintCheckerFactory(factory);
		}
	}
}
