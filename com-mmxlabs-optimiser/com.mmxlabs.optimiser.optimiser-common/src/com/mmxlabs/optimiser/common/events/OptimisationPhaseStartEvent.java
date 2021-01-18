/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.events;

public class OptimisationPhaseStartEvent {

	private final String phase;

	public OptimisationPhaseStartEvent(final String phase) {
		this.phase = phase;
	}

	public String getPhase() {
		return phase;
	}
}
