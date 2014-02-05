package com.mmxlabs.scheduler.optimiser.providers.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IVesselCharterInRateProvider;

public class DefaultVesselCharterCurveProvider implements IVesselCharterInRateProvider {

	@Override
	public String getName() {
		return "";
	}

	@Override
	public void dispose() {

	}

	@Override
	public ICurve getCharterInRatePerDay(final IVessel vessel) {
		final ICurve charterCostCurve = vessel.getHourlyCharterInPrice();
		if (charterCostCurve == null) {
			return null;
		}
		// Temp hack to convert to daily rate
		return new ICurve() {

			@Override
			public int getValueAtPoint(final int point) {
				return 24 * charterCostCurve.getValueAtPoint(point);
			}
		};
	}

}
