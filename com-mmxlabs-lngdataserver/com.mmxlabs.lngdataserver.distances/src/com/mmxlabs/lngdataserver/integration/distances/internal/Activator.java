package com.mmxlabs.lngdataserver.integration.distances.internal;

import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.integration.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.rcp.common.RunnerHelper;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.integration.distances"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private final CompositeNode distancesDataRoot = BrowserFactory.eINSTANCE.createCompositeNode();
	private DistanceRepository distanceRepository;
	private boolean active;

	/**
	 * The constructor
	 */
	public Activator() {
		final Node loading = BrowserFactory.eINSTANCE.createLeaf();
		loading.setDisplayName("loading...");
		distancesDataRoot.setDisplayName("Distances (loading...)");
		distancesDataRoot.setType(LNGScenarioSharedModelTypes.DISTANCES.getID());
		distancesDataRoot.getChildren().add(loading);
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

		distanceRepository = new DistanceRepository(getPreferenceStore());
		distanceRepository.listenToPreferenceChanges();
		distancesDataRoot.setActionHandler(new DistanceRepositoryActionHandler(distanceRepository, distancesDataRoot));

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
		if (distanceRepository != null) {
			distanceRepository.stopListeningForNewLocalVersions();
			distanceRepository.stopListenToPreferenceChanges();
			distanceRepository = null;
		}
		distancesDataRoot.setActionHandler(null);
		distancesDataRoot.getChildren().clear();
		distancesDataRoot.setCurrent(null);

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
		return distancesDataRoot;
	}

	public DistanceRepository getDistanceRepository() {
		return distanceRepository;
	}

	private void loadVersions() {
		while (!distanceRepository.isReady() && active) {
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
			distancesDataRoot.getChildren().clear();
			boolean first = true;
			List<DataVersion> versions = distanceRepository.getVersions();
			if (versions != null) {
				for (final DataVersion v : versions) {
					final Node version = BrowserFactory.eINSTANCE.createLeaf();
					version.setParent(distancesDataRoot);
					version.setDisplayName(v.getIdentifier());
					version.setPublished(v.isPublished());
					if (first) {
						RunnerHelper.asyncExec(c -> distancesDataRoot.setCurrent(version));
					}
					first = false;
					RunnerHelper.asyncExec(c -> distancesDataRoot.getChildren().add(version));
				}
			}
			distancesDataRoot.setDisplayName("Distances");

			// register consumer to update on new version
			distanceRepository.registerLocalVersionListener(versionString -> {
				RunnerHelper.asyncExec(c -> {
					// Check for existing versions
					for (final Node n : distancesDataRoot.getChildren()) {
						if (versionString.contentEquals(n.getDisplayName())) {
							return;
						}
					}

					final Node newVersion = BrowserFactory.eINSTANCE.createLeaf();
					newVersion.setDisplayName(versionString);
					newVersion.setParent(distancesDataRoot);
					distancesDataRoot.getChildren().add(0, newVersion);
				});
			});
			distanceRepository.startListenForNewLocalVersions();

			distanceRepository.registerUpstreamVersionListener(versionString -> {
				RunnerHelper.asyncExec(c -> {
					try {
						distanceRepository.syncUpstreamVersion(versionString);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				});
			});
			distanceRepository.startListenForNewUpstreamVersions();
		}
	}

}
