/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

public class TravelRouteData {
	// Note: These are travel time and do not include slot visit duration
	public final int travelTimeAtMaxSpeed;
	public final int travelTimeAtNBOSpeed;
	public final long routeCost;
	public final long routeDistance;
	public final int transitTime;

	public TravelRouteData(final int travelTimeAtMaxSpeed, final int travelTimeAtNBOSpeed, final long routeCost, final long routeDistance, final int transitTime) {
		this.travelTimeAtMaxSpeed = travelTimeAtMaxSpeed;
		this.travelTimeAtNBOSpeed = travelTimeAtNBOSpeed;
		this.routeCost = routeCost;
		this.routeDistance = routeDistance;
		this.transitTime = transitTime;
	}

	public static int minTimeAtMaxSpeed(final TravelRouteData a, final TravelRouteData b) {
		return Long.compare(a.travelTimeAtMaxSpeed, b.travelTimeAtMaxSpeed);
	}

	public static int getMinimumTravelTime(final TravelRouteData[] data) {
		int minimumTravelTime = Integer.MAX_VALUE;
		for (final TravelRouteData ladenRouteData : data) {
			if (ladenRouteData.travelTimeAtMaxSpeed < minimumTravelTime) {
				minimumTravelTime = ladenRouteData.travelTimeAtMaxSpeed;
			}
		}
		return minimumTravelTime;
	}
}
