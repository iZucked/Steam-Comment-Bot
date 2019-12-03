package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.NavigableSet;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;

public class WeightedAverageCharterCostCalculator implements ICharterCostCalculator {

	private ILongCurve charterRateCurve;
	
	@Override
	public void setCharterRateCurve(final ILongCurve charterRateCurve) {
		this.charterRateCurve = charterRateCurve;
	}

	@Override
	public long getCharterCost(int vesselStartTime, int voyagePlanStartTime, int eventStartTime, int duration) {
		if (charterRateCurve != null) {
			long charterCost = 0;
			final int vesselEndTime = vesselStartTime + duration;
			NavigableSet<Integer> changePoints = charterRateCurve.getChangePoints();
						
			//Otherwise have to step through the change points, from the vesselStartTime up to the vesselEndTime.
			Integer firstT = vesselStartTime;
			Integer lastT = vesselEndTime;
			int durationCharterRate = 1;
			int durationRemaining = duration;
			for (int t = firstT; t < lastT; t+= durationCharterRate) {
				long charterRatePerDay = charterRateCurve.getValueAtPoint(t);
				Integer nextT = changePoints.ceiling(t+1);
				if (nextT == null) {
					nextT = lastT;
				}
				durationCharterRate = Math.min(nextT - t, durationRemaining);				
				charterCost += (charterRatePerDay * durationCharterRate);
				durationRemaining -= durationCharterRate;
			}
			return charterCost / 24L;
		}
		return 0;
	}	
}
