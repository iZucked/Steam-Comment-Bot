/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class CargoShortEnd extends PortSlot {

	public CargoShortEnd(final @NonNull String id, final @NonNull IPort port) {
		// Fake time window for null analysis
		super(id, port, new TimeWindow(0, 0));
		setPortType(PortType.Short_Cargo_End);
	}

	@Override
	public ITimeWindow getTimeWindow() {
		throw new UnsupportedOperationException();
	}
}
