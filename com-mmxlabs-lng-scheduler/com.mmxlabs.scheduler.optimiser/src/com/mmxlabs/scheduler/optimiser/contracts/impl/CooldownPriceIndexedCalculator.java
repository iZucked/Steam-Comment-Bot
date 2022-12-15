/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

/**
 * Implementation of a {@link ICooldownLumpSumPriceCalculator} which calculates
 * the lump sum cost of a cooldown, for a given {@link ICurve}.
 * 
 * @author Alex Churchill
 */
public class CooldownPriceIndexedCalculator implements ICooldownCalculator {

	private final ICurve expressionCurve;

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	public CooldownPriceIndexedCalculator(final ICurve expressionCurve) {
		this.expressionCurve = expressionCurve;
	}

	@Override
	public long calculateCooldownCost(final IVessel vessel, final IPort port, final int cooldownCV, final int time) {
		final long volumeM3 = vessel.getCooldownVolume();
//
//		final Map<String, String> params = new HashMap<>();
//		params.put(VolumeTierSeries.PARAM_VOLUME_M3, Double.toString(OptimiserUnitConvertor.convertToExternalFloatVolume(volumeM3)));
//		params.put(VolumeTierSeries.PARAM_VOLUME_MMBTU, Double.toString(OptimiserUnitConvertor.convertToExternalFloatVolume(Calculator.convertM3ToMMBTu(volumeM3, cooldownCV))));

		final int pricePerMMBTU = expressionCurve.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(time, port));
		final int priceM3 = Calculator.costPerM3FromMMBTu(pricePerMMBTU, cooldownCV);
		return Calculator.costFromConsumption(volumeM3, priceM3);
	}

}
