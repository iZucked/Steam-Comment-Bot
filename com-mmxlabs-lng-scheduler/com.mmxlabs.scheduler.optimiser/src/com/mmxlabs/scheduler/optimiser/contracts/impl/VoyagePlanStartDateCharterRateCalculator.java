/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
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
	public long getCharterRatePerDay(final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime, final int voyagePlanStartTime) {
		final ILongCurve rate = IVesselCharterInRateProvider.getCharterInRatePerDay(vesselAvailability);
		if (rate != null) {
			return rate.getValueAtPoint(voyagePlanStartTime);
		}
		return 0L;
	}

}
