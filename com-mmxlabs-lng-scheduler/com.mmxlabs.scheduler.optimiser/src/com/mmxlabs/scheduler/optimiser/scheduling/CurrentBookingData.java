/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;

public final class CurrentBookingData {
	public Map<IPort, List<IRouteOptionBooking>> assignedBookings = new HashMap<>();
	public Map<IPort, List<IRouteOptionBooking>> unassignedBookings = new HashMap<>();

	public CurrentBookingData copy() {
		final CurrentBookingData newBookingData = new CurrentBookingData();
		// this.unassignedBookings = new HashMap<>();
		if (this.unassignedBookings != null) {
			for (final Map.Entry<IPort, List<IRouteOptionBooking>> e : this.unassignedBookings.entrySet()) {
				newBookingData.unassignedBookings.put(e.getKey(), new ArrayList<>(e.getValue()));
			}
		}
		if (this.assignedBookings != null) {
			for (final Map.Entry<IPort, List<IRouteOptionBooking>> e : this.assignedBookings.entrySet()) {
				newBookingData.assignedBookings.put(e.getKey(), new ArrayList<>(e.getValue()));
			}
		}
		return newBookingData;
	}
}
