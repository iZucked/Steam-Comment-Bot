package com.mmxlabs.optimiser.lso;

/**
 * Interface defining an object which can generate {@link IMove} instances to be
 * used within a Local Search Optimisation.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IMoveGenerator<T> {

	/**
	 * Returns a {@link IMove} object to change the current solution state.
	 * 
	 * @return
	 */
	IMove<T> generateMove();
}
