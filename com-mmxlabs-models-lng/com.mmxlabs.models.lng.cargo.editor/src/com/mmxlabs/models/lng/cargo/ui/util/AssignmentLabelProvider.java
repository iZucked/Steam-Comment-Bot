/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.util;

import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
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
	
	public static String getLabelFor(final Vessel vessel, boolean showVolume) {
		if (vessel != null) {
			if(showVolume) {
				return String.format("%s (%dk)", vessel.getName(), vessel.getVesselOrDelegateCapacity() / 1000);
			}
			else {
				return String.format("%s", vessel.getName());
			}
		}
		return "";
	}

	public static String getLabelFor(final CharterInMarketOverride charterInMarketOverride) {
		
		final CharterInMarket charterInMarket = charterInMarketOverride.getCharterInMarket();
		if (charterInMarket != null) {
			return getLabelFor(charterInMarket, charterInMarketOverride.getSpotIndex(), true) + " Override";
		}
		return "";
	}

	public static String getLabelFor(final CharterInMarket charterInMarket, int spotIndex, boolean showVolume) {
		Vessel vessel = charterInMarket.getVessel();
		int capacity = vessel == null ? 0 : vessel.getVesselOrDelegateCapacity();
		String type;
		if (spotIndex >= 0) {
			type = String.format("option %d", spotIndex + 1);
		} else if (spotIndex == -1) {
			type = "nominal";
		} else {
			type = "market";
		}
		if(showVolume) {
			return String.format("%s (%dk) (%s)", charterInMarket.getName(), capacity / 1000, type);
		}
		else {
			return String.format("%s (%s)", charterInMarket.getName(), type);
		}
	}
}