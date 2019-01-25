/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.ui.DataExtension;
import com.mmxlabs.lngdataserver.integration.ports.internal.Activator;

public class PortsDataExtension implements DataExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortsDataExtension.class);

	@Override
	public CompositeNode getDataRoot() {
		LOGGER.debug("Port versions for Data Browser requested");

		return Activator.getDefault().getPortsDataRoot();
	}
}
