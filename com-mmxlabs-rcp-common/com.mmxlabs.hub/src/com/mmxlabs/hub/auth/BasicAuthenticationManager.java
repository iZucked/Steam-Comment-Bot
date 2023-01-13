/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import org.apache.http.HttpMessage;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.services.users.UsernameProvider;

public class BasicAuthenticationManager extends AbstractAuthenticationManager {

	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";

	private static BasicAuthenticationManager instance = null;

	private BasicAuthenticationManager() {
	}

	public static BasicAuthenticationManager getInstance() {
		if (instance == null) {
			instance = new BasicAuthenticationManager();
		}
		return instance;
	}

	@Override
	public void logout(final String upstreamURL, @Nullable final Shell shell) {
		deleteFromSecurePreferences(KEY_USERNAME);
		deleteFromSecurePreferences(KEY_PASSWORD);
	}

	@Override
	public boolean isAuthenticated(final String upstreamURL) {
		boolean authenticated = false;

		final Optional<String> optionalUsername = retrieveFromSecurePreferences(KEY_USERNAME);
		final Optional<String> optionalPassword = retrieveFromSecurePreferences(KEY_PASSWORD);

		if (upstreamURL != null && !upstreamURL.isEmpty() && optionalUsername.isPresent() && optionalPassword.isPresent()
				&& checkCredentials(upstreamURL, optionalUsername.get(), optionalPassword.get())) {
			authenticated = true;
		}

		return authenticated;
	}

	@Override
	public void run(final String upstreamURL, @Nullable final Shell optionalShell) {

		if (!isAuthenticated(upstreamURL)) {
			startAuthenticationShell(upstreamURL, optionalShell);
		}
	}

	public boolean checkCredentials(final String url, final String username, final String password) {
		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			return false;
		}

		HttpGet request = new HttpGet(url + UpstreamUrlProvider.BASIC_LOGIN_PATH);

		try (var httpClient = HttpClientUtil.createBasicHttpClient(URIUtils.extractHost(request.getURI()))) {
			if (httpClient == null) {
				return false;
			}
			request.addHeader("Authorization", HttpClientUtil.basicAuthHeader(username, password));
			request.addHeader("Cache-Control", "no-store, max-age=0");

			try (var response = httpClient.execute(request)) {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (!HttpClientUtil.isSuccessful(responseCode)) {
					LOGGER.error("Invalid data hub credentials");
					return false;
				} else {
					UsernameProvider.INSTANCE.setUserId(username);
					return true;
				}
			} catch (final IOException e) {
				LOGGER.debug(String.format("Unexpected exception: %s", e.getMessage()));
			}
		} catch (final IOException e) {
			LOGGER.debug(String.format("Unexpected exception: %s", e.getMessage()));
		}

		return false;
	}

	protected void startAuthenticationShell(final String upstreamURL, @Nullable final Shell optionalShell) {

		if (authenticationShellIsOpen.compareAndSet(false, true)) {
			final BasicAuthenticationDialog dialog = new BasicAuthenticationDialog(optionalShell);
			dialog.setUrl(upstreamURL);
			dialog.addDisposeListener(() -> authenticationShellIsOpen.compareAndSet(true, false));
			dialog.open();
		}
	}

	public void buildRequestWithBasicAuth(HttpMessage msg) {
		final Optional<String> username = retrieveFromSecurePreferences(KEY_USERNAME);
		final Optional<String> password = retrieveFromSecurePreferences(KEY_PASSWORD);

		if (username.isPresent() && password.isPresent()) {
			final byte[] encodedAuth = Base64.getEncoder().encode(String.format("%s:%s", username.get(), password.get()).getBytes(StandardCharsets.UTF_8));
			final String authHeader = "Basic " + new String(encodedAuth);
			msg.addHeader("Authorization", authHeader);
		}
	}

	@Override
	public void clearCookies(final String url) {
		// Nothing to do
	}

	public void withCredentials(String username, String password) {
		storeInSecurePreferences(KEY_USERNAME, username);
		storeInSecurePreferences(KEY_PASSWORD, password);
	}
}
