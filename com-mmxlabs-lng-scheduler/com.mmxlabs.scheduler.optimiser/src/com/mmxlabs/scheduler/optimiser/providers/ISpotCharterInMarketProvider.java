/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * Provider for spot charter in markets.
 *
 */
@NonNullByDefault
public interface ISpotCharterInMarketProvider extends IDataComponentProvider {

	Collection<ISpotCharterInMarket> getSpotCharterInMarkets();

	int getSpotCharterInMarketCount(ISpotCharterInMarket market);

	IVesselAvailability getSpotMarketAvailability(ISpotCharterInMarket market, int spotIndex);
}
