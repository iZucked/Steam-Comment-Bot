/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;

/**
 * Custom import logic for loading a {@link CharterIndex}.
 * 
 * @author Simon Goodall
 * 
 */
public class CharterIndexImporter extends GenericIndexImporter<CharterCurve> {

	public CharterIndexImporter() {
		TYPE_VALUE = "charter_index";
	}

	@Override
	protected CharterCurve createCurve() {
		return PricingFactory.eINSTANCE.createCharterCurve();
	}
	
}
