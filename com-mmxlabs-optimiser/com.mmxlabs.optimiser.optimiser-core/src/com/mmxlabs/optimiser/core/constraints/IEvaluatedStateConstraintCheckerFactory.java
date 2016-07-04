/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;

/**
 * Interface defining a factory for creating {@link IConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public interface IEvaluatedStateConstraintCheckerFactory {

	/**
	 * Returns the name of the {@link IEvaluatedStateConstraintChecker}. This is expected to be the same as {@link IEvaluatedStateConstraintChecker#getName()}
	 * 
	 * @return
	 */
	@NonNull
	String getName();

	/**
	 * Instantiate an instance of the {@link IEvaluatedStateConstraintChecker} this factory creates.
	 * 
	 * @return
	 */
	@NonNull
	IEvaluatedStateConstraintChecker instantiate();
}
