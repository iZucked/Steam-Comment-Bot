package com.mmxlabs.lngscheduler.emf.extras.inject;

import scenario.Scenario;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.lngscheduler.emf.extras.LNGScenarioTransformer;

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

	public static LNGScenarioTransformer createLNGScenarioTransformer(final Scenario scenario) {
		final Injector injector = Guice.createInjector(new LNGTransformerModule(scenario));

		return injector.getInstance(LNGScenarioTransformer.class);
	}
}
