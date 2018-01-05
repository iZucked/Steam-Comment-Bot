/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.verifier;

import com.google.inject.Injector;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;

public class SolutionData {
	private final OptimiserDataMapper optimiserDataMapper;
	private final LookupManager lookupManager;

	public SolutionData(final OptimiserDataMapper optimiserDataMapper, final LookupManager lookupManager) {
		this.optimiserDataMapper = optimiserDataMapper;
		this.lookupManager = lookupManager;

	}

	public SolutionData(final OptimiserDataMapper optimiserDataMapper, final ISequences rawSequences) {
		this(optimiserDataMapper, new LookupManager(rawSequences));
	}

	public OptimiserDataMapper getOptimiserDataMapper() {
		return optimiserDataMapper;
	}

	public LookupManager getLookupManager() {
		return lookupManager;
	}

	public Injector getInjector() {
		return optimiserDataMapper.getDataTransformer().getInjector();
	}

}
