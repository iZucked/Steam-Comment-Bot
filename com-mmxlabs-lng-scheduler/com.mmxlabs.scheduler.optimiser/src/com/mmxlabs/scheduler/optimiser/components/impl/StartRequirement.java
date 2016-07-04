/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;

public class StartRequirement extends StartEndRequirement implements IStartRequirement {
	private final IHeelOptions heelOptions;

	public StartRequirement(final IPort port, final boolean portIsSpecified, final ITimeWindow timeWindow, final IHeelOptions heelOptions) {
		super(port, null, portIsSpecified, timeWindow);
		this.heelOptions = heelOptions == null ? new HeelOptions() : heelOptions;
	}

	@Override
	public IHeelOptions getHeelOptions() {
		return heelOptions;
	}
}
