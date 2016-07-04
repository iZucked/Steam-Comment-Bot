/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public class ClampedSpeedVessel implements IVessel {

	private final IVessel vessel;
	private final IVesselClass vesselClass;

	public ClampedSpeedVessel(@NonNull final IVessel vessel, final int clampedSpeed) {
		this.vessel = vessel;
		this.vesselClass = new ClampedSpeedVesselClass(vessel.getVesselClass(), clampedSpeed);
	}

	@Override
	public String getName() {
		return vessel.getName();
	}

	@Override
	public IVesselClass getVesselClass() {
		return vesselClass;
	}

	@Override
	public long getCargoCapacity() {
		return vessel.getCargoCapacity();
	}

	@Override
	public int hashCode() {
		return vessel.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return vessel.equals(obj);
	}
}
