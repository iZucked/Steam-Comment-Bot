/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;

public class SpotMarketsModelBuilder {

	private final @NonNull SpotMarketsModel spotMarketsModel;

	public SpotMarketsModelBuilder(@NonNull final SpotMarketsModel spotMarketsModel) {
		this.spotMarketsModel = spotMarketsModel;
	}

	@NonNull
	public CharterInMarket createCharterInMarket(@NonNull final String name, @NonNull final VesselClass vesselClass, @NonNull final CharterIndex charterInPrice, final int charterInCount) {

		final CharterInMarket charterInMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		charterInMarket.setName(name);
		charterInMarket.setVesselClass(vesselClass);
		charterInMarket.setCharterInPrice(charterInPrice);
		charterInMarket.setSpotCharterCount(charterInCount);

		spotMarketsModel.getCharterInMarkets().add(charterInMarket);
		return charterInMarket;
	}
}
