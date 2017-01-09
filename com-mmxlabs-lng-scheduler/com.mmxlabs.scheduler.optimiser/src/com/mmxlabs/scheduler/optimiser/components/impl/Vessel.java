/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 * Default implementation of {@link IVessel}.
 * 
 * @author Simon Goodall
 * 
 */
public final class Vessel implements IVessel {
	@NonNull
	private final String name;

	@NonNull
	private final IVesselClass vesselClass;

	private final long cargoCapacity;

	public Vessel(@NonNull final String name, final @NonNull IVesselClass vesselClass, final long cargoCapacity) {
		this.name = name;
		this.vesselClass = vesselClass;
		this.cargoCapacity = cargoCapacity;
	}

	@Override
	@NonNull
	public String toString() {
		return getName();
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	@NonNull
	public IVesselClass getVesselClass() {
		return vesselClass;
	}

	@Override
	public long getCargoCapacity() {
		return cargoCapacity;
	}
}
