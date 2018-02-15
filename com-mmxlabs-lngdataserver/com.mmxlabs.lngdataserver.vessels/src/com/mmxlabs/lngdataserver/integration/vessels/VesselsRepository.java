package com.mmxlabs.lngdataserver.integration.vessels;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.vessel.api.VesselsApi;

public class VesselsRepository extends AbstractDataRepository {
	private Logger LOG = LoggerFactory.getLogger(VesselsRepository.class);
	private VesselsApi vesselsApi = new VesselsApi();
	private String backendUrl;

	public VesselsRepository(IPreferenceStore preferenceStore, String localUrl) {
		super(preferenceStore, localUrl);
	}

	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		} else if (BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			vesselsApi.getApiClient().setBasePath(backendUrl);
			return true;
		} else {
			return false;
		}
	}

	public List<DataVersion> getVersions() {
		ensureReady();
		try {
			return vesselsApi.getVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt = LocalDateTime.now();// LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getNano() / 1000L), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, /* v.isPublished() */ false);
			}).collect(Collectors.toList());
		} catch (Exception e) {
			LOG.error("Error fetching vessels versions" + e.getMessage());
			throw new RuntimeException("Error fetching vessels versions", e);
		}
	}

	public IVesselsProvider getVesselsProvider(String versionTag) {
		try {
			return new DefaultVesselsProvider(versionTag, vesselsApi.getVesselsUsingGET());
		} catch (Exception e) {
			// Pass
		}
		return null;
	}

	private void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException("Pricing back-end not ready yet");
		}
	}

	@Override
	public void syncUpstreamVersion(String version) throws Exception {

	}

	@Override
	public void publishVersion(String version) throws Exception {

	}

	@Override
	public List<DataVersion> updateAvailable() throws Exception {
		return Collections.emptyList();
	}

	@Override
	protected CompletableFuture<String> waitForNewLocalVersion() {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	protected CompletableFuture<String> waitForNewUpstreamVersion() {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	protected void newUpstreamURL(String upstreamURL) {

	}
}
