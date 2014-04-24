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
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * Returns a Guice {@link Module} to provide additional components to add to the main optimisation {@link Injector}
 */
public class LingoOptimiserModuleService implements IOptimiserInjectorService {

	@Override
	public Module requestModule(final String... hints) {

		return new AbstractModule() {

			@Override
			protected void configure() {

			}
		};
	}

	@Override
	public Map<ModuleType, List<Module>> requestModuleOverrides(final String... hints) {

		return null;
	}
}
