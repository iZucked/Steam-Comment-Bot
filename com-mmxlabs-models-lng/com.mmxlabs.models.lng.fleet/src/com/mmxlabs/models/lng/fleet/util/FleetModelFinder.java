/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;

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
	public Vessel findVessel(@NonNull final String vesselName) {
		for (final Vessel vessel : getFleetModel().getVessels()) {
			if (vesselName.equals(vessel.getName())) {
				return vessel;
			}
		}
		throw new IllegalArgumentException("Unknown vessel");
	}

}
