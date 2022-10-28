/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.utils.URIUtils;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;

public class OAuthManager extends AbstractAuthenticationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuthManager.class);
	public static final String COOKIE = "cookie";

	private static OAuthManager instance = null;

	private OAuthManager() {
	}

	public static OAuthManager getInstance() {
		if (instance == null) {
			instance = new OAuthManager();
		}
		return instance;
	}

	@Override
	public boolean isAuthenticated(final String upstreamURL) {

		boolean authenticated = false;

		final Optional<String> token = retrieveFromSecurePreferences(COOKIE);

		if (token.isPresent() && isTokenValid(upstreamURL)) {
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
	 * 
	 * @throws URISyntaxException
	 */
	public void buildRequestWithToken(final HttpUriRequestBase msg, final BasicCookieStore store) throws URISyntaxException {
		final Optional<String> token = retrieveFromSecurePreferences(COOKIE);
		if (token.isPresent()) {
			final String tkn = token.get();
			final BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", tkn.replace("JSESSIONID=", ""));
			cookie.setDomain(msg.getUri().getHost());
			cookie.setPath("/");
			store.addCookie(cookie);
		}
	}

	public boolean hasToken() {
		return retrieveFromSecurePreferences(COOKIE).isPresent();
	}

	protected void startAuthenticationShell(final String upstreamURL, final String path, @Nullable final Shell optionalShell) {
		if (authenticationShellIsOpen.compareAndSet(false, true)) {
			final OAuthDialog authenticationShell = new OAuthDialog(upstreamURL, path, optionalShell);
			// Set access token when shell is disposed
			authenticationShell.addDisposeListener(() -> {
				authenticationShellIsOpen.compareAndSet(true, false);
			});
			try {
				authenticationShell.open();
			} catch (final Exception e) {
				// Make sure we have cleared the setting.
				authenticationShellIsOpen.set(false);
				throw e;
			}
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

		final HttpGet request = new HttpGet(upstreamURL + UpstreamUrlProvider.URI_AFTER_SUCCESSFULL_AUTHENTICATION);
		try (var httpClient = HttpClientUtil.createBasicHttpClient(URIUtils.extractHost(request.getUri()), false).build()) {
			if (httpClient == null) {
				return valid;
			}
			final HttpClientContext ctx = new HttpClientContext();
			final BasicCookieStore store = new BasicCookieStore();
			buildRequestWithToken(request, store);
			ctx.setCookieStore(store);

			valid = httpClient.execute(request, ctx, response -> {
				final int responseCode = response.getCode();
				if (HttpClientUtil.isSuccessful(responseCode)) {
					return true;
				} else {
					// token is expired, log the user out
					Display.getDefault().asyncExec(() -> logout(upstreamURL, null));
				}
				return false;
			});
		} catch (final Exception e) {
			LOGGER.debug(String.format("Unexpected exception: %s", e.getMessage()));
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
		// Browser.setCookie("JSESSIONID=;", url);
		// Browser.setCookie("authenticated=;", url + "/authenticated");
	}
}
