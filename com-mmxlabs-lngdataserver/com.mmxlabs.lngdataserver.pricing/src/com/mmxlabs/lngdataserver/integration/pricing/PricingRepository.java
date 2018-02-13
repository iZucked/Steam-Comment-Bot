package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.pricing.internal.Activator;
import com.mmxlabs.lngdataserver.integration.pricing.preferences.PreferenceConstants;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataservice.pricing.model.Curve;
import com.mmxlabs.lngdataservice.pricing.model.Version;

public class PricingRepository {

	private static final Logger LOG = LoggerFactory.getLogger(PricingRepository.class);

	private String backendUrl;
	private String upstreamUrl;
	private boolean listenForNewVersions;
	private final List<Consumer<String>> newVersionCallbacks = new LinkedList<Consumer<String>>();
	private final IPropertyChangeListener listener = new IPropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			switch (event.getProperty()) {
			case PreferenceConstants.P_URL_KEY:
			case PreferenceConstants.P_USERNAME_KEY:
			case PreferenceConstants.P_PASSWORD_KEY:
				upstreamUrl = getUpstreamUrl();
				break;
			default:
			}
		}
	};

	public PricingRepository() {
		// try to get ready
		isReady();
		this.upstreamUrl = getUpstreamUrl();
	}

	public PricingRepository(String backendUrl) {
		this.backendUrl = backendUrl;
		this.upstreamUrl = getUpstreamUrl();
	}

	private String getUpstreamUrl() {
		final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		final String url = prefs.getString(PreferenceConstants.P_URL_KEY);
		if ("".equals(url)) {
			throw new RuntimeException("No URL found for upstream pricing repository");
		}
		return url;
	}

	public void listenToPreferenceChanges() {
		final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		prefs.addPropertyChangeListener(listener);
	}

	public void stopListenToPreferenceChanges() {
		final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		prefs.removePropertyChangeListener(listener);
	}

	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		} else if (BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			return true;
		} else {
			return false;
		}
	}

	private void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException("Pricing back-end not ready yet");
		}
	}

	public List<PricingVersion> getVersions() throws IOException {
		ensureReady();
		return PricingClient.getVersions(backendUrl);
	}

	public void publishVersion(String version) throws IOException {
		PricingClient.publishVersion(backendUrl, upstreamUrl, version);
	}

	public void saveVersion(Version version) throws IOException {
		PricingClient.saveVersion(backendUrl, version);
	}

	public void registerVersionListener(Consumer<String> versionConsumer) {
		newVersionCallbacks.add(versionConsumer);
	}

	public void listenForNewVersions() {
		listenForNewVersions = true;

		new Thread(() -> {
			while (listenForNewVersions) {
				CompletableFuture<String> newVersion = PricingClient.notifyOnNewVersion(backendUrl);
				try {
					String version = newVersion.get();
					newVersionCallbacks.forEach(c -> c.accept(version));
				} catch (InterruptedException e) {
					LOG.error(e.getMessage());
				} catch (ExecutionException e) {
					LOG.error(e.getMessage());
				}

				// make sure not everything is blocked in case of consecutive failure
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}

	public void stopListenForNewVersion() {
		listenForNewVersions = false;
	}

	public IPricingProvider getLatestPrices() throws IOException {
		ensureReady();
		return getPricingProvider(getVersions().get(0).getIdentifier());
	}

	public IPricingProvider getPricingProvider(String version) throws IOException {
		ensureReady();

		List<Curve> commoditiesCurves = PricingClient.getCommodityCurves(backendUrl, version);

		List<Curve> baseFuelCurves = PricingClient.getFuelCurves(backendUrl, version);

		List<Curve> charterCurves = PricingClient.getCharterCurves(backendUrl, version);

		List<Curve> currencyCurves = PricingClient.getCurrencyCurves(backendUrl, version);

		return new ForwardingPricingProvider(backendUrl, version, commoditiesCurves, charterCurves, baseFuelCurves, currencyCurves);
	}
}
