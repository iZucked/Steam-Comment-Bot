/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.Curve;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.Version;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;

public class PricingRepository extends AbstractDataRepository {

	public static PricingRepository INSTANCE = new PricingRepository();

	private static final Logger LOG = LoggerFactory.getLogger(PricingRepository.class);

	private static final String SYNC_VERSION_ENDPOINT = "/pricing/sync/versions/";

	private PricingRepository() {
		isReady();
		doHandleUpstreamURLChange();

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
				final LocalDateTime createdAt = v.getCreatedAt();
				return new DataVersion(v.getIdentifier(), createdAt, v.isPublished(), v.isCurrent());
			}).collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("Error fetching pricing versions" + e.getMessage());
			throw new RuntimeException("Error fetching pricing versions", e);
		}
	}

	public List<DataVersion> getUpstreamVersions() {
		ensureReady();
		try {
			return PricingClient.getVersions(upstreamUrl, UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()).stream().map(v -> {
				final LocalDateTime createdAt = v.getCreatedAt();
				return new DataVersion(v.getIdentifier(), createdAt, v.isPublished(), v.isCurrent());
			}).collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("Error fetching pricing versions" + e.getMessage());
			throw new RuntimeException("Error fetching pricing versions", e);
		}
	}

	public void saveVersion(Version version) throws IOException {
		PricingClient.saveVersion(backendUrl, version);
	}

	public boolean deleteVersion(String version) throws IOException {
		return PricingClient.deleteVersion(backendUrl, version);
	}

	public boolean renameVersion(String oldVersion, String newVersion) throws IOException {
		return PricingClient.renameVersion(backendUrl, oldVersion, newVersion);
	}

	public boolean setCurrentVersion(String version) throws IOException {
		return PricingClient.setCurrentVersion(backendUrl, version);
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
	public List<DataVersion> updateAvailable() throws Exception {
		final List<DataVersion> upstreamVersions = getUpstreamVersions();
		final Set<String> localVersions = getVersions().stream().map(v -> v.getIdentifier()).collect(Collectors.toSet());
		upstreamVersions.removeIf(uv -> localVersions.contains(uv.getIdentifier()));
		return upstreamVersions.stream().map(v -> new DataVersion(v.getIdentifier(), v.getCreatedAt(), v.isPublished())).collect(Collectors.toList());
	}

	@Override
	protected void doHandleUpstreamURLChange() {

	}

	@Override
	protected boolean canWaitForNewLocalVersion() {
		return true;
	}

	@Override
	protected boolean canWaitForNewUpstreamVersion() {
		return true;
	}

	@Override
	protected String getSyncVersionEndpoint() {
		return SYNC_VERSION_ENDPOINT;
	}

	@Override
	protected String getVersionNotificationEndpoint() {
		return "/pricing/version_notification";
	}
}
