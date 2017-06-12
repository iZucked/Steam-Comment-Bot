package com.mmxlabs.scheduler.optimiser.components;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.impl.RouteOptionSlot;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

@NonNullByDefault
public interface IRouteOptionSlot extends Comparable<IRouteOptionSlot> {
	
	int getSlotDate();
	IPort getEntryPoint();
	ERouteOption getRouteOption();
	Optional<IPortSlot> getSlot();
	
	public static IRouteOptionSlot of(int slotDate, IPort entryPoint, ERouteOption routeOption){
		return new RouteOptionSlot(slotDate, entryPoint, routeOption);
	}
	
	public static IRouteOptionSlot of(int slotDate, IPort entryPoint, ERouteOption routeOption, IPortSlot portSlot){
		return new RouteOptionSlot(slotDate, entryPoint, routeOption, portSlot);
	}
}
