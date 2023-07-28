/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.cii;

import java.time.LocalDate;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Event;

public class VerySImpleCIIAccumulatableModel implements CIIAccumulatableEventModel {
	
	private double totalEmissionForCII;
	private LocalDate eventStartForCII;
	private LocalDate eventEndForCII;
	private Vessel vesselForCII;
	private Event eventForCII;
	
	@Override
	public Vessel getCIIVessel() {
		return vesselForCII;
	}

	@Override
	public void setCIIVessel(Vessel vessel) {
		this.vesselForCII = vessel;
	}

	@Override
	public Event getCIIEvent() {
		return eventForCII;
	}

	@Override
	public void setCIIEvent(Event event) {
		this.eventForCII = event;
	}

	@Override
	public LocalDate getCIIStartDate() {
		return eventStartForCII;
	}

	@Override
	public void setCIIStartDate(final LocalDate startDate) {
		this.eventStartForCII = startDate;
	}

	@Override
	public LocalDate getCIIEndDate() {
		return eventEndForCII;
	}

	@Override
	public void setCIIEndDate(final LocalDate endDate) {
		this.eventEndForCII = endDate;
	}

	@Override
	public double getTotalEmissionForCII() {
		return totalEmissionForCII;
	}

	@Override
	public void addToTotalEmissionForCII(double emission) {
		this.totalEmissionForCII += emission;
	}

}
