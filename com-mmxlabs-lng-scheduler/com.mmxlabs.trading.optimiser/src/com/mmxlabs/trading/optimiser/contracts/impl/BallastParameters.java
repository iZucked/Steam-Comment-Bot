/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

public final class BallastParameters {
	private final IVesselClass vesselClass;

	private final Integer speed;
	private final Integer hireCost;
	private final Integer nboRate;
	private final Integer baseFuelRate;

	private final String[] routes;

	public BallastParameters(final IVesselClass vesselClass, final Integer speed, final Integer hireCost, final Integer nboRate, final Integer baseFuelRate, final String[] routes) {
		this.vesselClass = vesselClass;
		this.speed = speed;
		this.hireCost = hireCost;
		this.nboRate = nboRate;
		this.baseFuelRate = baseFuelRate;
		this.routes = routes;
	}

	public final IVesselClass getVesselClass() {
		return vesselClass;
	}

	public int getSpeed() {
		if (speed != null) {
			return speed.intValue();
		}
		return vesselClass.getMaxSpeed();
	}

	public final int getHireCost(final int time) {
		if (hireCost != null) {
			return hireCost.intValue();
		}
		return 0;//vesselClass.getHourlyCharterInPrice();
	}

	public final long getNBORate() {
		if (nboRate != null) {
			return nboRate.intValue();
		}
		return vesselClass.getNBORate(VesselState.Ballast);
	}

	public final long getBaseFuelRate() {
		if (baseFuelRate != null) {
			return baseFuelRate.intValue();
		}
		return vesselClass.getConsumptionRate(VesselState.Ballast).getRate(getSpeed());
	}

	public final String[] getRoutes() {
		return routes;
	}
}
