/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.browser.util.DataBrowserNodeHandler;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.integration.pricing"; //$NON-NLS-1$

	private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

	// The shared instance
	private static Activator plugin;

	private DataBrowserNodeHandler pricingNodeHandler;

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
		if (LicenseFeatures.isPermitted("features:hub-sync-pricing")) {
			pricingNodeHandler = new DataBrowserNodeHandler("Pricing", LNGScenarioSharedModelTypes.MARKET_CURVES.getID(), PricingRepository.INSTANCE,
					root -> new PricingRepositoryActionHandler(PricingRepository.INSTANCE, root));
			pricingNodeHandler.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		if (pricingNodeHandler != null) {
			pricingNodeHandler.stop();
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
