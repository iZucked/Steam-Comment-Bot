/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.auth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.SecretKeyEntry;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.utils.URIUtils;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.eclipse.jdt.annotation.Nullable;
import org.json.simple.JSONObject;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.license.ssl.LicenseManager;
import com.mmxlabs.rcp.common.appversion.VersionHelper;

public abstract class CloudOptiClasspathKeystoreProvider implements ICloudAuthenticationProvider {

	private static final String GW_MINIMAXLABS_COM = "gw.minimaxlabs.com";
	private static final String PROVIDER = "lingo";

	protected abstract String getKeyId();

	protected abstract char[] getPassword();

	protected abstract InputStream getKeystoreStream();

	@Override
	@Nullable
	public Pair<String, String> provideAuthenticationHeader(final URI url) throws Exception {

		if (!(url.getHost().endsWith(GW_MINIMAXLABS_COM))) {
			return null;
		}

		// Load up the private key
		final KeyStore keyStore = KeyStore.getInstance("PKCS12");
		try (InputStream is = getKeystoreStream()) {
			keyStore.load(is, getPassword());
		}

		// Extract the RSA private key
		final SecretKey secretKey = (SecretKey) keyStore.getKey(GW_MINIMAXLABS_COM, getPassword());
		final EncodedKeySpec spec = new PKCS8EncodedKeySpec(secretKey.getEncoded());
		final KeyFactory rsaFact = KeyFactory.getInstance("RSA");
		final RSAPrivateKey privateKey = (RSAPrivateKey) rsaFact.generatePrivate(spec);

		// Now generate the JWT token
		final Map<String, Object> headers = new HashMap<>();
		// which client are we requesting access to?
		headers.put("client", VersionHelper.getInstance().getClientCode());
		headers.put("code", VersionHelper.getInstance().getClientCode());
		// Set to true if we are using the developers license.
		headers.put("dev", LicenseManager.isDevLicense());

		final Algorithm algorithm = Algorithm.RSA256(null, privateKey);

		// Random data to make each signature unique
		final SecureRandom r = new SecureRandom();
		final byte[] bytes = new byte[32];
		r.nextBytes(bytes);

		final String token = JWT.create() //
				.withIssuer(PROVIDER) //
				.withKeyId(getKeyId()) // Which keypair has been used
				.withJWTId(bytesToString(bytes)) // Random data
				.withHeader(headers) //
				// How long token is valid for
				// Note: 5 seconds too low
				.withExpiresAt(Date.from(ZonedDateTime.now().plusSeconds(10).toInstant())) //
				.sign(algorithm);

		return Pair.of("Authorization", String.format("Bearer %s", token));
	}

	/**
	 * Converts a byte array to a hex-decimal string
	 * 
	 * @param bytes
	 * @return
	 */
	private static String bytesToString(final byte[] bytes) {
		final StringBuilder sb = new StringBuilder();
		for (final byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	/**
	 * Method to generate a new keypair for JWT tokens. The keystore should be kept in the client repo. The public key should be saved under "public_key" in the AWS secret. The private key should be
	 * saved under "private_key" in the AWS secret.
	 * 
	 * @param keystoreFile
	 * @param publicKeyPath
	 * @param privateKeyPath
	 * @param password
	 * @throws Exception
	 */
	protected void generate(final File keystoreFile, String code, String keyId, final File secretJSONFile, final char[] password) throws Exception {

		final KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(null);

		final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		final KeyPair kp = kpg.generateKeyPair();

		final RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();// Get the key instance
		final RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();// Get the key instance

		// JSON Object to define the AWS secret data
		JSONObject secretJSON = new JSONObject();
		secretJSON.put("client", code);
		secretJSON.put("code", code);

		{
			final String publicKeyContent = Base64.getEncoder().encodeToString(publicKey.getEncoded());
			String publicKeyFormatted = "-----BEGIN PUBLIC KEY-----" + System.lineSeparator();
			for (final String row : Splitter.fixedLength(64).split(publicKeyContent)) {
				publicKeyFormatted += row + System.lineSeparator();
			}
			publicKeyFormatted += "-----END PUBLIC KEY-----";
			secretJSON.put("public_key", Base64.getEncoder().encodeToString(publicKeyFormatted.getBytes()));

		}
		{
			final String privateKeyContent = Base64.getEncoder().encodeToString(privateKey.getEncoded());
			String privateKeyFormatted = "-----BEGIN PRIVATE KEY-----" + System.lineSeparator();
			for (final String row : Splitter.fixedLength(64).split(privateKeyContent)) {
				privateKeyFormatted += row + System.lineSeparator();
			}
			privateKeyFormatted += "-----END PRIVATE KEY-----";
			secretJSON.put("private_key", Base64.getEncoder().encodeToString(privateKeyFormatted.getBytes()));

		}
		// Save AWS secret data file
		Files.asCharSink(secretJSONFile, StandardCharsets.UTF_8).write(secretJSON.toJSONString());
		{

			final SecretKey key = new SecretKeySpec(privateKey.getEncoded(), "AES");
			final byte[] salt = new byte[20];
			new SecureRandom().nextBytes(salt);
			keyStore.setEntry(GW_MINIMAXLABS_COM, new SecretKeyEntry(key), new PasswordProtection(password, "PBEWithHmacSHA512AndAES_128", new PBEParameterSpec(salt, 100_000)));
		}

		try (FileOutputStream os = new FileOutputStream(keystoreFile)) {
			keyStore.store(os, password);
		}

		// Print secret creation information
		System.out.println("Execute the following command to create the secret in AWS");
		String cli = String.format("aws --region=eu-west-2 secretsmanager create-secret --tags Key=client,Value=%s --name \"opticloudgw/%s\" --secret-string %s", code, keyId,
				secretJSONFile.toPath().toUri().toString().replace("file:///", "file://"));
		System.out.println(cli);
	}

	public void checkAuthWorks(final String code) throws Exception {

		final URI url = new URI(String.format("https://%s.gw.minimaxlabs.com/info", code));
//		final HttpClientBuilder localHttpClient = HttpClientBuilder.create();
		final HttpHost httpHost = URIUtils.extractHost(url);
		final HttpClientBuilder localHttpClient = HttpClientUtil.createBasicHttpClient(httpHost, true);

		final HttpGet request = new HttpGet(url);

		VersionHelper.getInstance().setClientCode(code);
		final Pair<String, String> header = provideAuthenticationHeader(url);
		
		localHttpClient.addRequestInterceptorFirst(new HttpRequestInterceptor() {
			@Override
			public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context) throws HttpException, IOException {
				try {

					URI uri = request.getUri();
					if (uri.getHost().endsWith(GW_MINIMAXLABS_COM)) {
						request.addHeader(header.getFirst(), header.getSecond());
					}
				} catch (final URISyntaxException e) {
					throw new IOException(e);
				}
			}

		});

		System.out.println("Printing response code and status. Expected return code of 200");
		try (var httpClient = localHttpClient.build()) {
			try (var response = httpClient.execute(request)) {
				System.out.println(response.getCode());
				System.out.println(response.getReasonPhrase());
				System.out.println(EntityUtils.toString(response.getEntity()));
			}
		}
	}
}
