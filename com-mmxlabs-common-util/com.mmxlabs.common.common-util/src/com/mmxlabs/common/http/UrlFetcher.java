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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.HttpClients;

public class UrlFetcher {
	

	/**
	 * Fetch the content of a URL.
	 * @param url
	 * @return
	 * @throws AuthenticationException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String fetchUrlContent(String url) throws AuthenticationException, ClientProtocolException, IOException {
		return fetchURLContent(url, null, null);
	}
	
	/**
	 * Fetch the content of a URL with basic authentication.
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	public static String fetchURLContent(String url, String username, String password) throws ClientProtocolException, IOException, AuthenticationException {
		HttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		
		if (url != null) {
			Header auth = new BasicScheme(StandardCharsets.UTF_8).authenticate(new UsernamePasswordCredentials(username, password), request, null);
			request.addHeader(auth);
		}

		HttpResponse response = client.execute(request);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}
}
