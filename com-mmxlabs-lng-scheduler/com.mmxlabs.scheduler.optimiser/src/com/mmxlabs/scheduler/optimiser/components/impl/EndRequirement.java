/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
	private boolean isMinimalDurationSet;
	private boolean isMaximalDurationSet;
	private int minimalDurationInHours;
	private int maximalDurationInHours;
	
	
	public EndRequirement(final Collection<IPort> portSet, final boolean portIsSpecified, final boolean hasTimeRequirement, final ITimeWindow timeWindow,
			final @NonNull IHeelOptionConsumer heelOptions, final boolean isHireCostOnlyEndRule) {
		super(portSet.size() == 1 ? portSet.iterator().next() : null, portSet, portIsSpecified, hasTimeRequirement, timeWindow);
		assert timeWindow != null;
		this.heelOptions = heelOptions;
		this.isHireCostOnlyEndRule = isHireCostOnlyEndRule;
		this.isMaximalDurationSet = false;
		this.isMinimalDurationSet = false;
		this.minimalDurationInHours = 0;
		this.maximalDurationInHours = 0;
	}

	@Override
	public boolean isHireCostOnlyEndRule() {
		return isHireCostOnlyEndRule;
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptions() {
		return heelOptions;
	}
	
	public int getMinDurationInHours() {
		return minimalDurationInHours;
	}
	
	public int getMaxDurationInHours() {
		return maximalDurationInHours;
	}
	
	public void setMinDurationInHours(int value) {
		if (isMaximalDurationSet) {
			assert value <= maximalDurationInHours : "Minimal duration is superior the the max possible duration, min:" + value + ", max: " + maximalDurationInHours;
		}
		
		this.isMinimalDurationSet = true;
		this.minimalDurationInHours = value;
	}
	
	public void setMaxDurationInHours(int value) {
		if (isMinimalDurationSet) {
			assert value >= maximalDurationInHours : "Maximal duration is inferior the the min duration, max:" + value + ", min: " + minimalDurationInHours;
		}
		
		this.isMaximalDurationSet = true;
		this.maximalDurationInHours = value;
	}

	public boolean isMinDurationSet() {
		return isMinimalDurationSet;
	}
	
	public boolean isMaxDurationSet() {
		return isMaximalDurationSet;
	}
}
