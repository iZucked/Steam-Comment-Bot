package com.mmxlabs.optimiser.core.constraints;

import java.util.List;

import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;

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
			IConstraintCheckerRegistry registry);

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
			final IConstraintCheckerRegistry registry,
			final List<String> constraintCheckerNames);

}
