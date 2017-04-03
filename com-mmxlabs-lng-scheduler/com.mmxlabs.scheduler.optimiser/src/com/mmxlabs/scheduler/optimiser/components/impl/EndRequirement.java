/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class EndRequirement extends StartEndRequirement implements IEndRequirement {
	private final @NonNull IHeelOptionConsumer heelOptions;
	private final boolean isHireCostOnlyEndRule;

	public EndRequirement(final Collection<IPort> portSet, final boolean portIsSpecified, final boolean hasTimeRequirement, final ITimeWindow timeWindow,
			final @NonNull IHeelOptionConsumer heelOptions, final boolean isHireCostOnlyEndRule) {
		super(portSet.size() == 1 ? portSet.iterator().next() : null, portSet, portIsSpecified, hasTimeRequirement, timeWindow);
		this.heelOptions = heelOptions;
		this.isHireCostOnlyEndRule = isHireCostOnlyEndRule;
	}

	@Override
	public boolean isHireCostOnlyEndRule() {
		return isHireCostOnlyEndRule;
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptions() {
		return heelOptions;
	}
}
