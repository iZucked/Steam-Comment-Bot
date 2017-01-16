/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import javax.inject.Inject;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelCurveProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * Implementation of {@link IVesselBaseFuelCalculator} looking up the base fuel rate of the vessel based on the voyage plan.
 * 
 * @author achurchill
 * 
 */
public class VesselBaseFuelCalculator implements IVesselBaseFuelCalculator {

	@Inject
	private IBaseFuelCurveProvider baseFuelProvider;

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Override
	public int getBaseFuelPrice(final IVessel vessel, final int voyagePlanStartTime) {
		return getBaseFuelPrice(vessel.getVesselClass(), voyagePlanStartTime);
	}

	@Override
	public int getBaseFuelPrice(final IVesselClass vesselClass, final int voyagePlanStartTime) {
		final IBaseFuel bf = vesselClass.getBaseFuel();
		
		final ICurve curve = baseFuelProvider.getBaseFuelCurve(bf);
		if (curve != null) {
			return curve.getValueAtPoint(voyagePlanStartTime);
		}
		return 0;
	}

	@Override
	public int getBaseFuelPrice(final IVessel vessel, final IPortTimesRecord portTimesRecord) {
		int startOfLoad = portTimesRecord.getFirstSlotTime();
		int startOfLoadUTC = timeZoneToUtcOffsetProvider.UTC(startOfLoad, portTimesRecord.getFirstSlot());
		return getBaseFuelPrice(vessel, startOfLoadUTC);
	}

}
