package com.acme.optimiser;

import java.util.Collection;

/**
 * 
 * @author Simon Goodall
 *
 * @param <T> Sequence element type
 */
public interface IOptimiser<T> {

	void optimise(IOptimisationContext<T> optimiserContext,
			Collection<ISolution> initialSolutions, Object archiver_callback);
}
