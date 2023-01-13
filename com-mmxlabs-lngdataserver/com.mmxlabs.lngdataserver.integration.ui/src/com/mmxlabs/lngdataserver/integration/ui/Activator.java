/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.browser.util.DataBrowserNodeHandler;
import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelsRepository;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupsRepository;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupsRepository;
import com.mmxlabs.lngdataserver.integration.ui.handlers.GenericRepositoryActionHandler;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;

public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.integration.ui"; //$NON-NLS-1$

	static final String URL_PREFIX = "/ui";

	// The shared instance
	private static Activator plugin;

	private DataBrowserNodeHandler portGroupsNodeHandler;

	private DataBrowserNodeHandler vesselGroupsNodeHandler;

	private DataBrowserNodeHandler bunkerFuelsNodeHandler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		if (LicenseFeatures.isPermitted("features:hub-sync-distances")) {
			portGroupsNodeHandler = new DataBrowserNodeHandler("Port Groups", LNGScenarioSharedModelTypes.PORT_GROUPS.getID(), PortGroupsRepository.INSTANCE,
					root -> new GenericRepositoryActionHandler(PortGroupsRepository.INSTANCE, root));
			portGroupsNodeHandler.start();
		}
		if (LicenseFeatures.isPermitted("features:hub-sync-vessels")) {
			bunkerFuelsNodeHandler = new DataBrowserNodeHandler("Bunker Fuels", LNGScenarioSharedModelTypes.BUNKER_FUELS.getID(), BunkerFuelsRepository.INSTANCE,
					root -> new GenericRepositoryActionHandler(BunkerFuelsRepository.INSTANCE, root));
			bunkerFuelsNodeHandler.start();
			vesselGroupsNodeHandler = new DataBrowserNodeHandler("Vessel Groups", LNGScenarioSharedModelTypes.VESSEL_GROUPS.getID(), VesselGroupsRepository.INSTANCE,
					root -> new GenericRepositoryActionHandler(VesselGroupsRepository.INSTANCE, root));
			vesselGroupsNodeHandler.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		if (portGroupsNodeHandler != null) {
			portGroupsNodeHandler.stop();
			portGroupsNodeHandler = null;
		}
		if (bunkerFuelsNodeHandler != null) {
			bunkerFuelsNodeHandler.stop();
			bunkerFuelsNodeHandler = null;
		}
		if (vesselGroupsNodeHandler != null) {
			vesselGroupsNodeHandler.stop();
			vesselGroupsNodeHandler = null;
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

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

}
