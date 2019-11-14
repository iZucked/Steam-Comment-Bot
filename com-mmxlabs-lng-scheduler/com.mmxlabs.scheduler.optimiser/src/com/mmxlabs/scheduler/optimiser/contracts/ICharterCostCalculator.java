package com.mmxlabs.scheduler.optimiser.contracts;

public interface ICharterCostCalculator extends ICalculator {
	long getCharterCost(int vesselStartTime, int voyagePlanStartTime, int eventStartTime, int duration);
}
