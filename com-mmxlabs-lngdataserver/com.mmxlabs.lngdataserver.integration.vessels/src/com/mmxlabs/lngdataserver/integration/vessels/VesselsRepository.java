/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.lngdataservice.client.vessel.ApiClient;
import com.mmxlabs.lngdataservice.client.vessel.api.VesselsApi;
import com.mmxlabs.lngdataservice.client.vessel.model.Version;

import okhttp3.OkHttpClient;

public class VesselsRepository extends AbstractDataRepository<VesselsVersion> {

	private static final Logger LOG = LoggerFactory.getLogger(VesselsRepository.class);

	private static final String SYNC_VERSION_ENDPOINT = "/vessels/sync/versions/";

	private VesselsApi localApi = new VesselsApi();
	private VesselsApi localWaitingApi = new VesselsApi();
	private VesselsApi upstreamApi = new VesselsApi();
	private VesselsApi upstreamWaitingApi = new VesselsApi();

	private static final OkHttpClient CLIENT = new OkHttpClient();

	public static final VesselsRepository INSTANCE = new VesselsRepository();

	public VesselsRepository() {
		super("Vessels", VesselsVersion.class);
		localApi = new VesselsApi(new ApiClient());
		localWaitingApi = new VesselsApi(new ApiClient());

		upstreamApi = new VesselsApi(new ApiClient());
		upstreamWaitingApi = new VesselsApi(new ApiClient());

		BackEndUrlProvider.INSTANCE.addAvailableListener(() -> {
			String localURL = BackEndUrlProvider.INSTANCE.getUrl();
			if (localURL != null) {
				localApi.getApiClient().setBasePath(localURL);
				localWaitingApi.getApiClient().setBasePath(localURL);
				localWaitingApi.getApiClient().getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);
			}
		});

		doHandleUpstreamURLChange();
	}

	@Override
	public boolean isReady() {
		if (BackEndUrlProvider.INSTANCE.isAvailable()) {
			String backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			localApi.getApiClient().setBasePath(backendUrl);
			localWaitingApi.getApiClient().setBasePath(backendUrl);
			localWaitingApi.getApiClient().getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);
			return true;
		} else {
			return false;
		}
	}

	public List<DataVersion> getLocalVersions() {
		ensureReady();
		try {
			return localApi.getVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt = fromDateTimeAtUTC(v.getCreatedAt());
				return new DataVersion(v.getIdentifier(), createdAt, false);
			}).collect(Collectors.toList());
		} catch (Exception e) {
			LOG.error("Error fetching vessels versions" + e.getMessage());
			throw new RuntimeException("Error fetching vessels versions", e);
		}
	}

	public List<DataVersion> getUpstreamVersions() {
		ensureReady();
		try {
			return upstreamApi.getVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt = fromDateTimeAtUTC(v.getCreatedAt());
				return new DataVersion(v.getIdentifier(), createdAt, v.isPublished());
			}).collect(Collectors.toList());
		} catch (Exception e) {
			LOG.error("Error fetching vessels versions" + e.getMessage());
			throw new RuntimeException("Error fetching vessels versions", e);
		}
	}

	public IVesselsProvider getVesselsProvider(String versionTag) {
		try {
			return new DefaultVesselsProvider(versionTag, localApi.getVesselsUsingGET());
		} catch (Exception e) {
			// Pass
		}
		return null;
	}

	@Override
	public List<DataVersion> updateAvailable() throws Exception {
		return Collections.emptyList();
	}

	@Override
	protected boolean canWaitForNewLocalVersion() {
		return false;
	}

	@Override
	protected boolean canWaitForNewUpstreamVersion() {
		return false;
	}

	@Override
	protected void doHandleUpstreamURLChange() {

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		upstreamApi.getApiClient().setBasePath(upstreamURL);
		upstreamApi.getApiClient().setUsername(UpstreamUrlProvider.INSTANCE.getUsername());
		upstreamApi.getApiClient().setPassword(UpstreamUrlProvider.INSTANCE.getPassword());
		upstreamApi.getApiClient().getHttpClient().setAuthenticator(getAuthenticator());

		upstreamWaitingApi.getApiClient().setBasePath(upstreamURL);
		upstreamWaitingApi.getApiClient().setUsername(UpstreamUrlProvider.INSTANCE.getUsername());
		upstreamWaitingApi.getApiClient().setPassword(UpstreamUrlProvider.INSTANCE.getPassword());
		upstreamWaitingApi.getApiClient().getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);

		upstreamWaitingApi.getApiClient().getHttpClient().setAuthenticator(getAuthenticator());
	}

	@Override
	protected String getSyncVersionEndpoint() {
		return SYNC_VERSION_ENDPOINT;
	}

	@Override
	protected String getVersionNotificationEndpoint() {
		return "/vessels/version_notification";
	}

	@Override
	protected SimpleVersion wrap(VesselsVersion version) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getVersionsURL() {
		throw new UnsupportedOperationException();
	}
}
