/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

/**
 * Implementation of a {@link ICooldownLumpSumPriceCalculator} which calculates the lump sum cost of a cooldown, for a given {@link ICurve}.
 * 
 * @author Alex Churchill
 */
public class CooldownLumpSumCalculator implements ICooldownCalculator {
	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	private @NonNull ILongCurve expressionCurve;

	public CooldownLumpSumCalculator(final @NonNull ILongCurve expressionCurve) {
		this.expressionCurve = expressionCurve;
	}

	protected long calculateUnitPriceAtUTCTime(final int localTime, final @NonNull IPort port) {
		return expressionCurve.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(localTime, port));
	}

	/**
	 * Calculates the lump sum cost of a cooldown, according to a given point on an {@link ICurve}.
	 * 
	 * @param time
	 * @return lump sum cost
	 */
	private long getLumpSumCost(final int localTime, final @NonNull IPort port) {
		return calculateUnitPriceAtUTCTime(localTime, port);
	}

	@Override
	public long calculateCooldownCost(final @NonNull IVessel vessel, final @NonNull IPort port, final int cv, final int time) {
		return getLumpSumCost(time, port);
	}

}
