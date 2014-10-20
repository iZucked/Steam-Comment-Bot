/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 * Implementation of a {@link ICooldownLumpSumPriceCalculator} which calculates the lump sum cost of a cooldown, for a given {@link ICurve}.
 * 
 * @author Alex Churchill
 */
public class CooldownPriceIndexedCalculator extends AbstractCooldownCalculator {

	public CooldownPriceIndexedCalculator(ICurve expressionCurve) {
		super(expressionCurve);
	}

	/**
	 * Find the unit price in dollars per mmbtu for gas at the given {@link ILoadSlot}, at the given time.
	 * 
	 * @param slot
	 * @param time
	 */
	public int calculateCooldownUnitPrice(final ILoadSlot slot, final int time) {
		return calculateSimpleUnitPrice(time, slot.getPort());
	}

	/**
	 * Find the unit price in dollars per mmbtu for gas at the given time. To be used when an {@link ILoadSlot} is not available.
	 * 
	 * @param time
	 * @param port
	 *            {@link IPort} for local to UTC conversion
	 */
	public int calculateCooldownUnitPrice(final int time, final IPort port) {
		return calculateSimpleUnitPrice(time, port);
	}

	public long calculateCooldownCost(IVesselClass vesselClass, IPort port, int cooldownCV, int time) {
		int priceM3 = Calculator.costPerM3FromMMBTu(calculateCooldownUnitPrice(time, port), cooldownCV);
		long volumeM3 = vesselClass.getCooldownVolume();
		long cost = Calculator.costFromConsumption(volumeM3, priceM3);
		return cost;
	}

}
