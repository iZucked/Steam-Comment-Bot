/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public interface IEvaluatedStateConstraintCheckerInstantiator {

	/**
	 * Return new instances of all {@link IEvaluatedStateConstraintChecker}s the {@link IEvaluatedStateConstraintCheckerRegistry} knows about.
	 * 
	 * @param registry
	 * @return
	 */
	@NonNull
	List<@NonNull IEvaluatedStateConstraintChecker> instantiateConstraintCheckers(@NonNull IEvaluatedStateConstraintCheckerRegistry registry);

	/**
	 * Return new instances of all {@link IConstraintChecker}s the {@link IConstraintCheckerRegistry} knows about. The returned list will contain {@link IConstraintChecker}s in the same order as the
	 * input list. Missing {@link IConstraintChecker}s will be replaced with a null entry in the list.
	 * 
	 * @param registry
	 * @param constraintCheckerNames
	 * @return
	 */
	@NonNull
	List<@Nullable IEvaluatedStateConstraintChecker> instantiateConstraintCheckers(@NonNull IEvaluatedStateConstraintCheckerRegistry registry, @NonNull List<@NonNull String> constraintCheckerNames);

}
