/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationManager.class);

	private static final String BASIC = "basic";
	private static final String OAUTH = "oauth";

	static final ISecurePreferences PREFERENCES = SecurePreferencesFactory.getDefault();

	private static final String PREFERENCES_NODE = "upstream";

	private static AuthenticationManager instance = null;

	private AuthenticationManager() {
	}

	public static AuthenticationManager getInstance() {
		if (instance == null) {
			instance = new AuthenticationManager();
		}
		return instance;
	}

	private final BasicAuthenticationManager basicAuthenticationManager = BasicAuthenticationManager.getInstance();
	private final OAuthManager oauthManager = OAuthManager.getInstance();

	private String authenticationScheme = BASIC;
	private String upstreamURL = null;

	public AtomicBoolean forceBasicAuthentication = new AtomicBoolean(false);

	public synchronized void setForceBasicAuthentication(final boolean value) {
		forceBasicAuthentication.set(value);
	}

	public synchronized void updateAuthenticationScheme(final String upstreamURL, final String scheme) {
		this.upstreamURL = upstreamURL;
		this.authenticationScheme = scheme;
	}

	public boolean isOAuthEnabled() {
		return OAUTH.equals(authenticationScheme);
	}

	public boolean isAuthenticated() {
		boolean authenticated = false;

		if (isOAuthEnabled() && !forceBasicAuthentication.get()) {
			authenticated = oauthManager.isAuthenticated(upstreamURL);
		} else {
			authenticated = basicAuthenticationManager.isAuthenticated(upstreamURL);
		}

		return authenticated;
	}

	public void logout(@Nullable final Shell shell) {
		if (isOAuthEnabled() && !forceBasicAuthentication.get()) {
			oauthManager.logout(upstreamURL, shell);
		} else {
			basicAuthenticationManager.logout(upstreamURL, shell);
		}
	}

	public void logoutAll(@Nullable final Shell shell) {
		oauthManager.logout(upstreamURL, shell);
		basicAuthenticationManager.logout(upstreamURL, shell);
	}

	public void clearCookies(final String url) {
		basicAuthenticationManager.clearCookies(url);
		oauthManager.clearCookies(url);
	}

	public void run(@Nullable final Shell optionalShell) {
		if (isOAuthEnabled() && !forceBasicAuthentication.get()) {
			oauthManager.run(upstreamURL, optionalShell);
		} else {
			basicAuthenticationManager.run(upstreamURL, optionalShell);
		}
	}

	protected void startAuthenticationShell(@Nullable final Shell optionalShell) {
		if (isOAuthEnabled() && !forceBasicAuthentication.get()) {
			oauthManager.run(upstreamURL, optionalShell);
		} else {
			basicAuthenticationManager.run(upstreamURL, optionalShell);
		}
	}

	public void addAuthToRequest(@NonNull final HttpRequestBase request, @NonNull final HttpClientContext ctx) {
		if (isOAuthEnabled() && !forceBasicAuthentication.get()) {
			oauthManager.buildRequestWithToken(request, ctx);
		} else if (BASIC.equals(authenticationScheme) || forceBasicAuthentication.get()) {
			basicAuthenticationManager.buildRequestWithBasicAuth(request);
		} else {
			request.addHeader("Cache-Control", "no-store, max-age=0");
		}
	}

	public void storeInSecurePreferences(final String key, final String value) {
		// returns node corresponding to the path specified. If such node does not
		// exist, a new node is created
		try {
			final ISecurePreferences node = PREFERENCES.node(PREFERENCES_NODE);
			node.put(key, value, true);
		} catch (final IllegalArgumentException e) {
			LOGGER.error(String.format("Failed to create a node in secure preferences, please use a valid node path: %s", e.getMessage()));
		} catch (final StorageException e) {
			LOGGER.error(String.format("Failed to encrypt the value and save it to secure preferences: %s", e.getMessage()));
		}
	}

	public Optional<String> retrieveFromSecurePreferences(final String key) {
		Optional<String> value = Optional.empty();

		final ISecurePreferences node = PREFERENCES.node(PREFERENCES_NODE);
		try {
			value = Optional.ofNullable(node.get(key, null));
		} catch (final StorageException e) {
			LOGGER.error(String.format("Failed to get value with key %s from secure preferences: %s", key, e.getMessage()));
		}

		return value;
	}

}
