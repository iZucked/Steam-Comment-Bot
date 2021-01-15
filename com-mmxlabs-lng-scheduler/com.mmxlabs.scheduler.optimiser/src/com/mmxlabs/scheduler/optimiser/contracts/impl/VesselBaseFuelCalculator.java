/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelCurveProvider;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * Implementation of {@link IVesselBaseFuelCalculator} looking up the base fuel rate of the vessel based on the voyage plan.
 * 
 * @author achurchill
 * 
 */
@NonNullByDefault
public class VesselBaseFuelCalculator implements IVesselBaseFuelCalculator {

	@Inject
	private IBaseFuelProvider baseFuelProvider;

	@Inject
	private IBaseFuelCurveProvider baseFuelCurveProvider;

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Override
	public int[] getBaseFuelPrices(final IVessel vessel, final int voyagePlanStartTime) {
		final IBaseFuel bf = vessel.getTravelBaseFuel();
		final IBaseFuel idleBf = vessel.getIdleBaseFuel();
		final IBaseFuel pilotLightBf = vessel.getPilotLightBaseFuel();
		final IBaseFuel inPortBf = vessel.getInPortBaseFuel();

		final int[] fuelPrices = new int[baseFuelProvider.getNumberOfBaseFuels()];
		{
			final ICurve curve = baseFuelCurveProvider.getBaseFuelCurve(bf);
			if (curve != null) {
				int price = curve.getValueAtPoint(voyagePlanStartTime);
				fuelPrices[bf.getIndex()] = price;
			}
		}
		{
			final ICurve curve = baseFuelCurveProvider.getBaseFuelCurve(idleBf);
			if (curve != null) {
				int price = curve.getValueAtPoint(voyagePlanStartTime);
				fuelPrices[idleBf.getIndex()] = price;
			}
		}
		{
			final ICurve curve = baseFuelCurveProvider.getBaseFuelCurve(pilotLightBf);
			if (curve != null) {
				int price = curve.getValueAtPoint(voyagePlanStartTime);
				fuelPrices[pilotLightBf.getIndex()] = price;
			}
		}
		{
			final ICurve curve = baseFuelCurveProvider.getBaseFuelCurve(inPortBf);
			if (curve != null) {
				int price = curve.getValueAtPoint(voyagePlanStartTime);
				fuelPrices[inPortBf.getIndex()] = price;
			}
		}

		return fuelPrices;
	}

	@Override
	public int[] getBaseFuelPrices(final IVessel vessel, final IPortTimesRecord portTimesRecord) {
		int startOfLoad = portTimesRecord.getFirstSlotTime();
		int startOfLoadUTC = timeZoneToUtcOffsetProvider.UTC(startOfLoad, portTimesRecord.getFirstSlot());
		return getBaseFuelPrices(vessel, startOfLoadUTC);
	}

}
