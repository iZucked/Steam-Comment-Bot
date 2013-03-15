/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.peaberry;

import java.util.List;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.schedule.VoyagePlanAnnotator;

public class SchedulerComponentsInjectorService implements IOptimiserInjectorService {

	@Override
	public Module requestModule(String... hints) {
		return new AbstractModule() {

			@Override
			protected void configure() {
				bind(VoyagePlanAnnotator.class);
				bind(VoyagePlanIterator.class);
			}
		};
	}

	@Override
	public Map<ModuleType, List<Module>> requestModuleOverrides(String... hints) {
		return null;
	}

}
