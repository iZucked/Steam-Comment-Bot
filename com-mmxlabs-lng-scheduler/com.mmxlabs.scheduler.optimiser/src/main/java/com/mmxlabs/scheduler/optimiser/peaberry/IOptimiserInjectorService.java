package com.mmxlabs.scheduler.optimiser.peaberry;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;

/**
 * Implementations of {@link IOptimiserInjectorService} provide additional objects to be injected into {@link IConstraintChecker}s and {@link IFitnessComponent}s. Implementations are expected to be
 * registered as an OSGi Service and provides a {@link Module} which will be added to the {@link Injector} created to instantiate the {@link IConstraintChecker}s and {@link IFitnessComponent}s.
 * 
 * @since 2.0
 */
public interface IOptimiserInjectorService {
	/**
	 * Return a {@link Module} instance to be passed into the {@link Injector} used to instantiate {@link IConstraintChecker}s and {@link IFitnessComponent}s.
	 * 
	 * @param hints
	 * @return
	 */
	public Module requestModule();
}
