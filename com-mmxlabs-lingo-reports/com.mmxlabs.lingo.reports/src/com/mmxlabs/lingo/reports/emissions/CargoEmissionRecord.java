/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;

public class CargoEmissionRecord {
	
	private @NonNull Vessel vessel;
	private CargoAllocation cargoAllocation;
	private VesselEvent vesselEvent;
	private List<Event> events;
	
	public CargoEmissionRecord(final @NonNull Vessel vessel, final CargoAllocation cargoAllocation, 
			final VesselEvent vesselEvent, final List<Event> events) {
		this.vessel = vessel;
		this.cargoAllocation = cargoAllocation;
		this.vesselEvent = vesselEvent;
		this.events = events;
	}
	
	public boolean vesselEndEvent = false;
	
	public boolean vesselStartEvent = false;

	public Vessel getVessel() {
		return vessel;
	}

	public CargoAllocation getCargoAllocation() {
		return cargoAllocation;
	}

	public VesselEvent getVesselEvent() {
		return vesselEvent;
	}

	public List<Event> getEvents() {
		return events;
	}
	
	public String getName() {
		if (vesselEndEvent) {
			return String.format("End %s", vessel.getName());
		} else if (vesselStartEvent){
			return String.format("Start %s", vessel.getName());
		} else {
			if (cargoAllocation != null) {
				return cargoAllocation.getName();
			} else if (vesselEvent != null){
				return vesselEvent.getName();
			}
		}
		throw new IllegalStateException(String.format("Vessel %s does not have a cargo and vessel event.", vessel.getName()));
	}
}
