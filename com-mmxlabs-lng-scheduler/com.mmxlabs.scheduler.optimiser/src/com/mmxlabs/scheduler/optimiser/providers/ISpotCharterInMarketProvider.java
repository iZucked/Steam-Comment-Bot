/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

/**
 * Provider for spot charter in markets.
 *
 */
@NonNullByDefault
public interface ISpotCharterInMarketProvider extends IDataComponentProvider {

	Collection<ISpotCharterInMarket> getSpotCharterInMarkets();

	int getSpotCharterInMarketCount(ISpotCharterInMarket market);

	IVesselCharter getSpotMarketAvailability(ISpotCharterInMarket market, int spotIndex);
}
