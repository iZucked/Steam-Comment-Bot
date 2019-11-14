package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;

public class CharterRateToCharterCostCalculator implements ICharterCostCalculator {

	IVesselAvailability vesselAvailability;
	ICharterRateCalculator charterRateCalculator;
	
	public CharterRateToCharterCostCalculator(IVesselAvailability vesselAvailability, ICharterRateCalculator charterRateCalculator) {
		this.vesselAvailability = vesselAvailability;
		this.charterRateCalculator = charterRateCalculator;
	}
	
	@Override
	public long getCharterCost(int vesselStartTime, int voyagePlanStartTime, int eventStartTime, int duration) {
		long charterRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, vesselStartTime, voyagePlanStartTime);
		return (charterRatePerDay * duration) / 24L;
	}
}
