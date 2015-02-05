/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.license.features.properties;

import org.apache.shiro.SecurityUtils;
import org.eclipse.core.expressions.PropertyTester;

public class FeatureEnablementPropertyTester extends PropertyTester {
	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {

		boolean permitted = args.length > 0;
		for (final Object arg : args) {
			permitted &= SecurityUtils.getSubject().isPermitted(arg.toString());
		}
		return permitted;
	}
}
