package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionSlot;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

public class RouteOptionSlot implements IRouteOptionSlot {
	
	private int slotDate;
	private IPort entryPoint;
	private ERouteOption routeOption;
	private Optional<IPortSlot> slot = Optional.empty();
	
	public RouteOptionSlot(int slotDate, IPort entryPoint, ERouteOption routeOption) {
		this.slotDate = slotDate;
		this.entryPoint = entryPoint;
		this.routeOption = routeOption;
	}
	
	public RouteOptionSlot(int slotDate, IPort entryPoint, ERouteOption routeOption, IPortSlot slot) {
		this.slotDate = slotDate;
		this.entryPoint = entryPoint;
		this.routeOption = routeOption;
		this.slot = Optional.of(slot);
	}

	@Override
	public int getSlotDate() {
		return slotDate;
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
	public Optional<IPortSlot> getSlot() {
		return slot;
	}

	@Override
	public int compareTo(@NonNull IRouteOptionSlot o) {
		return Integer.valueOf(this.getSlotDate()).compareTo(((IRouteOptionSlot)o).getSlotDate());
	}
}
