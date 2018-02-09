package com.mmxlabs.lngdataserver.integration.distances.internal;

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
	private Thread versionLoader;
	private boolean active;
	
	/**
	 * The constructor
	 */
	public Activator() {
		Node loading = BrowserFactory.eINSTANCE.createNode();
		loading.setDisplayName("loading...");
		distancesDataRoot.setDisplayName("Distances (loading...)");
		distancesDataRoot.getChildren().add(loading);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		distanceRepository = new DistanceRepository();
		active = true;
		
		BackEndUrlProvider.INSTANCE.addAvailableListener(() -> loadVersions());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		distanceRepository.stopListeningForNewVersions();
		active = false;
	}
	
	private void loadVersions() {
		while(!distanceRepository.isReady() && active) {
			try {
				LOGGER.debug("Distances back-end not ready yet...");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		if (active) {
			LOGGER.debug("Distances back-end ready, retrieving versions...");
			distancesDataRoot.getChildren().clear();
			for (DataVersion v : distanceRepository.getVersions()) {
				Node version = BrowserFactory.eINSTANCE.createNode();
				version.setParent(distancesDataRoot);
				version.setDisplayName(v.getIdentifier());
				version.setPublished(v.isPublished());
				RunnerHelper.asyncExec(c -> distancesDataRoot.getChildren().add(version));
			}
			distancesDataRoot.setDisplayName("Distances");
			
			// register consumer to update on new version
			distanceRepository.registerVersionListener(versionString -> {
				Node newVersion = BrowserFactory.eINSTANCE.createNode();
				newVersion.setDisplayName(versionString);
				newVersion.setParent(distancesDataRoot);
				RunnerHelper.asyncExec(c -> distancesDataRoot.getChildren().add(0, newVersion));
			});
			distanceRepository.listenForNewVersions();
		}
	}
	
	public CompositeNode getDistancesDataRoot() {
		return distancesDataRoot;
	}
	
	public DistanceRepository getDistanceRepository() {
		return distanceRepository;
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
