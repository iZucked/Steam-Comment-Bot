package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Map;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;

public class CurrentBookingData {
	public Map<IPort, Set<IRouteOptionBooking>> assignedBookings;
	public Map<IPort, Set<IRouteOptionBooking>> unassignedBookings;
}
