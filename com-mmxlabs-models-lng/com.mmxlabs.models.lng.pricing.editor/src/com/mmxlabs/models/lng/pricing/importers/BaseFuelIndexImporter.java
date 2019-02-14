/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;

/**
 * Custom import logic for loading a base fuel index.
 * 
 * @author Simon McGregor, achurchill
 * 
 */
public class BaseFuelIndexImporter extends GenericIndexImporter<BunkerFuelCurve> {

	public BaseFuelIndexImporter() {
		TYPE_VALUE = "base_fuel_index";
	}

	@Override
	protected BunkerFuelCurve createCurve() {
		return PricingFactory.eINSTANCE.createBunkerFuelCurve();
	}

}
