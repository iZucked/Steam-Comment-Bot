/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.ISpotCharterInMarketProviderEditor;

@NonNullByDefault
public class DefaultSpotCharterInMarketProviderEditor implements ISpotCharterInMarketProviderEditor {

	private final Set<ISpotCharterInMarket> spotCharterInMarkets = new HashSet<>();
	private final Map<ISpotCharterInMarket, Integer> spotCharterInMarketCount = new HashMap<>();
	private final Map<Pair<ISpotCharterInMarket, Integer>, IVesselCharter> spotCharterInMarketMap = new HashMap<>();

	@Override
	public Collection<ISpotCharterInMarket> getSpotCharterInMarkets() {
		return spotCharterInMarkets;
	}

	@Override
	public int getSpotCharterInMarketCount(final ISpotCharterInMarket market) {
		return spotCharterInMarketCount.getOrDefault(market, 0);
	}

	@Override
	public IVesselCharter getSpotMarketAvailability(final ISpotCharterInMarket market, final int spotIndex) {
		return spotCharterInMarketMap.get(Pair.of(market, spotIndex));
	}

	@Override
	public void addSpotMarketAvailability(final IVesselCharter vesselCharter, final ISpotCharterInMarket market, final int spotIndex) {
		spotCharterInMarketMap.put(Pair.of(market, spotIndex), vesselCharter);
		spotCharterInMarkets.add(market);

		// If not a nominal option, update the highest option count.
		// Note: This does not validation we have an option for each index.
		if (spotIndex != -1) {
			spotCharterInMarketCount.merge(market, spotIndex + 1, Math::max);
		}
	}
}
