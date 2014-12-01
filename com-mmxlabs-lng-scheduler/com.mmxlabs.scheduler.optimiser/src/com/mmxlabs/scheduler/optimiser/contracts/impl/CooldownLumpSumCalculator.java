/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 * Implementation of a {@link ICooldownLumpSumPriceCalculator} which calculates the lump sum cost of a cooldown, for a given {@link ICurve}.
 * 
 * @author Alex Churchill
 */
public class CooldownLumpSumCalculator extends AbstractCooldownCalculator {

	public CooldownLumpSumCalculator(final ICurve expressionCurve) {
		super(expressionCurve);
	}

	/**
	 * Calculates the lump sum cost of a cooldown, according to a given point on an {@link ICurve}.
	 * 
	 * @param time
	 * @return lump sum cost
	 */
	private long getLumpSumCost(final int localTime, IPort port) {
		return calculateUnitPriceAtUTCTime(localTime, port);
	}

	@Override
	public long calculateCooldownCost(final IVesselClass vesselClass, final IPort port, final int cv, final int time) {
		return getLumpSumCost(time, port);
	}

}
