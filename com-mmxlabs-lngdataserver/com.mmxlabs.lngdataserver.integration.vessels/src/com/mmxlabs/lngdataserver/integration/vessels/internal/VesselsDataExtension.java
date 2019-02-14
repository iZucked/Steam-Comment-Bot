/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.ui.DataExtension;

public class VesselsDataExtension implements DataExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(VesselsDataExtension.class);

	@Override
	public CompositeNode getDataRoot() {
		LOGGER.debug("Vessels versions for Data Browser requested");

		return Activator.getDefault().getVesselsDataRoot();
	}
}
