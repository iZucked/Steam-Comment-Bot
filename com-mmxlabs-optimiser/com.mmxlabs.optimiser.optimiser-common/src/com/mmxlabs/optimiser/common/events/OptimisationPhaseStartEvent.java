/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.events;

import java.util.Collection;

public class OptimisationPhaseStartEvent implements IReplayableEvent {

	private final String phase;
	private final Collection<String> hints;

	public OptimisationPhaseStartEvent(final String phase, final Collection<String> hints) {
		this.phase = phase;
		this.hints = hints;
	}

	public String getPhase() {
		return phase;
	}

	public Collection<String> getHints() {
		return hints;
	}
}
