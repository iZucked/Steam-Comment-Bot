/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import java.util.EnumMap;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Implementation of {@link IIdleEvent}.
 * 
 * @author Simon Goodall
 */
public final class IdleEventImpl extends AbstractFuelUsingEventImpl implements IIdleEvent {

	private IPort port;

	private VesselState vesselState;

	private int cooldownDuration;

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

	public void setCooldownDuration(final int cooldownDuration) {
		this.cooldownDuration = cooldownDuration;
	}

	@Override
	public int getCooldownDuration() {
		return cooldownDuration;
	}
}
