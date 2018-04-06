/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.lngdataservice.client.ports.ApiClient;
import com.mmxlabs.lngdataservice.client.ports.api.PortApi;
import com.mmxlabs.lngdataservice.client.ports.model.Version;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PortsRepository extends AbstractDataRepository {
	private static final Logger LOG = LoggerFactory.getLogger(PortsRepository.class);

	private static final OkHttpClient CLIENT = new OkHttpClient();
	private static final String SYNC_VERSION_ENDPOINT = "/ports/sync/versions/";
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private final PortApi localApi;
	private final PortApi upstreamApi;

	public PortsRepository(final IPreferenceStore preferenceStore, final String localUrl) {
		localApi = new PortApi(new ApiClient());
		upstreamApi = new PortApi(new ApiClient());

		BackEndUrlProvider.INSTANCE.addAvailableListener(() -> {
			final String localURL = BackEndUrlProvider.INSTANCE.getUrl();
			if (localURL != null) {
				localApi.getApiClient().setBasePath(localURL);
			}
		});

		doHandleUpstreamURLChange();
	}

	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		} else if (BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			localApi.getApiClient().setBasePath(BackEndUrlProvider.INSTANCE.getUrl());
			return true;
		} else {
			return false;
		}
	}

	private void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException("Ports back-end not ready yet");
		}
	}

	public List<DataVersion> getVersions() {
		ensureReady();
		try {
			return localApi.fetchVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt = LocalDateTime.now();// LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getNano() / 1000L), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, /* v.isPublished() */ false);
			}).collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("Error fetching ports versions" + e.getMessage());
			throw new RuntimeException("Error fetching ports versions", e);
		}
	}

	public List<DataVersion> getUpstreamVersions() {
		ensureReady();
		try {
			return upstreamApi.fetchVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt = LocalDateTime.now();// LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getNano() / 1000L), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, /* v.isPublished() */ false);
			}).collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("Error fetching ports versions" + e.getMessage());
			throw new RuntimeException("Error fetching ports versions", e);
		}
	}

	public IPortsProvider getPortsProvider(final String versionTag) {
		ensureReady();
		try {
			return new DefaultPortsProvider(versionTag, localApi.fetchAllUsingGET());
		} catch (final Exception e) {
			// Pass
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public List<DataVersion> updateAvailable() throws Exception {
		final Set<String> localVersions = getVersions().stream().map(v -> v.getIdentifier()).collect(Collectors.toSet());
		final List<DataVersion> upstreamVersions = upstreamApi.fetchVersionsUsingGET().stream() //
				.filter(v -> !localVersions.contains(v.getIdentifier())) //
				.map(v -> new DataVersion(v.getIdentifier(), LocalDateTime.now() /* v.getCreatedAt() */, true /* v.isPublished() */))//
				.collect(Collectors.toList());
		return upstreamVersions;
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
	protected void doHandleUpstreamURLChange() {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		upstreamApi.getApiClient().setBasePath(upstreamURL);
		upstreamApi.getApiClient().setUsername(UpstreamUrlProvider.INSTANCE.getUsername());
		upstreamApi.getApiClient().setPassword(UpstreamUrlProvider.INSTANCE.getPassword());
		upstreamApi.getApiClient().getHttpClient().setAuthenticator(getAuthenticator());
	}

	public void saveVersion(final Version version) throws Exception {
		final String json = new ObjectMapper().writeValueAsString(version);

		final RequestBody body = RequestBody.create(JSON, json);
		final Request request = new Request.Builder().url(backendUrl + SYNC_VERSION_ENDPOINT).post(body).build();
		final Response response = CLIENT.newCall(request).execute();

		if (!response.isSuccessful()) {
			LOG.error("Error publishing version: " + response.message());
		}
	}

	@Override
	protected String getSyncVersionEndpoint() {
		return SYNC_VERSION_ENDPOINT;
	}

	@Override
	protected String getVersionNotificationEndpoint() {
		return "/ports/version_notification";
	}
}
