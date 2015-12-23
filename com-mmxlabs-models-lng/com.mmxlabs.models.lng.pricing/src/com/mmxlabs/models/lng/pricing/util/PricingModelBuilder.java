/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;

public class PricingModelBuilder {

	private final @NonNull PricingModel pricingModel;

	public PricingModelBuilder(@NonNull final PricingModel pricingModel) {
		this.pricingModel = pricingModel;
	}

	@NonNull
	public CharterIndex createCharterIndex(@NonNull final String name, @Nullable final String units, final int fixedPricePerDay) {

		final CharterIndex charterIndex = PricingFactory.eINSTANCE.createCharterIndex();
		charterIndex.setName(name);
		if (units != null) {
			charterIndex.setUnits(units);
		}

		final DerivedIndex<Integer> derivedIndex = PricingFactory.eINSTANCE.createDerivedIndex();
		derivedIndex.setExpression(Integer.toString(fixedPricePerDay));

		charterIndex.setData(derivedIndex);

		pricingModel.getCharterIndices().add(charterIndex);

		return charterIndex;
	}
}
