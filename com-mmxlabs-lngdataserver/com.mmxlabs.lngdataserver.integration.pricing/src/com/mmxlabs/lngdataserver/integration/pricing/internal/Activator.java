/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.rcp.common.RunnerHelper;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.integration.pricing"; //$NON-NLS-1$

	private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

	// The shared instance
	private static Activator plugin;

	private final CompositeNode pricingDataRoot = BrowserFactory.eINSTANCE.createCompositeNode();
	private PricingRepository repository = null;

	private boolean active;

	/**
	 * The constructor
	 */
	public Activator() {
		final Node loading = BrowserFactory.eINSTANCE.createLeaf();
		loading.setDisplayName("loading...");
		pricingDataRoot.setDisplayName("Pricing (loading...)");
		pricingDataRoot.setType(LNGScenarioSharedModelTypes.MARKET_CURVES.getID());
		pricingDataRoot.getChildren().add(loading);

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

		repository = PricingRepository.INSTANCE;
		pricingDataRoot.setActionHandler(new PricingRepositoryActionHandler(repository, pricingDataRoot));

		active = true;
		BackEndUrlProvider.INSTANCE.addAvailableListener(() -> loadVersions());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		if (repository != null) {
			repository.stopListeningForNewLocalVersions();
			repository = null;
		}
		pricingDataRoot.setActionHandler(null);
		pricingDataRoot.getChildren().clear();
		pricingDataRoot.setCurrent(null);

		plugin = null;
		super.stop(context);
		active = false;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public CompositeNode getPricingDataRoot() {
		return pricingDataRoot;
	}

	public PricingRepository getPricingRepository() {
		return repository;
	}

	private void loadVersions() {
		while (!repository.isReady() && active) {
			try {
				LOGGER.debug("Pricing back-end not ready yet...");
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				LOGGER.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		if (active) {
			LOGGER.debug("Pricing back-end ready, retrieving versions...");
			try {
				pricingDataRoot.setDisplayName("Pricing");
				pricingDataRoot.getChildren().clear();
				pricingDataRoot.getActionHandler().refreshLocal();
			} catch (final Exception e) {
				LOGGER.error("Error retrieving pricing versions");
			}

			// register consumer to update on new version
			repository.registerLocalVersionListener(() -> pricingDataRoot.getActionHandler().refreshLocal());
			repository.startListenForNewLocalVersions();

			repository.registerDefaultUpstreamVersionListener();
			repository.startListenForNewUpstreamVersions();
		}
	}
}
