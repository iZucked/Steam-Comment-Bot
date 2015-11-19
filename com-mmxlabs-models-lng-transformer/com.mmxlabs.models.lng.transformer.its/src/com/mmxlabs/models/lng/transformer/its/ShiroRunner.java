/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.mmxlabs.license.features.LicenseFeatures;

/**
 * Runner for test cases run outside of main application to initialise the Shiro Framework
 * 
 * @author Simon Goodall
 * 
 */
public class ShiroRunner extends BlockJUnit4ClassRunner {

	public ShiroRunner(Class<?> klass) throws InitializationError {
		super(klass);
		initAccessControl();
	}

	private void initAccessControl() {
		// Initialise feature enablements
		LicenseFeatures.initialiseFeatureEnablements("optimisation-period", "optimisation-charter-out-generation");

		// Login our default user
		final Subject subject = SecurityUtils.getSubject();
		subject.logout();
		subject.login(new UsernamePasswordToken("user", "password"));
	}

}