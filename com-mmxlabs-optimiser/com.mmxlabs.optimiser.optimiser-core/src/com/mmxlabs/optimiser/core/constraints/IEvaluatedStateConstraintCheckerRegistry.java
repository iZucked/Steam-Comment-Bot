/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

/**
 * The {@link IEvaluatedStateConstraintCheckerRegistry} is a store for {@link IConstraintCheckerFactory} instances. The Registry is to be used to create new instances of {@link IConstraintChecker} for
 * use in optimisations.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public interface IEvaluatedStateConstraintCheckerRegistry {

	/**
	 * Registers a {@link IEvaluatedStateConstraintCheckerFactory} instance.
	 * 
	 * @param factory
	 */
	void registerConstraintCheckerFactory(@NonNull IEvaluatedStateConstraintCheckerFactory factory);

	/**
	 * De-registers a {@link IEvaluatedStateConstraintCheckerFactory} instance.
	 * 
	 * @param factory
	 */
	void deregisterConstraintCheckerFactory(@NonNull IEvaluatedStateConstraintCheckerFactory factory);

	/**
	 * Return a {@link Collection} of the registered {@link IEvaluatedStateConstraintCheckerFactory} instances.
	 * 
	 * @return
	 */
	@NonNull
	Collection<@NonNull IEvaluatedStateConstraintCheckerFactory> getConstraintCheckerFactories();

	/**
	 * Returns a {@link Collection} of the registered {@link IEvaluatedStateConstraintChecker} names.
	 * 
	 * @return
	 */
	@NonNull
	Collection<@NonNull String> getConstraintCheckerNames();

	/**
	 * Returns a {@link Collection} of {@link IEvaluatedStateConstraintCheckerFactory} instances based on the list of {@link IEvaluatedStateConstraintChecker} names.
	 * 
	 * @param names
	 * @return
	 */
	@NonNull
	Collection<@NonNull IEvaluatedStateConstraintCheckerFactory> getConstraintCheckerFactories(@NonNull Collection<@NonNull String> names);
}
