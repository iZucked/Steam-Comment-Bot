package com.mmxlabs.lngdataserver.integration.ports;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.port.api.PortApi;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;

public class PortsRepository extends AbstractDataRepository {
	private static final Logger LOG = LoggerFactory.getLogger(PortsRepository.class);

	private PortApi portsApi = new PortApi();
	private String backendUrl;

	public PortsRepository(IPreferenceStore preferenceStore, String localUrl) {
		super(preferenceStore, localUrl);
	}

	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		} else if (BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			portsApi.getApiClient().setBasePath(backendUrl);
			return true;
		} else {
			return false;
		}
	}

	public List<DataVersion> getVersions() {
		ensureReady();
		try {
			return portsApi.fetchVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt =  LocalDateTime.now();//LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getNano() / 1000L), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, /* v.isPublished() */ false);
			}).collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("Error fetching ports versions" + e.getMessage());
			throw new RuntimeException("Error fetching ports versions", e);
		}
	}

	public IPortsProvider getPortsProvider(String versionTag) {
		try {
			return new DefaultPortsProvider(versionTag, portsApi.fetchAllUsingGET());
		} catch (Exception e) {
			// Pass
			System.out.println(e.getMessage());
		}
		return null;
	}

	private void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException("Ports back-end not ready yet");
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
