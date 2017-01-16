/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import java.util.Comparator;

public class LadenRouteData {
	public final long ladenTimeAtMaxSpeed;
	public final long ladenTimeAtNBOSpeed;
	public final long ladenRouteCost;
	public final long ladenRouteDistance;
	public final int transitTime;
	
	public LadenRouteData(final long ladenMaxSpeed, final long ladenNBOSpeed, final long ladenRouteCost, final long ladenRouteDistance, final int transitTime) {
		this.ladenTimeAtMaxSpeed = ladenMaxSpeed;
		this.ladenTimeAtNBOSpeed = ladenNBOSpeed;
		this.ladenRouteCost = ladenRouteCost;
		this.ladenRouteDistance = ladenRouteDistance;
		this.transitTime = transitTime;
	}
	
	public static int minTimeAtMaxSpeed(LadenRouteData a, LadenRouteData b) {
		return Long.compare(a.ladenTimeAtMaxSpeed, b.ladenTimeAtMaxSpeed);
	}
	
	public static long getMinimumTravelTime(LadenRouteData[] data) {
		long minimumTravelTime = Long.MAX_VALUE;
		for (LadenRouteData ladenRouteData : data) {
			if (ladenRouteData.ladenTimeAtMaxSpeed < minimumTravelTime) {
				minimumTravelTime = ladenRouteData.ladenTimeAtMaxSpeed;
			}
		}
		return minimumTravelTime;
	}
}
