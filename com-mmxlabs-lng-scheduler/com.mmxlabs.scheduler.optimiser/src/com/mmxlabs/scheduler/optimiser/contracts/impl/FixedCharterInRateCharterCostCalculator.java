package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;

public class FixedCharterInRateCharterCostCalculator implements ICharterCostCalculator {
	private final long charterInRatePerDay;
	
	public FixedCharterInRateCharterCostCalculator(long charterInRatePerDay) {
		this.charterInRatePerDay = charterInRatePerDay;
	}

	@Override
	public long getCharterCost(int vesselStartTime, int voyagePlanStartTime, int eventStartTime, int duration) {
		return (charterInRatePerDay * duration) / 24L;
	}	
}
