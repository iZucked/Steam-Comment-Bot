/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class UrlFetcher {

	private UrlFetcher() {

	}

	/**
	 * Fetch the content of a URL.
	 * 
	 * @param url
	 * @return
	 * @throws AuthenticationException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String fetchURLContent(final String url) throws AuthenticationException, IOException {
		return fetchURLContent(url, null, null);
	}

	/**
	 * Fetch the content of a URL with basic authentication.
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	public static String fetchURLContent(final String url, final String username, final String password) throws IOException, AuthenticationException {
		try (final CloseableHttpClient client = HttpClients.createDefault()) {

			final HttpGet request = new HttpGet(url);

			if (url != null) {
				final Header auth = new BasicScheme(StandardCharsets.UTF_8).authenticate(new UsernamePasswordCredentials(username, password), request, null);
				request.addHeader(auth);
			}

			final HttpResponse response = client.execute(request);

			try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {

				final StringBuilder result = new StringBuilder();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				return result.toString();
			}

		}
	}
}
