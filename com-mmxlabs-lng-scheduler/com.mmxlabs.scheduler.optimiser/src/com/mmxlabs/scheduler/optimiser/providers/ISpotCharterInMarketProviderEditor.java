/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * Provider for spot charter in markets.
 *
 */
public interface ISpotCharterInMarketProviderEditor extends ISpotCharterInMarketProvider {

	void addSpotMarketAvailability(@NonNull IVesselAvailability vesselAvailability, @NonNull ISpotCharterInMarket market, int spotIndex);
}
