/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import java.util.Collection;

/**
 * The {@link IConstraintCheckerRegistry} is a store for {@link IConstraintCheckerFactory} instances. The Registry is to be used to create new instances of {@link IConstraintChecker} for use in
 * optimisations.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public interface IConstraintCheckerRegistry {

	/**
	 * Registers a {@link IConstraintCheckerFactory} instance.
	 * 
	 * @param factory
	 */
	void registerConstraintCheckerFactory(IConstraintCheckerFactory factory);

	/**
	 * De-registers a {@link IConstraintCheckerFactory} instance.
	 * 
	 * @param factory
	 */
	void deregisterConstraintCheckerFactory(IConstraintCheckerFactory factory);

	/**
	 * Return a {@link Collection} of the registered {@link IConstraintCheckerFactory} instances.
	 * 
	 * @return
	 */

	Collection<IConstraintCheckerFactory> getConstraintCheckerFactories();

	/**
	 * Returns a {@link Collection} of the registered {@link IConstraintChecker} names.
	 * 
	 * @return
	 */
	Collection<String> getConstraintCheckerNames();

	/**
	 * Returns a {@link Collection} of {@link IConstraintCheckerFactory} instances based on the list of {@link IConstraintChecker} names.
	 * 
	 * @param names
	 * @return
	 */
	Collection<IConstraintCheckerFactory> getConstraintCheckerFactories(Collection<String> names);
}
