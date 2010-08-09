package com.mmxlabs.optimiser.lso;

import com.mmxlabs.optimiser.core.ISequences;

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

	/**
	 * Returns {@link ISequences} used to generate moves
	 * 
	 * @return
	 */
	ISequences<T> getSequences();

	/**
	 * Set the {@link ISequences} used to generate moves. This should be
	 * whenever the {@link ISequences} have changed.
	 * 
	 * @param sequences
	 */
	void setSequences(ISequences<T> sequences);
}
