/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

/**
 * Immutable RoutOptionBooking class.
 * @author Patrick
 */
@NonNullByDefault
public class RouteOptionBooking implements IRouteOptionBooking {

	private final int bookingDate;
	private final ECanalEntry entryPoint;
	private final ERouteOption routeOption;
	private final Set<IVessel> vessels;

	public RouteOptionBooking(final int slotDate, final ECanalEntry entryPoint, final ERouteOption routeOption) {
		this.bookingDate = slotDate;
		this.entryPoint = entryPoint;
		this.routeOption = routeOption;
		this.vessels = Collections.emptySet();
	}

	public RouteOptionBooking(final int slotDate, final ECanalEntry entryPoint, final ERouteOption routeOption, final Set<IVessel> vessels) {
		this.bookingDate = slotDate;
		this.entryPoint = entryPoint;
		this.routeOption = routeOption;
		this.vessels = vessels;
	}

	@Override
	public int getBookingDate() {
		return bookingDate;
	}

	@Override
	public ECanalEntry getEntryPoint() {
		return entryPoint;
	}

	@Override
	public ERouteOption getRouteOption() {
		return routeOption;
	}

	@Override
	public Set<IVessel> getVessels() {
		return vessels;
	}

	@Override
	public int compareTo(@NonNull final IRouteOptionBooking other) {
		return Integer.valueOf(this.getBookingDate()).compareTo(other.getBookingDate());
	}
}
