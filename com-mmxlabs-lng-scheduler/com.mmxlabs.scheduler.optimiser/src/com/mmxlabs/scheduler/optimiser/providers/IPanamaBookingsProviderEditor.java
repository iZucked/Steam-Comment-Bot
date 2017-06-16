/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Map;
import java.util.SortedSet;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;

/**
 * An editor interface for {@link IPanamaBookingsProvider}
 * 
 * @author Robert
 */
public interface IPanamaBookingsProviderEditor extends IPanamaBookingsProvider {
	/**
	 * Sets the bookings, overwriting existing ones.
	 */
	void setBookings(Map<IPort, SortedSet<IRouteOptionBooking>> bookings);
	
	/**
	 * All dates before this boundary strictly need a Panama booking. Exclusive
	 * @param boundary
	 */
	void setStrictBoundary(int boundary);
	
	/**
	 * Between the {@link #getStrictBoundary()} and {@link #getRelaxedBoundary()}, there can be some relaxation, i.e. not all journeys through Panama need a booking.
	 * @return
	 */
	void setRelaxedBookingCount(int bookingCount);
	
	/**
	 * All dates after this boundary don't need a Panama booking. Inclusive
	 * @return
	 */
	void setRelaxedBoundary(int boundary);
}
