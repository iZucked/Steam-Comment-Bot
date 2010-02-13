package com.acme.optimiser;

import java.util.Collection;

public interface IOptimiser {

	void optimise(IOptimisationContext optimiserContext,
			Collection<ISolution> initialSolutions, Object archiver_callback);
}
