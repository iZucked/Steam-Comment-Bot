/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Map;
import java.util.SortedSet;

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
	void setBookings(Map<ECanalEntry, SortedSet<IRouteOptionBooking>> bookings);

	/**
	 * All dates before this boundary strictly need a Panama booking. Exclusive
	 * 
	 * @param boundary
	 */
	void setStrictBoundary(int boundary);

	/**
	 * Between the {@link #getStrictBoundary()} and {@link #getRelaxedBoundary()}, there can be some relaxation, i.e. not all journeys through Panama need a booking.
	 * 
	 * @return
	 */
	void setRelaxedBookingCountNorthbound(int bookingCount);

	/**
	 * Between the {@link #getStrictBoundary()} and {@link #getRelaxedBoundary()}, there can be some relaxation, i.e. not all journeys through Panama need a booking.
	 * 
	 * @return
	 */
	void setRelaxedBookingCountSouthbound(int bookingCount);
	
	void setNorthboundMaxIdleDays(int maxIdleDays);

	/**
	 * All dates after this boundary don't need a Panama booking. Inclusive
	 * 
	 * @return
	 */
	void setRelaxedBoundary(int boundary);

	void setArrivalMargin(int margin);
}
