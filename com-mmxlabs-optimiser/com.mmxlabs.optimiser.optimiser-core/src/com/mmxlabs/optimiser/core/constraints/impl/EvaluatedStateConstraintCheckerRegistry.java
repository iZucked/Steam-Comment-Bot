/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;

/**
 * An implementation of {@link IConstraintCheckerRegistry}.
 * 
 * @author Simon Goodall
 * 
 */
public final class EvaluatedStateConstraintCheckerRegistry implements IEvaluatedStateConstraintCheckerRegistry {

	@NonNull
	private final Map<@NonNull String, @NonNull IEvaluatedStateConstraintCheckerFactory> constraintFactoriesByName = new HashMap<>();

	@Override
	public void registerConstraintCheckerFactory(@NonNull final IEvaluatedStateConstraintCheckerFactory factory) {

		if (constraintFactoriesByName.containsKey(factory.getName())) {
			throw new RuntimeException("Constraint checker name already registered: " + factory.getName());
		}

		constraintFactoriesByName.put(factory.getName(), factory);
	}

	@Override
	public void deregisterConstraintCheckerFactory(@NonNull final IEvaluatedStateConstraintCheckerFactory factory) {

		constraintFactoriesByName.remove(factory.getName());
	}

	@Override
	@NonNull
	public Collection<@NonNull String> getConstraintCheckerNames() {
		return constraintFactoriesByName.keySet();
	}

	@Override
	@NonNull
	public Collection<@NonNull IEvaluatedStateConstraintCheckerFactory> getConstraintCheckerFactories() {
		return constraintFactoriesByName.values();
	}

	@Override
	@NonNull
	public Set<@NonNull IEvaluatedStateConstraintCheckerFactory> getConstraintCheckerFactories(@NonNull final Collection<@NonNull String> names) {

		final Set<@NonNull IEvaluatedStateConstraintCheckerFactory> factories = new HashSet<>(names.size());

		for (final String name : names) {

			if (constraintFactoriesByName.containsKey(name)) {
				final IEvaluatedStateConstraintCheckerFactory factory = constraintFactoriesByName.get(name);
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
	public void setConstraintCheckerFactories(@NonNull final Collection<@NonNull IEvaluatedStateConstraintCheckerFactory> factories) {

		for (final IEvaluatedStateConstraintCheckerFactory factory : factories) {
			if (factory != null) {
				registerConstraintCheckerFactory(factory);
			}
		}
	}
}
