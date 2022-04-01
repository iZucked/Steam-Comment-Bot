/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.impl.RouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

@NonNullByDefault
public interface IRouteOptionBooking extends Comparable<IRouteOptionBooking> {

	/**
	 * Returns the date and time of the booking. This should encode the 3AM arrival time, but not the extra arrival time margin.
	 * 
	 * @return
	 */
	int getBookingDate();

	/**
	 * Which entry point does the booking apply to.
	 * 
	 * @return
	 */
	ECanalEntry getEntryPoint();

	/**
	 * Which route does this apply to. Currently PANAMA is the only expected option.
	 * 
	 * @return
	 */

	ERouteOption getRouteOption();

	/**
	 * Which vessels can this booking be assigned to? A single vessel implies the booking was directly assigned to a specific vessel. Multiple vessels implies a flexible booking code was used. An
	 * empty set implies any vessel can be used.
	 * 
	 * @return
	 */
	Set<IVessel> getVessels();

	public static IRouteOptionBooking of(int slotDate, ECanalEntry entryPoint, ERouteOption routeOption) {
		return new RouteOptionBooking(slotDate, entryPoint, routeOption);
	}

	public static IRouteOptionBooking of(int slotDate, ECanalEntry entryPoint, ERouteOption routeOption, Set<IVessel> vessels) {
		return new RouteOptionBooking(slotDate, entryPoint, routeOption, vessels);
	}
}
