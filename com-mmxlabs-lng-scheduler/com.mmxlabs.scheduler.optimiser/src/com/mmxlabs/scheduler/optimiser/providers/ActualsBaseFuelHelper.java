/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class ActualsBaseFuelHelper {

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private IBaseFuelProvider baseFuelProvider;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	public int[] getActualisedOrForecastBaseFuelPrices(final @NonNull IVessel vessel, final @NonNull IPortTimesRecord portTimesRecord, final @NonNull IPortSlot actualisedSlot) {
		if (actualsDataProvider.hasActuals(actualisedSlot)) {
			final int[] baseFuelPricesPerMT = new int[baseFuelProvider.getNumberOfBaseFuels()];
			baseFuelPricesPerMT[actualsDataProvider.getPortBaseFuel(actualisedSlot).getIndex()] = actualsDataProvider.getPortBaseFuelPricePerMT(actualisedSlot);
			baseFuelPricesPerMT[actualsDataProvider.getNextVoyageBaseFuel(actualisedSlot).getIndex()] = actualsDataProvider.getNextVoyageBaseFuelPricePerMT(actualisedSlot);
			baseFuelPricesPerMT[actualsDataProvider.getNextIdleBaseFuel(actualisedSlot).getIndex()] = actualsDataProvider.getNextIdleBaseFuelPricePerMT(actualisedSlot);
			baseFuelPricesPerMT[actualsDataProvider.getNextVoyagePilotBaseFuel(actualisedSlot).getIndex()] = actualsDataProvider.getNextVoyagePilotBaseFuelPricePerMT(actualisedSlot);
			return baseFuelPricesPerMT;
		} else {
			return vesselBaseFuelCalculator.getBaseFuelPrices(vessel, portTimesRecord);
		}
	}
}
