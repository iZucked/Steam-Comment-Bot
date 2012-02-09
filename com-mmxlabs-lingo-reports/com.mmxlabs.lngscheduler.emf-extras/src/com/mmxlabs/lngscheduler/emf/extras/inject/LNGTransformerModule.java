/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.inject;

import scenario.Scenario;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.lngscheduler.emf.extras.LNGScenarioTransformer;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 */
public class LNGTransformerModule extends AbstractModule {

	private final Scenario scenario;

	public LNGTransformerModule(final Scenario scenario) {
		this.scenario = scenario;
	}

	@Override
	protected void configure() {
		install(new ScheduleBuilderModule());

		bind(LNGScenarioTransformer.class).toInstance(new LNGScenarioTransformer(scenario));
	}

	/**
	 * Helper method to create a {@link LNGScenarioTransformer} instance using injection. TODO: This should not really be part of the module.
	 * 
	 * @param scenario
	 * @return
	 */
	public static LNGScenarioTransformer createLNGScenarioTransformer(final Scenario scenario) {
		final Injector injector = Guice.createInjector(new LNGTransformerModule(scenario), new DataComponentProviderModule());

		return injector.getInstance(LNGScenarioTransformer.class);
	}
}
