/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;

/**
 * Class used to obtain {@link IEvaluationStateConstraintChecker}s from the {@link IEvaluationStateConstraintCheckerRegistry}
 * 
 * @author Simon Goodall
 * 
 */
public final class EvaluatedStateConstraintCheckerInstantiator implements IEvaluatedStateConstraintCheckerInstantiator {

	@Override
	@NonNull
	public List<@NonNull IEvaluatedStateConstraintChecker> instantiateConstraintCheckers(@NonNull final IEvaluatedStateConstraintCheckerRegistry registry) {

		final Collection<@NonNull IEvaluatedStateConstraintCheckerFactory> factories = registry.getConstraintCheckerFactories();
		final List<@NonNull IEvaluatedStateConstraintChecker> checkers = new ArrayList<>(factories.size());
		for (final IEvaluatedStateConstraintCheckerFactory factory : factories) {
			final IEvaluatedStateConstraintChecker checker = factory.instantiate();

			checkers.add(checker);
		}
		return checkers;
	}

	@Override
	@NonNull
	public List<@Nullable IEvaluatedStateConstraintChecker> instantiateConstraintCheckers(@NonNull final IEvaluatedStateConstraintCheckerRegistry registry,
			@NonNull final List<@NonNull String> constraintNames) {

		final List<@Nullable IEvaluatedStateConstraintChecker> checkers = new ArrayList<>(constraintNames.size());

		// Mapping between constraint checker name and instance
		final Map<String, IEvaluatedStateConstraintChecker> constraintCheckerMap = new HashMap<>();

		// Get Collection of relevant factories.
		final Collection<@NonNull IEvaluatedStateConstraintCheckerFactory> factories = registry.getConstraintCheckerFactories(constraintNames);

		// Process factories instantiating constraint checkers and add to the
		// map.
		for (final IEvaluatedStateConstraintCheckerFactory factory : factories) {

			final String name = factory.getName();
			final IEvaluatedStateConstraintChecker checker = factory.instantiate();
			constraintCheckerMap.put(name, checker);
		}

		// Generate the ordered list of constraint checkers.
		for (final String name : constraintNames) {

			final IEvaluatedStateConstraintChecker checker;
			if (constraintCheckerMap.containsKey(name)) {
				checker = constraintCheckerMap.get(name);
			} else {
				checker = null;
			}
			checkers.add(checker);
		}

		return checkers;
	}
}
