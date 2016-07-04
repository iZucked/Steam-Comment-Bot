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
public interface IConstraintCheckerFactory {

	/**
	 * Returns the name of the {@link IConstraintChecker}. This is expected to be the same as {@link IConstraintChecker#getName()}
	 * 
	 * @return
	 */
	@NonNull
	String getName();

	/**
	 * Instantiate an instance of the {@link IConstraintChecker} this factory creates. A call to {@link Injector#injectMembers(Object)} using the optimiser {@link Injector} should be performed on the
	 * returned object.
	 * 
	 * @return
	 */
	@NonNull
	IConstraintChecker instantiate();
}
