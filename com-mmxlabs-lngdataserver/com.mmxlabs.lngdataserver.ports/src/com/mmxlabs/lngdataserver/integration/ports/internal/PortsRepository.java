package com.mmxlabs.lngdataserver.integration.ports.internal;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.mmxlabs.lngdataserver.port.ApiException;
import com.mmxlabs.lngdataserver.port.api.PortApi;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;

public class PortsRepository {

	private PortApi portsApi = new PortApi();
	private String backendUrl;
	
	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		}else if(BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			portsApi.getApiClient().setBasePath(backendUrl);
			return true;
		}else {
			return false;
		}
	}
	
	public List<PortsVersion> getVersions() throws IOException, ApiException {
		ensureReady();
		try {
			return portsApi.fetchVersionsUsingGET().stream().map(v -> {
				PortsVersion vv = new PortsVersion(v.getIdentifier(), LocalDateTime.now(), false);
				return vv;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
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
}

