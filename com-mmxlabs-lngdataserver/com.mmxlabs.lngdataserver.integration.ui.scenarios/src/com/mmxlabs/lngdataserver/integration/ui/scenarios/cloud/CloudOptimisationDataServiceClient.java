/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressHttpEntityWrapper;
import com.mmxlabs.license.ssl.LicenseManager;
import com.mmxlabs.license.ssl.TrustStoreManager;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.debug.CloudOptiDebugContants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse.GatewayResponseMaker;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse.IGatewayResponse;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.preferences.CloudOptimiserPreferenceConstants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.Activator;

public class CloudOptimisationDataServiceClient {

	private static final Logger LOG = LoggerFactory.getLogger(CloudOptimisationDataServiceClient.class);

	private static final String LIST_SCENARIOS_URL = "/scenarios";
	private static final String LIST_RESULTS_URL = "/results";
	private static final String SCENARIO_RESULT_URL = "/result";
	private static final String JOB_STATUS_URL = "/status";
	private static final String SCENARIO_CLOUD_UPLOAD_URL = "/scenario"; // for localhost - "/scenarios/v1/cloud/opti/upload"
	private static final String PUBLIC_KEY_URL = "/publickey";
	private static final String INFO_URL = "/info";
	private static final String ABORT_URL = "/abort";

	private String userid;

	private final Instant lastSuccessfulAccess = Instant.EPOCH.plusNanos(1);

	public Instant getLastSuccessfulAccess() {
		return this.lastSuccessfulAccess;
	}

	private HttpClientBuilder createHttpBuilder(final URI url) {
		final var builder = HttpClientBuilder.create();

		final var sslBuilder = SSLContexts.custom();
		if (url.getHost().contains("gw.minimaxlabs.com")) {
			try {
				LicenseManager.loadLicenseKeystore(sslBuilder);
			} catch (final Exception e1) {
				e1.printStackTrace();
			}
		}
		try {
			final Pair<KeyStore, char[]> p = TrustStoreManager.loadLocalTruststore();
			if (p != null) {
				sslBuilder.loadTrustMaterial(p.getFirst(), null);
			}
			builder.setSSLContext(sslBuilder.build());
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
		{

			final Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			final ServiceTracker<IProxyService, IProxyService> proxyTracker = new ServiceTracker<>(bundle.getBundleContext(), IProxyService.class.getName(), null);
			proxyTracker.open();
			try {
				final IProxyService service = proxyTracker.getService();
				if (service != null && service.isProxiesEnabled()) {
					final IProxyData[] data = service.select(url);
					if (data != null && data.length > 0) {
						for (final var pd : data) {
							if (Objects.equals(pd.getType(), IProxyData.HTTPS_PROXY_TYPE)) {
								final HttpHost proxy = new HttpHost(pd.getHost(), pd.getPort());
								final DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
								builder.setRoutePlanner(routePlanner);
							}
						}
					}
				}
			} finally {
				proxyTracker.close();
			}
		}

		return builder;

	}

	private void withAuthHeader(final URI url, final HttpMessage b) {
		b.addHeader("Authorization", HttpClientUtil.basicAuthHeader(getUsername(), getPassword()));
	}

	private String getUsername() {
		return Activator.getDefault().getPreferenceStore().getString(CloudOptimiserPreferenceConstants.P_USERNAME);
	}

	private String getPassword() {
		return Activator.getDefault().getPreferenceStore().getString(CloudOptimiserPreferenceConstants.P_PASSWORD);
	}

	private String getGateway() {
		String gateway = Activator.getDefault().getPreferenceStore().getString(CloudOptimiserPreferenceConstants.P_GATEWAY_URL_KEY);
		if (gateway != null && gateway.endsWith("/")) {
			gateway = gateway.substring(0, gateway.length() - 1);
		}
		return gateway;
	}

	public void setUserId(final String userid) {
		this.userid = userid;
	}

	public String getUserId() {
		return this.userid;
	}

	public String getInfo() throws IOException, URISyntaxException {
		final String gateway = getGateway();
		if (gateway == null) {
			return null;
		}
		final URI url = new URI(String.format("%s%s", getGateway(), INFO_URL));

		final HttpClientBuilder builder = createHttpBuilder(url);

		try (var httpClient = builder.build()) {
			final HttpGet request = new HttpGet(url);
			withAuthHeader(url, request);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				if (response.getStatusLine().getStatusCode() == 200) {
					return EntityUtils.toString(response.getEntity());
				}
				throw new IOException("Unexpected code: " + response);
			}
		}
	}

