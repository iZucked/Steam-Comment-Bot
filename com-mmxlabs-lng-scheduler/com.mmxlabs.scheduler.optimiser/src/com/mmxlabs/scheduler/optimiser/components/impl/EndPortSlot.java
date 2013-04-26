/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class EndPortSlot extends PortSlot {

	public EndPortSlot(final String id, final IPort port, final ITimeWindow timeWindow) {
		super(id, port, timeWindow);
		setPortType(PortType.End);
	}

	public EndPortSlot() {
		setPortType(PortType.End);
	}
}
