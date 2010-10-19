/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.constraints;

import java.util.List;

import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public interface IConstraintCheckerInstantiator {

	/**
	 * Return new instances of all {@link IConstraintChecker}s the
	 * {@link IConstraintCheckerRegistry} knows about.
	 * 
	 * @param <T>
	 * @param registry
	 * @return
	 */

	<T> List<IConstraintChecker<T>> instantiateConstraintCheckers(
			IConstraintCheckerRegistry registry,
			IOptimisationData<T> optimisationData);

	/**
	 * Return new instances of all {@link IConstraintChecker}s the
	 * {@link IFitnessFunctionRegistry} knows about. The returned list will
	 * contain {@link IConstraintChecker}s in the same order as the input list.
	 * Missing {@link IConstraintChecker}s will be replaced with a null entry in
	 * the list.
	 * 
	 * @param <T>
	 * @param registry
	 * @param constraintCheckerNames
	 * @return
	 */
	<T> List<IConstraintChecker<T>> instantiateConstraintCheckers(
			IConstraintCheckerRegistry registry,
			List<String> constraintCheckerNames,
			IOptimisationData<T> optimisationData);

}
