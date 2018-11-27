/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.time.LocalDateTime;
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

public class PricingRepository extends AbstractDataRepository<PricingVersion> {

	public static final PricingRepository INSTANCE = new PricingRepository();

	private static final Logger LOG = LoggerFactory.getLogger(PricingRepository.class);

	private static final String SYNC_VERSION_ENDPOINT = "/pricing/sync/versions/";

	private PricingRepository() {
		super("Pricing", PricingVersion.class);
		isReady();
		doHandleUpstreamURLChange();

	}

	public List<DataVersion> getLocalVersions() {
		ensureReady();
		try {
			return PricingClient.getVersions(BackEndUrlProvider.INSTANCE.getUrl()).stream().map(v -> {
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
			return PricingClient.getVersions(getUpstreamUrl(), UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()).stream().map(v -> {
				final LocalDateTime createdAt = v.getCreatedAt();
				return new DataVersion(v.getIdentifier(), createdAt, v.isPublished(), v.isCurrent());
			}).collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("Error fetching pricing versions" + e.getMessage());
			throw new RuntimeException("Error fetching pricing versions", e);
		}
	}

	public void saveVersion(Version version) throws IOException {
		PricingClient.saveVersion(BackEndUrlProvider.INSTANCE.getUrl(), version);
	}

	public boolean deleteVersion(String version) throws IOException {
		return PricingClient.deleteVersion(BackEndUrlProvider.INSTANCE.getUrl(), version);
	}

	public boolean renameVersion(String oldVersion, String newVersion) throws IOException {
		return PricingClient.renameVersion(BackEndUrlProvider.INSTANCE.getUrl(), oldVersion, newVersion);
	}

	public boolean setCurrentVersion(String version) throws IOException {
		return PricingClient.setCurrentVersion(BackEndUrlProvider.INSTANCE.getUrl(), version);
	}

	public IPricingProvider getLatestPrices() throws IOException {
		ensureReady();
		return getPricingProvider(getLocalVersions().get(0).getIdentifier());
	}

	public IPricingProvider getPricingProvider(String version) throws IOException {
		ensureReady();

		String backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
		List<Curve> commoditiesCurves = PricingClient.getCommodityCurves(backendUrl, version);

		List<Curve> baseFuelCurves = PricingClient.getFuelCurves(backendUrl, version);

		List<Curve> charterCurves = PricingClient.getCharterCurves(backendUrl, version);

		List<Curve> currencyCurves = PricingClient.getCurrencyCurves(backendUrl, version);

		return new ForwardingPricingProvider(backendUrl, version, commoditiesCurves, charterCurves, baseFuelCurves, currencyCurves);
	}

	@Override
	public List<DataVersion> updateAvailable() throws Exception {
		final List<DataVersion> upstreamVersions = getUpstreamVersions();
		final Set<String> localVersions = getLocalVersions().stream().map(v -> v.getIdentifier()).collect(Collectors.toSet());
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

	@Override
	protected String getVersionsURL() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected SimpleVersion wrap(PricingVersion version) {
		throw new UnsupportedOperationException();
	}
}
