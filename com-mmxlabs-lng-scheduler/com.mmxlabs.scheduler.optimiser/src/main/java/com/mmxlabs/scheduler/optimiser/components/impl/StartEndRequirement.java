/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;

public class StartEndRequirement implements IStartEndRequirement {
	IPort port;
	boolean portIsSpecified;
	ITimeWindow timeWindow;
	
	public StartEndRequirement(IPort port, boolean portIsSpecified, ITimeWindow timeWindow) {
		super();
		this.port = port;
		this.portIsSpecified = portIsSpecified;
		this.timeWindow = timeWindow;
	}

	@Override
	public boolean hasPortRequirement() {
		return portIsSpecified;
	}

	@Override
	public boolean hasTimeRequirement() {
		return timeWindow != null;
	}

	@Override
	public IPort getLocation() {
		return port;
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return timeWindow;
	}
}
