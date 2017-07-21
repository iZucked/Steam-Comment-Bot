/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;

/**
 * 
 * @author Robert Erdin
 */
public interface IPanamaBookingsProvider extends IDataComponentProvider {

	ImmutableMap<IPort, ImmutableList<IRouteOptionBooking>> getAllBookings();

	/**
	 * All dates before this boundary strictly need a Panama booking. Exclusive
	 * 
	 * @return
	 */
	int getStrictBoundary();

	/**
	 * Between the {@link #getStrictBoundary()} and {@link #getRelaxedBoundary()}, there can be some relaxation, i.e. not all journeys through Panama need a booking.
	 * 
	 * @return
	 */
	int getRelaxedBookingCountNorthbound();

	/**
	 * Between the {@link #getStrictBoundary()} and {@link #getRelaxedBoundary()}, there can be some relaxation, i.e. not all journeys through Panama need a booking.
	 * 
	 * @return
	 */
	int getRelaxedBookingCountSouthbound();

	/**
	 * All dates after this boundary don't need a Panama booking. Inclusive.
	 * 
	 * @return
	 */
	int getRelaxedBoundary();

	/**
	 * The speed to be used to calculate the time to reach the canal.
	 * 
	 * @return
	 */
	int getSpeedToCanal();

	/**
	 * The margin in hours before the booking, i.e. a vessel has to arrive n hours before the booking time.
	 * 
	 * @return
	 */
	int getMargin();

	// Sorted
	ImmutableMap<IPort, ImmutableList<IRouteOptionBooking>> getAssignedBookings();

	// Sorted
	ImmutableMap<IPort, ImmutableList<IRouteOptionBooking>> getUnassignedBookings();
}
