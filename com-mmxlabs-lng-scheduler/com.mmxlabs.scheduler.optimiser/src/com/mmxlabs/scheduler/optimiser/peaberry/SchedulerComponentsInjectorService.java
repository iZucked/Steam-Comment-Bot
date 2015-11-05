/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.peaberry;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.schedule.VoyagePlanAnnotator;

public class SchedulerComponentsInjectorService implements IOptimiserInjectorService {

	@Override
	public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<String> hints) {
		if (moduleType == ModuleType.Module_Evaluation) {
			return new AbstractModule() {

				@Override
				protected void configure() {
//					bind(VoyagePlanAnnotator.class);
//					bind(VoyagePlanner.class);
				}
			};
		}
		return null;
	}

	@Override
	public List<Module> requestModuleOverrides(final ModuleType moduleType, @NonNull final Collection<String> hints) {
		return null;
	}
}
