/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.extensions;

import java.util.Collection;
import java.util.List;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * Returns a Guice {@link Module} to provide additional components to add to the main optimisation {@link Injector}
 */
public class LingoOptimiserModuleService implements IOptimiserInjectorService {

	@Override
	public Module requestModule(final ModuleType moduleType, final Collection<String> hints) {
		return null;
	}

	@Override
	public List<Module> requestModuleOverrides(final ModuleType moduleType, final Collection<String> hints) {
		return null;
	}

}
