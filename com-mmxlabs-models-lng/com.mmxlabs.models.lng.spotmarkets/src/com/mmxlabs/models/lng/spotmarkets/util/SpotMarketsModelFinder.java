/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.util;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotType;

public class SpotMarketsModelFinder {
	private final @NonNull SpotMarketsModel spotMarketsModel;

	public SpotMarketsModelFinder(final @NonNull SpotMarketsModel spotMarketsModel) {
		this.spotMarketsModel = spotMarketsModel;
	}

	@NonNull
	public SpotMarketsModel getSpotMarketsModel() {
		return spotMarketsModel;
	}

	public SpotMarket findSpotMarket(final @NonNull SpotType spotType, final @NonNull String marketName) {
		List<SpotMarket> markets = null;
		if (spotType == SpotType.DES_PURCHASE) {
			markets = spotMarketsModel.getDesPurchaseSpotMarket().getMarkets();
		} else if (spotType == SpotType.FOB_PURCHASE) {
			markets = spotMarketsModel.getFobPurchasesSpotMarket().getMarkets();
		} else if (spotType == SpotType.DES_SALE) {
			markets = spotMarketsModel.getDesSalesSpotMarket().getMarkets();
		} else if (spotType == SpotType.FOB_SALE) {
			markets = spotMarketsModel.getFobSalesSpotMarket().getMarkets();
		}
		if (markets == null) {
			throw new IllegalStateException();
		}
		for (final SpotMarket spotMarket : markets) {
			if (marketName.equals(spotMarket.getName())) {
				return spotMarket;
			}
		}

		return null;
	}
}
