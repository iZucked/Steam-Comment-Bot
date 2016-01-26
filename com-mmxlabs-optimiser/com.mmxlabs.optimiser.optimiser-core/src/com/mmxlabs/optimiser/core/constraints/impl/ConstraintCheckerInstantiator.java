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

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Class used to obtain {@link IConstraintChecker}s from the {@link IConstraintCheckerRegistry}
 * 
 * @author Simon Goodall
 * 
 */
public final class ConstraintCheckerInstantiator implements IConstraintCheckerInstantiator {

	@Override
	@NonNull
	public List<IConstraintChecker> instantiateConstraintCheckers(@NonNull final IConstraintCheckerRegistry registry, @NonNull final IOptimisationData optimisationData) {

		final Collection<IConstraintCheckerFactory> factories = registry.getConstraintCheckerFactories();
		final List<IConstraintChecker> checkers = new ArrayList<IConstraintChecker>(factories.size());
		for (final IConstraintCheckerFactory factory : factories) {
			final IConstraintChecker checker = factory.instantiate();

			checkers.add(checker);
			checker.setOptimisationData(optimisationData);
		}
		return checkers;
	}

	@Override
	@NonNull
	public List<IConstraintChecker> instantiateConstraintCheckers(@NonNull final IConstraintCheckerRegistry registry, @NonNull final List<String> constraintNames,
			@NonNull final IOptimisationData optimisationData) {

		final List<IConstraintChecker> checkers = new ArrayList<IConstraintChecker>(constraintNames.size());

		// Mapping between constraint checker name and instance
		final Map<String, IConstraintChecker> constraintCheckerMap = new HashMap<String, IConstraintChecker>();

		// Get Collection of relevant factories.
		final Collection<IConstraintCheckerFactory> factories = registry.getConstraintCheckerFactories(constraintNames);

		// Process factories instantiating constraint checkers and add to the
		// map.
		for (final IConstraintCheckerFactory factory : factories) {

			final String name = factory.getName();
			final IConstraintChecker checker = factory.instantiate();
			checker.setOptimisationData(optimisationData);
			constraintCheckerMap.put(name, checker);
		}

		// Generate the ordered list of constraint checkers.
		for (final String name : constraintNames) {

			final IConstraintChecker checker;
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
