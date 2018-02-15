package com.mmxlabs.lngdataserver.integration.pricing.internal;

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
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
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
		final Node loading = BrowserFactory.eINSTANCE.createLeaf();
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

		pricingRepository = new PricingRepository(getPreferenceStore());
		pricingRepository.listenToPreferenceChanges();
		pricingDataRoot.setActionHandler(new PricingRepositoryActionHandler(pricingRepository, pricingDataRoot));

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
		if (pricingRepository != null) {
			pricingRepository.stopListenToPreferenceChanges();
			pricingRepository.stopListeningForNewLocalVersions();
			pricingRepository = null;
		}
		pricingDataRoot.setActionHandler(null);
		pricingDataRoot.getChildren().clear();
		pricingDataRoot.setLatest(null);

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
				boolean first = true;
				List<DataVersion> versions = pricingRepository.getVersions();
				if (versions != null) {
					for (final DataVersion v : versions) {
						final Node version = BrowserFactory.eINSTANCE.createLeaf();
						version.setParent(pricingDataRoot);
						version.setDisplayName(v.getIdentifier());
						version.setPublished(v.isPublished());
						if (first) {
							RunnerHelper.asyncExec(c -> pricingDataRoot.setLatest(version));
						}
						first = false;
						RunnerHelper.asyncExec(c -> pricingDataRoot.getChildren().add(version));
					}
				}
				pricingDataRoot.setDisplayName("Pricing");
			} catch (final Exception e) {
				LOGGER.error("Error retrieving pricing versions");
			}

			// register consumer to update on new version
			pricingRepository.registerLocalVersionListener(versionString -> {

				RunnerHelper.asyncExec(c -> {
					// Check for existing versions
					for (final Node n : pricingDataRoot.getChildren()) {
						if (Objects.equals(versionString, n.getDisplayName())) {
							return;
						}
					}
					final Node newVersion = BrowserFactory.eINSTANCE.createLeaf();
					newVersion.setDisplayName(versionString);
					pricingDataRoot.getChildren().add(newVersion);
				});
			});
			pricingRepository.startListenForNewLocalVersions();

			pricingRepository.registerUpstreamVersionListener(versionString -> {
				RunnerHelper.asyncExec(c -> {
					try {
						pricingRepository.syncUpstreamVersion(versionString);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				});
			});
			pricingRepository.startListenForNewUpstreamVersions();
		}
	}
}
