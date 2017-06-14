package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

public class RouteOptionBooking implements IRouteOptionBooking {
	
	private int bookingDate;
	private IPort entryPoint;
	private ERouteOption routeOption;
	private Optional<IPortSlot> portSlot = Optional.empty();
	
	public RouteOptionBooking(int slotDate, IPort entryPoint, ERouteOption routeOption) {
		this.bookingDate = slotDate;
		this.entryPoint = entryPoint;
		this.routeOption = routeOption;
	}
	
	public RouteOptionBooking(int slotDate, IPort entryPoint, ERouteOption routeOption, IPortSlot slot) {
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
	public IPort getEntryPoint() {
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
	public int compareTo(@NonNull IRouteOptionBooking o) {
		return Integer.valueOf(this.getBookingDate()).compareTo(((IRouteOptionBooking)o).getBookingDate());
	}
}
