/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class VesselEventPortSlot extends PortSlot implements IVesselEventPortSlot {
	private IVesselEvent charterOut;

	public VesselEventPortSlot(String id, IPort port, ITimeWindow timeWindow, IVesselEvent charterOut) {
		super(id, port, timeWindow);
		this.charterOut = charterOut;
	}	
	
	@Override
	public IVesselEvent getVesselEvent() {
		return charterOut;
	}
}
