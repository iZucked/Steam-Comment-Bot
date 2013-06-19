/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;

/**
 * A {@link Module} providing the data from {@link SettingsOverride} to the {@link Injector} framework.
 * 
 * @author Simon Goodall
 * 
 */
public class SettingsOverrideModule extends AbstractModule {

	private final SettingsOverride settings;

	public SettingsOverrideModule(final SettingsOverride settings) {
		this.settings = settings;
	}

	@Override
	protected void configure() {

	}

	@Provides
	@Named(LocalSearchOptimiserModule.RANDOM_SEED)
	private long getRandomSeed() {
		return settings.getSeed();
	}

	@Provides
	@Named(LocalSearchOptimiserModule.LSO_NUMBER_OF_ITERATIONS)
	private int getNumberOfIterations() {
		return settings.getIterations();
	}
}
