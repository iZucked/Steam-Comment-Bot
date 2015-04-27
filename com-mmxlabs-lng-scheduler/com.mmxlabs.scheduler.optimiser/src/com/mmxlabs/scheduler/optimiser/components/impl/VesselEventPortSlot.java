/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;

public class VesselEventPortSlot extends PortSlot implements IVesselEventPortSlot {
	protected IVesselEvent charterOut;

	public VesselEventPortSlot(@NonNull final String id, @NonNull final IPort port, final ITimeWindow timeWindow, @NonNull final IVesselEvent charterOut) {
		super(id, port, timeWindow);
		this.charterOut = charterOut;
	}

	@Override
	public IVesselEvent getVesselEvent() {
		return charterOut;
	}

	@Override
	public IHeelOptions getHeelOptions() {
		return getVesselEvent();
	}
}
