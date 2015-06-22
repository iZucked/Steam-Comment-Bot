package com.mmxlabs.models.lng.transformer.its;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.mmxlabs.license.features.LicenseFeatures;

/**
 * Runner for test cases run outside of main application to initalise the Shiro Framework
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
		LicenseFeatures.initialiseFeatureEnablements();

		// Login our default user
		final Subject subject = SecurityUtils.getSubject();
		subject.login(new UsernamePasswordToken("user", "password"));
	}

}