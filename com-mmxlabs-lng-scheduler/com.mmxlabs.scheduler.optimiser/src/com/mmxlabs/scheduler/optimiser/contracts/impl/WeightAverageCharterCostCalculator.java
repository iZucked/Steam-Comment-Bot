package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;

public class WeightAverageCharterCostCalculator implements ICharterCostCalculator {

	private final ILongCurve charterRateCurve;
	
	public WeightAverageCharterCostCalculator(ILongCurve charterRateCurve) {
		this.charterRateCurve = charterRateCurve;
	}

	@Override
	public long getCharterCost(int vesselStartTime, int voyagePlanStartTime, int eventStartTime, int duration) {
		if (charterRateCurve != null) {
			long charterCost = 0;
			final int vesselEndTime = vesselStartTime + duration;
			for (int t = vesselStartTime; t <= vesselEndTime; t++) {
				long charterRatePerDay = charterRateCurve.getValueAtPoint(t);
				charterCost += charterRatePerDay;
			}
			return charterCost / 24L;
		}
		return 0;
	}	
}
