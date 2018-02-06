package com.mmxlabs.lngdataserver.integration.vessels.internal;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.mmxlabs.lngdataserver.vessel.ApiException;
import com.mmxlabs.lngdataserver.vessel.api.VesselsApi;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;

public class VesselsRepository {

	private VesselsApi vesselsApi = new VesselsApi();
	private String backendUrl;
	
	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		}else if(BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			vesselsApi.getApiClient().setBasePath(backendUrl);
			return true;
		}else {
			return false;
		}
	}
	
	public List<VesselsVersion> getVersions() throws IOException, ApiException {
		ensureReady();
		try {
			return vesselsApi.getVersionsUsingGET().stream().map(v -> {
				VesselsVersion vv = new VesselsVersion(v.getIdentifier(), LocalDateTime.now(), false);
				return vv;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			return null;
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
}

