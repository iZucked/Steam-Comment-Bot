/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.events;

public class OptimisationPhaseEndEvent {

	private final String phase;

	public OptimisationPhaseEndEvent(final String phase) {
		this.phase = phase;
	}

	public String getPhase() {
		return phase;
	}
}
