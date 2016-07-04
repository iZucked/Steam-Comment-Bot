/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;

public class FleetModelFinder {
	private final @NonNull FleetModel fleetModel;

	public FleetModelFinder(final @NonNull FleetModel fleetModel) {
		this.fleetModel = fleetModel;
	}

	@NonNull
	public FleetModel getFleetModel() {
		return fleetModel;
	}

	@NonNull
	public VesselClass findVesselClass(@NonNull final String vesselClassName) {
		for (final VesselClass vesselClass : getFleetModel().getVesselClasses()) {
			if (vesselClassName.equals(vesselClass.getName())) {
				return vesselClass;
			}
		}
		throw new IllegalArgumentException("Unknown vessel class");
	}

	@NonNull
	public Vessel findVessel(@NonNull final String vesselName) {
		for (final Vessel vessel : getFleetModel().getVessels()) {
			if (vesselName.equals(vessel.getName())) {
				return vessel;
			}
		}
		throw new IllegalArgumentException("Unknown vessel");
	}

}
