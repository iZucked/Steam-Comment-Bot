/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.impl.RouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

@NonNullByDefault
public interface IRouteOptionBooking extends Comparable<IRouteOptionBooking> {
	
	int getBookingDate();
	ECanalEntry getEntryPoint();
	ERouteOption getRouteOption();
	Optional<IPortSlot> getPortSlot();
	
	public static IRouteOptionBooking of(int slotDate, ECanalEntry entryPoint, ERouteOption routeOption){
		return new RouteOptionBooking(slotDate, entryPoint, routeOption);
	}
	
	public static IRouteOptionBooking of(int slotDate, ECanalEntry entryPoint, ERouteOption routeOption, IPortSlot portSlot){
		return new RouteOptionBooking(slotDate, entryPoint, routeOption, portSlot);
	}
}
