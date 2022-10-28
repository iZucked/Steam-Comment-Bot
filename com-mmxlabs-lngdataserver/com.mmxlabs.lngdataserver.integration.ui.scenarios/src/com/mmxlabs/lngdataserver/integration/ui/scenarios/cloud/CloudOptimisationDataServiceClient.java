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
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.utils.URIUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressHttpEntityWrapper;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.auth.ICloudAuthenticationProvider;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.debug.CloudOptiDebugContants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse.GatewayResponseMaker;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse.IGatewayResponse;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.preferences.CloudOptimiserPreferenceConstants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.Activator;
import com.mmxlabs.rcp.common.ServiceHelper;

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
	private static final String DELETE_URL = "/delete";

	private String userid;

	private final Instant lastSuccessfulAccess = Instant.EPOCH.plusNanos(1);

	private final LoadingCache<HttpHost, CloseableHttpClient> cache = CacheBuilder.newBuilder() //

			.removalListener(new RemovalListener<HttpHost, CloseableHttpClient>() {
				@Override
				public void onRemoval(final RemovalNotification<HttpHost, CloseableHttpClient> notification) {
					try {
						notification.getValue().close();
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			})
			.build(new CacheLoader<HttpHost, CloseableHttpClient>() {

				@Override
				public CloseableHttpClient load(final HttpHost baseUrl) throws Exception {
					final boolean needsClientAuth = baseUrl.getHostName().endsWith("gw.minimaxlabs.com");

					final HttpClientBuilder builder = HttpClientUtil.createBasicHttpClient(baseUrl, needsClientAuth);
					builder.addRequestInterceptorFirst(new HttpRequestInterceptor() {
						@Override
						public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context) throws HttpException, IOException {
							try {
								URI uri = request.getUri();
								if (uri.getHost().endsWith("gw.minimaxlabs.com")) {
									final URI url = uri;
									ServiceHelper.withOptionalServiceConsumer(ICloudAuthenticationProvider.class, p -> {
										if (p != null) {
											try {
												final Pair<String, String> header = p.provideAuthenticationHeader(url);
												if (header != null) {
													request.addHeader(header.getFirst(), header.getSecond());
												}
											} catch (final Exception exception) {
												// Error generating the token
												exception.printStackTrace();
											}
										}
									});
								}
							} catch (final URISyntaxException e) {
								throw new IOException(e);
							}
						}

					});

					return builder.build();
				}

			});

	public CloudOptimisationDataServiceClient() {
		// Any changes to the http client config will clear the client cache
		HttpClientUtil.addInvalidationListener(cache::invalidateAll);
	}

	public Instant getLastSuccessfulAccess() {
		return this.lastSuccessfulAccess;
	}

	private CloseableHttpClient getHttpClient(final URI url) {
		final HttpHost httpHost = URIUtils.extractHost(url);
		return cache.getUnchecked(httpHost);
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

		final var httpClient = getHttpClient(url);
		final HttpGet request = new HttpGet(url);
		return httpClient.execute(request, response -> {
			if (response.getCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
			throw new IOException("Unexpected code: " + response);
		});
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

		final var httpClient = getHttpClient(url);
		{
			final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
			formDataBuilder.setMode(HttpMultipartMode.LEGACY);
			formDataBuilder.addTextBody("sha256", checksum);
			formDataBuilder.addBinaryBody("scenario", scenario, ContentType.DEFAULT_BINARY, scenarioName + ".zip");
			formDataBuilder.addBinaryBody("encryptedsymkey", encryptedSymmetricKey, ContentType.DEFAULT_BINARY, "aes.key.enc");
			final HttpEntity entity = formDataBuilder.build();
			final HttpPost request = new HttpPost(url);
			// Wrap entity for progress monitor
			try (ProgressHttpEntityWrapper wrappedEntity = new ProgressHttpEntityWrapper(entity, progressListener)) {
				request.setEntity(wrappedEntity);
				return httpClient.execute(request, response -> {
					final int statusCode = response.getCode();
					if (statusCode >= 200 && statusCode < 300) {
						return EntityUtils.toString(response.getEntity());
					}
					// 400 - missing data or data filename in request
					// 415 - archive was not a zip file
					throw new IOException("Unexpected code " + response);
				});
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
		final var httpClient = getHttpClient(url);
		{
			final HttpGet request = new HttpGet(url);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				final int statusCode = response.getCode();
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
		final var httpClient = getHttpClient(url);
		{
			final HttpGet request = new HttpGet(url);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				final int statusCode = response.getCode();
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
		final var httpClient = getHttpClient(url);
		{
			final HttpGet request = new HttpGet(url);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				final int statusCode = response.getCode();
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
		final var httpClient = getHttpClient(url);
		{
			final HttpDelete request = new HttpDelete(url);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				final int statusCode = response.getCode();
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

	public boolean deleteTask(String jobid) throws IOException, URISyntaxException {
		final String gateway = getGateway();
		if (gateway == null) {
			return false;
		}
		final URI url = new URI(String.format("%s%s/%s/%s", getGateway(), DELETE_URL, jobid, getUserId()));
		final var httpClient = getHttpClient(url);
		{
			final HttpDelete request = new HttpDelete(url);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				final int statusCode = response.getCode();
				// Could return 200 OK or 204 no content
				if (statusCode != 200 && statusCode != 204) {
					if (statusCode == 500) {
						LOG.error("failed to delete remote optimisation job");
					}
					return false;
				}
				return true;
			}
		}
	}

}
