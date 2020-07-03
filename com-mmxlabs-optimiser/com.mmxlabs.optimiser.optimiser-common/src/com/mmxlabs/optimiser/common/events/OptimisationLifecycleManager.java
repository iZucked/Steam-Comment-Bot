/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.events;

import javax.inject.Inject;

import com.google.common.eventbus.EventBus;

public class OptimisationLifecycleManager {

	@Inject
	private EventBus eventBus;

	public void startPhase(String phase) {
		eventBus.post(new OptimisationPhaseStartEvent(phase));

	}

	public void endPhase(String phase) {
		eventBus.post(new OptimisationPhaseEndEvent(phase));
	}
}
