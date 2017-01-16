/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation.internal;

import org.osgi.framework.BundleContext;

import com.mmxlabs.models.ui.validation.ValidationPlugin;

public class Activator extends ValidationPlugin {
	public static final String PLUGIN_ID = "com.mmxlabs.models.lng.analytics.validation";
	private static Activator instance = null;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return instance;
	}
}
