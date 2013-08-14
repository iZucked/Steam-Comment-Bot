/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.List;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.impl.DefaultEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl.DefaultGeneratedCharterOutEvaluator;

public class TransformerITSOptimiserInjectorService implements IOptimiserInjectorService {

	@Override
	public Map<ModuleType, List<Module>> requestModuleOverrides(String... hints) {
		return null;
	}

	@Override
	public Module requestModule(String... hints) {
		return new AbstractModule() {

			@Override
			protected void configure() {

			

				bind(IEntityValueCalculator.class).to(DefaultEntityValueCalculator.class);
				bind(IGeneratedCharterOutEvaluator.class).to(DefaultGeneratedCharterOutEvaluator.class);
			}
		};
	}
}