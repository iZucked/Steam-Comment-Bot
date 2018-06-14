/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.optimiser.common.scenario.PhaseOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

public class InitialPhaseOptimisationDataModule extends AbstractModule {

	@Provides
	@Singleton
	private IPhaseOptimisationData provideOptimisationData(Injector injector, IOptimisationData optimisationData) throws IncompleteScenarioException {
		final PhaseOptimisationData phaseOptimisationData = injector.getInstance(PhaseOptimisationData.class);
		phaseOptimisationData.setSequenceElements(optimisationData.getSequenceElements());
		phaseOptimisationData.setResources(optimisationData.getResources());
		return phaseOptimisationData;
	}

	@Override
	protected void configure() {
		
	}

}
