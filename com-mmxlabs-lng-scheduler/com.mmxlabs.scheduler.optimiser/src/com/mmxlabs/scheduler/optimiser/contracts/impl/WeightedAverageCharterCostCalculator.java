/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
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
	public long getCharterCost(final int voyagePlanStartTime, final int eventStartTime, final int duration) {
		if (charterRateCurve != null) {
			long charterCost = 0;
			final int eventEndTime = eventStartTime + duration;
			final NavigableSet<Integer> changePoints = charterRateCurve.getChangePoints();

			// Otherwise have to step through the change points, from the vesselStartTime up
			// to the vesselEndTime.
			final Integer firstT = eventStartTime;
			final Integer lastT = eventEndTime;
			int durationCharterRate = 1;
			int durationRemaining = duration;
			for (int t = firstT; t < lastT; t += durationCharterRate) {
				final long charterRatePerDay = charterRateCurve.getValueAtPoint(t);
				Integer nextT = changePoints.ceiling(t + 1);
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
