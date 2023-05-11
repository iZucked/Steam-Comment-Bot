/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.inject.Named;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

/**
 * @author robert.erdin@gmail.com created on 24/01/17.
 */
public class AtoBviaCAdapter {

	private static TypeReference<List<AtoBviaCVersion>> PORT_LIST_TYPE = new TypeReference<List<AtoBviaCVersion>>() {

	};

	private static final Logger LOGGER = LoggerFactory.getLogger(AtoBviaCAdapter.class);

	public static final String IDENTIFIER = "abc";

	@Inject(optional = true)
	@Named(Constants.API_KEY)
	private String apiKey = "demo";

	@Inject(optional = true)
	@Named(Constants.API_URL)
	private String baseUrl = "https://api.atobviaconline.com/v1/";

	@Inject
	private CloseableHttpClient client;
	/**
	 * Executor service to limit number of concurrent requests to AtoBviaC
	 */
	@Inject
	private ExecutorService executor;

	public CompletableFuture<CloseableHttpResponse> getDistance(String srcPort, String dstPort, boolean antiPiracy) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return client.execute(new HttpGet(requestString(srcPort, dstPort, antiPiracy)));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}, executor);
	}

	public List<AtoBviaCPort> getPortsBlocking() throws IOException {

		return client.execute(new HttpGet(baseUrl + "Ports?api_key=" + apiKey), response -> {

			ObjectMapper mapper = new ObjectMapper();
			return mapper.readerForListOf(AtoBviaCPort.class).readValue(response.getEntity().getContent());
		});

		// ResponseEntity<List<AtoBviaCPort>> ports = restTemplate.exchange(baseUrl + "Ports?api_key=" + apiKey, HttpMethod.GET, null, new ParameterizedTypeReference<List<AtoBviaCPort>>() {
		// });
		//
		// return ports.getBody();
	}

	// @Bean
	// public RestTemplate restTemplate(RestTemplateBuilder builder) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
	//
	// /**
	// * This is a hack to allow all SSL connections as old jvm's are unable to validate certificates.
	// */
	//
	// if (true) {
	// TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
	//
	// SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
	//
	// SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
	//
	// CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	//
	// HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	//
	// requestFactory.setHttpClient(httpClient);
	// // builder.requestFactory(requestFactory.);
	// return new RestTemplate(requestFactory);
	// }
	// return builder.build();
	// }

	public String getCurrentDataVersion() throws Exception {
		final String url = baseUrl + "Ports?api_key=" + apiKey;

		HttpGet request = new HttpGet(url);
		return client.execute(request, response -> {
			// response.get
			// return
			Header[] headers = response.getHeaders("x-abc-version");
			if (headers != null && headers.length == 1) {
				String[] vComponents = headers[0].getValue().trim().split("\\s+");
				if (vComponents.length == 3) {
					return vComponents[1];
				}
			}
			LOGGER.warn("Could not identify version: " + headers);
			return "UNKNOWN";
		});

	}

	/**
	 * Blocking call!
	 *
	 * @param srcPort
	 * @param dstPort
	 * @param openRoutingPoints
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public double getDistanceBlocking(String srcPort, String dstPort, boolean antiPiracy) throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(requestString(srcPort, dstPort, antiPiracy));
		return client.execute(request, response -> Double.parseDouble(EntityUtils.toString(response.getEntity())));
		// ResponseEntity<String> response = restTemplate.exchange(requestString(srcPort, dstPort, antiPiracy), HttpMethod.GET, null, String.class);
		// return Double.parseDouble(response.getBody());
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	private String requestString(String srcPort, String dstPort, boolean antiPiracy) {

		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append("Distance");
		sb.append("?port=").append(srcPort);
		sb.append("&port=").append(dstPort);
		sb.append("&api_key=").append(apiKey);

		// SUZ & PAN ALWAYS CLOSED
		sb.append("&close=PAN").append("&close=SUZ");
		sb.append("&antipiracy=").append(antiPiracy);

		return sb.toString();
	}

	public String getAccountRequestString() {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append("AccountDetails");
		sb.append("?api_key=").append(apiKey);
		return sb.toString();
	}

	public String getAccountDetails(String accountRequestString) throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(accountRequestString);
		return client.execute(request, response -> EntityUtils.toString(response.getEntity()));
	}

	public void setApiKey(String string) {
		apiKey = string;
	}

}
