package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.List;

import com.mmxlabs.common.Pair;

/**
 * Wrapper class for representing the ramping up of emisions covered by EU ETS
 */
public class EuEtsSeasonalityCurve {
	private List<Pair<Integer, Integer>> changePoints;
	
	public EuEtsSeasonalityCurve(List<Pair<Integer, Integer>> changePoints) {
		this.changePoints = changePoints;
	}
	
	public int getEmissionsCoveredForYear(int year) {
		return 0;
	}
}
