/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;

/**
 * Custom import logic for loading a commodity index.
 * 
 * @author Simon Goodall, achurchill
 * 
 */
public class CommodityIndexImporter extends GenericIndexImporter<CommodityCurve> {

	public CommodityIndexImporter() {
		TYPE_VALUE = "commodity_index";
	}

	@Override
	protected CommodityCurve createCurve() {
		return PricingFactory.eINSTANCE.createCommodityCurve();
	}
}
