/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import javax.inject.Inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

/**
 * Helper class to create {@link LNGScenarioTransformer}, {@link OptimisationTransformer}, {@link IOptimisationData} and {@link ModelEntityMap} instances using Guice to inject components as necessary.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGTransformer {

	private final MMXRootObject scenario;

	private final Injector injector;

	@Inject
	private ResourcelessModelEntityMap entities;

	@Inject
	private LNGScenarioTransformer lngScenarioTransformer;

	private IOptimisationData optimisationData;

	private final OptimisationTransformer optimisationTransformer;

	public LNGTransformer(final MMXRootObject scenario) {
		this(scenario, null);
	}

	public LNGTransformer(final MMXRootObject scenario, final Module module) {
		this.scenario = scenario;

		if (module != null) {
			injector = Guice.createInjector(module, new LNGTransformerModule(scenario), new DataComponentProviderModule());
		} else {
			injector = Guice.createInjector(new LNGTransformerModule(scenario), new DataComponentProviderModule());
		}

		injector.injectMembers(this);

		entities.setScenario(scenario);

		try {
			optimisationData = lngScenarioTransformer.createOptimisationData(entities);
		} catch (final IncompleteScenarioException e) {
			throw new RuntimeException(e);
		}
		optimisationTransformer = new OptimisationTransformer(scenario, lngScenarioTransformer.getOptimisationSettings());
		injector.injectMembers(optimisationTransformer);
	}

	public synchronized LNGScenarioTransformer getLNGScenarioTransformer() {
		return lngScenarioTransformer;
	}

	public OptimisationTransformer getOptimisationTransformer() {
		return optimisationTransformer;
	}

	public MMXRootObject getScenario() {
		return scenario;
	}

	public ResourcelessModelEntityMap getEntities() {
		return entities;
	}

	public LNGScenarioTransformer getLngScenarioTransformer() {
		return lngScenarioTransformer;
	}

	public IOptimisationData getOptimisationData() {
		return optimisationData;
	}
}
