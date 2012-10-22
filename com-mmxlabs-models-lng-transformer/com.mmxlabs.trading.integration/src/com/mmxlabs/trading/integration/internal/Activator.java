/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.integration.internal;

import org.osgi.framework.BundleContext;

import com.mmxlabs.models.ui.validation.ValidationPlugin;

public class Activator extends ValidationPlugin {

	public static final String PLUGIN_ID = "com.mmxlabs.trading.integration";

	private static BundleContext context;

	private static Activator plugin = null;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.plugin = this;
		super.start(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(final BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		Activator.context = null;
		Activator.plugin = null;
	}

	public static Activator getDefault() {
		return plugin;
	}

}
