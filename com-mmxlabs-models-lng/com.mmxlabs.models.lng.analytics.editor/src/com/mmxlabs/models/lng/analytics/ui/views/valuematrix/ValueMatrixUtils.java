/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.Range;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters;

@NonNullByDefault
public class ValueMatrixUtils {
	private ValueMatrixUtils() {
	}

	public static SwapValueMatrixModel createEmptyValueMatrixModel(final String name) {
		final SwapValueMatrixModel model = AnalyticsFactory.eINSTANCE.createSwapValueMatrixModel();
		model.setName(name);
		final SwapValueMatrixParameters parameters = AnalyticsFactory.eINSTANCE.createSwapValueMatrixParameters();
		final Range baseRange = AnalyticsFactory.eINSTANCE.createRange();
		parameters.setBasePriceRange(baseRange);
		final Range swapRange = AnalyticsFactory.eINSTANCE.createRange();
		parameters.setSwapPriceRange(swapRange);

		final BuyReference buyReference = AnalyticsFactory.eINSTANCE.createBuyReference();
		final SellReference sellReference = AnalyticsFactory.eINSTANCE.createSellReference();
		final ExistingVesselCharterOption vesselCharterReference = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
		final BuyMarket buyMarket = AnalyticsFactory.eINSTANCE.createBuyMarket();
		final SellMarket sellMarket = AnalyticsFactory.eINSTANCE.createSellMarket();
		parameters.setBaseLoad(buyReference);
		parameters.setBaseDischarge(sellReference);
		parameters.setSwapLoadMarket(buyMarket);
		parameters.setSwapDischargeMarket(sellMarket);
		parameters.setBaseVesselCharter(vesselCharterReference);
		model.setParameters(parameters);
		return model;
	}
}
