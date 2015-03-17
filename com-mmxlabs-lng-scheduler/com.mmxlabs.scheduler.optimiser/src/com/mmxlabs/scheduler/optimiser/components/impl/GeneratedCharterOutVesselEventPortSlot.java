/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;

public class GeneratedCharterOutVesselEventPortSlot extends VesselEventPortSlot implements IGeneratedCharterOutVesselEventPortSlot {

	public GeneratedCharterOutVesselEventPortSlot(final String id, final IPort port, final ITimeWindow timeWindow, final IGeneratedCharterOutVesselEvent charterOut) {
		super(id, port, timeWindow, charterOut);
	}

	@Override
	public IGeneratedCharterOutVesselEvent getVesselEvent() {
		return (IGeneratedCharterOutVesselEvent) charterOut;
	}

	@Override
	public IHeelOptions getHeelOptions() {
		return getVesselEvent();
	}

	@Override
	public void setVesselEvent(IGeneratedCharterOutVesselEvent event) {
		this.charterOut = event;
		
	}
}
