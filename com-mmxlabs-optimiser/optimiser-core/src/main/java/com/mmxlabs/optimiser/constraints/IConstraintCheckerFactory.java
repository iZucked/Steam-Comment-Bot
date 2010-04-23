package com.mmxlabs.optimiser.constraints;

/**
 * Interface defining a factory for creating {@link IConstraintChecker}
 * instances.
 * 
 * @author Simon Goodall
 * 
 */
public interface IConstraintCheckerFactory {

	/**
	 * Returns the name of the {@link IConstraintChecker}. This is expected to
	 * be the same as {@link IConstraintChecker#getName()}
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Instantiate an instance of the {@link IConstraintChecker} this factory
	 * creates.
	 * 
	 * @param <T>
	 * @return
	 */
	<T> IConstraintChecker<T> instantiate();
}
