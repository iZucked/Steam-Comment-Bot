package com.mmxlabs.lngdataserver.integration.vessels.internal;

import java.io.IOException;
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
		Node loading = BrowserFactory.eINSTANCE.createNode();
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

		vesselsRepository = new VesselsRepository(getPreferenceStore(), null);
		vesselsRepository.listenToPreferenceChanges();
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
			vesselsRepository.stopListenToPreferenceChanges();
			vesselsRepository = null;
		}
		vesselsDataRoot.setActionHandler(null);
		vesselsDataRoot.getChildren().clear();
		vesselsDataRoot.setLatest(null);

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
				vesselsDataRoot.getChildren().clear();
				try {
					List<DataVersion> versions = vesselsRepository.getVersions();
					if (versions != null) {
						boolean first = true;
						for (DataVersion v : versions) {
							Node version = BrowserFactory.eINSTANCE.createNode();
							version.setParent(vesselsDataRoot);
							version.setDisplayName(v.getIdentifier());
							version.setPublished(v.isPublished());
							if (first) {
								RunnerHelper.asyncExec(c -> vesselsDataRoot.setLatest(version));
							}
							first = false;
							RunnerHelper.asyncExec(c -> vesselsDataRoot.getChildren().add(version));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				vesselsDataRoot.setDisplayName("Vessels");
			} catch (Exception e) {
				LOGGER.error("Error retrieving vessels versions");
			}

			// register consumer to update on new version
			vesselsRepository.registerLocalVersionListener(versionString -> {
				RunnerHelper.asyncExec(c -> {
					// Check for existing versions
					for (final Node n : vesselsDataRoot.getChildren()) {
						if (Objects.equals(versionString, n.getDisplayName())) {
							return;
						}
					}

					final Node newVersion = BrowserFactory.eINSTANCE.createNode();
					newVersion.setDisplayName(versionString);
					newVersion.setParent(vesselsDataRoot);
					vesselsDataRoot.getChildren().add(0, newVersion);
				});
			});
			vesselsRepository.startListenForNewLocalVersions();

			vesselsRepository.registerUpstreamVersionListener(versionString -> {
				RunnerHelper.asyncExec(c -> {
					try {
						vesselsRepository.syncUpstreamVersion(versionString);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				});
			});
			vesselsRepository.startListenForNewUpstreamVersions();
		}
	}
}
