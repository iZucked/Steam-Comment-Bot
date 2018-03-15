package com.mmxlabs.lngdataserver.integration.distances;

import java.io.IOException;
import java.util.List;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.models.lng.port.Port;

public class LocationRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationRepository.class);

	public List<Port> getPorts(String version) throws AuthenticationException, ClientProtocolException, IOException, ParseException {
		return UpstreamPortFetcher.getPorts(UpstreamUrlProvider.INSTANCE.getBaseURL(), version, UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());
	}
}
