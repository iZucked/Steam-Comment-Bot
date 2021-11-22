/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Class to represent Panama seasonal waiting days.
 * <p>Stores change points as hours from the scenario start.
 * <p>When vessel is null record applies to default group.
 * @author FM
 * @version 1
 * @since 29-07-2021
 *
 */
public class PanamaSeasonalityCurve {
	
	@Nullable IVessel vessel;
	int changePoints[];
	int waitingDaysNB[];
	int waitingDaysSB[];
	
	public PanamaSeasonalityCurve(IVessel vessel, int[] changePoints, int[] waitingDaysNB, int[] waitingDaysSB) {
		this.vessel = vessel;
		this.changePoints = changePoints;
		this.waitingDaysNB = waitingDaysNB;
		this.waitingDaysSB = waitingDaysSB;
	}
	
	public int getNorthboundMaxIdleDays(int date) {
		for (int i = 0; i < changePoints.length; i++) {
			if (changePoints[i] > date) {
				if (i > 0) {
					if (changePoints[i-1] < date) {
						return waitingDaysNB[i-1];
					}
				}
				return waitingDaysNB[i];
			}
		}
		return Integer.MAX_VALUE;
	}
	
	public int getSouthboundMaxIdleDays(int date) {
		for (int i = 0; i < changePoints.length; i++) {
			if (changePoints[i] > date) {
				if (i > 0) {
					if (changePoints[i-1] < date) {
						return waitingDaysSB[i-1];
					}
				}
				return waitingDaysSB[i];
			}
		}
		return Integer.MAX_VALUE;
	}
	
	public IVessel getVessel() {
		return this.vessel;
	}
	
}
