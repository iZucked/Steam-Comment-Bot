/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;

public class VesselEventPortSlot extends PortSlot implements IVesselEventPortSlot {
	private final IVesselEvent charterOut;

	public VesselEventPortSlot(String id, IPort port, ITimeWindow timeWindow, IVesselEvent charterOut) {
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
