package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataservice.pricing.model.Curve;
import com.mmxlabs.lngdataservice.pricing.model.Version;

public class PricingRepository extends AbstractDataRepository {

	private static final Logger LOG = LoggerFactory.getLogger(PricingRepository.class);

	public PricingRepository(@Nullable IPreferenceStore preferenceStore) {
		super(preferenceStore, null);
		// try to get ready
		isReady();
		this.upstreamUrl = getUpstreamUrl();
	}

	public PricingRepository(@Nullable IPreferenceStore preferenceStore, String localURL) {
		super(preferenceStore, localURL);
		this.upstreamUrl = getUpstreamUrl();
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

	public List<DataVersion> getVersions() {
		ensureReady();
		try {
			return PricingClient.getVersions(backendUrl).stream().map(v -> {
				final LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getNano() / 1000L), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, v.isPublished());
			}).collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("Error fetching pricing versions" + e.getMessage());
			throw new RuntimeException("Error fetching pricing versions", e);
		}
	}

	public void publishVersion(String version) throws IOException {
		PricingClient.publishVersion(backendUrl, upstreamUrl, version);
	}

	public void saveVersion(Version version) throws IOException {
		PricingClient.saveVersion(backendUrl, version);
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

	@Override
	public void syncUpstreamVersion(String version) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<DataVersion> updateAvailable() throws Exception {
		final List<PricingVersion> upstreamVersions = PricingClient.getVersions(upstreamUrl);
		final Set<String> localVersions = getVersions().stream().map(v -> v.getIdentifier()).collect(Collectors.toSet());
		upstreamVersions.removeIf(uv -> localVersions.contains(uv.getIdentifier()));
		return upstreamVersions.stream().map(v -> new DataVersion(v.getIdentifier(), v.getCreatedAt(), v.isPublished())).collect(Collectors.toList());
	}

	@Override
	protected CompletableFuture<String> waitForNewLocalVersion() {
		// final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
		// Version futureVersion;
		// try {
		// futureVersion = waitingPricingApi.getPricingUpdateUsingGET();
		// } catch (final ApiException e) {
		// throw new RuntimeException(e);
		// }
		// return futureVersion.getIdentifier();
		// });
		// return completableFuture;
		return CompletableFuture.completedFuture(null);
	}

	@Override
	protected CompletableFuture<String> waitForNewUpstreamVersion() {

		// final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
		// Version futureVersion;
		// try {
		// futureVersion = upstreamWaitingPricingApi.getPricingUpdateUsingGET();
		// } catch (final ApiException e) {
		// throw new RuntimeException(e);
		// }
		// return futureVersion.getIdentifier();
		// });
		// return completableFuture;
		return CompletableFuture.completedFuture(null);

	}

	@Override
	protected void newUpstreamURL(String upstreamURL) {
		// TODO Auto-generated method stub

	}
	
	
}
