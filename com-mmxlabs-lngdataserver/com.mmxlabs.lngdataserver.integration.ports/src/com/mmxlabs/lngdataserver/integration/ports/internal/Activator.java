/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports.internal;

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
import com.mmxlabs.lngdataserver.integration.ports.PortsRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.rcp.common.RunnerHelper;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.integration.ports"; //$NON-NLS-1$

	private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

	// The shared instance
	private static Activator plugin;

	private final CompositeNode dataRoot = BrowserFactory.eINSTANCE.createCompositeNode();
	private PortsRepository repository;
	private boolean active;

	/**
	 * The constructor
	 */
	public Activator() {
		final Node loading = BrowserFactory.eINSTANCE.createLeaf();
		loading.setDisplayName("loading...");
		dataRoot.setDisplayName("Ports (loading...)");
		dataRoot.setType(LNGScenarioSharedModelTypes.LOCATIONS.getID());
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

		repository = PortsRepository.INSTANCE;
		dataRoot.setActionHandler(new PortsRepositoryActionHandler(repository, dataRoot));

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

	public CompositeNode getPortsDataRoot() {
		return dataRoot;
	}

	private void loadVersions() {

		while (!repository.isReady() && active) {
			try {
				LOGGER.debug("Port back-end not ready yet...");
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				LOGGER.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}

		if (active) {
			LOGGER.debug("Ports back-end ready, retrieving versions...");
			try {
				dataRoot.getChildren().clear();
				try {
					final List<DataVersion> versions = repository.getVersions();
					if (versions != null) {
						boolean first = true;
						for (final DataVersion v : versions) {
							final Node version = BrowserFactory.eINSTANCE.createLeaf();
							version.setParent(dataRoot);
							version.setDisplayName(v.getFullIdentifier());
							version.setVersionIdentifier(v.getIdentifier());
							version.setPublished(v.isPublished());
							if (first) {
								RunnerHelper.asyncExec(c -> dataRoot.setCurrent(version));
							}
							first = false;
							RunnerHelper.asyncExec(c -> dataRoot.getChildren().add(version));
						}
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
				dataRoot.setDisplayName("Ports");
			} catch (final Exception e) {
				LOGGER.error("Error retrieving ports versions");
			}

			// register consumer to update on new version
			repository.registerLocalVersionListener(versionString -> {
				RunnerHelper.asyncExec(c -> {
					// Check for existing versions
					for (final Node n : dataRoot.getChildren()) {
						if (Objects.equals(versionString, n.getDisplayName())) {
							return;
						}
					}

					final Node newVersion = BrowserFactory.eINSTANCE.createLeaf();
					newVersion.setDisplayName(versionString);
					newVersion.setParent(dataRoot);
					newVersion.setVersionIdentifier(versionString);
					dataRoot.getChildren().add(0, newVersion);
				});
			});
			repository.startListenForNewLocalVersions();

			repository.registerUpstreamVersionListener(versionString -> {
				RunnerHelper.asyncExec(c -> {
					try {
						repository.syncUpstreamVersion(versionString);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				});
			});
			repository.startListenForNewUpstreamVersions();
		}
	}
}
