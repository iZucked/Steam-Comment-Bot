package com.mmxlabs.models.lng.pricing.util;

import org.eclipse.jdt.annotation.NonNull;

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
}
