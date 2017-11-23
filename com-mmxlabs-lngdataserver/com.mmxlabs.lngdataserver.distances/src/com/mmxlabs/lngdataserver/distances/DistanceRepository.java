package com.mmxlabs.lngdataserver.distances;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.auth.AuthenticationException;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Triple;
import com.mmxlabs.lngdataserver.distances.internal.Activator;
import com.mmxlabs.lngdataserver.distances.preferences.PreferenceConstants;

/**
 * This implementation is not thread-safe.
 * 
 * @author Robert Erdin
 */
@NonNullByDefault
public class DistanceRepository {

	private static final Logger LOG = LoggerFactory.getLogger(DistanceRepository.class);
	private Triple<String, String, String> auth;

	public DistanceRepository() {
		auth = getUserServiceAuth();
	}

	public DistanceRepository(String url) {
		auth = new Triple<>(url, "", "");
	}

	private Triple<String, String, String> getUserServiceAuth() {
		final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		final String url = prefs.getString(PreferenceConstants.P_URL_KEY);
		if ("".equals(url)) {
			throw new RuntimeException("No URL found for upstream distance repository");
		}
		final String username = prefs.getString(PreferenceConstants.P_USERNAME_KEY);
		final String password = prefs.getString(PreferenceConstants.P_PASSWORD_KEY);

		return new Triple<>(url, username, password);
	}

	public List<String> getVersions() {
		try {
			final Triple<String, String, String> serviceAuth = getServiceAuth();
			return UpstreamDistancesFetcher.getUpstreamVersions(serviceAuth.getFirst(), serviceAuth.getSecond(), serviceAuth.getThird());
		} catch (AuthenticationException | IOException | ParseException e) {
			LOG.error("Error fetching versions from upstream service", e);
			throw new RuntimeException("Error fetching versions from upstream service", e);
		}
	}

	public IDistanceProvider getDistances(final String version) {

		try {
			final Triple<String, String, String> serviceAuth = getServiceAuth();
			final Map<Via, Map<String, Map<String, Integer>>> result = UpstreamDistancesFetcher.getDistances(serviceAuth.getFirst(), version, serviceAuth.getSecond(), serviceAuth.getThird());
			return new DefaultDistanceProvider(version, result);
		} catch (AuthenticationException | IOException | ParseException e) {
			LOG.error("Error fetching versions from upstream service", e);
			throw new RuntimeException("Error fetching versions from upstream service", e);
		}
	}

	private Triple<String, String, String> getServiceAuth() {
		return auth;
	}

	public @Nullable IDistanceProvider getLatestDistances() {

		final List<String> versions = getVersions();
		if (versions.isEmpty()) {
			return null;
		}
		final String version = versions.get(0);

		try {
			final Triple<String, String, String> serviceAuth = getServiceAuth();
			final Map<Via, Map<String, Map<String, Integer>>> result = UpstreamDistancesFetcher.getDistances(serviceAuth.getFirst(), version, serviceAuth.getSecond(), serviceAuth.getThird());
			return new DefaultDistanceProvider(version, result);
		} catch (AuthenticationException | IOException | ParseException e) {
			LOG.error("Error fetching versions from upstream service", e);
			throw new RuntimeException("Error fetching versions from upstream service", e);
		}
	}

}