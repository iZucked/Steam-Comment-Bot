/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.integration.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.integration.distances"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private final CompositeNode dataRoot = BrowserFactory.eINSTANCE.createCompositeNode();
	private DistanceRepository repository;
	private boolean active;

	/**
	 * The constructor
	 */
	public Activator() {
		final Node loading = BrowserFactory.eINSTANCE.createLeaf();
		loading.setDisplayName("loading...");
		dataRoot.setDisplayName("Distances (loading...)");
		dataRoot.setType(LNGScenarioSharedModelTypes.DISTANCES.getID());
		dataRoot.getChildren().add(loading);
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

		repository = DistanceRepository.INSTANCE;
		dataRoot.setActionHandler(new DistanceRepositoryActionHandler(repository, dataRoot));

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
		dataRoot.setActionHandler(null);
		dataRoot.getChildren().clear();
		dataRoot.setCurrent(null);

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

	public CompositeNode getDistancesDataRoot() {
		return dataRoot;
	}

	private void loadVersions() {
		while (!repository.isReady() && active) {
			try {
				LOGGER.debug("Distances back-end not ready yet...");
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				LOGGER.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		if (active) {
			LOGGER.debug("Distances back-end ready, retrieving versions...");
			try {
				dataRoot.getChildren().clear();
				dataRoot.setDisplayName("Distances");
				dataRoot.getActionHandler().refreshLocal();
			} catch (Exception e) {
				LOGGER.error("Error retrieving distance versions");
			}
			// register consumer to update on new version
			repository.registerLocalVersionListener(() -> dataRoot.getActionHandler().refreshLocal());
			repository.startListenForNewLocalVersions();

			repository.registerDefaultUpstreamVersionListener();
			repository.startListenForNewUpstreamVersions();
		}
	}

}
