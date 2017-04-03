/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;

public class StartRequirement extends StartEndRequirement implements IStartRequirement {
	private final IHeelOptionSupplier heelOptions;

	public StartRequirement(final IPort port, final boolean portIsSpecified, final boolean hasTimeRequirement, final @Nullable ITimeWindow timeWindow, final IHeelOptionSupplier heelOptions) {
		super(port, null, portIsSpecified, hasTimeRequirement, timeWindow);
		this.heelOptions = heelOptions == null ? new HeelOptionSupplier(0, 0, 0, new ConstantHeelPriceCalculator(0)) : heelOptions;
	}

	@Override
	public IHeelOptionSupplier getHeelOptions() {
		return heelOptions;
	}
}
