/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.auth.BasicAuthenticationManager;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.license.LicenseManager;
import com.mmxlabs.lingo.its.datahub.HubTestHelper;
import com.mmxlabs.lingo.its.tests.category.TestCategories;

import okhttp3.Credentials;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Testcontainers
@Tag(TestCategories.HUB_TEST)
class LicenseTests {

	static final Logger log = LoggerFactory.getLogger(LicenseTests.class);
	private final OkHttpClient httpClient = HttpClientUtil.basicBuilder() //
			.build();

	public static final int DATAHUB_PORT = 8090;
	static List<Integer> portPool = List.of(8090, 8091, 8092, 8093, 8094, 8095, 8096, 8097, 8098, 8099);
	static int timeout = 60000;
	static int availablePort = HubTestHelper.waitForAvailablePort(portPool, timeout);

	private static BasicAuthenticationManager basicAuthenticationManager = BasicAuthenticationManager.getInstance();
	private static SWTWorkbenchBot bot;
	static String datahubHost;
	static String upstreamUrl;

	// @formatter:off
	@Container
	public static GenericContainer datahubContainer = new FixedHostPortGenericContainer("docker.mmxlabs.com/datahub-v:1.9.3-SNAPSHOT")
	.withFixedExposedPort(availablePort, DATAHUB_PORT)
	.withExposedPorts(DATAHUB_PORT)
	.withEnv("PORT", Integer.toString(DATAHUB_PORT))
	.withEnv("PROTOCOL", "http")
	.withEnv("HOSTNAME", "localhost")
	.withEnv("AUTHENTICATION_SCHEME", "basic")
	.withEnv("INSTANCE_TAG", "docker")
	.withEnv("DB_HOST", "0.0.0.0")
	.withEnv("DB_PORT", "27000")
	.withEnv("AZURE_CLIENT_ID", "e52d83a9-40c0-42ae-aae3-d5054ef24919")
	.withEnv("AZURE_TENANT_ID", "dceff11f-6e74-436e-b9a0-65f9697b8472")
	.withEnv("AZURE_CLIENT_SECRET", "n-LV4_3vPJ3s1v.9MWwLy1.ZO-1oz17u54")
	.withEnv("AZURE_GROUPS", "MinimaxUsers, MinimaxLingo, MinimaxBasecase")
	.withEnv("AZURE_BASECASE_GROUP_ID", "452fe6d8-7360-47fd-b5b5-8cd8108d9233")
	.withEnv("AZURE_BASECASE_GROUP_NAME", "MinimaxBasecase")
	.withEnv("AZURE_LINGO_GROUP_ID", "80a34e39-50d3-405d-88b0-3522307dfed8")
	.withEnv("AZURE_LINGO_GROUP_NAME", "MinimaxLingo")
	.withEnv("AZURE_USERS_GROUP_ID", "287686ff-d399-4875-b86d-1dd9426973d6")
	.withEnv("AZURE_USERS_GROUP_NAME", "MinimaxUsers")
	.waitingFor(Wait.forLogMessage(".*Started ServerConnector.*", 1));
	// @formatter:on

	@BeforeAll
	private static void beforeAll() {
		datahubHost = datahubContainer.getHost();
		upstreamUrl = String.format("http://%s:%s", datahubHost, availablePort);
		log.info(upstreamUrl);
		bot = new SWTWorkbenchBot();
		HubTestHelper.setDatahubUrl(upstreamUrl);
		// authenticate and force trigger refresh
		basicAuthenticationManager.withCredentials("philippe", "philippe");
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
		pkcs12.setKeyEntry("privatekeyalias", pair.getPrivate(), "entrypassphrase".toCharArray(), new Certificate[] {cert});

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
		File license;
		try {
			license = createLicense();

			RequestBody requestBody = new MultipartBody.Builder() //
					.setType(MultipartBody.FORM) //
					.addFormDataPart("license", "license.p12", RequestBody.create(HttpClientUtil.MEDIA_TYPE_FORM_DATA, license))//
					.build();

			final String postLicenseURL = "/license";
			final Request.Builder builder = DataHubServiceProvider.getInstance().makeRequestBuilder(postLicenseURL);

			if (builder != null) {
				final Request request = builder //
						.post(requestBody) //
						.header("Authorization", Credentials.basic("philippe", "philippe")) //
						.build();

				// Check the response
				try (Response response = httpClient.newCall(request).execute()) {
					if (!response.isSuccessful()) {
						log.error(response.message());
					}
					log.info(response.message());
					response.body().close();
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
		KeyStore keystore = LicenseManager.getLicenseFromDatahub();
		Assertions.assertNotNull(keystore);
	}

}
