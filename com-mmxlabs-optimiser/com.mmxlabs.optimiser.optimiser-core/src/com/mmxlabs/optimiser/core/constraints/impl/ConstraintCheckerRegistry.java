/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;

/**
 * An implementation of {@link IConstraintCheckerRegistry}.
 * 
 * @author Simon Goodall
 * 
 */
public final class ConstraintCheckerRegistry implements IConstraintCheckerRegistry {

	@NonNull
	private final Map<String, IConstraintCheckerFactory> constraintFactoriesByName = new HashMap<>();

	@Override
	public void registerConstraintCheckerFactory(@NonNull final IConstraintCheckerFactory factory) {

		if (constraintFactoriesByName.containsKey(factory.getName())) {
			throw new RuntimeException("Constraint checker name already registered: " + factory.getName());
		}

		constraintFactoriesByName.put(factory.getName(), factory);
	}

	@Override
	public void deregisterConstraintCheckerFactory(@NonNull final IConstraintCheckerFactory factory) {

		constraintFactoriesByName.remove(factory.getName());
	}

	@SuppressWarnings("null")
	@Override
	@NonNull
	public Collection<@NonNull String> getConstraintCheckerNames() {
		return constraintFactoriesByName.keySet();
	}

	@SuppressWarnings("null")
	@Override
	@NonNull
	public Collection<@NonNull IConstraintCheckerFactory> getConstraintCheckerFactories() {
		return constraintFactoriesByName.values();
	}

	@Override
	@NonNull
	public Set<@NonNull IConstraintCheckerFactory> getConstraintCheckerFactories(@NonNull final Collection<String> names) {

		final Set<IConstraintCheckerFactory> factories = new HashSet<IConstraintCheckerFactory>(names.size());

		for (final String name : names) {

			if (constraintFactoriesByName.containsKey(name)) {
				final IConstraintCheckerFactory factory = constraintFactoriesByName.get(name);
				factories.add(factory);
			}
		}

		return factories;
	}

	/**
	 * Setter to register a {@link Collection} of {@link IConstraintCheckerFactory} instances.
	 * 
	 * @param factories
	 */
	public void setConstraintCheckerFactories(@NonNull final Collection<IConstraintCheckerFactory> factories) {

		for (final IConstraintCheckerFactory factory : factories) {
			if (factory != null) {
				registerConstraintCheckerFactory(factory);
			}
		}
	}
}
