/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * Provider for spot charter in markets.
 *
 */
public interface ISpotCharterInMarketProvider extends IDataComponentProvider {

	@NonNull
	Collection<@NonNull ISpotCharterInMarket> getSpotCharterInMarkets();

	int getSpotCharterInMarketCount(@NonNull ISpotCharterInMarket market);

	@NonNull
	IVesselAvailability getSpotMarketAvailability(@NonNull ISpotCharterInMarket market, int spotIndex);

	@NonNull
	ISpotCharterInMarket getDefaultMarketForNominalCargoes();
}
