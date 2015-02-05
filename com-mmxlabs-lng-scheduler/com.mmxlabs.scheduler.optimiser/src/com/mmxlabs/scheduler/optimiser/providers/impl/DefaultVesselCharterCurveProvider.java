/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IVesselCharterInRateProvider;

public class DefaultVesselCharterCurveProvider implements IVesselCharterInRateProvider {

	@Override
	public ICurve getCharterInRatePerDay(final IVesselAvailability vesselAvailability) {
		return vesselAvailability.getDailyCharterInRate();
	}
}