	public String upload(final File scenario, //
			final String checksum, //
			final String scenarioName, //
			final IProgressListener progressListener, final File encryptedSymmetricKey) throws IOException, URISyntaxException {
		final String gateway = getGateway();
		if (gateway == null) {
			return null;
		}

		final URI url = new URI(String.format("%s%s/%s", getGateway(), SCENARIO_CLOUD_UPLOAD_URL, getUserId()));
		final HttpClientBuilder builder = createHttpBuilder(url);

		try (var httpClient = builder.build()) {
			final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
			formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			formDataBuilder.addTextBody("sha256", checksum);
			formDataBuilder.addBinaryBody("scenario", scenario, ContentType.DEFAULT_BINARY, scenarioName + ".zip");
			formDataBuilder.addBinaryBody("encryptedsymkey", encryptedSymmetricKey, ContentType.DEFAULT_BINARY, "aes.key.enc");
			final HttpEntity entity = formDataBuilder.build();
			final HttpPost request = new HttpPost(url);
			withAuthHeader(url, request);
			// Wrap entity for progress monitor
			request.setEntity(new ProgressHttpEntityWrapper(entity, progressListener));
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode >= 200 && statusCode < 300) {
					return EntityUtils.toString(response.getEntity());
				}
				// 400 - missing data or data filename in request
				// 415 - archive was not a zip file
				throw new IOException("Unexpected code " + response);
			}
		}
	}

	/**
	 * Download the job result. Note we expect the original request to return a 302 redirect into an AWS s3 bucket. Apache will follow the redirect, but we need to ensure the auth header is removed.
	 * 
	 */
	public IGatewayResponse downloadTo(final String jobid, final int resultIdx, final File file, final IProgressListener progressListener) throws IOException, URISyntaxException {

		final String gateway = getGateway();
		if (gateway == null) {
			if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_DOWNLOAD)) {
				LOG.trace("Download Result (%s): null requestBuilder", jobid);
			}
			return null;
		}
		final URI url = new URI(String.format("%s%s/%s/%s/%d", getGateway(), SCENARIO_RESULT_URL, jobid, getUserId(), resultIdx));
		final HttpClientBuilder builder = createHttpBuilder(url);

		builder.addInterceptorFirst(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
				try {
					URI uri = new URI(request.getRequestLine().getUri());
					if (request instanceof HttpRequestWrapper w) {
						uri = new URI(w.getOriginal()
								.getRequestLine().getUri());
					}
					if (uri.getHost().contains("gw.minimaxlabs.com")) {
						withAuthHeader(uri, request);
					}
				} catch (URISyntaxException e) {
					throw new IOException(e);
				}
			}

		});

		try (var httpClient = builder.build()) {
			final HttpGet request = new HttpGet(url);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != 200) {
					if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_DOWNLOAD)) {
						LOG.trace("Download Result (%s): Error response code is %d", jobid, statusCode);
					}
					return GatewayResponseMaker.makeGatewayResponse(response);
				}

				try (FileOutputStream fos = new FileOutputStream(file)) {
					final ProgressHttpEntityWrapper w = new ProgressHttpEntityWrapper(response.getEntity(), progressListener);
					w.writeTo(fos);

					if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_DOWNLOAD)) {
						LOG.trace("Download Result (%s): Completed", jobid);
					}
					return GatewayResponseMaker.makeGatewayResponse(response);

				}
			}
		}
	}

	public String getJobStatus(final @NonNull String jobid) throws Exception {

		final String gateway = getGateway();
		if (gateway == null) {
			return null;
		}
		final URI url = new URI(String.format("%s%s/%s/%s", getGateway(), JOB_STATUS_URL, jobid, getUserId()));
		final HttpClientBuilder builder = createHttpBuilder(url);

		try (var httpClient = builder.build()) {
			final HttpGet request = new HttpGet(url);
			withAuthHeader(url, request);

			try (CloseableHttpResponse response = httpClient.execute(request)) {
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != 200) {
					if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_POLL)) {
						LOG.trace("Status Result (%s): Response code is %d", jobid, statusCode);
					}

					if (statusCode == 404) {
						return "{ \"status\": \"" + ResultStatus.STATUS_NOTFOUND + "\" }";
					}
					throw new IOException("Unexpected code: " + response);

				}

				final String str = EntityUtils.toString(response.getEntity());
				if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_POLL)) {
					LOG.trace("Status Result (%s): Response body is %s", jobid, str);
				}
				return str;
			}
		}
	}

	public RSAPublicKey getOptimisationServerPublicKey(final File pubkey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException {

		final String gateway = getGateway();
		if (gateway == null) {
			return null;
		}
		final URI url = new URI(String.format("%s%s/%s", getGateway(), PUBLIC_KEY_URL, getUserId()));
		final HttpClientBuilder builder = createHttpBuilder(url);

		try (var httpClient = builder.build()) {
			final HttpGet request = new HttpGet(url);
			withAuthHeader(url, request);

			try (CloseableHttpResponse response = httpClient.execute(request)) {
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != 200) {
					if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_POLL)) {
						throw new IOException("Unexpected code: " + response);
					}

				}
				try (FileOutputStream fos = new FileOutputStream(pubkey)) {
					response.getEntity().writeTo(fos);
				}
				final KeyFactory factory = KeyFactory.getInstance("RSA");

				try (FileReader keyReader = new FileReader(pubkey); PemReader pemReader = new PemReader(keyReader)) {

					final PemObject pemObject = pemReader.readPemObject();
					final byte[] content = pemObject.getContent();
					final X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
					return (RSAPublicKey) factory.generatePublic(pubKeySpec);
				}
			}
		}
	}

	public boolean abort(final String jobid) throws IOException, URISyntaxException {
		final String gateway = getGateway();
		if (gateway == null) {
			return false;
		}
		final URI url = new URI(String.format("%s%s/%s/%s", getGateway(), ABORT_URL, jobid, getUserId()));
		final HttpClientBuilder builder = createHttpBuilder(url);

		try (var httpClient = builder.build()) {
			final HttpGet request = new HttpGet(url);
			withAuthHeader(url, request);

			try (CloseableHttpResponse response = httpClient.execute(request)) {
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != 200) {
					if (statusCode == 500) {
						LOG.error("failed to abort remote optimisation job");
					}
					return false;
				}
				return true;
			}
		}
	}

	private static final TypeReference<List<CloudOptimisationDataResultRecord>> TYPE_GDR_LIST = new TypeReference<List<CloudOptimisationDataResultRecord>>() {
	};

	public static List<CloudOptimisationDataResultRecord> parseRecordsJSONData(final String jsonData) {
		if (jsonData != null && !jsonData.isEmpty()) {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			try {
				return mapper.readValue(jsonData, TYPE_GDR_LIST);
			} catch (final JsonParseException e) {
				e.printStackTrace();
			} catch (final JsonMappingException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<>();
	}

	public static String getJSON(final List<CloudOptimisationDataResultRecord> records) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			return mapper.writeValueAsString(records);
		} catch (final JsonParseException e) {
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
