/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.license.features.properties;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.license.features.LicenseFeatures;

public class FeatureEnablementPropertyTester extends PropertyTester {
	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {

		boolean permitted = args.length > 0;
		for (final Object arg : args) {
			if (arg != null) {
				permitted &= LicenseFeatures.isPermitted(arg.toString());
			}
		}
		return permitted;
	}
}
