/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;

/**
 * Implementation of {@link IIdleEvent}.
 * 
 * @author Simon Goodall
 */
public final class IdleEventImpl extends AbstractFuelUsingEventImpl implements IIdleEvent {

	private IPort port;

	private VesselState vesselState;

	@Override
	public IPort getPort() {
		return port;
	}

	public void setPort(final IPort port) {
		this.port = port;
	}

	@Override
	public VesselState getVesselState() {
		return vesselState;
	}

	public void setVesselState(final VesselState vesselState) {
		this.vesselState = vesselState;
	}
}
