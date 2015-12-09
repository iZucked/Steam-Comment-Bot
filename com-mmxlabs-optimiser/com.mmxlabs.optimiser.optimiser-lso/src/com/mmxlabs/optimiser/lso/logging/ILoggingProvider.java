package com.mmxlabs.optimiser.lso.logging;

import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;

public interface ILoggingProvider {
	LSOLogger providerLSOLogger(IFitnessEvaluator fitnessEvaluator, IOptimisationContext optimisationContext);
}
