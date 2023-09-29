/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.datahub;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.hub.DataHubActivator;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.auth.BasicAuthenticationManager;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
import com.mmxlabs.lingo.app.updater.model.Version;

import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

public class HubTestHelper {

	static final Logger logger = LoggerFactory.getLogger(HubTestHelper.class);
	public static final String HOST = "localhost";

	public static final String DYNAMODB_TABLE_NAME = "datahub_versions";
	public static final String DYNAMODB_KEY = "client";

	// initialise http client with small connection timeout
	private static final int CONNECTION_TIMEOUT_IN_SECONDS = 5;
	private static final int READ_TIMEOUT_IN_SECONDS = 1;
	private static final int WRITE_TIMEOUT_IN_SECONDS = 1;
	// private final static OkHttpClient httpClient = HttpClientUtil.basicBuilder(CONNECTION_TIMEOUT_IN_SECONDS, READ_TIMEOUT_IN_SECONDS, WRITE_TIMEOUT_IN_SECONDS).build();
	
	static {
		// Some of the AWS SDK doesn't pick up the http client from the service client, so set the property here.
		// Specifically the reading of credentials for SSO / Default Profile 
		System.setProperty("software.amazon.awssdk.http.service.impl", "software.amazon.awssdk.http.apache.ApacheSdkHttpService");
	}

