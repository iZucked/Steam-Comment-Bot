/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
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

	public @NonNull BaseFuelIndex createBaseFuelExpressionIndex(@NonNull final String name, final double baseFuelUnitPrice) {

		final DerivedIndex<Double> indexData = PricingFactory.eINSTANCE.createDerivedIndex();
		indexData.setExpression(Double.toString(baseFuelUnitPrice));

		final BaseFuelIndex baseFuelIndex = PricingFactory.eINSTANCE.createBaseFuelIndex();
		baseFuelIndex.setName(name);
		baseFuelIndex.setData(indexData);

		pricingModel.getBaseFuelPrices().add(baseFuelIndex);

		return baseFuelIndex;
	}
}
