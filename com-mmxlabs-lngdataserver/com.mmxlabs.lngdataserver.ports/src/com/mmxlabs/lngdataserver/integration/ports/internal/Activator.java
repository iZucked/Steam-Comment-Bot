package com.mmxlabs.lngdataserver.integration.ports.internal;

import java.io.IOException;
import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.port.ApiException;
import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
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
	private final PortsRepository portsRepository = new PortsRepository();
	private boolean active;

	/**
	 * The constructor
	 */
	public Activator() {
		Node loading = BrowserFactory.eINSTANCE.createNode();
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

	//
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

		}

		if (active) {
			LOGGER.debug("Ports back-end ready, retrieving versions...");
			try {
				portsDataRoot.getChildren().clear();
				try {
					List<PortsVersion> versions = portsRepository.getVersions();
					if (versions != null) {
						for (PortsVersion v : versions) {
							Node version = BrowserFactory.eINSTANCE.createNode();
							version.setParent(portsDataRoot);
							version.setDisplayName(v.getIdentifier());
							version.setPublished(v.isPublished());
							RunnerHelper.asyncExec(c -> portsDataRoot.getChildren().add(version));
						}
					}
				} catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				portsDataRoot.setDisplayName("Ports");
			} catch (IOException e) {
				LOGGER.error("Error retrieving ports versions");
			}
		}
	}
}
