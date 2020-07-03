/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.shared.ISharedDataTransformerService;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.IDistanceMatrixProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 * 
 */
public class LNGSharedDataTransformerModule extends AbstractModule {

	private @NonNull LNGScenarioModel scenario;
	private ISharedDataTransformerService sharedDataTransformerService;
	private @NonNull IScenarioDataProvider scenarioDataProvider;

	public LNGSharedDataTransformerModule(final @NonNull IScenarioDataProvider scenarioDataProvider, ISharedDataTransformerService sharedDataTransformerService) {
		this.scenarioDataProvider = scenarioDataProvider;
		this.scenario = (@NonNull LNGScenarioModel) scenarioDataProvider.getScenario();
		this.sharedDataTransformerService = sharedDataTransformerService;
		assert scenario != null;
	}

	@Override
	protected void configure() {
		bind(PortModel.class).toInstance(ScenarioModelUtil.getPortModel(scenario));
	}

	@Provides
	@Singleton
	private IPortProvider provideSharedPortProvider(PortModel portModel) {

		return sharedDataTransformerService.getPortAndDistanceProvider(scenarioDataProvider).getPortProvider();
	}

	@Provides
	@Singleton
	private IDistanceMatrixProvider provideDistanceMatrix(PortModel portModel) {

		return sharedDataTransformerService.getPortAndDistanceProvider(scenarioDataProvider).getDistanceMatrixProvider();
	}

}
