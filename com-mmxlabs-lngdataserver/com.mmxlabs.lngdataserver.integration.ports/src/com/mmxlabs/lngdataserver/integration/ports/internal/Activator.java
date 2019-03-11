/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.browser.util.DataBrowserNodeHandler;
import com.mmxlabs.lngdataserver.integration.ports.PortsRepository;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.integration.ports"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private DataBrowserNodeHandler portsNodeHandler;

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
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
//		if (LicenseFeatures.isPermitted("features:hub-sync-ports")) {
//			portsNodeHandler = new DataBrowserNodeHandler("Ports", LNGScenarioSharedModelTypes.LOCATIONS.getID(), PortsRepository.INSTANCE,
//					root -> new PortsRepositoryActionHandler(PortsRepository.INSTANCE, root));
//			portsNodeHandler.start();
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		if (portsNodeHandler != null) {
			portsNodeHandler.stop();
		}

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
}
