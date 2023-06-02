/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.RegasPricingParams;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class DefaultExposuresCustomiser implements IExposuresCustomiser{
	
	@Override
	public @Nullable String provideExposedPriceExpression(@NonNull Slot<?> slot) {
		if (slot.eIsSet(CargoPackage.Literals.SLOT__PRICE_EXPRESSION)) {
			return slot.getPriceExpression();
		} else if (slot.eIsSet(CargoPackage.Literals.SLOT__CONTRACT)){
			final Contract contract = slot.getContract();
			if (contract != null) {
				final LNGPriceCalculatorParameters priceInfo = contract.getPriceInfo();
				if (priceInfo instanceof ExpressionPriceParameters epp) {
					return epp.getPriceExpression();
				} else if (priceInfo instanceof RegasPricingParams rpp) {
					return rpp.getPriceExpression();
				}
			}
		} else if (slot instanceof SpotSlot sslot) {
			final SpotMarket spotMarket = sslot.getMarket();
			if (spotMarket != null) {
				final LNGPriceCalculatorParameters priceInfo = spotMarket.getPriceInfo();
				if (priceInfo instanceof DateShiftExpressionPriceParameters dsepp) {
					return dsepp.getPriceExpression();
				}
			}
		}

		return null;
	}

	@Override
	public Slot<?> getExposed(Slot<?> slot) {
		return slot;
	}

	@Override
	public @Nullable String provideExposedPriceExpression(@NonNull Slot<?> slot, final ModelMarketCurveProvider mmCurveProvider) {
		return provideExposedPriceExpression(slot);
	}

}
