/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.license.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.rcp.common.tests.internal.Activator;

class LicenseCheckerTests {

	private static final Logger log = LoggerFactory.getLogger(LicenseCheckerTests.class);

	@Test
	void loadFromURIWithSpaces() throws Exception {

		Path tempDir = Files.createTempDirectory("lingo test spaces");
		try {
			String location = "file:///" + tempDir.toString().replaceAll("\\\\", "/");
			System.out.println(location);
			File f = LicenseChecker.getCACertsFileFromEclipseHomeURL(location);

			Assertions.assertNotNull(f);
		} finally {
			Files.delete(tempDir);
		}
	}

	@Test
	void importExtraCertsFromHome() throws CertificateException, KeyStoreException {
		// TODO use mock keystore instead
		final File file = new File("license.p12");
		if (file.exists()) {
			try (InputStream inStream = new FileInputStream(file)) {
				final KeyStore keystore = KeyStore.getInstance("PKCS12");
				keystore.load(inStream, "helloworld".toCharArray());

				Assertions.assertNull(keystore.getCertificate("lingo"));
				LicenseChecker.importExtraCertsFromHome(keystore);

				Assertions.assertNotNull(keystore.getCertificate("cert.pem"));
			} catch (NoSuchAlgorithmException | IOException e) {
				log.error("failed to load test license: " + e.getMessage());
			}
		}
	}

	@Test
	void licenseFromSystemProperty() throws CertificateException {
		KeyStore keystore = LicenseChecker.getLicenseFromSystemProperty();
		Assertions.assertNotNull(keystore);
	}

	@Test
	void licenseFromEclipseHome() throws CertificateException {
		KeyStore keystore = LicenseChecker.getEclipseHomeLicense();
		Assertions.assertNotNull(keystore);
	}

	@Test
	void licenseFromUserData() {
		KeyStore keystore = LicenseChecker.getUserDataLicense();
		Assertions.assertNotNull(keystore);
	}

	@Test
	void createKeystoreCopies() {
		// TODO use mock keystore instead
		Mockito.mock(KeyStore.class);
		final File licenseFile = new File("license.p12");
		final File keystoreFile = new File("keystore.p12");
		if (licenseFile.exists() && keystoreFile.exists()) {
			try (InputStream inStreamLic = new FileInputStream(licenseFile); InputStream inStreamKey = new FileInputStream(keystoreFile)) {
				final KeyStore license = KeyStore.getInstance("PKCS12");
				license.load(inStreamLic, "helloworld".toCharArray());

				final KeyStore keystore = KeyStore.getInstance("PKCS12");
				keystore.load(inStreamKey, "helloworld".toCharArray());

				final File keyStoreFile = Activator.getDefault().getBundle().getDataFile("local-keystore.jks");
				final File trustStoreFile = Activator.getDefault().getBundle().getDataFile("local-truststore.jks");
				Assertions.assertNull(keyStoreFile);
				Assertions.assertNull(trustStoreFile);

				LicenseChecker.createKeystoreCopies(keystore, license);

				Assertions.assertNotNull(keyStoreFile);
				Assertions.assertNotNull(trustStoreFile);
			} catch (NoSuchAlgorithmException | IOException | CertificateException | KeyStoreException e) {
				log.error("failed to load test license: " + e.getMessage());
			}
		}
	}
}
