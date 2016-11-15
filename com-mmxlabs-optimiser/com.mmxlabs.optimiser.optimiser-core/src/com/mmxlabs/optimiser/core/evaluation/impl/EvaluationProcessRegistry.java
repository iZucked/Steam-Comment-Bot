/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.evaluation.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessFactory;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;

/**
 * An implementation of {@link IEvaluationProcessRegistry}.
 * 
 * @author Simon Goodall
 * 
 */
public final class EvaluationProcessRegistry implements IEvaluationProcessRegistry {

	private final Map<String, IEvaluationProcessFactory> factoriesByName;

	public EvaluationProcessRegistry() {
		this.factoriesByName = new HashMap<String, IEvaluationProcessFactory>();
	}

	@Override
	public void registerEvaluationProcessFactory(@NonNull final IEvaluationProcessFactory factory) {

		if (factoriesByName.containsKey(factory.getName())) {
			throw new RuntimeException("Evaluation process name already registered: " + factory.getName());
		}

		factoriesByName.put(factory.getName(), factory);
	}

	@Override
	public void deregisterEvaluationProcessFactory(@NonNull final IEvaluationProcessFactory factory) {

		factoriesByName.remove(factory.getName());
	}

	@Override
	@NonNull
	public Collection<String> getEvaluationProcessNames() {
		return factoriesByName.keySet();
	}

	@Override
	@NonNull
	public Collection<IEvaluationProcessFactory> getEvaluationProcessFactories() {
		return factoriesByName.values();
	}

	@Override
	@NonNull
	public Set<IEvaluationProcessFactory> getEvaluationProcessFactories(@NonNull final Collection<String> names) {

		final Set<IEvaluationProcessFactory> factories = new HashSet<IEvaluationProcessFactory>(names.size());

		for (final String name : names) {

			if (factoriesByName.containsKey(name)) {
				final IEvaluationProcessFactory factory = factoriesByName.get(name);
				factories.add(factory);
			}
		}

		return factories;
	}

	/**
	 * Setter to register a {@link Collection} of {@link IEvaluationProcessFactory} instances.
	 * 
	 * @param factories
	 */
	public void setConstraintCheckerFactories(@NonNull final Collection<IEvaluationProcessFactory> factories) {

		for (final IEvaluationProcessFactory factory : factories) {
			if (factory != null) {
				registerEvaluationProcessFactory(factory);
			}
		}
	}
}
