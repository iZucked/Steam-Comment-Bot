package com.mmxlabs.optimiser;


/**
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IOptimiser<T> {

	void optimise(IOptimisationContext<T> optimisationContext);
}
