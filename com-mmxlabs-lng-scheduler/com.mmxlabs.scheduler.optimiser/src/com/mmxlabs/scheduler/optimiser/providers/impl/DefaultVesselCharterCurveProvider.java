/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.IVesselCharterInRateProvider;

public class DefaultVesselCharterCurveProvider implements IVesselCharterInRateProvider {

	@Override
	public ILongCurve getCharterInRatePerDay(final IVesselCharter vesselCharter) {
		return vesselCharter.getDailyCharterInRate();
	}
}
