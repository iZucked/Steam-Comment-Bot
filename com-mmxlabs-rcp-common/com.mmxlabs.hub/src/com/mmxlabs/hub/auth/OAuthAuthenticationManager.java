/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import java.io.IOException;
import java.util.Optional;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.UpstreamUrlProvider;

import okhttp3.Request;
import okhttp3.Response;

public class OAuthAuthenticationManager extends AbstractAuthenticationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuthAuthenticationManager.class);
	public static final String COOKIE = "cookie";

	private static OAuthAuthenticationManager instance = null;

	private OAuthAuthenticationManager() {
	}

	public static OAuthAuthenticationManager getInstance() {
		if (instance == null) {
			instance = new OAuthAuthenticationManager();
		}
		return instance;
	}

	@Override
	public boolean isAuthenticated(final String upstreamURL) {

		boolean authenticated = false;

		final Optional<String> token = retrieveFromSecurePreferences(COOKIE);

		if (token.isPresent() && !isTokenValid(upstreamURL)) {
			authenticated = true;
		}
		return authenticated;
	}

	@Override
	public void run(final String upstreamURL, @Nullable final Shell optionalShell) {
		if (!isAuthenticated(upstreamURL)) {
			final String path = UpstreamUrlProvider.OAUTH_LOGIN_PATH;
			startAuthenticationShell(upstreamURL, path, optionalShell);
		}
	}

	/**
	 * Create a request builder with Authorization header.
	 */
	public Optional<Request.Builder> buildRequestWithToken() {
		final Optional<String> token = retrieveFromSecurePreferences(COOKIE);
		Optional<Request.Builder> builder;

		if (token.isPresent()) {
			// @formatter:off
			builder = Optional.of( //
					new Request.Builder() //
							.header("Cookie", token.get()) //
							.header("cache-control", "no-cache")); //
			// @formatter:on
		} else {
			builder = Optional.empty();
		}

		return builder;
	}

	public boolean hasToken() {
		return retrieveFromSecurePreferences(COOKIE).isPresent();
	}
	
	protected void startAuthenticationShell(final String upstreamURL, final String path, @Nullable final Shell optionalShell) {
		if (authenticationShellIsOpen.compareAndSet(false, true)) {
			final OAuthAuthenticationDialog authenticationShell = new OAuthAuthenticationDialog(optionalShell, upstreamURL, path);
			// Set access token when shell is disposed
			authenticationShell.addDisposeListener(() -> {
				authenticationShellIsOpen.compareAndSet(true, false);
			});
			authenticationShell.open();
		}
	}

	/**
	 * Checks if the SSO token (in the form of a cookie) is valid by making a request to the hub
	 *
	 * @param upstreamURL
	 * @return true is the token is valid, false otherwise
	 */
	public boolean isTokenValid(final String upstreamURL) {
		boolean valid = false;

		final Optional<Request.Builder> builder = buildRequestWithToken();

		if (builder.isPresent()) {
			final Request request = builder.get().url(upstreamURL + UpstreamUrlProvider.URI_AFTER_SUCCESSFULL_AUTHENTICATION).build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					valid = true;

					// token is expired, log the user out
					logout(upstreamURL, null);
				}
			} catch (final IOException e) {
				LOGGER.debug(String.format("Unexpected exception: %s", e.getMessage()));
			}
		}

		return valid;
	}

	@Override
	public void logout(final String upstreamURL, @Nullable final Shell shell) {
		final String path = UpstreamUrlProvider.LOGOUT_PATH;
		startAuthenticationShell(upstreamURL, path, shell);
		clearCookies(upstreamURL);
	}

	@Override
	public void clearCookies(final String url) {
		// delete sso cookie from secure preferences
		deleteFromSecurePreferences(COOKIE);
		// delete cookie from swt browser
		// doesn't work if the user clicks "stay logged in"
		Browser.setCookie("JSESSIONID", url);
		Browser.setCookie("authenticated", url + "/authenticated");
	}
}
