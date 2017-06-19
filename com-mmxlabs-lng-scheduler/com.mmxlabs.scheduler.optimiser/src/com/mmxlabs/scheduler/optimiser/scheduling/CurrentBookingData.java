package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;

public class CurrentBookingData {
	public Map<IPort, Set<IRouteOptionBooking>> assignedBookings = new HashMap<>();
	public Map<IPort, TreeSet<IRouteOptionBooking>> unassignedBookings = new HashMap<>();

	public CurrentBookingData copy() {
		final CurrentBookingData newBookingData = new CurrentBookingData();
		// this.unassignedBookings = new HashMap<>();
		if (this.unassignedBookings != null) {
			for (final Map.Entry<IPort, TreeSet<IRouteOptionBooking>> e : this.unassignedBookings.entrySet()) {
				newBookingData.unassignedBookings.put(e.getKey(), new TreeSet<>(e.getValue()));
			}
		}
		if (this.assignedBookings != null) {
			for (final Map.Entry<IPort, Set<IRouteOptionBooking>> e : this.assignedBookings.entrySet()) {
				newBookingData.assignedBookings.put(e.getKey(), new TreeSet<>(e.getValue()));
			}
		}
		return newBookingData;
	}
}
