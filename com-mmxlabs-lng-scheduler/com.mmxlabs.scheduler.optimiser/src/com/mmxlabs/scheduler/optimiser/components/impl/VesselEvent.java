/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;

@NonNullByDefault
public class VesselEvent implements IVesselEvent {

	private @Nullable ITimeWindow timeWindow;
	private int durationHours;
	private IPort startPort, endPort;

	public VesselEvent(final @Nullable ITimeWindow timeWindow, final IPort port) {
		this(timeWindow, port, port);
	}

	public VesselEvent(final @Nullable ITimeWindow timeWindow, final IPort startPort, final IPort endPort) {
		this.timeWindow = timeWindow;
		this.startPort = startPort;
		this.endPort = endPort;
	}

	public void setTimeWindow(final @Nullable ITimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	public void setDurationHours(final int durationHours) {
		this.durationHours = durationHours;
	}

	public void setStartPort(final IPort startPort) {
		this.startPort = startPort;
	}

	public void setEndPort(final IPort endPort) {
		this.endPort = endPort;
	}

	@Override
	public @Nullable ITimeWindow getTimeWindow() {
		return timeWindow;
	}

	@Override
	public int getDurationHours() {
		return durationHours;
	}

	@Override
	public IPort getStartPort() {
		return startPort;
	}

	@Override
	public IPort getEndPort() {
		return endPort;
	}

}
