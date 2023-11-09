/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.paper.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.mmxlabs.lngdataserver.browser.util.DataBrowserNodeHandler;
import com.mmxlabs.lngdataserver.integration.paper.PaperRepository;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.integration.paper"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private DataBrowserNodeHandler paperNodeHandler;

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
		paperNodeHandler = new DataBrowserNodeHandler("Paper", LNGScenarioSharedModelTypes.PAPER_DEALS.getID(), PaperRepository.INSTANCE,
				root -> new PaperRepositoryActionHandler(PaperRepository.INSTANCE, root));
		paperNodeHandler.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		if (paperNodeHandler != null) {
			paperNodeHandler.stop();
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
