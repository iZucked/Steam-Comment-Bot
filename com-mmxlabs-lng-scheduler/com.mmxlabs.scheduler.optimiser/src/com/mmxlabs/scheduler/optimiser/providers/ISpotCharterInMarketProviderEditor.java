/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

/**
 * Provider for spot charter in markets.
 *
 */
@NonNullByDefault
public interface ISpotCharterInMarketProviderEditor extends ISpotCharterInMarketProvider {

	void addSpotMarketAvailability(IVesselCharter vesselCharter, ISpotCharterInMarket market, int spotIndex);
}
