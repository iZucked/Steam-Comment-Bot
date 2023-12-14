package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.List;

import com.mmxlabs.common.Pair;

/**
 * Wrapper class for representing the ramping up of emisions covered by EU ETS
 */
public class EuEtsSeasonalityCurve {
	private int[] changeValues;
	private int[] changeTimes; 
	
	public EuEtsSeasonalityCurve(final List<Pair<Integer, Integer>> changePoints) {
		changeValues = new int[changePoints.size()];
		changeTimes = new int[changePoints.size()];
		for(int i = 0; i < changePoints.size(); i++) {
			changeTimes[i] = changePoints.get(i).getFirst();
			changeValues[i] = changePoints.get(i).getSecond();
		}
	}
	
	public int getEmissionsCoveredForYear(int time) {
		if(changeTimes.length == 0 || changeValues.length == 0 || time < changeTimes[0]) {
			return 0;
		}
		
		for(int i = 1; i < changeTimes.length; i++) {
			if(time < changeTimes[i]) {
				return changeValues[i - 1];
			}
		}
		
		return changeValues[changeValues.length - 1];
	}
}
