package com.mmxlabs.optimiser.constraints.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;

/**
 * Class used to obtain {@link IConstraintChecker}s from the
 * {@link IConstraintCheckerRegistry}
 * 
 * @author Simon Goodall
 * 
 */
public final class ConstraintCheckerInstantiator implements
		IConstraintCheckerInstantiator {

	public <T> List<IConstraintChecker<T>> instantiateConstraintCheckers(
			final IConstraintCheckerRegistry registry) {

		final Collection<IConstraintCheckerFactory> factories = registry
				.getConstraintCheckerFactories();
		final List<IConstraintChecker<T>> checkers = new ArrayList<IConstraintChecker<T>>(
				factories.size());
		for (final IConstraintCheckerFactory factory : factories) {
			final IConstraintChecker<T> checker = factory.instantiate();

			checkers.add(checker);
		}
		return checkers;
	}

	public <T> List<IConstraintChecker<T>> instantiateConstraintCheckers(
			final IConstraintCheckerRegistry registry,
			final List<String> constraintNames) {

		final List<IConstraintChecker<T>> checkers = new ArrayList<IConstraintChecker<T>>(
				constraintNames.size());

		// Mapping between constraint checker name and instance
		final Map<String, IConstraintChecker<T>> constraintCheckerMap = new HashMap<String, IConstraintChecker<T>>();

		// Get Collection of relevant factories.
		final Collection<IConstraintCheckerFactory> factories = registry
				.getConstraintCheckerFactories(constraintNames);

		// Process factories instantiating constraint checkers and add to the
		// map.
		for (final IConstraintCheckerFactory factory : factories) {

			final String name = factory.getName();
			final IConstraintChecker<T> checker = factory.instantiate();
			constraintCheckerMap.put(name, checker);
		}

		// Generate the ordered list of constraint checkers.
		for (final String name : constraintNames) {

			final IConstraintChecker<T> checker;
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