	public static void configureHub(final String url, final boolean enableBaseCase, final boolean enableTeam) {
		DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, url);
	}

	public static void asStandardUser() {
		BasicAuthenticationManager.getInstance().withCredentials("u_users", "u_users");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void asBaseCaseUser() {
		BasicAuthenticationManager.getInstance().withCredentials("u_basecase", "u_basecase");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void asSuperUser() {
		BasicAuthenticationManager.getInstance().withCredentials("u_full", "u_full");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void asAlternativeStandardUser() {
		BasicAuthenticationManager.getInstance().withCredentials("simon", "simon");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void asAlternativeBaseCaseUser() {
		BasicAuthenticationManager.getInstance().withCredentials("simon", "simon");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void setDatahubUrl(final String upstreamUrl) {
		DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, upstreamUrl);
	}

	/**
	 * Wait until a port from the portPool is available and return it. If no ports are available before the timeout duration is reached return -1
	 *
	 * @param portPool
	 * @param timeout
	 * @return port number or -1 if no ports are available
	 */
	public static int waitForAvailablePort(final List<Integer> portPool, final int timeout) {
		int availablePort = -1;
		final long start = System.currentTimeMillis();
		final long end = start + timeout;
		while (availablePort == -1 && System.currentTimeMillis() < end) {
			availablePort = getAvailablePort(portPool);
		}
		return availablePort;
	}

	/**
	 * Check if any port from a port pool is available
	 *
	 * @param portPool
	 * @return port number or -1 if no ports from the pool are available
	 */
	public static int getAvailablePort(final List<Integer> portPool) {
		Optional<Integer> availablePort;
		// @formatter:off
		availablePort = portPool.stream().filter(port -> isTcpPortAvailable(HOST, port)).findAny();
		// @formatter:on
		if (availablePort.isPresent()) {
			logger.info(String.format("Port %s is available", availablePort.get()));
			return availablePort.get();
		} else {
			logger.info("There are no available ports at the moment...");
			return -1;
		}
	}

	/**
	 * Check to see if a tcp port is available by trying to open it. Note: this is vulnerable to race conditions.
	 *
	 * @param host
	 * @param port
	 * @return true if the port is available and false otherwise. Could also return false if the bind operation fails
	 */
	public static boolean isTcpPortAvailable(final String host, final int port) {
		try (ServerSocket socket = new ServerSocket()) {
			socket.bind(new InetSocketAddress(InetAddress.getByName(host), port));
			return true;
		} catch (final IOException ex) {
			logger.info(String.format("socket of port %s is already bound", port));
			return false;
		}
	}

	public static String getContainerString(String client) {
		// in case we're running ITS (non-client)
		if (client == null) {
			client = "v";
		}
		try {
			return HubTestHelper.buildDatahubContainerString(client);
		} catch (final Exception e) {
			logger.error(e.getMessage());
			System.exit(-1);
		}
		return "";
	}

	public static String buildDatahubContainerString(final String client) throws IOException, Exception {
		String version;
		if (isReleaseTest()) {
			version = getVersionFromDynamodb(client);
			version = version.concat(".RELEASE");
		} else {
			version = getLatestVersionFromContainerRegistry(client);
		}
		if (version != null) {
			return String.format("docker.mmxlabs.com/datahub-%s:%s", client, version);
		} else {
			throw new Exception("Failed to get DataHub version from go server");
		}
	}

	public static String getVersionFromDynamodb(final String client) throws IOException {
		final HashMap<String, AttributeValue> keyToGet = new HashMap<>();
		String version = "";

		final DynamoDbClient ddb = DynamoDbClient.builder() //
				.httpClient(ApacheHttpClient.create()) //
				.build();

		keyToGet.put(DYNAMODB_KEY, AttributeValue.builder().s(client).build());

		final GetItemRequest request = GetItemRequest.builder().key(keyToGet).tableName(DYNAMODB_TABLE_NAME).build();

		try {
			final Map<String, AttributeValue> returnedItem = ddb.getItem(request).item();

			if (returnedItem != null) {
				final Set<String> keys = returnedItem.keySet();
				logger.info("Amazon DynamoDB table attributes: \n");

				for (final String key1 : keys) {
					logger.info("%s: %s\n", key1, returnedItem.get(key1).toString());
					if ("version".equals(key1)) {
						// TODO fallback on go server value?
						version = returnedItem.get(key1).s();
					}
					version = returnedItem.get(key1).toString();
				}
			} else {
				logger.info("No item found with the key %s!\n", DYNAMODB_KEY);
			}
		} catch (final DynamoDbException e) {
			logger.error(e.getMessage());
			System.exit(1);
		}
		return version;
	}

	public static String getLatestVersionFromContainerRegistry(final String client) {
		final List<Version> versions = getAvailableContainerVersionsFromRegistry(client);
		// sort the versions
		Collections.sort(versions);
		versions.stream().forEach(System.out::print);
		if (versions.size() > 1) {
			return versions.get(versions.size() - 1).toString();
		} else {
			logger.error(String.format("There are no versions for client %s", client));
			System.exit(1);
		}
		return null;
	}

	public static List<Version> getAvailableContainerVersionsFromRegistry(final String client) {

		final HttpGet request = new HttpGet(String.format("https://docker.mmxlabs.com/v2/datahub-%s/tags/list", client));
		try (var httpClient = HttpClientUtil.createBasicHttpClient(request, false).build()) {
			try (var response = httpClient.execute(request)) {
				final int responseCode = response.getStatusLine().getStatusCode();

				if (HttpClientUtil.isSuccessful(responseCode)) {
					final ObjectMapper objectMapper = new ObjectMapper();
					final DockerVersions versions = objectMapper.readValue(response.getEntity().getContent(), DockerVersions.class);
					return versions.tags;
				} else {
					logger.error("request to docker.mmxlabs.com failed");
				}
			}
		} catch (final Exception e) {
			logger.error(e.getMessage());
		}

		return new ArrayList<>();
	}

	public static boolean isReleaseTest() {
		final String releaseVersion = System.getenv("RELEASE_VERSION");
		return (releaseVersion != null && !releaseVersion.isBlank());
	}

	static class DockerVersions {
		public String name;
		ArrayList<Version> tags = new ArrayList<>();

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public ArrayList<Version> getTags() {
			return tags;
		}

		public void setTags(final ArrayList<Version> tags) {
			this.tags = tags;
		}
	}

	// various html element names
	public static String otherTile = "otherTile";
	public static String emailInputName = "loginfmt";
	public static String passwordInputName = "passwd";
	public static String nextButtonId = "idSIButton9";
	public static String noButtonId = "idBtn_Back";

	// test credentials
	public static String email = "Testy@testminimaxlabs.onmicrosoft.com";
	// TODO update
	public static String password = getSecret();
	// javascript actions executed in the browser - tested in IE and Firefox
	public static String clickUserAnotherAccount = "document.getElementById('" + otherTile + "').click();";
	public static String fillEmail = "document.getElementsByName('" + emailInputName + "')[0].value = '" + email + "';";
	public static String focusEmail = "var email = document.getElementsByName('" + emailInputName
			+ "')[0]; var inputEvent = document.createEvent('KeyboardEvent'); inputEvent.initEvent('input', true, false, null, false, false, false, false, 0, 0); email.dispatchEvent(inputEvent);";
	public static String clickNext = "document.getElementById('" + nextButtonId + "').click();";
	public static String fillPassword = "document.getElementsByName('" + passwordInputName + "')[0].value = '" + password + "';";
	public static String focusPassword = "var password = document.getElementsByName('" + passwordInputName
			+ "')[0]; var inputEvent2 = document.createEvent('KeyboardEvent'); inputEvent2.initEvent('input', true, false, null, false, false, false, false, 0, 0); password.dispatchEvent(inputEvent2);";

	// check if the user is signed in
	public static String userIsSignedIn = "document.querySelector('div[data-test-id=" + email + "]') !== null ? document.querySelector('div[data-test-id=" + email + "]').click() : false;";
	public static String clickSignedIn = "document.querySelector('div[data-test-id=" + email + "]').click();";
	public static String clickNo = "document.getElementById('" + noButtonId + "').click();";

	// Use this code snippet in your app.
	// If you need more information about configurations or implementing the sample
	// code, visit the AWS docs:
	// https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/java-dg-samples.html#prerequisites

	public static String getSecret() {

		// Copy env vars to system properties as the SDK can fail to read them for some
		// reason in the ITS runs.
		final Map<String, String> getenv = System.getenv();
		String id = getenv.get("AWS_ACCESS_KEY_ID");
		if (id != null) {
			System.setProperty("aws.accessKeyId", id);
		}
		String key = getenv.get("AWS_SECRET_ACCESS_KEY");
		if (key != null) {
			System.setProperty("aws.secretAccessKey", key);
		}

		final String secretName = "lingo/hub-test";
		final Region region = Region.of("eu-west-2");

		// Create a Secrets Manager client
		final SecretsManagerClient client = SecretsManagerClient.builder() //
				.region(region) //
				.httpClient(ApacheHttpClient.create()) //
				.build();

		String secret;
		final GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder() //
				.secretId(secretName) //
				.build();
		GetSecretValueResponse getSecretValueResponse = null;

		try {
			getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
		} catch (final SecretsManagerException e) {
			logger.error(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}

		// Decrypts secret using the associated KMS key.
		// Depending on whether the secret is a string or binary, one of these fields
		// will be populated.
		if (getSecretValueResponse.secretString() != null) {
			secret = getSecretValueResponse.secretString();
			AWSSecret awsSecret;
			try {
				awsSecret = new ObjectMapper().readValue(secret, AWSSecret.class);
				return awsSecret.password;
			} catch (final JsonProcessingException e) {
				logger.error(e.getLocalizedMessage());
			}
		}

		throw new RuntimeException("Coudn't get secret from AWS");
	}

	public static AWSSecret getSecrets() {
		final String secretName = "lingo/hub-test";
		final Region region = Region.of("eu-west-2");

		// Create a Secrets Manager client
		// Can't use okhttp unfortunatly, limitied to http clients described here:
		// https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/http-configuration.html
		final SecretsManagerClient client = SecretsManagerClient.builder() //
				.region(region) //
				.httpClient(ApacheHttpClient.create()) //
				.build();

		String secret;
		final GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder() //
				.secretId(secretName) //
				.build();
		GetSecretValueResponse getSecretValueResponse = null;

		try {
			getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
		} catch (final SecretsManagerException e) {
			logger.error(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}

		// Decrypts secret using the associated KMS key.
		// Depending on whether the secret is a string or binary, one of these fields
		// will be populated.
		if (getSecretValueResponse.secretString() != null) {
			secret = getSecretValueResponse.secretString();
			AWSSecret awsSecret;
			try {
				awsSecret = new ObjectMapper().readValue(secret, AWSSecret.class);
				return awsSecret;
			} catch (final JsonProcessingException e) {
				logger.error(e.getLocalizedMessage());
			}
		}

		throw new RuntimeException("Coudn't get secret from AWS");
	}

	static class AWSSecret {
		@JsonProperty
		String password;
		@JsonProperty
		String secret_id;
		@JsonProperty
		String client_id;
		@JsonProperty
		String tenant_id;
	}

	// we need a fixed port for this test because we cannot programmatically modify
	// the redirection-url on Azure
	// AD has port 8090 - 8099 set for testing purposes
	public static GenericContainer createDataHubContainer(final String CONTAINER, final int availablePort, final int DATAHUB_PORT, final boolean oauth) {

		final AWSSecret secrets = getSecrets();

		// @formatter:off
		return new FixedHostPortGenericContainer(CONTAINER).withFixedExposedPort(availablePort, DATAHUB_PORT)
				.withExposedPorts(DATAHUB_PORT)
				.withEnv("PORT", Integer.toString(DATAHUB_PORT))
				.withEnv("PROTOCOL", "http")
				.withEnv("HOSTNAME", "localhost")
				.withEnv("AUTHENTICATION_SCHEME", oauth ? "oauth" : "basic")
				.withEnv("INSTANCE_TAG", "docker")
				.withEnv("DB_HOST", "0.0.0.0")
				.withEnv("DB_PORT", "27000")
				.withEnv("AZURE_CLIENT_ID", secrets.client_id)
				.withEnv("AZURE_TENANT_ID", secrets.tenant_id)
				.withEnv("AZURE_CLIENT_SECRET", secrets.secret_id)
				.withEnv("AZURE_GROUPS", "MinimaxUsers, MinimaxLingo, MinimaxBasecase, MinimaxAdmin")
				.waitingFor(Wait.forLogMessage(".*Started DataHubApplication.*", 1));
		// @formatter:on
	}
}
