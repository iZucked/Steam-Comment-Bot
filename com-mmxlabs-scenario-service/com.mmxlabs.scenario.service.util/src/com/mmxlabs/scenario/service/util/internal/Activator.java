/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.scenario.service.util"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceTracker<IModelCommandProvider, IModelCommandProvider> commandProviderTracker;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		commandProviderTracker = new ServiceTracker<IModelCommandProvider, IModelCommandProvider>(context, IModelCommandProvider.class, null);
		commandProviderTracker.open();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(final BundleContext context) throws Exception {

		commandProviderTracker.close();
		commandProviderTracker = null;

		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public ServiceTracker<IModelCommandProvider, IModelCommandProvider> getCommandProviderTracker() {
		return commandProviderTracker;
	}
}
