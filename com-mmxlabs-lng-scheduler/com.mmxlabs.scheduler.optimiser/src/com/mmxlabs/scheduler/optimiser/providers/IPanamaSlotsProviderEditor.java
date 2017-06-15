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
public interface IPanamaSlotsProviderEditor extends IPanamaBookingsProvider {
	/**
	 * Sets the slots overwriting existing ones.
	 */
	void setBookings(Map<IPort, SortedSet<IRouteOptionBooking>> slots);
	
	/**
	 * All dates before this boundary strictly need a Panama slot. Exclusive
	 * @param boundary
	 */
	void setStrictBoundary(int boundary);
	
	/**
	 * Between the {@link #getStrictBoundary()} and {@link #getRelaxedBoundary()}, there can be some relaxation, i.e. not all journeys through Panama need a slot.
	 * @return
	 */
	void setRelaxedSlotCount(int slotCount);
	
	/**
	 * All dates after this boundary don't need a Panama slot. Inclusive
	 * @return
	 */
	void setRelaxedBoundary(int boundary);
}
