/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import static org.ops4j.peaberry.Peaberry.service;

import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 */
public class LNGTransformerModule extends AbstractModule {

	private final MMXRootObject scenario;

	public LNGTransformerModule(final MMXRootObject scenario) {
		this.scenario = scenario;
	}

	@Override
	protected void configure() {
		install(new ScheduleBuilderModule());

		bind(MMXRootObject.class).toInstance(scenario);

		bind(LNGScenarioTransformer.class).in(Singleton.class);

		bind(ModelEntityMap.class).to(ResourcelessModelEntityMap.class).in(Singleton.class);
		
		if (Platform.isRunning()) {
			bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
			bind(IConstraintCheckerRegistry.class).toProvider(service(IConstraintCheckerRegistry.class).single());
			bind(IEvaluationProcessRegistry.class).toProvider(service(IEvaluationProcessRegistry.class).single());
		}
	}
}
