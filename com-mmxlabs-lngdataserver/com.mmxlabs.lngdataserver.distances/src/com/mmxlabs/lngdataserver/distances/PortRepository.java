package com.mmxlabs.lngdataserver.distances;

import java.io.IOException;
import java.util.List;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Triple;
import com.mmxlabs.lngdataserver.distances.internal.Activator;
import com.mmxlabs.lngdataserver.distances.preferences.PreferenceConstants;
import com.mmxlabs.models.lng.port.Port;

public class PortRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortRepository.class);

	private Triple<String, String, String> getServiceAuth() {
		final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		final String url = prefs.getString(PreferenceConstants.P_URL_KEY);
		if ("".equals(url)) {
			throw new RuntimeException("No URL found for upstream distance repository");
		}
		final String username = prefs.getString(PreferenceConstants.P_USERNAME_KEY);
		final String password = prefs.getString(PreferenceConstants.P_PASSWORD_KEY);

		return new Triple<>(url, username, password);
	}

	public List<Port> getPorts(String version) throws AuthenticationException, ClientProtocolException, IOException, ParseException {
		Triple<String, String, String> serviceAuth = getServiceAuth();

		return UpstreamPortFetcher.getPorts(serviceAuth.getFirst(), version, serviceAuth.getSecond(), serviceAuth.getThird());
	}
}
