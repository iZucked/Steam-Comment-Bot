/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

public class LadenRouteData {
	public final long ladenTimeAtMaxSpeed;
	public final long ladenTimeAtNBOSpeed;
	public final long ladenRouteCost;
	public final long ladenRouteDistance;
	
	public LadenRouteData(final long ladenMaxSpeed, final long ladenNBOSpeed, final long ladenRouteCost, final long ladenRouteDistance) {
		this.ladenTimeAtMaxSpeed = ladenMaxSpeed;
		this.ladenTimeAtNBOSpeed = ladenNBOSpeed;
		this.ladenRouteCost = ladenRouteCost;
		this.ladenRouteDistance = ladenRouteDistance;
	}
}
