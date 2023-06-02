/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;

/**
 * Custom import logic for loading a commodity index contained as a formula
 * 
 * @author Simon Goodall, achurchill, FM
 * 
 */
public class FormulaImporter extends GenericIndexImporter<CommodityCurve> {

	public FormulaImporter() {
		TYPE_VALUE = "formula";
	}

	@Override
	protected CommodityCurve createCurve() {
		return PricingFactory.eINSTANCE.createCommodityCurve();
	}
}
