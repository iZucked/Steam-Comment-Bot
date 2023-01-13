/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.auth.BasicAuthenticationManager;
import com.mmxlabs.hub.license.HubLicenseManager;
import com.mmxlabs.lingo.its.datahub.HubTestHelper;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.rcp.common.appversion.VersionHelper;

@Testcontainers
@Tag(TestCategories.HUB_TEST)
class LicenseTests {

	private static final Logger log = LoggerFactory.getLogger(LicenseTests.class);

	public static final int DATAHUB_PORT = 8090;
	private static final List<Integer> portPool = List.of(8090, 8091, 8092, 8093, 8094, 8095, 8096, 8097, 8098, 8099);
	private static final int timeout = 60000;
	private static final int availablePort = HubTestHelper.waitForAvailablePort(portPool, timeout);

	private static final BasicAuthenticationManager basicAuthenticationManager = BasicAuthenticationManager.getInstance();
	private static SWTWorkbenchBot bot;
	private static String datahubHost;
	private static String upstreamUrl;

	private static final String CONTAINER = HubTestHelper.getContainerString(VersionHelper.getInstance().getClientCode());

	@Container
	public static GenericContainer datahubContainer = HubTestHelper.createDataHubContainer(CONTAINER, availablePort, DATAHUB_PORT, false);

	@BeforeAll
	private static void beforeAll() {
		datahubHost = datahubContainer.getHost();
		upstreamUrl = String.format("http://%s:%s", datahubHost, availablePort);
		log.info(upstreamUrl);
		bot = new SWTWorkbenchBot();
		HubTestHelper.setDatahubUrl(upstreamUrl);
		// authenticate and force trigger refresh
		basicAuthenticationManager.withCredentials("test", "test");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
		log.info(Boolean.toString(basicAuthenticationManager.isAuthenticated(upstreamUrl)));
	}

	@AfterAll
	public static void afterAll() throws Exception {
		bot.resetWorkbench();
		bot = null;
	}

	@BeforeEach
	public void beforeEach() {
		bot.resetWorkbench();
	}

	File createLicense() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, FileNotFoundException, IOException, CertificateException, OperatorCreationException {
		// generate a key pair (you did this already it seems)
		KeyPairGenerator rsaGen = KeyPairGenerator.getInstance("RSA");
		final KeyPair pair = rsaGen.generateKeyPair();

		// create the self signed cert
		Certificate cert = createSelfSigned(pair);

		// create a new pkcs12 key store in memory
		KeyStore pkcs12 = KeyStore.getInstance("PKCS12");
		pkcs12.load(null, null);

		// create entry in PKCS12
		pkcs12.setKeyEntry("privatekeyalias", pair.getPrivate(), "entrypassphrase".toCharArray(), new Certificate[] { cert });

		// License checker expects the license to contain a PKCS cert with alias "1"...
		pkcs12.setCertificateEntry("1", cert);

		// store PKCS12 as file
		try (FileOutputStream p12 = new FileOutputStream("license.p12")) {
			pkcs12.store(p12, "Lok3pDTS".toCharArray());
		}

		// read PKCS12 as file
		KeyStore testp12 = KeyStore.getInstance("PKCS12");
		try (FileInputStream p12 = new FileInputStream("license.p12")) {
			testp12.load(p12, "Lok3pDTS".toCharArray());
		}

		// retrieve private key
		System.out.println(Hex.toHexString(testp12.getKey("privatekeyalias", "entrypassphrase".toCharArray()).getEncoded()));

		return new File("license.p12");
	}

	private static X509Certificate createSelfSigned(KeyPair pair) throws OperatorCreationException, CertIOException, CertificateException {
		X500Name dnName = new X500Name("CN=publickeystorageonly");
		BigInteger certSerialNumber = BigInteger.ONE;

		Date startDate = new Date(); // now

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.YEAR, 1);
		Date endDate = calendar.getTime();

		ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(pair.getPrivate());
		JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, certSerialNumber, startDate, endDate, dnName, pair.getPublic());

		return new JcaX509CertificateConverter().getCertificate(certBuilder.build(contentSigner));
	}

	public void setCurrentLicense() throws IOException {
		try {

			final String postLicenseURL = "/license";
			HttpPost request = new HttpPost();
			final CloseableHttpClient client = DataHubServiceProvider.getInstance().makeRequest(postLicenseURL, request);

			if (client == null) {
				throw new IllegalStateException();
			}

			File license = createLicense();

			final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
			formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			formDataBuilder.addBinaryBody("license", license, ContentType.DEFAULT_BINARY, "license.p12");
			final HttpEntity entity = formDataBuilder.build();

			// Add file to post request
			request.setEntity(entity);

			if (client != null) {

				// Replace any auth header for test purposes
				final byte[] encodedAuth = Base64.getEncoder().encode(String.format("%s:%s", "test", "test").getBytes(StandardCharsets.UTF_8));
				final String authHeader = "Basic " + new String(encodedAuth);
				request.setHeader("Authorization", authHeader);

				// Check the response
				try (var response = client.execute(request)) {
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode < 200 || statusCode >= 400) {
						log.error(EntityUtils.toString(response.getEntity()));
					}
					log.info(EntityUtils.toString(response.getEntity()));
				}
			}
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException | OperatorCreationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void getLicenseFromDatahub() throws IOException {
		log.info(Boolean.toString(basicAuthenticationManager.isAuthenticated(upstreamUrl)));
		setCurrentLicense();

		File keystoreFile = HubLicenseManager.getLicenseFromDatahub();
		// TODO: Update test - this may be an old file!
		Assertions.assertNotNull(keystoreFile);
		Assertions.assertTrue(keystoreFile.exists());
	}

}
