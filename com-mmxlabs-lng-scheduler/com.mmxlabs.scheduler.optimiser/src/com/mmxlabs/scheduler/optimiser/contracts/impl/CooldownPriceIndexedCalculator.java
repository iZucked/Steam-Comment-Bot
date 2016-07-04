/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

/**
 * Implementation of a {@link ICooldownLumpSumPriceCalculator} which calculates the lump sum cost of a cooldown, for a given {@link ICurve}.
 * 
 * @author Alex Churchill
 */
public class CooldownPriceIndexedCalculator implements ICooldownCalculator {

	private final ICurve expressionCurve;

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	public CooldownPriceIndexedCalculator(final @NonNull ICurve expressionCurve) {
		this.expressionCurve = expressionCurve;
	}

	@Override
	public void prepareEvaluation(final ISequences sequences) {

	}

	protected int calculateUnitPriceAtUTCTime(final int localTime, final IPort port) {
		return expressionCurve.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(localTime, port));
	}

	@Override
	public long calculateCooldownCost(final IVesselClass vesselClass, final IPort port, final int cooldownCV, final int time) {
		final int priceM3 = Calculator.costPerM3FromMMBTu(calculateUnitPriceAtUTCTime(time, port), cooldownCV);
		final long volumeM3 = vesselClass.getCooldownVolume();
		final long cost = Calculator.costFromConsumption(volumeM3, priceM3);
		return cost;
	}

}
