/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

/**
 * Implementation of a {@link ICooldownCalculator} which calculates the combined
 * lump sum cost and per volume unit cost of a cooldown, for a given
 * {@link ICurve}.
 * 
 * @author Alex Churchill
 */
public class CooldownCalculator implements ICooldownCalculator {

	private final @Nullable ICurve volumeExpressionCurve;
	private final @Nullable ILongCurve lumpsumExpressionCurve;

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	public CooldownCalculator(final @Nullable ILongCurve lumpsumExpressionCurve, final @Nullable ICurve volumeExpressionCurve) {
		this.lumpsumExpressionCurve = lumpsumExpressionCurve;
		this.volumeExpressionCurve = volumeExpressionCurve;
	}

	@Override
	public long calculateCooldownCost(final IVessel vessel, final IPort port, final int cooldownCV, final int time) {

		final int pricingTime = timeZoneToUtcOffsetProvider.UTC(time, port);

		long totalCost = 0L;
		if (lumpsumExpressionCurve != null) {
			totalCost += lumpsumExpressionCurve.getValueAtPoint(pricingTime);
		}
		if (volumeExpressionCurve != null) {
			int unitPricePerMMBTU = volumeExpressionCurve.getValueAtPoint(pricingTime);
			final int priceM3 = Calculator.costPerM3FromMMBTu(unitPricePerMMBTU, cooldownCV);
			final long volumeM3 = vessel.getCooldownVolume();
			final long cost = Calculator.costFromConsumption(volumeM3, priceM3);
			totalCost += cost;
		}

		return totalCost;
	}

}
