package com.mmxlabs.scheduler.optimiser.contracts.impl;

import javax.inject.Inject;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IVesselCharterInRateProvider;

/**
 * Implementation of {@link ICharterRateCalculator} looking up the charter rate of the vessel based on the time the vessel is first used.
 * 
 * @author Simon Goodall
 * 
 */
public class VesselStartDateCharterRateCalculator implements ICharterRateCalculator {

	@Inject
	private IVesselCharterInRateProvider IVesselCharterInRateProvider;

	@Override
	public int getCharterRatePerDay(IVessel vessel, int vesselStartTime, int voyagePlanStartTime) {
		ICurve rate = IVesselCharterInRateProvider.getCharterInRatePerDay(vessel);
		if (rate != null) {
			return rate.getValueAtPoint(vesselStartTime);
		}
		return 0;
	}

}
