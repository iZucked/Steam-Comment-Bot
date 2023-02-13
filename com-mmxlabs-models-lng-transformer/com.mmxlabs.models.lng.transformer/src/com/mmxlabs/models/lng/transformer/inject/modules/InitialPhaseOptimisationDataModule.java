/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.optimiser.common.scenario.PhaseOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

public class InitialPhaseOptimisationDataModule extends AbstractModule {

	@Provides
	@Singleton
	private IPhaseOptimisationData provideOptimisationData(final Injector injector, final IOptimisationData optimisationData) {
		final PhaseOptimisationData phaseOptimisationData = injector.getInstance(PhaseOptimisationData.class);
		phaseOptimisationData.setSequenceElements(optimisationData.getSequenceElements());
		phaseOptimisationData.setResources(optimisationData.getResources());
		return phaseOptimisationData;
	}
}
