/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Class to represent Panama seasonal waiting days.
 * <p>
 * Stores change points as hours from the scenario start.
 * <p>
 * When vessel is null record applies to default group.
 * 
 * @author FM
 * @version 1
 * @since 29-07-2021
 *
 */
public class PanamaSeasonalityCurve {

	private @Nullable IVessel vessel;
	private int[] changePoints;
	private int[] waitingDaysNB;
	private int[] waitingDaysSB;

	public PanamaSeasonalityCurve(IVessel vessel, int[] changePoints, int[] waitingDaysNB, int[] waitingDaysSB) {
		assert changePoints.length != 0;

		this.vessel = vessel;
		this.changePoints = changePoints;
		this.waitingDaysNB = waitingDaysNB;
		this.waitingDaysSB = waitingDaysSB;
	}

	public int getNorthboundMaxIdleDays(int date) {
		return getWaitingDays(changePoints, waitingDaysNB, date);
	}

	public int getSouthboundMaxIdleDays(int date) {
		return getWaitingDays(changePoints, waitingDaysSB, date);
	}

	private static int getWaitingDays(int[] changePoints, int[] waitingDays, int date) {
		assert date >= changePoints[0];
		for (int i = 1; i < changePoints.length; ++i) {
			if (date < changePoints[i]) {
				return waitingDays[i - 1];
			}
		}
		return waitingDays[waitingDays.length - 1];
	}

	public IVessel getVessel() {
		return this.vessel;
	}

}
