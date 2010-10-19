/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ICharterOut;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class CharterOut implements ICharterOut {
	private ITimeWindow timeWindow;
	private int durationHours;
	private IPort port;
		
	public CharterOut(ITimeWindow timeWindow, int durationHours, IPort port) {
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
