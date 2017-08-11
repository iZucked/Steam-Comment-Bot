/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.util;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;

public class AssignmentLabelProvider {

	public static String getLabelFor(final VesselAvailability vesselAvailability) {

		final Vessel vessel = vesselAvailability.getVessel();
		if (vessel != null) {
			return String.format("%s (%dk)", vessel.getName(), vessel.getVesselOrDelegateCapacity() / 1000);
		}
		return "";
	}

	public static String getLabelFor(final CharterInMarket charterInMarket, int spotIndex) {
		Vessel vessel = charterInMarket.getVessel();
		int capacity = vessel == null ? 0 : vessel.getVesselOrDelegateCapacity();
		String type;
		if (spotIndex >= 0) {
			type = "model";
		} else if (spotIndex == -1) {
			type = "nominal";
		} else {
			type = "market";
		}
		return String.format("%s (%dk) (%s)", charterInMarket.getName(), capacity / 1000, type);
	}
}