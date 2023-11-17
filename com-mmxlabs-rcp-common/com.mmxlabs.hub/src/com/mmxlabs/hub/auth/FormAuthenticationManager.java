/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import java.util.Optional;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.services.users.UsernameProvider;

public class FormAuthenticationManager extends AbstractAuthenticationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(FormAuthenticationManager.class);

	public static final String COOKIE = "cookie";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";

	private static FormAuthenticationManager instance = null;

	private FormAuthenticationManager() {
	}

	public static FormAuthenticationManager getInstance() {
		if (instance == null) {
			instance = new FormAuthenticationManager();
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
			final String path = UpstreamUrlProvider.BASIC_LOGIN_FORM_PATH;
			startAuthenticationShell(upstreamURL, path, optionalShell);
		}
	}

	/**
	 * Create a request builder with Authorization header.
	 */
	public void buildRequestWithToken(final HttpRequestBase msg, final BasicCookieStore store) {
		final Optional<String> token = retrieveFromSecurePreferences(COOKIE);
		if (token.isPresent()) {
			final String tkn = token.get();
			final BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", tkn.replace("JSESSIONID=", ""));
			cookie.setDomain(msg.getURI().getHost());
			cookie.setPath("/");
			store.addCookie(cookie);
		}
	}

	public boolean hasToken() {
		return retrieveFromSecurePreferences(COOKIE).isPresent();
	}

	protected void startAuthenticationShell(final String upstreamURL, final String path, @Nullable final Shell optionalShell) {

		if (authenticationShellIsOpen.compareAndSet(false, true)) {
			final FormAuthenticationDialog dialog = new FormAuthenticationDialog(optionalShell);
			dialog.setUrl(upstreamURL);
			dialog.addDisposeListener(() -> authenticationShellIsOpen.compareAndSet(true, false));
			dialog.open();
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
		try (var httpClient = HttpClientUtil.createBasicHttpClient(URIUtils.extractHost(request.getURI()), false).build()) {
			if (httpClient == null) {
				return valid;
			}
			final HttpClientContext ctx = new HttpClientContext();
			final BasicCookieStore store = new BasicCookieStore();
			buildRequestWithToken(request, store);
			ctx.setCookieStore(store);

			valid = httpClient.execute(request, response -> {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (HttpClientUtil.isSuccessful(responseCode)) {
					return true;
				} else {
					// token is expired, log the user out
					Display.getDefault().asyncExec(() -> logout(upstreamURL, null));
				}
				return false;
			}, ctx);
		} catch (final Exception e) {
			LOGGER.debug(String.format("Unexpected exception: %s", e.getMessage()));
		}

		return valid;
	}

	@Override
	public void logout(final String upstreamURL, @Nullable final Shell shell) {

		final HttpPost request = new HttpPost(upstreamURL + UpstreamUrlProvider.LOGOUT_PATH);
		try (var httpClient = HttpClientUtil.createBasicHttpClient(URIUtils.extractHost(request.getURI()), false)
				.setDefaultRequestConfig(RequestConfig.custom() //
						.setCookieSpec(CookieSpecs.STANDARD)
						.build()) //
				.build()) {

			final HttpClientContext ctx = new HttpClientContext();
			final BasicCookieStore store = new BasicCookieStore();
			buildRequestWithToken(request, store);
			ctx.setCookieStore(store);

			// TODO: We need the CSRF token!
			try (var response = httpClient.execute(request, ctx)) {
				// System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (final Exception e) {
			LOGGER.debug(String.format("Unexpected exception: %s", e.getMessage()));
		}

		clearCookies(upstreamURL);
	}

	@Override
	public void clearCookies(final String url) {
		deleteFromSecurePreferences(COOKIE);
		deleteFromSecurePreferences(KEY_USERNAME);
		deleteFromSecurePreferences(KEY_PASSWORD);
	}

	public boolean checkCredentials(final String url, final String username, final String password, final BasicCookieStore s) {
		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			return false;
		}

		final HttpGet getRequest = new HttpGet(url + "/login/local");
		try (var http = HttpClientBuilder.create()
				// .setRedirectStrategy(new LaxRedirectStrategy())
				.setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
				.setDefaultCookieStore(s)
				.build())

		{
			// Call the session endpoint (if it exists) to prepare a new login session
			try (var resp = http.execute(new HttpGet(url + "/api/session"))) {
				// Ignore any response etc. This is a new endpoint for Data Hub 2.0 which also has lazy sessions.
				// Older data hubs have the older spring boot framework where a session will be always created
			}

			String csrf = null;
			// Issue the get request to allocate the session cookies and to grab the csrf parameter from the page if present.
			try (var resp = http.execute(getRequest)) {
				final String pge = EntityUtils.toString(resp.getEntity());
				final var js = Jsoup.parse(pge);
				final Elements select = js.body().select("form");
				final var tt = select.select("input[name=_csrf]");
				csrf = tt.attr("value");
			} catch (final Exception e) {
				// Assume there is no csrf token on the page
				e.printStackTrace();
			}

			// Now we can issue the login request
			final var reqBuilder = RequestBuilder.post()
					.setUri(url + UpstreamUrlProvider.BASIC_LOGIN_FORM_PATH)
					.addParameter("Submit", "Login")
					.addParameter("username", username)
					.addParameter("password", password);

			// Add the CSRF token back in if we got one.
			if (csrf != null) {
				reqBuilder.addParameter("_csrf", csrf);
			}
			final var p = reqBuilder.build();
			try (var response = http.execute(p)) {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (!HttpClientUtil.isSuccessful(responseCode)) {
					LOGGER.error("Invalid data hub credentials");
					return false;
				} else {
					UsernameProvider.INSTANCE.setUserId(username);
					return true;
				}
			}

		} catch (final Exception e) {
			LOGGER.debug(String.format("Unexpected exception: %s", e.getMessage()));
		}

		return false;
	}
}
