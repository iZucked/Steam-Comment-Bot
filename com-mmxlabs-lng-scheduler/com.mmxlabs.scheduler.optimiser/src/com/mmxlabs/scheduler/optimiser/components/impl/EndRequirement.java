/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
	private final boolean isHireCostOnlyEndRule;

	public EndRequirement(final Collection<IPort> portSet, final boolean portIsSpecified, final boolean hasTimeRequirement, final ITimeWindow timeWindow, final boolean endCold,
			final long tagetHeelInM3, final boolean isHireCostOnlyEndRule) {
		super(portSet.size() == 1 ? portSet.iterator().next() : null, portSet, portIsSpecified,hasTimeRequirement,  timeWindow);
		this.endCold = endCold;
		this.targetHeelInM3 = tagetHeelInM3;
		this.isHireCostOnlyEndRule = isHireCostOnlyEndRule;
	}

	@Override
	public boolean isEndCold() {
		return endCold;
	}

	@Override
	public long getTargetHeelInM3() {
		return targetHeelInM3;
	}

	@Override
	public boolean isHireCostOnlyEndRule() {
		return isHireCostOnlyEndRule;
	}

}
