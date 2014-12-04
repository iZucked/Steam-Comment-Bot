/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 * Implementation of a {@link ICooldownLumpSumPriceCalculator} which calculates the lump sum cost of a cooldown, for a given {@link ICurve}.
 * 
 * @author Alex Churchill
 */
public class CooldownPriceIndexedCalculator extends AbstractCooldownCalculator {

	public CooldownPriceIndexedCalculator(final ICurve expressionCurve) {
		super(expressionCurve);
	}

	@Override
	public long calculateCooldownCost(final IVesselClass vesselClass, final IPort port, final int cooldownCV, final int time) {
		final int priceM3 = Calculator.costPerM3FromMMBTu(calculateUnitPriceAtUTCTime(time, port), cooldownCV);
		final long volumeM3 = vesselClass.getCooldownVolume();
		final long cost = Calculator.costFromConsumption(volumeM3, priceM3);
		return cost;
	}

}
