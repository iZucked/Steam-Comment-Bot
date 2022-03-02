/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;

/**
 * Custom import logic for loading a commodity index.
 * 
 * @author Simon Goodall, achurchill
 * 
 */
public class CurrencyIndexImporter extends GenericIndexImporter<CurrencyCurve> {

	public CurrencyIndexImporter() {
		TYPE_VALUE = "currency_index";
	}

	@Override
	protected CurrencyCurve createCurve() {
		return PricingFactory.eINSTANCE.createCurrencyCurve();
	}
}
