/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 * Default implementation of {@link IVessel}.
 * 
 * @author Simon Goodall
 * 
 */
public final class Vessel implements IVessel {

	private String name;

	private IVesselClass vesselClass;

	private long cargoCapacity;

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public IVesselClass getVesselClass() {
		return vesselClass;
	}

	public void setVesselClass(final IVesselClass vesselClass) {
		this.vesselClass = vesselClass;
	}

	@Override
	public long getCargoCapacity() {
		return cargoCapacity;
	}

	public void setCargoCapacity(final long value) {
		cargoCapacity = value;
	}
}
