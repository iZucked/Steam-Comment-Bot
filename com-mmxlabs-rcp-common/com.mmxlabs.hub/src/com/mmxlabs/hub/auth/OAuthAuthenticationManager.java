package com.mmxlabs.hub.auth;

import java.io.IOException;
import java.util.Optional;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.UpstreamUrlProvider;

import okhttp3.Request;
import okhttp3.Response;

public class OAuthAuthenticationManager extends AbstractAuthenticationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuthAuthenticationManager.class);
	private static final String ACCESS_TOKEN = "access_token";

	public static OAuthAuthenticationManager instance = null;

	private OAuthAuthenticationManager() {
	}

	public static OAuthAuthenticationManager getInstance() {
		if (instance == null) {
			instance = new OAuthAuthenticationManager();
		}
		return instance;
	}

	@Override
	public boolean isAuthenticated(String upstreamURL) {

		boolean authenticated = false;

		Optional<String> token = retrieveFromSecurePreferences(ACCESS_TOKEN);

		if (token.isPresent() && !isTokenExpired(upstreamURL)) {
			authenticated = true;
		}

		return authenticated;
	}

	@Override
	public void run(String upstreamURL, @Nullable Shell optionalShell) {
		if (!isAuthenticated(upstreamURL)) {
			String path = UpstreamUrlProvider.OAUTH_LOGIN_PATH;
			startAuthenticationShell(upstreamURL, path, optionalShell);
		}
	}

	/**
	 * Create a request builder with Authorization header.
	 */
	public Optional<Request.Builder> buildRequestWithToken() {
		final Optional<String> token = retrieveFromSecurePreferences(ACCESS_TOKEN);
		Optional<Request.Builder> builder;

		if (token.isPresent()) {
			// @formatter:off
			builder = Optional.of( //
					new Request.Builder() //
							.header("Authorization", "Bearer " + token.get()) //
							.header("cache-control", "no-cache")); //
			// @formatter:on
		} else {
			builder = Optional.empty();
		}

		return builder;
	}

	protected void startAuthenticationShell(String upstreamURL, String path, @Nullable Shell optionalShell) {
		final Display display;
		if (optionalShell == null) {
			if (PlatformUI.isWorkbenchRunning()) {
				display = PlatformUI.getWorkbench().getDisplay();
			} else {
				display = null;
			}
		} else {
			display = optionalShell.getDisplay();
		}

		if (display != null && authenticationShellIsOpen.compareAndSet(false, true)) {
			display.syncExec(() -> {
				final Shell shell = optionalShell == null ? display.getActiveShell() : optionalShell;
				OAuthAuthenticationShell authenticationShell = new OAuthAuthenticationShell(shell, upstreamURL, path);

				// Set access token when shell is disposed
				authenticationShell.addDisposeListener(event -> {
					storeInSecurePreferences(ACCESS_TOKEN, authenticationShell.getAccessToken());
					authenticationShellIsOpen.compareAndSet(true, false);
				});

				authenticationShell.run(shell);
			});
		}
	}

	public boolean isTokenExpired(String upstreamURL) {
		boolean expired = false;

		final Request request = buildRequestWithToken().get().url(upstreamURL + UpstreamUrlProvider.URI_AFTER_SUCCESSFULL_AUTHENTICATION) //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful() || response.code() == 403 || response.code() == 401) {
				expired = true;
				// token is expired, log the user out
				logout(upstreamURL, null);
			}
		} catch (IOException e) {
			LOGGER.debug(String.format("Unexpected exception: %s", e.getMessage()));
		}

		return expired;
	}

	@Override
	public void logout(String upstreamURL, @Nullable Shell shell) {
		String path = UpstreamUrlProvider.LOGOUT_PATH;
		startAuthenticationShell(upstreamURL, path, shell);
		clearCookies(upstreamURL);
		deleteFromSecurePreferences(ACCESS_TOKEN);
	}

	@Override
	public void clearCookies(String url) {
		// delete cookie from swt browser
		// doesn't work if the user clicks "stay logged in"
		Browser.setCookie("JSESSIONID", url);
		Browser.setCookie("authenticated", url + "/authenticated");
	}
}
