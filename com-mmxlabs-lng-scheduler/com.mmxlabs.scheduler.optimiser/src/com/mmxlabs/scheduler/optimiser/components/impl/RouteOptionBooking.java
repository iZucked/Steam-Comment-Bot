/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

@NonNullByDefault
public class RouteOptionBooking implements IRouteOptionBooking {

	private final int bookingDate;
	private final ECanalEntry entryPoint;
	private final ERouteOption routeOption;
	private Optional<IPortSlot> portSlot = Optional.empty();

	public RouteOptionBooking(final int slotDate, final ECanalEntry entryPoint, final ERouteOption routeOption) {
		this.bookingDate = slotDate;
		this.entryPoint = entryPoint;
		this.routeOption = routeOption;
	}

	public RouteOptionBooking(final int slotDate, final ECanalEntry entryPoint, final ERouteOption routeOption, final IPortSlot slot) {
		this.bookingDate = slotDate;
		this.entryPoint = entryPoint;
		this.routeOption = routeOption;
		this.portSlot = Optional.of(slot);
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
	public Optional<IPortSlot> getPortSlot() {
		return portSlot;
	}

	@Override
	public int compareTo(@NonNull final IRouteOptionBooking o) {
		return Integer.valueOf(this.getBookingDate()).compareTo(((IRouteOptionBooking) o).getBookingDate());
	}
}
