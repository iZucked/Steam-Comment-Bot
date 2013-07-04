/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.extensions;

import java.util.List;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.impl.DefaultEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl.DefaultBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl.DefaultGeneratedCharterOutEvaluator;

/**
 * Returns a Guice {@link Module} to provide additional components to add to the main optimisation {@link Injector}
 */
public class LingoOptimiserModuleService implements IOptimiserInjectorService {

	@Override
	public Module requestModule(final String... hints) {

		return new AbstractModule() {

			@Override
			protected void configure() {
				// Register default implementations
				bind(IEntityValueCalculator.class).to(DefaultEntityValueCalculator.class);

				if (hints != null) {
					for (final String hint : hints) {
						if (LNGTransformer.HINT_GENERATE_CHARTER_OUTS.equals(hint)) {
							bind(IGeneratedCharterOutEvaluator.class).to(DefaultGeneratedCharterOutEvaluator.class);
						}
						break;
					}
				}
				bind(IBreakEvenEvaluator.class).to(DefaultBreakEvenEvaluator.class);
			}
		};
	}

	@Override
	public Map<ModuleType, List<Module>> requestModuleOverrides(final String... hints) {
		
		return null;
	}
}
