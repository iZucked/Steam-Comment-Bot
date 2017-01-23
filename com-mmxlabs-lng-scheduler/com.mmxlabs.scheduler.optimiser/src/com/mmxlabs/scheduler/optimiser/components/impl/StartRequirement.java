/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;

public class StartRequirement extends StartEndRequirement implements IStartRequirement {
	private final IHeelOptions heelOptions;

	public StartRequirement(final IPort port, final boolean portIsSpecified, final boolean hasTimeRequirement, final @Nullable ITimeWindow timeWindow, final IHeelOptions heelOptions) {
		super(port, null, portIsSpecified, hasTimeRequirement, timeWindow);
		this.heelOptions = heelOptions == null ? new HeelOptions() : heelOptions;
	}

	@Override
	public IHeelOptions getHeelOptions() {
		return heelOptions;
	}
}
