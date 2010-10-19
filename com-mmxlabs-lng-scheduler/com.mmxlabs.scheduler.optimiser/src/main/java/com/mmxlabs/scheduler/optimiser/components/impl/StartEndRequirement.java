/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;

public class StartEndRequirement implements IStartEndRequirement {
	IPort port;
	boolean portIsSpecified;
	int time;
	boolean timeIsSpecified;
	
	public StartEndRequirement(IPort port, boolean portIsSpecified, int time,
			boolean timeIsSpecified) {
		super();
		this.port = port;
		this.portIsSpecified = portIsSpecified;
		this.time = time;
		this.timeIsSpecified = timeIsSpecified;
	}

	@Override
	public boolean hasPortRequirement() {
		return portIsSpecified;
	}

	@Override
	public boolean hasTimeRequirement() {
		return timeIsSpecified;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public IPort getLocation() {
		return port;
	}
	
	
}
