/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;

public abstract class StartEndRequirement implements IStartEndRequirement {
	private final IPort port;
	private final Collection<IPort> portSet;
	private final boolean portIsSpecified;
	private final @Nullable ITimeWindow timeWindow;
	private boolean hasTimeRequirement;

	protected StartEndRequirement(final IPort port, Collection<IPort> portSet, final boolean portIsSpecified, final boolean hasTimeRequirement, final @Nullable ITimeWindow timeWindow) {
		super();
		this.port = port;
		this.portSet = portSet;
		this.portIsSpecified = portIsSpecified;
		this.hasTimeRequirement = hasTimeRequirement;
		this.timeWindow = timeWindow;
	}

	@Override
	public boolean hasPortRequirement() {
		return portIsSpecified;
	}

	@Override
	public boolean hasTimeRequirement() {
		return hasTimeRequirement;
	}

	@Override
	public IPort getLocation() {
		return port;
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return timeWindow;
	}

	@Override
	public Collection<IPort> getLocations() {
		return portSet;
	}
}
