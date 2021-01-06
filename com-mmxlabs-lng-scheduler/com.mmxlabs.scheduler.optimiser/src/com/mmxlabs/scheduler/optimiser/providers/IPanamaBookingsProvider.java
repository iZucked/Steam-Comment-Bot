/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;

/**
 * 
 * @author Robert Erdin
 */
public interface IPanamaBookingsProvider extends IDataComponentProvider {

	ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> getAllBookings();

	/**
	 * All dates before this boundary strictly need a Panama booking. Exclusive
	 * 
	 * @return
	 */
	int getStrictBoundary();

//	/**
//	 * Between the {@link #getStrictBoundary()} and {@link #getRelaxedBoundary()}, there can be some relaxation, i.e. not all journeys through Panama need a booking.
//	 * 
//	 * @return
//	 */
//	int getRelaxedBookingCountNorthbound();

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
	int getMarginInHours();
	
	/**
	 * The amount of days a vessel will idle in front of the canal on a NORTHBOUND in order to try and get a spontaneous booking.
	 * @return
	 */
	int getNorthboundMaxIdleDays();

	/**
	 * The amount of days a vessel will idle in front of the canal on a NORTHBOUND in order to try and get a spontaneous booking.
	 * @return
	 */
	int getSouthboundMaxIdleDays();
	
	// Sorted
	ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> getAssignedBookings();

	// Sorted
	ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> getUnassignedBookings();
}
