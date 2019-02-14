/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.ui.DataExtension;
import com.mmxlabs.lngdataserver.integration.pricing.internal.Activator;

public class PricingDataExtension implements DataExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingDataExtension.class);

	@Override
	public CompositeNode getDataRoot() {
		LOGGER.debug("Pricing versions for Data Browser requested");

		return Activator.getDefault().getPricingDataRoot();
	}
}
