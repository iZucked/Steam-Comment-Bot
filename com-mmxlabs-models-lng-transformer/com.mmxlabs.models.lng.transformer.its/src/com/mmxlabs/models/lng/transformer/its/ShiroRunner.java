/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.mmxlabs.license.features.LicenseFeatures;

/**
 * Runner for test cases run outside of main application to initialise the Shiro Framework
 * 
 * @author Simon Goodall
 * 
 */
public class ShiroRunner implements BeforeAllCallback, AfterAllCallback {
	private List<String> requiredFeatures = new LinkedList<>();
	private List<String> addedFeatures = new LinkedList<>();

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		Class<?> requiredTestClass = context.getRequiredTestClass();

		RequireFeature annotation = requiredTestClass.getAnnotation(RequireFeature.class);
		if (annotation != null) {
			String[] features = annotation.features();
			if (features != null) {
				for (String f : features) {

					// Strip prefix if present.
					if (f.startsWith("features:")) {
						requiredFeatures.add(f.replace("features:", ""));
					} else {
						requiredFeatures.add(f);
					}
				}
			}
		}

		initAccessControl();

		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	private void initAccessControl() {
		// Initialise feature enablements
		LicenseFeatures.initialiseFeatureEnablements("optimisation-period", "optimisation-charter-out-generation", "panama-canal", "break-evens");
	}
}