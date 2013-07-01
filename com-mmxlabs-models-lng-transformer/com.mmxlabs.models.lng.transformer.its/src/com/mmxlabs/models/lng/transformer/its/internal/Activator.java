/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.mmxlabs.models.lng.transformer.its.tests.TransformerITSOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.models.lng.transformer.its"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceRegistration<?> reg;

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
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		reg = context.registerService(new String[] { IOptimiserInjectorService.class.getCanonicalName() }, new TransformerITSOptimiserInjectorService(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		reg.unregister();
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
}
