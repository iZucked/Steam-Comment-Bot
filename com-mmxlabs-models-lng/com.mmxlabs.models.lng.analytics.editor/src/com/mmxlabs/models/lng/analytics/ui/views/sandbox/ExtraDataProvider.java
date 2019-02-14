/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox;

import java.util.List;

import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;

public class ExtraDataProvider {
	public final List<VesselAvailability> extraVesselAvailabilities;
	public final List<CharterInMarketOverride> extraCharterInMarketOverrides;
	public final List<CharterInMarket> extraCharterInMarkets;

	public final List<LoadSlot> extraLoads;
	public final List<DischargeSlot> extraDischarges;

	public ExtraDataProvider(List<VesselAvailability> newAvailabilities, List<CharterInMarket> newCharterInMarkets, List<CharterInMarketOverride> newCharterInMarketOverrides,
			List<LoadSlot> extraLoads, List<DischargeSlot> extraDischarges) {
		this.extraVesselAvailabilities = newAvailabilities;
		this.extraCharterInMarkets = newCharterInMarkets;
		this.extraCharterInMarketOverrides = newCharterInMarketOverrides;
		this.extraLoads = extraLoads;
		this.extraDischarges = extraDischarges;
	}

}