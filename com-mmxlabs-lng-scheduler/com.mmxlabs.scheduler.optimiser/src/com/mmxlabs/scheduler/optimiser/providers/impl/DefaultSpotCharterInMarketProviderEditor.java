/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.ISpotCharterInMarketProviderEditor;

public class DefaultSpotCharterInMarketProviderEditor implements ISpotCharterInMarketProviderEditor {

	private @NonNull final Set<@NonNull ISpotCharterInMarket> spotCharterInMarkets = new HashSet<>();
	private @NonNull final Map<@NonNull ISpotCharterInMarket, @NonNull Integer> spotCharterInMarketCount = new HashMap<>();
	private @NonNull final Map<@NonNull Pair<@NonNull ISpotCharterInMarket, @NonNull Integer>, @NonNull IVesselAvailability> spotCharterInMarketMap = new HashMap<>();

	@Override
	public @NonNull Collection<@NonNull ISpotCharterInMarket> getSpotCharterInMarkets() {
		return spotCharterInMarkets;
	}

	@Override
	public int getSpotCharterInMarketCount(@NonNull final ISpotCharterInMarket market) {
		return spotCharterInMarketCount.getOrDefault(market, 0);
	}

	@Override
	public @NonNull IVesselAvailability getSpotMarketAvailability(@NonNull final ISpotCharterInMarket market, final int spotIndex) {
		return spotCharterInMarketMap.get(new Pair<>(market, spotIndex));
	}

	@Override
	public void addSpotMarketAvailability(@NonNull final IVesselAvailability vesselAvailability, @NonNull final ISpotCharterInMarket market, final int spotIndex) {
		spotCharterInMarketMap.put(new Pair<>(market, spotIndex), vesselAvailability);
		spotCharterInMarkets.add(market);

		// If not a nominal option, update the highest option count.
		// Note: This does not validation we have an option for each index.
		if (spotIndex != -1) {
			spotCharterInMarketCount.merge(market, spotIndex + 1, (existing, other) -> {
				return Math.max(existing, other);
			});
		}
	}
}
