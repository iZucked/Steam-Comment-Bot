package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;

public class ConstantCharterCostCalculator implements ICharterCostCalculator {
	private final long cost;
	
	public ConstantCharterCostCalculator(long cost) {
		this.cost = cost;
	}

	@Override
	public long getCharterCost(int vesselStartTime, int voyagePlanStartTime, int eventStartTime, int duration) {
		return cost;
	}	
}
