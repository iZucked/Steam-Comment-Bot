/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;

public class CharterRateToCharterCostCalculator implements ICharterCostCalculator {

	IVesselAvailability vesselAvailability;
	ICharterRateCalculator charterRateCalculator;
	
	@Override
	public long getCharterCost(int voyagePlanStartTime, int eventStartTime, int duration) {
		long charterRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, voyagePlanStartTime);
		return (charterRatePerDay * duration) / 24L;
	}

	public void initialise(IVesselAvailability vesselAvailability, @NonNull ICharterRateCalculator charterRateCalculator) {
		this.vesselAvailability = vesselAvailability;
		this.charterRateCalculator = charterRateCalculator;
	}
}
