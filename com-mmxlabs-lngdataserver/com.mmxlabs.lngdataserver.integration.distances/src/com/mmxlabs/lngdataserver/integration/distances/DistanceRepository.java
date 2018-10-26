/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;

import okhttp3.Request;
import okhttp3.Response;

/**
 * This implementation is not thread-safe.
 * 
 * @author Robert Erdin
 */
public class DistanceRepository extends AbstractDataRepository {

	public static final DistanceRepository INSTANCE = new DistanceRepository();

	private static final Logger LOG = LoggerFactory.getLogger(DistanceRepository.class);

	private static final String SYNC_VERSION_ENDPOINT = "/distances/sync/versions/";
	
	private static final TypeReference<List<DistancesVersion>> TYPE_VERSIONS_LIST = new TypeReference<List<DistancesVersion>>() {
	};

	private DistanceRepository() {
		doHandleUpstreamURLChange();
	}

	@Override
	public boolean isReady() {
		return BackEndUrlProvider.INSTANCE.isAvailable();
	}

	private void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException("Distances back-end not ready yet");
		}
	}

	@Override
	public List<DataVersion> getLocalVersions() {
		ensureReady();
		try {
			return getVersions(BackEndUrlProvider.INSTANCE.getUrl(), null, null);
		} catch (final Exception e) {
			LOG.error("Error fetching local distances versions" + e.getMessage());
			throw new RuntimeException("Error fetching local distances versions", e);
		}
	}

	@Override
	public List<DataVersion> getUpstreamVersions() {
		ensureReady();
		try {
			return getVersions(UpstreamUrlProvider.INSTANCE.getBaseURL(), UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());
		} catch (final Exception e) {
			LOG.error("Error fetching upstream distances versions" + e.getMessage());
			throw new RuntimeException("Error fetching upstream distances versions", e);
		}
	}

	public List<DataVersion> getVersions(final String baseUrl, final String username, final String password) throws IOException {
		final Request request = createRequestBuilder(baseUrl + "/distances/versions", username, password).build();
		try (Response response = CLIENT.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				final String body = response.body().string();
				throw new RuntimeException("Error making request to " + baseUrl + ". Reason " + response.message() + " Response body is " + body);
			} else {
				final List<DistancesVersion> hubVersions = new ObjectMapper().readValue(response.body().byteStream(), TYPE_VERSIONS_LIST);
				return hubVersions.stream() //
						.filter(v -> v.getIdentifier() != null) //
						.sorted((v1, v2) -> v2.getCreatedAt().compareTo(v1.getCreatedAt())) //
						.map(v -> new DataVersion(v.getIdentifier(), v.getCreatedAt(), false, false)) //
						.collect(Collectors.toList());
			}
		}
	}

	@Override
	public List<DataVersion> updateAvailable() throws Exception {
		final List<DataVersion> upstreamVersions = getUpstreamVersions();
		final Set<String> localVersions = getLocalVersions().stream().map(DataVersion::getIdentifier).collect(Collectors.toSet());
		upstreamVersions.removeIf(uv -> localVersions.contains(uv.getIdentifier()));
		return upstreamVersions.stream().map(v -> new DataVersion(v.getIdentifier(), v.getCreatedAt(), v.isPublished())).collect(Collectors.toList());
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
		return "/distances/version_notification";
	}

	public DistancesVersion getLocalVersion(String versionTag) throws IOException {
		ensureReady();
		String url = BackEndUrlProvider.INSTANCE.getUrl() + SYNC_VERSION_ENDPOINT;
		Request request = new Request.Builder().url(url + versionTag).build();
		try (Response response = CLIENT.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				final String body = response.body().string();
				throw new RuntimeException("Error making request to " + url + ". Reason " + response.message() + " Response body is " + body);
			} else {
				final ObjectMapper mapper = new ObjectMapper();
				mapper.findAndRegisterModules();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

				return mapper.readValue(response.body().byteStream(), DistancesVersion.class);
			}
		}

	}
}