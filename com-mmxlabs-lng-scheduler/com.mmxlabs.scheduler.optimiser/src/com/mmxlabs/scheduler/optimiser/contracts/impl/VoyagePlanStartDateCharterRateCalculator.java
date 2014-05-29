/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import javax.inject.Inject;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IVesselCharterInRateProvider;

/**
 * Implementation of {@link ICharterRateCalculator} looking up the charter rate of the vessel based on the start time of the current voyage plan
 * 
 * @author Simon Goodall
 * 
 */
public class VoyagePlanStartDateCharterRateCalculator implements ICharterRateCalculator {

	@Inject
	private IVesselCharterInRateProvider IVesselCharterInRateProvider;

	@Override
	public int getCharterRatePerDay(IVessel vessel, int vesselStartTime, int voyagePlanStartTime) {
		ICurve rate = IVesselCharterInRateProvider.getCharterInRatePerDay(vessel);
		if (rate != null) {
			return rate.getValueAtPoint(voyagePlanStartTime);
		}
		return 0;
	}

}
