package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class PortSlot implements IPortSlot {

	private String id;

	private IPort port;

	private ITimeWindow timeWindow;

	@Override
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public IPort getPort() {
		return port;
	}

	public void setPort(final IPort port) {
		this.port = port;
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return timeWindow;
	}

	public void setTimeWindow(final ITimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

}
