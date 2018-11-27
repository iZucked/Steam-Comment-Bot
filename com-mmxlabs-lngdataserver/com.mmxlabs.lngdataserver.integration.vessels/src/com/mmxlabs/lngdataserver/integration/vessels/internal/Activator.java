/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.internal;

import java.util.List;
import java.util.Objects;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.integration.vessels.VesselsRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.rcp.common.RunnerHelper;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.integration.vessels"; //$NON-NLS-1$

	private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

	// The shared instance
	private static Activator plugin;

	private final CompositeNode vesselsDataRoot = BrowserFactory.eINSTANCE.createCompositeNode();
	private VesselsRepository vesselsRepository;
	private boolean active;

	/**
	 * The constructor
	 */
	public Activator() {
		Node loading = BrowserFactory.eINSTANCE.createLeaf();
		loading.setDisplayName("loading...");
		vesselsDataRoot.setDisplayName("Vessels (loading...)");
		vesselsDataRoot.setType(LNGScenarioSharedModelTypes.FLEET.getID());
		vesselsDataRoot.getChildren().add(loading);

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

		vesselsRepository = VesselsRepository.INSTANCE;
		vesselsDataRoot.setActionHandler(new VesselsRepositoryActionHandler(vesselsRepository, vesselsDataRoot));

		active = true;
		BackEndUrlProvider.INSTANCE.addAvailableListener(() -> loadVersions());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		if (vesselsRepository != null) {
			vesselsRepository.stopListeningForNewLocalVersions();
			vesselsRepository = null;
		}
		vesselsDataRoot.setActionHandler(null);
		vesselsDataRoot.getChildren().clear();
		vesselsDataRoot.setCurrent(null);

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

	public CompositeNode getVesselsDataRoot() {
		return vesselsDataRoot;
	}

	//
	private void loadVersions() {

		while (!vesselsRepository.isReady() && active) {
			try {
				LOGGER.debug("Vessel back-end not ready yet...");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}

		if (active) {

		}

		if (active) {
			LOGGER.debug("Vessel back-end ready, retrieving versions...");
			try {
				vesselsDataRoot.setDisplayName("Vessels");
				vesselsDataRoot.getActionHandler().refreshLocal();
			} catch (Exception e) {
				LOGGER.error("Error retrieving vessels versions");
			}

			// register consumer to update on new version
			vesselsRepository.registerLocalVersionListener(v -> vesselsDataRoot.getActionHandler().refreshLocal());
			vesselsRepository.startListenForNewLocalVersions();

			vesselsRepository.registerUpstreamVersionListener(v -> {
				RunnerHelper.asyncExec(c -> {
					try {
						vesselsRepository.syncUpstreamVersion(v.getIdentifier());
					} catch (final Exception e) {
						e.printStackTrace();
					}
				});
			});
			vesselsRepository.startListenForNewUpstreamVersions();
		}
	}
}
