/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class VesselEvent implements IVesselEvent {
	private ITimeWindow timeWindow;
	private int durationHours;
	private IPort port;
		
	public VesselEvent(ITimeWindow timeWindow, int durationHours, IPort port) {
		super();
		this.timeWindow = timeWindow;
		this.durationHours = durationHours;
		this.port = port;
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return timeWindow;
	}

	@Override
	public int getDurationHours() {
		return durationHours;
	}

	@Override
	public IPort getPort() {
		return port;
	}
}
