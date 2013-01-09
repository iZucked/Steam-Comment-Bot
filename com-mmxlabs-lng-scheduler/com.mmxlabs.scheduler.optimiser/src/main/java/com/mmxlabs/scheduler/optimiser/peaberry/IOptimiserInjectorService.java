package com.mmxlabs.scheduler.optimiser.peaberry;

import java.util.List;
import java.util.Map;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * Implementations of {@link IOptimiserInjectorService} provide additional objects to be injected into {@link IConstraintChecker}s and {@link IFitnessComponent}s. Implementations are expected to be
 * registered as an OSGi Service and provides a {@link Module} which will be added to the {@link Injector} created to instantiate the {@link IConstraintChecker}s and {@link IFitnessComponent}s.
 * 
 * @since 2.0
 */
public interface IOptimiserInjectorService {

	enum ModuleType {
		/**
		 * Enum to specify modules to override the Module(s) providing {@link IDataComponentProvider} instances.
		 */
		Module_DataComponentProviderModule,

		/**
		 * Enum to specify modules to override the Module(s) providing transformations to the scenario
		 */
		Module_LNGTransformerModule,

		/**
		 * Enum to specify modules to override the Module providing parameters (e.g. Seed or number of iterations) to the scenario.
		 * @since 3.0
		 */
		Module_ParametersModule
	};

	/**
	 * Return a {@link Module} instance to be passed into the {@link Injector} used to instantiate {@link IConstraintChecker}s and {@link IFitnessComponent}s.
	 * 
	 * @param hints
	 *            An optional list of "hints". For example we may pass "optimisation" or "evaluation" to return different module configurations
	 * @return
	 */
	Module requestModule(String... hints);

	/**
	 * Request a {@link Map} of {@link Module} which override the default Module implementations. This permits the default bindings to be changed.
	 * 
	 * @see Modules#override(Iterable)
	 * 
	 * @param hints
	 *            An optional list of "hints". For example we may pass "optimisation" or "evaluation" to return different module configurations
	 * @return
	 */
	Map<ModuleType, List<Module>> requestModuleOverrides(String... hints);
}
