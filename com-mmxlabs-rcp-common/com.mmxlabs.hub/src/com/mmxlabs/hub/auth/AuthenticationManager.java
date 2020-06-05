package com.mmxlabs.hub.auth;

import java.util.Optional;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.preferences.DataHubPreferencePage;

import okhttp3.Request;
import okhttp3.Request.Builder;

public class AuthenticationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationManager.class);

	private static final String BASIC = "basic";
	private static final String OAUTH = "oauth";

	private static AuthenticationManager instance = null;

	private AuthenticationManager() {
	}

	public static AuthenticationManager getInstance() {
		if (instance == null) {
			instance = new AuthenticationManager();
		}
		return instance;
	}

	private BasicAuthenticationManager basicAuthenticationManager = BasicAuthenticationManager.getInstance();
	private OAuthAuthenticationManager oauthAuthenticationManager = OAuthAuthenticationManager.getInstance();

	private String authenticationScheme = "basic";
	private String upstreamURL = null;

	private boolean forceBasicAuthentication = false;

	public void setForceBasicAuthentication(boolean forceBasicAuthentication) {
		this.forceBasicAuthentication = forceBasicAuthentication;
	}

	public synchronized void updateAuthenticationScheme(String upstreamURL, String scheme) {
		this.upstreamURL = upstreamURL;
		this.authenticationScheme = scheme;
	}

	public boolean isOAuthEnabled() {
		return OAUTH.equals(authenticationScheme);
	}

	public boolean isAuthenticated() {
		boolean authenticated = false;

		if (isOAuthEnabled() && !forceBasicAuthentication) {
			authenticated = oauthAuthenticationManager.isAuthenticated(upstreamURL);
		} else {
			authenticated = basicAuthenticationManager.isAuthenticated(upstreamURL);
		}

		return authenticated;
	}

	public void logout(@Nullable Shell shell) {
		if (isOAuthEnabled() && !forceBasicAuthentication) {
			oauthAuthenticationManager.logout(upstreamURL, shell);
		} else {
			basicAuthenticationManager.logout(upstreamURL, shell);
		}
		// update the text in the datahub preference page
		DataHubPreferencePage.setButtonText();
	}

	public void clearCookies(String url) {
		basicAuthenticationManager.clearCookies(url);
		oauthAuthenticationManager.clearCookies(url);
	}

	public void run(@Nullable Shell optionalDisplay) {
		if (isOAuthEnabled() && !forceBasicAuthentication) {
			oauthAuthenticationManager.run(upstreamURL, optionalDisplay);
		} else {
			basicAuthenticationManager.run(upstreamURL, optionalDisplay);
		}
	}

	protected void startAuthenticationShell(@Nullable Shell optionalDisplay) {
		if (isOAuthEnabled() && !forceBasicAuthentication) {
			oauthAuthenticationManager.run(upstreamURL, optionalDisplay);
		} else {
			basicAuthenticationManager.run(upstreamURL, optionalDisplay);
		}
	}

	public static Request.Builder buildRequestWithoutAuthentication() {
		return new Request.Builder().header("cache-control", "no-cache");
	}

	public Request.Builder buildRequest() {
		if (isOAuthEnabled() && !forceBasicAuthentication) {
			Optional<Builder> buildRequestWithToken = oauthAuthenticationManager.buildRequestWithToken();
			if (buildRequestWithToken.isPresent()) {
				return buildRequestWithToken.get();
			} else {
				// Invalidate?
			}
		} else if (BASIC.equals(authenticationScheme) || forceBasicAuthentication) {
			Optional<Builder> buildRequestWithBasicAuth = basicAuthenticationManager.buildRequestWithBasicAuth();
			if (buildRequestWithBasicAuth.isPresent()) {
				return buildRequestWithBasicAuth.get();
			} else {
				// Invalidate?
			}
		}
		return buildRequestWithoutAuthentication();
	}
}
