package com.mmxlabs.scheduler.optimiser.voyage.util;

public class ApproximateFuelCosts {
	private final long boilOffCost;
	private final long journeyBunkerCost;
	
	public ApproximateFuelCosts(final long boiloffCost, final long journeyBunkers) {
		boilOffCost = boiloffCost;
		this.journeyBunkerCost = journeyBunkers;
	}

	public long getBoilOffCost() {
		return boilOffCost;
	}

	public long getJourneyBunkerCost() {
		return journeyBunkerCost;
	}
	
	public long getTotalCost() {
		return boilOffCost + journeyBunkerCost;
	}
}
