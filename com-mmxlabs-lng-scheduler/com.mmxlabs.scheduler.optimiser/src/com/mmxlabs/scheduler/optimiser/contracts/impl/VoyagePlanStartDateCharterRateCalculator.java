/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IVesselCharterInRateProvider;

/**
 * Implementation of {@link ICharterRateCalculator} looking up the charter rate
 * of the vessel based on the start time of the current voyage plan
 * 
 * @author Simon Goodall
 * 
 */
public class VoyagePlanStartDateCharterRateCalculator implements ICharterRateCalculator {

	@Inject
	private IVesselCharterInRateProvider vesselCharterInRateProvider;

	/**
	 * One client used to use vesselStartTime, probably not used anymore.
	 * 
	 * @param vesselStartTime
	 * @param voyagePlanStartTime
	 */
	@Override
	public long getCharterRatePerDay(final @NonNull IVesselCharter vesselCharter, final int voyagePlanStartTime) {
		final ILongCurve rate = vesselCharterInRateProvider.getCharterInRatePerDay(vesselCharter);
		if (rate != null) {
			return rate.getValueAtPoint(voyagePlanStartTime);
		}
		return 0L;
	}

}
