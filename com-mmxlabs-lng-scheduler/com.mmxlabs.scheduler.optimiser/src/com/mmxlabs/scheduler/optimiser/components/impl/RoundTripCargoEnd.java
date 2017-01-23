/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class RoundTripCargoEnd extends PortSlot {

	public RoundTripCargoEnd(final @NonNull String id, final @NonNull IPort port) {
		super(id, PortType.Round_Trip_Cargo_End, port, null);
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return null;
	}
}
