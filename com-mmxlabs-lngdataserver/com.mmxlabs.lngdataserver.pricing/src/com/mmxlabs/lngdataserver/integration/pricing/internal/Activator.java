package com.mmxlabs.lngdataserver.integration.pricing.internal;

import java.io.IOException;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.integration.pricing.PricingVersion;
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
	private PricingRepository pricingRepository = null;

	private boolean active;

	/**
	 * The constructor
	 */
	public Activator() {
		final Node loading = BrowserFactory.eINSTANCE.createNode();
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
		active = true;

		pricingRepository = new PricingRepository();
		pricingRepository.listenToPreferenceChanges();
		BackEndUrlProvider.INSTANCE.addAvailableListener(() -> loadVersions());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		if (pricingRepository != null) {
			pricingRepository.stopListenToPreferenceChanges();
			pricingRepository.stopListenForNewVersion();
		}
		pricingRepository = null;
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
		return pricingRepository;
	}

	private void loadVersions() {
		while (!pricingRepository.isReady() && active) {
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
				pricingDataRoot.getChildren().clear();
				for (final PricingVersion v : pricingRepository.getVersions()) {
					final Node version = BrowserFactory.eINSTANCE.createNode();
					version.setParent(pricingDataRoot);
					version.setDisplayName(v.getIdentifier());
					version.setPublished(v.isPublished());
					RunnerHelper.asyncExec(c -> pricingDataRoot.getChildren().add(version));
				}
				pricingDataRoot.setDisplayName("Pricing");
			} catch (final IOException e) {
				LOGGER.error("Error retrieving pricing versions");
			}

			// register consumer to update on new version
			pricingRepository.registerVersionListener(versionString -> {

				RunnerHelper.asyncExec(c -> {
					// Check for existing versions
					for (final Node n : pricingDataRoot.getChildren()) {
						if (versionString.contentEquals(n.getDisplayName())) {
							return;
						}
					}
					final Node newVersion = BrowserFactory.eINSTANCE.createNode();
					newVersion.setDisplayName(versionString);
					pricingDataRoot.getChildren().add(newVersion);
				});
			});
			pricingRepository.listenForNewVersions();
		}
	}
}
