/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * 
 * @author Robert Erdin
 */
public interface IPanamaBookingsProvider extends IDataComponentProvider {

	ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> getAllBookings();

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
	 * The default amount of days a vessel will idle in front of the canal on a NORTHBOUND voyage in order to try and get a spontaneous booking. Typically you will want to call
	 * {@link #getNorthboundMaxIdleDays(IVessel)} instead.
	 * 
	 * @return
	 */
	int getNorthboundMaxIdleDays();

	/**
	 * The default amount of days a vessel will idle in front of the canal on a SOUTHBOUND voyage in order to try and get a spontaneous booking. Typically you will want to call
	 * {@link #getSouthboundMaxIdleDays(IVessel)} instead.
	 * 
	 * @return
	 */
	int getSouthboundMaxIdleDays();

	/**
	 * The amount of days a vessel will idle in front of the canal on a NORTHBOUND voyage in order to try and get a spontaneous booking.
	 * 
	 * @return
	 */
	int getNorthboundMaxIdleDays(IVessel vessel);

	/**
	 * The amount of days a vessel will idle in front of the canal on a SOUTHBOUND voyage in order to try and get a spontaneous booking.
	 * 
	 * @return
	 */
	int getSouthboundMaxIdleDays(IVessel vessel);

	// Sorted
	ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> getAssignedBookings();

	// Sorted
	ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> getUnassignedBookings();
}
