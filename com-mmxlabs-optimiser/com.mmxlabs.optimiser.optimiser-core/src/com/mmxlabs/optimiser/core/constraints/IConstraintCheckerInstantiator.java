/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import java.util.List;

import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public interface IConstraintCheckerInstantiator {

	/**
	 * Return new instances of all {@link IConstraintChecker}s the {@link IConstraintCheckerRegistry} knows about.
	 * 
	 * @param registry
	 * @return
	 */

	List<IConstraintChecker> instantiateConstraintCheckers(IConstraintCheckerRegistry registry, IOptimisationData optimisationData);

	/**
	 * Return new instances of all {@link IConstraintChecker}s the {@link IConstraintCheckerRegistry} knows about. The returned list will contain {@link IConstraintChecker}s in the same order as the
	 * input list. Missing {@link IConstraintChecker}s will be replaced with a null entry in the list.
	 * 
	 * @param registry
	 * @param constraintCheckerNames
	 * @return
	 */
	List<IConstraintChecker> instantiateConstraintCheckers(IConstraintCheckerRegistry registry, List<String> constraintCheckerNames, IOptimisationData optimisationData);

}
