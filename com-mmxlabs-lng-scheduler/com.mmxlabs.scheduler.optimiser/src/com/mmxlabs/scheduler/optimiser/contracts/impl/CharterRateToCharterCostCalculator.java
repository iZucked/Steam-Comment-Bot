/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;

public class CharterRateToCharterCostCalculator implements ICharterCostCalculator {

	private IVesselCharter vesselCharter;
	private ICharterRateCalculator charterRateCalculator;

	@Override
	public long getCharterCost(int voyagePlanStartTime, int eventStartTime, int duration) {
		long charterRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselCharter, voyagePlanStartTime);
		return (charterRatePerDay * duration) / 24L;
	}

	public void initialise(IVesselCharter vesselCharter, @NonNull ICharterRateCalculator charterRateCalculator) {
		this.vesselCharter = vesselCharter;
		this.charterRateCalculator = charterRateCalculator;
	}
}
