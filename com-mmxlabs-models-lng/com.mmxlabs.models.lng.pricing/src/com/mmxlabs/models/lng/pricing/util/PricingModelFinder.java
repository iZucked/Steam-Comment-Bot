/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;

public class PricingModelFinder {
	private final @NonNull PricingModel pricingModel;

	public PricingModelFinder(final @NonNull PricingModel pricingModel) {
		this.pricingModel = pricingModel;
	}

	@NonNull
	public PricingModel getPricingModel() {
		return pricingModel;
	}

	@NonNull
	public BaseFuelIndex findBaseFuelCurve(@NonNull final String name) {

		for (final BaseFuelIndex index : getPricingModel().getBaseFuelPrices()) {
			if (name.equals(index.getName())) {
				return index;
			}
		}

		throw new IllegalArgumentException("Base Fuel Index not found: " + name);
	}

	@NonNull
	public CommodityIndex findCommodityCurve(@NonNull final String name) {

		for (final CommodityIndex index : getPricingModel().getCommodityIndices()) {
			if (name.equals(index.getName())) {
				return index;
			}
		}

		throw new IllegalArgumentException("Commodity Index not found: " + name);
	}

	@NonNull
	public CharterIndex findCharterCurve(@NonNull final String name) {

		for (final CharterIndex index : getPricingModel().getCharterIndices()) {
			if (name.equals(index.getName())) {
				return index;
			}
		}

		throw new IllegalArgumentException("Charter Index not found: " + name);
	}
}
