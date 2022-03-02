/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import java.io.IOException;
import java.util.Optional;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.services.users.UsernameProvider;

import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthenticationManager extends AbstractAuthenticationManager {

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
		deleteFromSecurePreferences("username");
		deleteFromSecurePreferences("password");
	}

	@Override
	public boolean isAuthenticated(final String upstreamURL) {
		boolean authenticated = false;

		final Optional<String> optionalUsername = retrieveFromSecurePreferences("username");
		final Optional<String> optionalPassword = retrieveFromSecurePreferences("password");

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

		// @formatter:off
		final Request loginRequest = new Request.Builder().header("Authorization", Credentials.basic(username, password)) //
				.header("Cache-Control", "no-store, max-age=0") //
				.url(url + UpstreamUrlProvider.BASIC_LOGIN_PATH) //
				.build();
		// @formatter:on

		try (Response loginResponse = httpClient.newCall(loginRequest).execute()) {
			if (!loginResponse.isSuccessful()) {
				LOGGER.error("Invalid data hub credentials");
				return false;
			} else {
				UsernameProvider.INSTANCE.setUserId(username);
				return true;
			}
		} catch (final IOException e) {
			e.printStackTrace();
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

	public Optional<Request.Builder> buildRequestWithBasicAuth() {
		final Optional<String> username = retrieveFromSecurePreferences("username");
		final Optional<String> password = retrieveFromSecurePreferences("password");
		Optional<Request.Builder> builder;

		if (username.isPresent() && password.isPresent()) {
			// @formatter:off
			builder = Optional.of(new Request.Builder() //
					.header("Authorization", Credentials.basic( //
							username.get(), //
							password.get())) //
					.header("Cache-Control", "no-store, max-age=0")); //
			// @formatter:on
		} else {
			builder = Optional.empty();
		}

		return builder;
	}

	@Override
	public void clearCookies(final String url) {
		// Nothing to do
	}

	public void withCredentials(String username, String password) {
		storeInSecurePreferences("username", username);
		storeInSecurePreferences("password", password);		
	}
}
