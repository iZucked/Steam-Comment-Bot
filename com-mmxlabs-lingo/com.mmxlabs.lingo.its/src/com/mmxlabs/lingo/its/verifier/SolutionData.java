/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.verifier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;

public class SolutionData {
	private final OptimiserDataMapper optimiserDataMapper;
	private final LookupManager lookupManager;
	private Schedule schedule;

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

	public @Nullable Schedule getSchedule() {

		return schedule;
	}

	public void setSchedule(@NonNull Schedule schedule) {
		this.schedule = schedule;
	}

}
