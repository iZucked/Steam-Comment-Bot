/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.cii;

import java.time.LocalDate;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Shared Interface for models which can be used to accumulate CII
 * @author Andrey Popov
 *
 */
public interface CIIAccumulatableEventModel {
	Vessel getCIIVessel();
	
	void setCIIVessel(Vessel vessel);
	
	Event getCIIEvent();
	
	void setCIIEvent(Event event);
	
	LocalDate getCIIStartDate();
	
	void setCIIStartDate(LocalDate startDate);
	
	LocalDate getCIIEndDate();
	
	void setCIIEndDate(LocalDate endDate);
	
	double getTotalEmissionForCII();
	
	void addToTotalEmissionForCII(double emission);
}
