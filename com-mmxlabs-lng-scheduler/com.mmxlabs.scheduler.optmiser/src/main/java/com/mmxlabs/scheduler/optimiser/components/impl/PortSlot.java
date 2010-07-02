package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class PortSlot implements IPortSlot {

	private String id;

	private IPort port;

	private ITimeWindow timeWindow;

	public PortSlot() {

	}

	public PortSlot(final String id, final IPort port,
			final ITimeWindow timeWindow) {
		this.id = id;
		this.port = port;
		this.timeWindow = timeWindow;
	}

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

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof PortSlot) {
			final PortSlot slot = (PortSlot) obj;

			if (!Equality.isEqual(id, slot.id)) {
				return false;
			}
			if (!Equality.isEqual(port, slot.port)) {
				return false;
			}
			if (!Equality.isEqual(timeWindow, slot.timeWindow)) {
				return false;
			}

			return true;
		}

		return false;
	}
}
