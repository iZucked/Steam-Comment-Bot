/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * An editor interface for {@link IPanamaBookingsProvider}
 * 
 * @author Robert
 */
public interface IPanamaBookingsProviderEditor extends IPanamaBookingsProvider {
	/**
	 * Sets the bookings, overwriting existing ones. Bookings assumed to be time sorted
	 */
	void setBookings(Map<ECanalEntry, List<IRouteOptionBooking>> bookings);

	void setArrivalMargin(int margin);
	
	void setNorthboundMaxIdleDays(int maxIdleDays);

	void setSouthboundMaxIdleDays(int maxIdleDays);
	
	void setNorthboundMaxIdleDays(IVessel vessel, int maxIdleDays);

	void setSouthboundMaxIdleDays(IVessel vessel, int maxIdleDays);
}
