/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class RoundTripCargoEnd extends PortSlot implements IHeelOptionConsumerPortSlot {

	private final @NonNull IHeelOptionConsumer heelOptionConsumer;

	public RoundTripCargoEnd(final @NonNull String id, final @NonNull IPort port, final @NonNull IHeelOptionConsumer heelOptionConsumer) {
		super(id, PortType.Round_Trip_Cargo_End, port, null);
		this.heelOptionConsumer = heelOptionConsumer;
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return null;
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptionsConsumer() {
		return heelOptionConsumer;
	}
}
