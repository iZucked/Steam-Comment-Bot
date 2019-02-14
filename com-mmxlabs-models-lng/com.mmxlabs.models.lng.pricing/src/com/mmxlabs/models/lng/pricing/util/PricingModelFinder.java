/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
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
	public BunkerFuelCurve findBaseFuelCurve(@NonNull final String name) {

		for (final BunkerFuelCurve index : getPricingModel().getBunkerFuelCurves()) {
			if (name.equals(index.getName())) {
				return index;
			}
		}

		throw new IllegalArgumentException("Base Fuel Index not found: " + name);
	}

	@NonNull
	public CommodityCurve findCommodityCurve(@NonNull final String name) {

		for (final CommodityCurve index : getPricingModel().getCommodityCurves()) {
			if (name.equals(index.getName())) {
				return index;
			}
		}

		throw new IllegalArgumentException("Commodity Index not found: " + name);
	}

	@NonNull
	public CharterCurve findCharterCurve(@NonNull final String name) {

		for (final CharterCurve index : getPricingModel().getCharterCurves()) {
			if (name.equals(index.getName())) {
				return index;
			}
		}

		throw new IllegalArgumentException("Charter Index not found: " + name);
	}
}
