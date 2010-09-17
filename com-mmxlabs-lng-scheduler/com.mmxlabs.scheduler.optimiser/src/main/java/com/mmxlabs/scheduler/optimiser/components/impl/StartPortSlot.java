package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class StartPortSlot extends PortSlot {

	public StartPortSlot(final String id, final IPort port,
			final ITimeWindow timeWindow) {
		super(id, port, timeWindow);
		setPortType(PortType.Start);
	}

	public StartPortSlot() {
		setPortType(PortType.Start);
	}
}
