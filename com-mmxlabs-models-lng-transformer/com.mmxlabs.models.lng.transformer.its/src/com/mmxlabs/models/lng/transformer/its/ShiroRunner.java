/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.mmxlabs.license.features.LicenseFeatures;

/**
 * Runner for test cases run outside of main application to initialise the Shiro Framework
 * 
 * @author Simon Goodall
 * 
 */
public class ShiroRunner implements BeforeAllCallback {

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		initAccessControl();
	}

	private void initAccessControl() {
		// Initialise feature enablements
		LicenseFeatures.initialiseFeatureEnablements("optimisation-period", "optimisation-charter-out-generation", "panama-canal", "break-evens");
	}
}