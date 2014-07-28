/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collection;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class EndRequirement extends StartEndRequirement implements IEndRequirement {
	private final boolean endCold;
	private final long targetHeelInM3;

	public EndRequirement(final Collection<IPort> portSet, final boolean portIsSpecified, final ITimeWindow timeWindow, final boolean endCold, final long tagetHeelInM3) {
		super(portSet.size() == 1 ? portSet.iterator().next() : null, portSet, portIsSpecified, timeWindow);
		this.endCold = endCold;
		this.targetHeelInM3 = tagetHeelInM3;
	}

	@Override
	public boolean isEndCold() {
		return endCold;
	}

	@Override
	public long getTargetHeelInM3() {
		return targetHeelInM3;
	}

}
