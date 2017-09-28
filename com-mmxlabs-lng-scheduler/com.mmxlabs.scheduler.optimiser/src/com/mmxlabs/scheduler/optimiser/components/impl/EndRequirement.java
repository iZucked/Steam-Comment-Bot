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
	private boolean isMinimalDurationSet;
	private boolean isMaximalDurationSet;
	private int minimalDuration;
	private int maximalDuration;
	
	
	public EndRequirement(final Collection<IPort> portSet, final boolean portIsSpecified, final boolean hasTimeRequirement, final ITimeWindow timeWindow,
			final @NonNull IHeelOptionConsumer heelOptions, final boolean isHireCostOnlyEndRule) {
		super(portSet.size() == 1 ? portSet.iterator().next() : null, portSet, portIsSpecified, hasTimeRequirement, timeWindow);
		assert timeWindow != null;
		this.heelOptions = heelOptions;
		this.isHireCostOnlyEndRule = isHireCostOnlyEndRule;
		this.isMaximalDurationSet = false;
		this.isMinimalDurationSet = false;
		this.minimalDuration = 0;
		this.maximalDuration = 0;
	}

	@Override
	public boolean isHireCostOnlyEndRule() {
		return isHireCostOnlyEndRule;
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptions() {
		return heelOptions;
	}
	
	public int getMinDuration() {
		return minimalDuration;
	}
	
	public int getMaxDuration() {
		return maximalDuration;
	}
	
	public void setMinDuration(int value) {
		if (isMaximalDurationSet) {
			assert value <= maximalDuration : "Minimal duration is superior the the max possible duration, min:" + value + ", max: " + maximalDuration;
		}
		
		this.isMinimalDurationSet = true;
		this.minimalDuration = value;
	}
	
	public void setMaxDuration(int value) {
		if (isMinimalDurationSet) {
			assert value >= maximalDuration : "Maximal duration is inferior the the min duration, max:" + value + ", min: " + minimalDuration;
		}
		
		this.isMaximalDurationSet = true;
		this.maximalDuration = value;
	}

	public boolean isMinDurationSet() {
		return isMinimalDurationSet;
	}
	
	public boolean isMaxDurationSet() {
		return isMaximalDurationSet;
	}
}
