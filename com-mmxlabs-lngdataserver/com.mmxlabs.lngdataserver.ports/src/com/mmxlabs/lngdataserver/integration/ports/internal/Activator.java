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

	private final CompositeNode portsDataRoot = BrowserFactory.eINSTANCE.createCompositeNode();
	private PortsRepository portsRepository;
	private boolean active;

	/**
	 * The constructor
	 */
	public Activator() {
		Node loading = BrowserFactory.eINSTANCE.createLeaf();
		loading.setDisplayName("loading...");
		portsDataRoot.setDisplayName("Ports (loading...)");
		portsDataRoot.setType(LNGScenarioSharedModelTypes.LOCATIONS.getID());
		portsDataRoot.getChildren().add(loading);
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

		portsRepository = new PortsRepository(getPreferenceStore(), null);
		portsRepository.listenToPreferenceChanges();
		portsDataRoot.setActionHandler(new PortsRepositoryActionHandler(portsRepository, portsDataRoot));

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
		if (portsRepository != null) {
			portsRepository.stopListeningForNewLocalVersions();
			portsRepository.stopListenToPreferenceChanges();
			portsRepository = null;
		}
		portsDataRoot.setActionHandler(null);
		portsDataRoot.getChildren().clear();
		portsDataRoot.setCurrent(null);

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
		return portsDataRoot;
	}

	private void loadVersions() {

		while (!portsRepository.isReady() && active) {
			try {
				LOGGER.debug("Port back-end not ready yet...");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}

		if (active) {
			LOGGER.debug("Ports back-end ready, retrieving versions...");
			try {
				portsDataRoot.getChildren().clear();
				try {
					List<DataVersion> versions = portsRepository.getVersions();
					if (versions != null) {
						boolean first = true;
						for (DataVersion v : versions) {
							Node version = BrowserFactory.eINSTANCE.createLeaf();
							version.setParent(portsDataRoot);
							version.setDisplayName(v.getIdentifier());
							version.setPublished(v.isPublished());
							if (first) {
								RunnerHelper.asyncExec(c -> portsDataRoot.setCurrent(version));
							}
							first = false;
							RunnerHelper.asyncExec(c -> portsDataRoot.getChildren().add(version));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				portsDataRoot.setDisplayName("Ports");
			} catch (Exception e) {
				LOGGER.error("Error retrieving ports versions");
			}

			// register consumer to update on new version
			portsRepository.registerLocalVersionListener(versionString -> {
				RunnerHelper.asyncExec(c -> {
					// Check for existing versions
					for (final Node n : portsDataRoot.getChildren()) {
						if (Objects.equals(versionString, n.getDisplayName())) {
							return;
						}
					}

					final Node newVersion = BrowserFactory.eINSTANCE.createLeaf();
					newVersion.setDisplayName(versionString);
					newVersion.setParent(portsDataRoot);
					portsDataRoot.getChildren().add(0, newVersion);
				});
			});
			portsRepository.startListenForNewLocalVersions();

			portsRepository.registerUpstreamVersionListener(versionString -> {
				RunnerHelper.asyncExec(c -> {
					try {
						portsRepository.syncUpstreamVersion(versionString);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				});
			});
			portsRepository.startListenForNewUpstreamVersions();
		}
	}
}
